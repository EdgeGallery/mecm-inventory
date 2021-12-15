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
import org.edgegallery.mecm.north.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecm.north.model.MecMPackageInfo;
import org.edgegallery.mecm.north.repository.mapper.MecMDeploymentMapper;
import org.edgegallery.mecm.north.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.InitParamsUtil;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ScheduleImplementFacade")
public class ScheduleInstantiateImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDistributeImpl.class);

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

    /**
     * Execute Instantiate.
     *
     * @param subJob object of job
     */
    public void executeInstantiate(MecMPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecMPackageInfo mecmPkg = mecMPackageMapper.getMecMPkgInfoByPkgId(subJob.getMecmPackageId());

        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());

        Map<String, Object> paramsMap = InitParamsUtil.handleParams(subJob.getParams());

        // instantiate original app
        String appInstanceId = mecmService.createInstanceFromAppoOnce(context, subJob.getMecmPkgName(),
            subJob.getHostIp(), paramsMap);
        context.put(Constant.APP_INSTANCE_ID, appInstanceId);

        MecMPackageDeploymentInfo infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId())
            .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob.getHostIp())
            .statusCode(Constant.STATUS_INSTANTIATING).appInstanceId(appInstanceId)
            .status(Constant.INSTANTIATING_STATUS).build();
        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }

    /**
     * Query Instantiate.
     *
     * @param subJob object of job
     */
    public void queryInstantiate(MecMPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecMPackageInfo mecmPkg = mecMPackageMapper.getMecMPkgInfoByPkgId(subJob.getMecmPackageId());

        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());

        context.put(Constant.APP_INSTANCE_ID, subJob.getAppInstanceId());

        String status = mecmService.getApplicationInstanceOnce(context, subJob.getAppInstanceId());
        MecMPackageDeploymentInfo infoGetFromApm;
        if (!status.equals(Constant.INSTANTIATED_STATUS) && !status.equals(Constant.INSTANTIATING_STATUS)) {
            LOGGER.error("Error happens when instantiating.");
            status = Constant.INSTANTIATE_ERROR_STATUS;
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
                .hostIp(subJob.getHostIp()).statusCode(Constant.STATUS_ERROR).appInstanceId(subJob.getAppInstanceId())
                .status(status).build();
        } else {
            LOGGER.error("package status finished. package id is: " + subJob.getMecmPackageId());
            status = Constant.FINISHED_STATUS;
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
                .hostIp(subJob.getHostIp()).statusCode(Constant.STATUS_FINISHED)
                .appInstanceId(subJob.getAppInstanceId()).status(status).build();
        }

        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }
}
