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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.edgegallery.mecm.north.facade.schedule.ScheduleDistributeImpl;
import org.edgegallery.mecm.north.facade.schedule.ScheduleInstantiateImpl;
import org.edgegallery.mecm.north.model.MecmPackageDeploymentInfo;
import org.edgegallery.mecm.north.repository.mapper.MecmDeploymentMapper;
import org.edgegallery.mecm.north.repository.mapper.MecmPackageMapper;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScheduleImplementFacade")
public class ScheduleImplementFacade {
    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecmPackageMapper mecMPackageMapper;

    @Autowired
    private MecmDeploymentMapper mecMDeploymentMapper;

    @Autowired
    private ScheduleDistributeImpl scheduleDistributeImpl;

    @Autowired
    private ScheduleInstantiateImpl scheduleInstantiate;

    private Set<MecmPackageDeploymentInfo> scheduleCache = new HashSet<>();

    /**
     * loadScheduleJobs for schedule.
     */
    public void loadScheduleJobs() {
        loadFromDB();

        executeJobs();
    }

    private void executeJobs() {
        for (MecmPackageDeploymentInfo subJob : scheduleCache) {

            if (subJob.getStatus().equals(Constant.DISTRIBUTING_STATUS)) {
                scheduleDistributeImpl.queryDistribute(subJob);
            }

            if (subJob.getStatus().equals(Constant.DISTRIBUTED_STATUS)) {
                scheduleInstantiate.createInstantiate(subJob);
            }

            if (subJob.getStatus().equals(Constant.CREATED)) {
                scheduleInstantiate.executeInstantiate(subJob);
            }

            if (subJob.getStatus().equals(Constant.INSTANTIATING_STATUS) || subJob.getStatus()
                .equals(Constant.CREATING)) {
                scheduleInstantiate.queryInstantiate(subJob);
            }

        }
    }

    private void loadFromDB() {
        List<MecmPackageDeploymentInfo> runningJobs = mecMDeploymentMapper.getMecMPkgDeploymentInfos();
        addToCache(runningJobs);
    }

    private void addToCache(List<MecmPackageDeploymentInfo> runningJobs) {
        for (MecmPackageDeploymentInfo item : runningJobs) {
            scheduleCache.add(item);
        }
    }
}
