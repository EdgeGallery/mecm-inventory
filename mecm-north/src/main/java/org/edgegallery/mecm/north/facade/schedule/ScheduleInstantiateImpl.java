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

@Service("ScheduleInstantiateImpl")
public class ScheduleInstantiateImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDistributeImpl.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecmPackageMapper mecMPackageMapper;

    @Autowired
    private MecmDeploymentMapper mecMDeploymentMapper;

    @Value("${serveraddress.appo}")
    private String appoServerAddress;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    /**
     * Create Instantiate.
     *
     * @param subJob object of job
     */
    public void createInstantiate(MecmPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);
        LOGGER.info("before create instance from appo, apmServerAddress:{}", apmServerAddress);
        LOGGER.info("before create instance from appo, appoServerAddress:{}", appoServerAddress);
        MecmPackageInfo mecmPkg = mecMPackageMapper.getMecmPkgInfoByPkgId(subJob.getMecmPackageId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());
        context.put(Constant.PACKAGE_ID, subJob.getAppPkgIdFromApm());
        context.put(Constant.APP_ID, subJob.getAppIdFromApm());
        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());

        LOGGER.info("before create instance from appo, access_token:{}", mecmPkg.getToken());
        LOGGER.info("before create instance from appo, TenantId:{}", mecmPkg.getTenantId());
        LOGGER.info("before create instance from appo, APP_CLASS:{}", mecmPkg.getMecmAppClass());
        LOGGER.info("before create instance from appo, PACKAGE_IDfromApm:{}", subJob.getAppPkgIdFromApm());
        LOGGER.info("before create instance from appo, APP_ID:{}",  subJob.getAppIdFromApm());
        LOGGER.info("before create instance from appo, hostIp:{}",  subJob.getHostIp());

        //Create app instance from appo to get appInstanceId
        String appInstanceId = mecmService.createInstanceFromAppoOnce(context, subJob.getMecmPkgName(),
            subJob.getHostIp());
        String statusStr;
        int statusCode;
        if (appInstanceId == null) {
            statusStr = Constant.CREATE_ERROR;
            statusCode = Constant.STATUS_ERROR;
            LOGGER.error("fail to create app instance from appo, since appInstanceId is null");
        } else {
            context.put(Constant.APP_INSTANCE_ID, appInstanceId);
            subJob.setAppInstanceId(appInstanceId);
            LOGGER.info(" appInstanceId:{}", appInstanceId);

            //get status probably is Instantiated
            String status = mecmService.getApplicationInstanceOnce(context, appInstanceId);

            if (status.equals("Created")) {
                statusStr = Constant.CREATED;
                statusCode = Constant.STATUS_INSTANTIATING;
            } else if (status.equals("Creating")) {
                statusStr = Constant.CREATING;
                statusCode = Constant.STATUS_INSTANTIATING;
            } else {
                LOGGER.info("while create instance error, get application instance status from appo: {}", status);
                statusStr = Constant.INSTANTIATE_ERROR_STATUS;
                statusCode = Constant.STATUS_ERROR;
            }
            LOGGER.info("after Created request, status:{}", status);
            LOGGER.info("after Created request, statusStr:{}", statusStr);
            LOGGER.info("after Created request, statusCode:{}", statusCode);
            LOGGER.info("after Created ,check package status finished. package id is: " + subJob.getMecmPackageId());
        }

        MecmPackageDeploymentInfo infoGetFromAppo = MecmPackageDeploymentInfo.builder().id(subJob.getId())
            .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
            .appIdFromApm(subJob.getAppIdFromApm()).appPkgIdFromApm(subJob.getAppPkgIdFromApm())
            .startTime(subJob.getStartTime()).hostIp(subJob.getHostIp()).statusCode(statusCode)
            .appInstanceId(appInstanceId).status(statusStr).params(subJob.getParams()).build();
        subJob.setStatus(statusStr);
        subJob.setStatusCode(statusCode);
        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromAppo);
    }

    /**
     * Execute Instantiate.
     *
     * @param subJob object of job
     */
    public void executeInstantiate(MecmPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecmPackageInfo mecmPkg = mecMPackageMapper.getMecmPkgInfoByPkgId(subJob.getMecmPackageId());

        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());
        context.put(Constant.PACKAGE_ID, subJob.getAppPkgIdFromApm());
        context.put(Constant.APP_ID, subJob.getAppIdFromApm());
        Map<String, Object> paramsMap = CommonUtil.handleParams(subJob.getParams());
        String appInstanceId = subJob.getAppInstanceId();

        // instantiate original app
        String statusStr = mecmService.instantiateAppFromAppoOnce(context, paramsMap, appInstanceId);
        int statusCode;
        LOGGER.info("while instantiate once, get application instance status from appo: {}", statusStr);
        if (statusStr.equals(Constant.INSTANTIATING_STATUS)) {
            statusStr = Constant.INSTANTIATING_STATUS;
            statusCode = Constant.STATUS_INSTANTIATING;
        } else {
            LOGGER.error("while instance error, get application instance status from appo: {}", statusStr);
            statusStr = Constant.INSTANTIATE_ERROR_STATUS;
            statusCode = Constant.STATUS_ERROR;
        }
        MecmPackageDeploymentInfo infoGetFromAppo = MecmPackageDeploymentInfo.builder().id(subJob.getId())
            .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
            .appIdFromApm(subJob.getAppIdFromApm()).appPkgIdFromApm(subJob.getAppPkgIdFromApm())
            .startTime(subJob.getStartTime()).hostIp(subJob.getHostIp()).statusCode(statusCode)
            .appInstanceId(appInstanceId).status(statusStr).params(subJob.getParams()).build();
        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromAppo);

        LOGGER.info("after instantiate once status:{}", statusStr);
        LOGGER.info("after instantiate once statusCode:{}", statusCode);
        LOGGER.info("after instantiate once package id is: " + subJob.getMecmPackageId());
        subJob.setStatus(statusStr);
        subJob.setStatusCode(statusCode);
    }

    /**
     * Query Instantiate.
     *
     * @param subJob object of job
     */
    public void queryInstantiate(MecmPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecmPackageInfo mecmPkg = mecMPackageMapper.getMecmPkgInfoByPkgId(subJob.getMecmPackageId());

        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());

        context.put(Constant.APP_INSTANCE_ID, subJob.getAppInstanceId());

        String status = mecmService.getApplicationInstanceOnce(context, subJob.getAppInstanceId());
        String statusStr;
        int statusCode;
        if (status.equalsIgnoreCase(Constant.CREATED)) {
            statusStr = Constant.CREATED;
            statusCode = Constant.STATUS_CREATED;
        } else if (status.equalsIgnoreCase(Constant.INSTANTIATED_STATUS)) {
            statusStr = Constant.FINISHED_STATUS;
            statusCode = Constant.STATUS_FINISHED;
        } else if (status.equalsIgnoreCase(Constant.CREATING) || status.equalsIgnoreCase(
            Constant.INSTANTIATING_STATUS)) {
            statusStr = status;
            statusCode = Constant.STATUS_INSTANTIATING;
        } else {
            LOGGER.info("while query instance error, get application instance status from appo: {}", status);
            statusStr = Constant.INSTANTIATE_ERROR_STATUS;
            statusCode = Constant.STATUS_ERROR;
        }
        LOGGER.info("after query status:{}", status);
        LOGGER.info("after query statusCode:{}", statusCode);
        LOGGER.info("package query status finished. package id is: " + subJob.getMecmPackageId());
        MecmPackageDeploymentInfo infoGetFromAppo = MecmPackageDeploymentInfo.builder().id(subJob.getId())
            .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName())
            .appIdFromApm(subJob.getAppIdFromApm()).appPkgIdFromApm(subJob.getAppPkgIdFromApm())
            .startTime(subJob.getStartTime()).hostIp(subJob.getHostIp()).statusCode(statusCode)
            .appInstanceId(subJob.getAppInstanceId()).params(subJob.getParams()).status(statusStr).build();
        subJob.setStatus(statusStr);
        subJob.setStatusCode(statusCode);

        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromAppo);
    }
}