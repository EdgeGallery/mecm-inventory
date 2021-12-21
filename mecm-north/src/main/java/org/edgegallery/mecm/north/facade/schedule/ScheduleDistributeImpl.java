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
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ScheduleDistributeImpl")
public class ScheduleDistributeImpl {

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
     * Query Distribute status.
     *
     * @param subJob object of job
     */
    public void queryDistribute(MecmPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecmPackageInfo mecmPkg = mecMPackageMapper.getMecmPkgInfoByPkgId(subJob.getMecmPackageId());

        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());

        MecmPackageDeploymentInfo infoGetFromApm;
        String status = mecmService.getApmPackageOnce(context, subJob.getAppPkgIdFromApm(), subJob.getHostIp());
        if (status.equals(Constant.DISTRIBUTED_STATUS)) {
            LOGGER.info("fail to distribute package, the mecm package id is:{}", subJob.getMecmPackageId());
            LOGGER.info("fail to distribute this package to ip:{}", subJob.getHostIp());
            infoGetFromApm = MecmPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).appIdFromApm(subJob
                    .getAppIdFromApm()).appPkgIdFromApm(subJob.getAppPkgIdFromApm()).startTime(subJob.getStartTime())
                .hostIp(subJob.getHostIp()).statusCode(Constant.STATUS_DISTRIBUTED).status(Constant.DISTRIBUTED_STATUS)
                .build();
            subJob.setStatus(Constant.DISTRIBUTED_STATUS);
            subJob.setStatusCode(Constant.STATUS_DISTRIBUTED);
        } else {
            infoGetFromApm = MecmPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
                .hostIp(subJob.getHostIp()).statusCode(Constant.STATUS_DISTRIBUTING)
                .status(Constant.INSTANTIATING_STATUS).build();
            subJob.setStatus(Constant.INSTANTIATING_STATUS);
            subJob.setStatusCode(Constant.STATUS_DISTRIBUTING);
        }

        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }
}
