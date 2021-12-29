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

package org.edgegallery.mecm.north.facade;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.edgegallery.mecm.north.controller.advice.RequestCheckBody;
import org.edgegallery.mecm.north.controller.advice.RequestPkgBody;
import org.edgegallery.mecm.north.controller.advice.ResponseOfStatus;
import org.edgegallery.mecm.north.controller.advice.ResponsePkgPost;
import org.edgegallery.mecm.north.controller.advice.StatusResponseBody;
import org.edgegallery.mecm.north.domain.ResponseConst;
import org.edgegallery.mecm.north.model.MecmPackageDeploymentInfo;
import org.edgegallery.mecm.north.model.MecmPackageInfo;
import org.edgegallery.mecm.north.repository.mapper.MecmDeploymentMapper;
import org.edgegallery.mecm.north.repository.mapper.MecmPackageMapper;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.CommonUtil;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.edgegallery.mecm.north.utils.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private MecmPackageMapper mecMPackageMapper;

    @Autowired
    private MecmDeploymentMapper mecMDeploymentMapper;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    @Value("${serveraddress.appo}")
    private String appoServerAddress;

    /**
     * uploadAndInstantiatePkg.
     */
    public ResponseEntity<ResponsePkgPost> uploadAndInstantiatePkg(RequestPkgBody pkgBody, String accessToken) {
        LOGGER.info("begin to upload and instantiate package in facade");
        if (!foreCheck(pkgBody)) {
            LOGGER.error("pkgBody has empty field, pls check pkgBody");
            ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_FAIL, null);
            return ResponseEntity.ok(new ResponsePkgPost("", 1, errMsg, "Failed to create server"));
            // throw new AppException("pkgBody has empty field, pls check pkgBody", RET_PKG_FILED_EMPTY_FAILED);
        }

        String pkgName = pkgBody.getAppPkgName();
        LOGGER.info("get package name is {}", pkgName);
        String pkgVersion = pkgBody.getAppPkgVersion();
        LOGGER.info("get package Version is {}", pkgVersion);
        String[] hostList = pkgBody.getHostList().split(";");
        String appClass = pkgBody.getAppClass();
        //String[] paramsMap = pkgBody.getParameters().split(";");
        String parameters = pkgBody.getParameters();
        // JSONObject obj = JSONObject.parseObject(parameters);
        String tenantId = pkgBody.getTenantId();

        String mecmPackageId = UUID.randomUUID().toString();
        String saveFilePath = mecmService.saveFileToLocal(pkgBody.getFile(), mecmPackageId);
        LOGGER.info("save file path is {}", saveFilePath);
        LOGGER.info("begin to upload and instantiate package in facade");

        MecmPackageInfo mecMPackageInfo = MecmPackageInfo.builder().mecmPackageId(mecmPackageId).mecmPkgName(pkgName)
            .mecmPkgVersion(pkgVersion).mecmAppClass(appClass).tenantId(tenantId).hostIps(listToIps(hostList))
            .status(DISTRIBUTING_STATUS).token(accessToken).saveFilePath(saveFilePath).build();
        mecMPackageMapper.insertMecmPkgInfo(mecMPackageInfo);
        LOGGER.info("create package info in database");

        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(Constant.APP_NAME, pkgName);
        packageInfo.put(Constant.APP_VERSION, pkgVersion);

        for (String ip : hostList) {

            LOGGER.info("uploading pkg to ip: {}", ip);
            LOGGER.info("apmServerAddress is: {}", apmServerAddress);
            LOGGER.info("appoServerAddress is: {}", appoServerAddress);
            LOGGER.info("the class of this app is: {}", appClass);

            Map<String, String> context = new HashMap<>();
            context.put("apmServerAddress", apmServerAddress);
            context.put("appoServerAddress", appoServerAddress);
            context.put(Constant.ACCESS_TOKEN, accessToken);
            context.put(Constant.TENANT_ID, pkgBody.getTenantId());
            context.put(APP_CLASS, appClass);

            String deploymentId = UUID.randomUUID().toString();
            ResponseEntity<String> response = mecmService.uploadFileToApm(saveFilePath, context, ip, packageInfo);
            if (null == response || !(HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED.equals(
                response.getStatusCode()))) {
                LOGGER.error("fail to upload file with ip: {}", ip);
                LOGGER.error("uploadFileToAPM failed to , response: {}", response);
                MecmPackageDeploymentInfo info = MecmPackageDeploymentInfo.builder().id(deploymentId)
                    .mecmPackageId(mecmPackageId).mecmPkgName(pkgName).hostIp(ip).statusCode(Constant.STATUS_ERROR)
                    .status(Constant.DISTRIBUTE_ERROR_STATUS).params(parameters).build();
                mecMDeploymentMapper.insertPkgDeploymentInfo(info);
            }
            JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
            String appIdFromApm = jsonObject.get("appId").getAsString();
            String appPkgIdFromApm = jsonObject.get("appPackageId").getAsString();

            MecmPackageDeploymentInfo info = MecmPackageDeploymentInfo.builder().id(deploymentId)
                .mecmPackageId(mecmPackageId).mecmPkgName(pkgName).appIdFromApm(appIdFromApm)
                .appPkgIdFromApm(appPkgIdFromApm).hostIp(ip).statusCode(Constant.STATUS_DISTRIBUTING)
                .status(DISTRIBUTING_STATUS).params(parameters).build();
            mecMDeploymentMapper.insertPkgDeploymentInfo(info);
            LOGGER.info("upload pkg to ip:{} success", ip);
        }

        ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_SUCCESS, null);
        return ResponseEntity.ok(new ResponsePkgPost(mecmPackageId, 0, errMsg, "create server in progress"));
    }

    /**
     * get PkgDisAndIns Status.
     *
     * @param checkBody body
     * @return ResponseOfStatus
     */
    public ResponseEntity<ResponseOfStatus> getPkgDisAndInsStatus(RequestCheckBody checkBody) {

        String mecmPackageId = checkBody.getMecmPackageId();
        LOGGER.info("begin to get package distribution and instantiation status with mecmPkgId:{}", mecmPackageId);
        List<MecmPackageDeploymentInfo> statusList = mecMDeploymentMapper.getMecMPkgDeploymentInfoByPkgId(
            mecmPackageId);
        return ResponseEntity.ok(
            ResponseOfStatus.builder().mecmPackageId(mecmPackageId).message("Query server success").retCode(0)
                .data(statusList).build());
    }

    /**
     * delete  Status.
     *
     * @param checkBody body
     * @param accessToken token
     * @return ResponseOfStatus
     */
    public ResponseEntity<ResponseOfStatus> deletePackageDisAndInsStatus(RequestCheckBody checkBody,
        String accessToken) {
        String mecmPackageId = checkBody.getMecmPackageId();
        LOGGER.info("begin to delete pkg distribution and instantiation status with mecmPackageId:{}", mecmPackageId);
        //    String[] hostList = mecMPackageMapper.getMecMPkgInfoByPkgId(mecmPackageId).getHostIps().split(",");

        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);
        context.put(Constant.ACCESS_TOKEN, accessToken);
        context.put(Constant.TENANT_ID, checkBody.getTenantId());
        List<MecmPackageDeploymentInfo> statusList = mecMDeploymentMapper.getMecMPkgDeploymentInfoByPkgId(
            mecmPackageId);
        List<StatusResponseBody> dataList = new LinkedList<>();

        for (MecmPackageDeploymentInfo deployment : statusList) {
            String ip = deployment.getHostIp();
            LOGGER.info("begin to delete pkg deployment from host ip:{}", ip);
            context.put(Constant.PACKAGE_ID, deployment.getAppPkgIdFromApm());
            String appInstanceId = deployment.getAppInstanceId();
            mecmService.delay();
            String res = deleteDeployment(appInstanceId, ip, context);

            if (Constant.SUCCESS.equals(res)) {
                LOGGER.info("Delete server success from ip:{}", ip);
                StatusResponseBody tmpRes = StatusResponseBody.builder().hostIp(ip).retCode(0)
                    .message("Delete server success").build();
                dataList.add(tmpRes);
            } else if (UNINSTANTIATE_APP_FAILED.equals(res)) {
                LOGGER.info("fail to delete instantiation from ip:{}", ip);
                StatusResponseBody tmpRes = StatusResponseBody.builder().hostIp(ip).retCode(2)
                    .message("fail to delete instantiation").build();
                dataList.add(tmpRes);
            } else {
                LOGGER.info("fail to delete package from ip:{}", ip);
                StatusResponseBody tmpRes = StatusResponseBody.builder().hostIp(ip).retCode(1)
                    .message("fail to delete package").build();
                dataList.add(tmpRes);
            }
        }
        CommonUtil.deleteFile(mecMPackageMapper.getMecmPkgInfoByPkgId(mecmPackageId).getSaveFilePath());
        mecMDeploymentMapper.deletePkgDeploymentInfoByPkgId(mecmPackageId);
        mecMPackageMapper.deletePkgInfoByPkgId(mecmPackageId);

        LOGGER.info("finish deleting pkg distribution and instantiation status with mecmPackageId:{}", mecmPackageId);
        return ResponseEntity.ok(
            ResponseOfStatus.builder().mecmPackageId(mecmPackageId).message("Delete server success").retCode(0)
                .data(dataList).build());
    }

    private boolean foreCheck(RequestPkgBody pkgBody) {

        LOGGER.info("begin to fore check pkgBody");

        if (StringUtils.isEmpty(pkgBody.getAppPkgName())) {
            LOGGER.error("pkgName is empty, check if pkgName is right");
            return false;
        }

        if (StringUtils.isEmpty(pkgBody.getAppPkgVersion())) {
            LOGGER.error("pkgVersion is empty, check if pkgVersion is right");
            return false;
        }

        if (StringUtils.isEmpty(pkgBody.getAppClass())) {
            LOGGER.error("AppClass is empty, check if AppClass is right");
            return false;
        }

        if (StringUtils.isEmpty(pkgBody.getHostList())) {
            LOGGER.error("hostList is empty, check if hostList is right");
            return false;
        }

        if (StringUtils.isEmpty(pkgBody.getTenantId())) {
            LOGGER.error("tenantId is empty, check if tenantId is right");
            return false;
        }

        if (pkgBody.getFile().isEmpty()) {
            LOGGER.error("uploadFile is empty, check if uploadFile is right");
            return false;
        }

        if (StringUtils.isEmpty(pkgBody.getParameters())) {
            LOGGER.warn("Parameters is empty, check if paramsMap is right");
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
            return Constant.SUCCESS;
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
        return Constant.SUCCESS;
    }

}
