package org.edgegallery.mecmNorth.facade;

import java.util.ArrayList;
import java.util.List;
import org.edgegallery.mecmNorth.facade.scheduleiml.ScheduleDistributeImpl;
import org.edgegallery.mecmNorth.facade.scheduleiml.ScheduleInstantiateImpl;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecmNorth.repository.mapper.MecMDeploymentMapper;
import org.edgegallery.mecmNorth.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScheduleImplementFacade")
public class ScheduleImplementFacade {
    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecMPackageMapper mecMPackageMapper;

    @Autowired
    private MecMDeploymentMapper mecMDeploymentMapper;

    @Autowired
    private ScheduleDistributeImpl scheduleDistributeImpl;

    @Autowired
    private ScheduleInstantiateImpl scheduleInstantiate;

    private List<MecMPackageDeploymentInfo> scheduleCache = new ArrayList<>();

    public void loadScheduleJobs() {
        loadFromDB();

        executeJobs();
    }

    private void executeJobs() {
        for(MecMPackageDeploymentInfo subJob: scheduleCache) {

            if(subJob.getStatus().equals(Constant.DISTRIBUTING_STATUS)) {
                scheduleDistributeImpl.queryDistribute(subJob);
            }

            if(subJob.getStatus().equals(Constant.DISTRIBUTED_STATUS)) {
                scheduleInstantiate.executeInstatiate(subJob);
            }

            if(subJob.getStatus().equals(Constant.INSTANTIATING_STATUS)) {
                scheduleInstantiate.queryInstantiate(subJob);
            }

        }
    }

    private void loadFromDB() {
        List<MecMPackageDeploymentInfo> runningJobs = mecMDeploymentMapper.getMecMPkgDeploymentInfos();
        addToCache(runningJobs);
    }

    private void addToCache(List<MecMPackageDeploymentInfo> runningJobs) {
        for(MecMPackageDeploymentInfo item: runningJobs) {
            if(checkIfContains(item)) {
                scheduleCache.add(item);
            }
        }
    }

    private boolean checkIfContains(MecMPackageDeploymentInfo item) {
        for(MecMPackageDeploymentInfo subJob: scheduleCache) {
            if(subJob.getAppIdFromApm().equals(item.getAppIdFromApm())) {
                return true;
            }
        }
        return false;
    }
}
