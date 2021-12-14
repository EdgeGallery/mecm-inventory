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

import org.apache.commons.lang3.StringUtils;
import org.edgegallery.mecmNorth.controller.advice.RequestPkgBody;
import org.edgegallery.mecmNorth.controller.advice.ResponseObject;
import org.edgegallery.mecmNorth.domain.ResponseConst;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecmNorth.model.MecMPackageInfo;
import org.edgegallery.mecmNorth.model.mapper.MecMDeploymentMapper;
import org.edgegallery.mecmNorth.model.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.exception.AppException;
import org.edgegallery.mecmNorth.utils.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.edgegallery.mecmNorth.constant.ResponseConst.RET_PKG_FILED_EMPTY_FAILED;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MecmPackageServiceFacade.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecMPackageMapper mecMPackageMapper;

    @Autowired
    private MecMDeploymentMapper mecMDeploymentMapper;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    public ResponseEntity<ResponseObject> uploadAndInstantiatePkg(RequestPkgBody pkgBody, String access_token) {

        if (!foreCheck(pkgBody)) {
            LOGGER.error("pkgBody has empty field, pls check pkgBody");
            throw new AppException("pkgBody has empty field, pls check pkgBody", RET_PKG_FILED_EMPTY_FAILED);
        }

        String pkgName = pkgBody.getAppPkgName();
        String pkgVersion = pkgBody.getAppPkgVersion();
        String[] hostList = pkgBody.getHostList();
        Map<String, String> paramsMap = pkgBody.getParamsMap();
        String tenantId = pkgBody.getTenantId();

        String mecmPackageId = UUID.randomUUID().toString();
        String saveFilePath = mecmService.saveFileToLocal(pkgBody.getFile(), mecmPackageId);

        MecMPackageInfo mecMPackageInfo = MecMPackageInfo.builder().mecmPackageId(mecmPackageId).
                mecmPkgName(pkgName).mecmPkgVersion(pkgVersion).tenantId(tenantId).hostIps(listToIps(hostList)).
                status(DISTRIBUTING_STATUS).build();

        mecMPackageMapper.insertMecmPkgInfo(mecMPackageInfo);
        LOGGER.info("create package info in database");

        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put(ACCESS_TOKEN, access_token);
        context.put(TENANT_ID, pkgBody.getTenantId());

        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(APP_NAME, pkgName);
        packageInfo.put(APP_VERSION, pkgVersion);

        for (String ip : hostList) {
            String deploymentId = UUID.randomUUID().toString();
            ResponseEntity<String> response = mecmService.uploadFileToAPM(saveFilePath, context, ip, packageInfo);
            if (null == response || !(HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED
                    .equals(response.getStatusCode()))) {
                LOGGER.error("fail to upload file with ip: " + ip);
                LOGGER.error("uploadFileToAPM failed to , response: {}", response);
                MecMPackageDeploymentInfo info = MecMPackageDeploymentInfo.builder().id(deploymentId).
                        mecmPackageId(mecmPackageId).mecmPkgName(pkgName).hostIp(ip).status(FAIL_TO_DISTRIBUTE_STATUS).build();
                mecMDeploymentMapper.insertPkgDeploymentInfo(info);
            }
            MecMPackageDeploymentInfo info = MecMPackageDeploymentInfo.builder().id(deploymentId).
                    mecmPackageId(mecmPackageId).mecmPkgName(pkgName).hostIp(ip).status(DISTRIBUTING_STATUS).build();
            mecMDeploymentMapper.insertPkgDeploymentInfo(info);
        }




        ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_SUCCESS, null);
        return ResponseEntity.ok(new ResponseObject("upload success", errMsg, "deactivate order success."));
    }

    private boolean foreCheck(RequestPkgBody pkgBody) {
        LOGGER.info("begin to fore check pkgBody");

        String pkgName = pkgBody.getAppPkgName();
        String pkgVersion = pkgBody.getAppPkgVersion();
        String[] hostList = pkgBody.getHostList();
        Map<String, String> paramsMap = pkgBody.getParamsMap();
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


}
