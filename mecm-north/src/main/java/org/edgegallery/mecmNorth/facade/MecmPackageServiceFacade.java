/* Copyright 2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.edgegallery.mecmNorth.facade;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.edgegallery.mecmNorth.controller.advice.*;
import org.edgegallery.mecmNorth.domain.ResponseConst;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecmNorth.model.MecMPackageInfo;
import org.edgegallery.mecmNorth.model.mapper.MecMDeploymentMapper;
import org.edgegallery.mecmNorth.model.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.edgegallery.mecmNorth.utils.constant.Constant.*;

@Service("MecmPackageServiceFacade")
public class MecmPackageServiceFacade {

    private static final String RESPONSE_FROM_APM_FAILED = "upload csar file to apm failed, and the response code is: ";

    private static final String DISTRIBUTING_STATUS = "Distributing";

    private static final String DISTRIBUTED_STATUS = "Distributed";

    private static final String INSTANTIATING_STATUS = "Instantiating";

    private static final String FINISHED_STATUS = "Finished";

    private static final String FAIL_TO_DISTRIBUTE_STATUS = "failed to distribute";

    private static final String FAILED_TO_INSTANTIATE_STATUS = "failed to instantiate";

    private static final String APP_INSTANCE_ID = "appInstanceId";

    private static final String APP_CLASS = "app_class";

    private static final String UNINSTANTIATE_APP_FAILED
            = "delete instantiate app from appo failed, the appInstanceId is: ";

    private static final String DELETE_EDGE_PKG_FAILED = "delete edge package from apm failed.";

    private static final String DELETE_APM_PKG_FAILED = "delete apm package from apm failed.";

    private static final Logger LOGGER = LoggerFactory.getLogger(MecmPackageServiceFacade.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecMPackageMapper mecMPackageMapper;

    @Autowired
    private MecMDeploymentMapper mecMDeploymentMapper;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    @Value("${serveraddress.appo}")
    private String appoServerAddress;


    public ResponseEntity<ResponsePkgPost> uploadAndInstantiatePkg(RequestPkgBody pkgBody, String access_token) {

        if (!foreCheck(pkgBody)) {
            LOGGER.error("pkgBody has empty field, pls check pkgBody");
            ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_FAIL, null);
            return ResponseEntity.ok(new ResponsePkgPost("", 1, errMsg, "Failed to create server"));
            // throw new AppException("pkgBody has empty field, pls check pkgBody", RET_PKG_FILED_EMPTY_FAILED);
        }

        String pkgName = pkgBody.getAppPkgName();
        String pkgVersion = pkgBody.getAppPkgVersion();
        String[] hostList = pkgBody.getHostList();
        String appClass = pkgBody.getAppClass();
        Map<String, Object> paramsMap = pkgBody.getParamsMap();
        String tenantId = pkgBody.getTenantId();

        String mecmPackageId = UUID.randomUUID().toString();
        String saveFilePath = mecmService.saveFileToLocal(pkgBody.getFile(), mecmPackageId);

        MecMPackageInfo mecMPackageInfo = MecMPackageInfo.builder().mecmPackageId(mecmPackageId).
                mecmPkgName(pkgName).mecmPkgVersion(pkgVersion).mecmAppClass(appClass).tenantId(tenantId).
                hostIps(listToIps(hostList)).status(DISTRIBUTING_STATUS).build();

        mecMPackageMapper.insertMecmPkgInfo(mecMPackageInfo);
        LOGGER.info("create package info in database");

        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(APP_NAME, pkgName);
        packageInfo.put(APP_VERSION, pkgVersion);

        //TODO:异步执行，这段代码
        for (String ip : hostList) {
            String deploymentId = UUID.randomUUID().toString();

            Map<String, String> context = new HashMap<>();
            context.put("apmServerAddress", apmServerAddress);
            context.put("appoServerAddress", appoServerAddress);
            context.put(ACCESS_TOKEN, access_token);
            context.put(TENANT_ID, pkgBody.getTenantId());
            context.put(APP_CLASS, appClass);

            ResponseEntity<String> response = mecmService.uploadFileToAPM(saveFilePath, context, ip, packageInfo);
            if (null == response || !(HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED
                    .equals(response.getStatusCode()))) {
                LOGGER.error("fail to upload file with ip: " + ip);
                LOGGER.error("uploadFileToAPM failed to , response: {}", response);
                MecMPackageDeploymentInfo info = MecMPackageDeploymentInfo.builder().id(deploymentId).
                        mecmPackageId(mecmPackageId).mecmPkgName(pkgName).hostIp(ip).statusCode(5).status(FAIL_TO_DISTRIBUTE_STATUS).build();
                mecMDeploymentMapper.insertPkgDeploymentInfo(info);
            }
            JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
            String appIdFromApm = jsonObject.get("appId").getAsString();
            String appPkgIdFromApm = jsonObject.get("appPackageId").getAsString();

            MecMPackageDeploymentInfo info = MecMPackageDeploymentInfo.builder().id(deploymentId).
                    mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm).
                    appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).statusCode(0).status(DISTRIBUTING_STATUS).build();
            mecMDeploymentMapper.insertPkgDeploymentInfo(info);

            context.put(APP_ID, appIdFromApm);
            context.put(PACKAGE_ID, appPkgIdFromApm);

            // get distribution status from apm
            if (!mecmService.getApmPackage(context, context.get(PACKAGE_ID), ip)) {
                MecMPackageDeploymentInfo infoGetFromApm = MecMPackageDeploymentInfo.builder().id(deploymentId).
                        mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm).
                        appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).statusCode(5).status(FAIL_TO_DISTRIBUTE_STATUS).build();
                mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
                LOGGER.error("fail to distribute package, the mecm package id is:{}", mecmPackageId);
                LOGGER.error("fail to distribute this package to ip:{}", ip);
            }

            MecMPackageDeploymentInfo infoGetFromAppo = MecMPackageDeploymentInfo.builder().id(deploymentId).
                    mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm).
                    appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).statusCode(3).status(INSTANTIATING_STATUS).build();
            mecMDeploymentMapper.insertPkgDeploymentInfo(infoGetFromAppo);

            // instantiate original app
            String appInstanceId = mecmService.createInstanceFromAppo(context, pkgName, ip, paramsMap);
            context.put(APP_INSTANCE_ID, appInstanceId);
            if (appInstanceId != null) {
                LOGGER.info("instantiate finished, original appInstanceId: {}", appInstanceId);
                MecMPackageDeploymentInfo infoFinishedFromAppo = MecMPackageDeploymentInfo.builder().id(deploymentId).
                        mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm).
                        appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).appInstanceId(appInstanceId).statusCode(4).status(FINISHED_STATUS).build();
                mecMDeploymentMapper.insertPkgDeploymentInfo(infoFinishedFromAppo);
            } else {
                LOGGER.error("instantiate failed, original appInstanceId: {}", appInstanceId);
                MecMPackageDeploymentInfo infoFinishedFromAppo = MecMPackageDeploymentInfo.builder().id(deploymentId).
                        mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm).
                        appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).statusCode(6).status(FAILED_TO_INSTANTIATE_STATUS).build();
                mecMDeploymentMapper.insertPkgDeploymentInfo(infoFinishedFromAppo);
            }
        }

        ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_SUCCESS, null);
        return ResponseEntity.ok(new ResponsePkgPost(mecmPackageId, 0, errMsg, "create server in progress"));
    }

    public ResponseEntity<ResponseOfStatus> getPkgDisAndInsStatus(RequestCheckBody checkBody, String access_token) {
        String mecmPackageId = checkBody.getMecmPackageId();
        List<MecMPackageDeploymentInfo> statusList = mecMDeploymentMapper.getMecMPkgDeploymentInfoByPkgId(mecmPackageId);
/*        List<StatusResponseBody> resList = new LinkedList<>();
        for (MecMPackageDeploymentInfo info:statusList) {
            StatusResponseBody res = new StatusResponseBody();
            res.setHostIp(info.getHostIp());
            res.setRetCode();
        }*/
        return ResponseEntity.ok(ResponseOfStatus.builder().mecmPackageId(mecmPackageId).message("Query server success").
                retCode(0).data(statusList).build());
    }

    public ResponseEntity<ResponseOfStatus> deletePackageDisAndInsStatus(RequestCheckBody checkBody, String access_token) {

        String mecmPackageId = checkBody.getMecmPackageId();
        List<MecMPackageDeploymentInfo> statusList = mecMDeploymentMapper.getMecMPkgDeploymentInfoByPkgId(mecmPackageId);
    //    String[] hostList = mecMPackageMapper.getMecMPkgInfoByPkgId(mecmPackageId).getHostIps().split(",");
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);
        context.put(ACCESS_TOKEN, access_token);
        context.put(TENANT_ID, checkBody.getTenantId());
        List<StatusResponseBody> dataList = new LinkedList<>();

        for (MecMPackageDeploymentInfo deployment: statusList) {
            String appInstanceId = deployment.getAppInstanceId();
            String ip = deployment.getHostIp();
            context.put(PACKAGE_ID,deployment.getAppIdFromApm());

            mecmService.delay();
            String res = deleteDeployment(appInstanceId,ip,context);

           if (SUCCESS.equals(res)){
               StatusResponseBody tmpRes= StatusResponseBody.builder().hostIp(ip).retCode(0).message("Delete server success").build();
               dataList.add(tmpRes);
           }else if(UNINSTANTIATE_APP_FAILED.equals(res)){
               StatusResponseBody tmpRes= StatusResponseBody.builder().hostIp(ip).retCode(2).message("fail to delete instantiation").build();
               dataList.add(tmpRes);
           }else {
               StatusResponseBody tmpRes= StatusResponseBody.builder().hostIp(ip).retCode(1).message("fail to delete package").build();
               dataList.add(tmpRes);
           }
        }

        return ResponseEntity.ok(ResponseOfStatus.builder().mecmPackageId(mecmPackageId).message("Delete server success").
                retCode(0).data(dataList).build());
    }


    private boolean foreCheck(RequestPkgBody pkgBody) {
        LOGGER.info("begin to fore check pkgBody");

        String pkgName = pkgBody.getAppPkgName();
        String pkgVersion = pkgBody.getAppPkgVersion();
        String[] hostList = pkgBody.getHostList();
        Map<String, Object> paramsMap = pkgBody.getParamsMap();
        String tenantId = pkgBody.getTenantId();
        MultipartFile file = pkgBody.getFile();

        if (StringUtils.isEmpty(pkgName)) {
            LOGGER.error("pkgName is empty, check if pkgName is right");
            return false;
        }
        if (StringUtils.isEmpty(pkgVersion)) {
            LOGGER.error("pkgVersion is empty, check if pkgVersion is right");
            return false;
        }
        if (hostList == null || hostList.length == 0) {
            LOGGER.error("hostList is empty, check if hostList is right");
            return false;
        }
        if (StringUtils.isEmpty(tenantId)) {
            LOGGER.error("tenantId is empty, check if tenantId is right");
            return false;
        }
        if (file.isEmpty()) {
            LOGGER.error("uploadFile is empty, check if uploadFile is right");
            return false;
        }
        if (paramsMap == null || paramsMap.size() == 0) {
            LOGGER.warn("paramsMap is empty, check if paramsMap is right");
        }
        return true;
    }

    private String listToIps(String[] list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.length; i++) {
            sb.append(list[i]);
            if (i != list.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * execute test case.
     *
     * @param context context info
     * @return execute result
     */
    public String deleteDeployment(String appInstanceId, String hostIp, Map<String, String> context) {
        mecmService.delay();
        if (null == appInstanceId) {
            LOGGER.info("appInstanceId is null, return success.");
            return SUCCESS;
        }
        if (!mecmService.deleteAppInstance(appInstanceId, context)) {
            return UNINSTANTIATE_APP_FAILED;
        }
        if (!mecmService.deleteEdgePackage(context, hostIp)) {
            return DELETE_EDGE_PKG_FAILED;
        }
        if (!mecmService.deleteApmPackage(context)) {
            return DELETE_APM_PKG_FAILED;
        }
        return SUCCESS;
    }


}
