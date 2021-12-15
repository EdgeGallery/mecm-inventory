package org.edgegallery.mecmNorth.facade.scheduleiml;

import static org.edgegallery.mecmNorth.utils.constant.Constant.APP_NAME;
import static org.edgegallery.mecmNorth.utils.constant.Constant.APP_VERSION;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecmNorth.model.MecMPackageInfo;
import org.edgegallery.mecmNorth.repository.mapper.MecMDeploymentMapper;
import org.edgegallery.mecmNorth.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.InitParamsUtil;
import org.edgegallery.mecmNorth.utils.constant.Constant;
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

    public void executeInstatiate(MecMPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecMPackageInfo mecmPkg = mecMPackageMapper
            .getMecMPkgInfoByPkgId(subJob.getMecmPackageId());


        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());


        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(APP_NAME, mecmPkg.getMecmPkgName());
        packageInfo.put(APP_VERSION, mecmPkg.getMecmPkgVersion());

        Map<String, Object> paramsMap = InitParamsUtil.handleParams(subJob.getParams());

        // instantiate original app
        String appInstanceId = mecmService.createInstanceFromAppoOnce(context, subJob.getMecmPkgName()
            , subJob.getHostIp(), paramsMap);
        context.put(Constant.APP_INSTANCE_ID, appInstanceId);

        MecMPackageDeploymentInfo infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId()).
            mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob
            .getHostIp()).statusCode(Constant.STATUS_INSTANTIATING).appInstanceId(appInstanceId).status(Constant
            .INSTANTIATING_STATUS).build();
        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }



    public void queryInstantiate(MecMPackageDeploymentInfo subJob) {
        Map<String, String> context = new HashMap<>();
        context.put("apmServerAddress", apmServerAddress);
        context.put("appoServerAddress", appoServerAddress);

        MecMPackageInfo mecmPkg = mecMPackageMapper
            .getMecMPkgInfoByPkgId(subJob.getMecmPackageId());


        context.put(Constant.ACCESS_TOKEN, mecmPkg.getToken());
        context.put(Constant.TENANT_ID, mecmPkg.getTenantId());
        context.put(Constant.APP_CLASS, mecmPkg.getMecmAppClass());


        Map<String, String> packageInfo = new HashMap<>();
        packageInfo.put(APP_NAME, mecmPkg.getMecmPkgName());
        packageInfo.put(APP_VERSION, mecmPkg.getMecmPkgVersion());

        context.put(Constant.APP_INSTANCE_ID, subJob.getAppInstanceId());

        String status = mecmService.getApplicationInstanceOnce(context, subJob.getAppInstanceId());
        MecMPackageDeploymentInfo infoGetFromApm;
        if(!status.equals(Constant.INSTANTIATED_STATUS) && !status.equals(Constant.INSTANTIATING_STATUS)) {
            LOGGER.error("Error happens when instantiating.");
            status = Constant.INSTANTIATE_ERROR_STATUS;
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId()).
                mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob
                .getHostIp()).statusCode(Constant.STATUS_ERROR).appInstanceId(subJob.getAppInstanceId()).status(status).build();
        } else {
            LOGGER.error("package status finished. package id is: " + subJob.getMecmPackageId());
            status = Constant.FINISHED_STATUS;
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId()).
                mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob
                .getHostIp()).statusCode(Constant.STATUS_FINISHED).appInstanceId(subJob.getAppInstanceId()).status(status).build();
        }

        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }
}
