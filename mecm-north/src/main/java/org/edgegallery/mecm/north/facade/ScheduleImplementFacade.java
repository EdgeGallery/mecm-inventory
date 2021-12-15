/*
 * Copyright 2021 Huawei Technologies Co., Ltd.
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
 *
 */

package org.edgegallery.mecm.north.facade;

import java.util.ArrayList;
import java.util.List;
import org.edgegallery.mecm.north.facade.schedule.ScheduleDistributeImpl;
import org.edgegallery.mecm.north.facade.schedule.ScheduleInstantiateImpl;
import org.edgegallery.mecm.north.model.MecMPackageDeploymentInfo;
import org.edgegallery.mecm.north.repository.mapper.MecMDeploymentMapper;
import org.edgegallery.mecm.north.repository.mapper.MecMPackageMapper;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.constant.Constant;
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

    /**
     * loadScheduleJobs for schedule.
     */
    public void loadScheduleJobs() {
        loadFromDB();

        executeJobs();
    }

    private void executeJobs() {
        for (MecMPackageDeploymentInfo subJob : scheduleCache) {

            if (subJob.getStatus().equals(Constant.DISTRIBUTING_STATUS)) {
                scheduleDistributeImpl.queryDistribute(subJob);
            }

            if (subJob.getStatus().equals(Constant.DISTRIBUTED_STATUS)) {
                scheduleInstantiate.executeInstantiate(subJob);
            }

            if (subJob.getStatus().equals(Constant.INSTANTIATING_STATUS)) {
                scheduleInstantiate.queryInstantiate(subJob);
            }

        }
    }

    private void loadFromDB() {
        List<MecMPackageDeploymentInfo> runningJobs = mecMDeploymentMapper.getMecMPkgDeploymentInfos();
        addToCache(runningJobs);
    }

    private void addToCache(List<MecMPackageDeploymentInfo> runningJobs) {
        for (MecMPackageDeploymentInfo item : runningJobs) {
            if (checkIfContains(item)) {
                scheduleCache.add(item);
            }
        }
    }

    private boolean checkIfContains(MecMPackageDeploymentInfo item) {
        for (MecMPackageDeploymentInfo subJob : scheduleCache) {
            if (subJob.getAppIdFromApm().equals(item.getAppIdFromApm())) {
                return true;
            }
        }
        return false;
    }
}
