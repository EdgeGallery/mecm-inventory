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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.edgegallery.mecm.north.facade.schedule.ScheduleClearErrorImpl;
import org.edgegallery.mecm.north.facade.schedule.ScheduleDistributeImpl;
import org.edgegallery.mecm.north.facade.schedule.ScheduleInstantiateImpl;
import org.edgegallery.mecm.north.model.MecmPackageDeploymentInfo;
import org.edgegallery.mecm.north.repository.mapper.MecmDeploymentMapper;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScheduleImplementFacade")
public class ScheduleImplementFacade {

    @Autowired
    private MecmDeploymentMapper mecMDeploymentMapper;

    @Autowired
    private ScheduleDistributeImpl scheduleDistributeImpl;

    @Autowired
    private ScheduleInstantiateImpl scheduleInstantiate;

    @Autowired
    private ScheduleClearErrorImpl scheduleClearErrorImpl;

    private Set<MecmPackageDeploymentInfo> scheduleCache = new HashSet<>();

    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDistributeImpl.class);

    /**
     * loadScheduleJobs for schedule.
     */
    public void loadScheduleJobs() {
        loadFromDB();

        executeJobs();
    }

    private void executeJobs() {
        Iterator<MecmPackageDeploymentInfo> iterator = scheduleCache.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            MecmPackageDeploymentInfo subJob = iterator.next();
            if (subJob.getStatus().equals(Constant.INSTANTIATE_ERROR_STATUS) || subJob.getStatus()
                .equals(Constant.DISTRIBUTE_ERROR_STATUS) || subJob.getStatus().equals(Constant.CREATE_ERROR)) {
                LOGGER.info("clear {} status is {}th step with mecmPkgID: {}", subJob.getStatus(), i++,
                    subJob.getMecmPackageId());
                scheduleClearErrorImpl.deleteErrorStatus(subJob);
                iterator.remove();
                continue;
            }

            if (subJob.getStatus().equals(Constant.FINISHED_STATUS)) {
                iterator.remove();
                LOGGER.info("finish status is {}th step", i++);
                continue;
            }

            if (subJob.getStatus().equals(Constant.DISTRIBUTING_STATUS)) {
                LOGGER.info("query distributing status is {}th step", i++);
                scheduleDistributeImpl.queryDistribute(subJob);
            }

            if (subJob.getStatus().equals(Constant.DISTRIBUTED_STATUS)) {
                LOGGER.info("create Instantiate status is {}th step", i++);
                scheduleInstantiate.createInstantiate(subJob);
            }

            if (subJob.getStatus().equals(Constant.CREATED)) {
                LOGGER.info("execute Instantiate status is {}th step", i++);
                scheduleInstantiate.executeInstantiate(subJob);
                continue;
            }

            if (subJob.getStatus().equals(Constant.INSTANTIATING_STATUS) || subJob.getStatus()
                .equals(Constant.CREATING)) {
                LOGGER.info("query Instantiate status is {}th step", i++);
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