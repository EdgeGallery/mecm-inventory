package org.edgegallery.mecmNorth.facade.scheduleiml;

import static org.edgegallery.mecmNorth.utils.constant.Constant.APP_NAME;
import static org.edgegallery.mecmNorth.utils.constant.Constant.APP_VERSION;
import static org.edgegallery.mecmNorth.utils.constant.Constant.PACKAGE_ID;
import java.util.HashMap;
import java.util.Map;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecmNorth.model.MecMPackageInfo;
import org.edgegallery.mecmNorth.repository.mapper.MecMDeploymentMapper;
import org.edgegallery.mecmNorth.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.constant.Constant;
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
    private MecMPackageMapper mecMPackageMapper;

    @Autowired
    private MecMDeploymentMapper mecMDeploymentMapper;

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    @Value("${serveraddress.appo}")
    private String appoServerAddress;


    public void queryDistribute(MecMPackageDeploymentInfo subJob) {
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

        MecMPackageDeploymentInfo infoGetFromApm;
        String status = mecmService.getApmPackageOnce(context, context.get(PACKAGE_ID), subJob.getHostIp());
        if (status.equals(Constant.DISTRIBUTED_STATUS)) {
            LOGGER.error("fail to distribute package, the mecm package id is:{}", subJob.getMecmPackageId());
            LOGGER.error("fail to distribute this package to ip:{}", subJob.getHostIp());
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob
                .getHostIp()).statusCode(Constant.STATUS_DISTRIBUTED).status(Constant.DISTRIBUTED_STATUS).build();
        } else {
            infoGetFromApm = MecMPackageDeploymentInfo.builder().id(subJob.getId())
                .mecmPackageId(subJob.getMecmPackageId()).mecmPkgName(subJob.getMecmPkgName()).hostIp(subJob
                .getHostIp()).statusCode(Constant.STATUS_DISTRIBUTING).status(Constant.INSTANTIATING_STATUS).build();
        }

        mecMDeploymentMapper.updateMecmPkgDeploymentInfo(infoGetFromApm);
    }
}
