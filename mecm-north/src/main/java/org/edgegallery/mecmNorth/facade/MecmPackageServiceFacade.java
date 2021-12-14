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

import org.edgegallery.mecmNorth.controller.advice.RequestPkgBody;
import org.edgegallery.mecmNorth.controller.advice.ResponseObject;
import org.edgegallery.mecmNorth.domain.ResponseConst;
import org.edgegallery.mecmNorth.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.edgegallery.mecmNorth.utils.constant.Constant.*;

@Service("MecmPackageServiceFacade")
public class MecmPackageServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecmPackageServiceFacade.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecMPackageMapper mecMPackageMapper;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    public ResponseEntity<ResponseObject> uploadAndInstantiatePkg(RequestPkgBody pkgBody, String access_token) {
        String mecmPackageId = UUID.randomUUID().toString();
        String saveFilePath = mecmService.saveFileToLocal(pkgBody.getFile(), mecmPackageId);

        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put(ACCESS_TOKEN, access_token);
        context.put(TENANT_ID, pkgBody.getTenantId());

        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(APP_NAME, pkgBody.getAppPkgName());
        packageInfo.put(APP_VERSION, pkgBody.getAppPkgVersion());

        String[] hostIpList = pkgBody.getHostList();
        for (String ip : hostIpList) {
            ResponseEntity<String> res = mecmService.uploadFileToAPM(saveFilePath, context, ip, packageInfo);
            if (res == null){
                LOGGER.error("fail to upload file with ip: "+ ip);

            }
        }


        ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_SUCCESS, null);
        return ResponseEntity.ok(new ResponseObject("deactivate order success", errMsg, "deactivate order success."));
        0
    }


}
