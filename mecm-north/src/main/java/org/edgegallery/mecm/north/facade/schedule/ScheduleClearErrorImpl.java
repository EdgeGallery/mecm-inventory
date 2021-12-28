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

package org.edgegallery.mecm.north.facade.schedule;

import java.util.HashMap;
import java.util.Map;
import org.edgegallery.mecm.north.model.MecmPackageDeploymentInfo;
import org.edgegallery.mecm.north.model.MecmPackageInfo;
import org.edgegallery.mecm.north.repository.mapper.MecmDeploymentMapper;
import org.edgegallery.mecm.north.repository.mapper.MecmPackageMapper;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.CommonUtil;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ScheduleClearErrorImpl")
public class ScheduleClearErrorImpl {

    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDistributeImpl.class);

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
     * delete error status.
     *
     * @param subJob object of job
     */
    public void deleteErrorStatus(MecmPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);
        LOGGER.info("before create instance from appo, apmServerAddress:{}", apmServerAddress);
        LOGGER.info("before create instance from appo, appoServerAddress:{}", appoServerAddress);
        MecmPackageInfo mecmPkg = mecMPackageMapper.getMecmPkgInfoByPkgId(subJob.getMecmPackageId());
        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());
        context.put(Constant.PACKAGE_ID, subJob.getAppPkgIdFromApm());
        context.put(Constant.APP_ID, subJob.getAppIdFromApm());
        String status = subJob.getStatus();
        String appInstanceId = subJob.getAppInstanceId();

        mecmService.delay();
        if (null == appInstanceId) {
            LOGGER.info("error status appInstanceId is null, return success.");
        }

        if (status.equals(Constant.CREATE_ERROR) || status.equals(Constant.INSTANTIATE_ERROR_STATUS)) {
            if (!mecmService.deleteAppInstance(appInstanceId, context)) {
                LOGGER.error("clear error status,fail to delete instance with appinstanceId:{}", appInstanceId);
            }
            LOGGER.info("clear error status, delete instance with appInstanceId:{} success", appInstanceId);
        }
        if (!mecmService.deleteEdgePackage(context, subJob.getHostIp())) {
            LOGGER.error("clear error status,fail to delete edge package from ip:{}", subJob.getHostIp());
        } else {
            LOGGER.info("clear error status, delete edge package from ip:{} success", subJob.getHostIp());
        }

        if (!mecmService.deleteApmPackage(context)) {
            LOGGER.error("clear error status,fail to delete apm package with pkgId:{}",
                context.get(Constant.PACKAGE_ID));
        } else {
            LOGGER.info("clear error status, delete apm package with pkgId:{} success",
                context.get(Constant.PACKAGE_ID));
        }

        String mecmPackageId = subJob.getMecmPackageId();
        CommonUtil.deleteFile(mecMPackageMapper.getMecmPkgInfoByPkgId(mecmPackageId).getSaveFilePath());
        mecMDeploymentMapper.deletePkgDeploymentInfoByPkgId(mecmPackageId);
        mecMPackageMapper.deletePkgInfoByPkgId(mecmPackageId);

        LOGGER.info(
            "clear error status,finish deleting pkg distribution and instantiation status with mecmPackageId:{}",
            mecmPackageId);

    }
}
