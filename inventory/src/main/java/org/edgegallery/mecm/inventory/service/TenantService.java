/*
 *  Copyright 2020 Huawei Technologies Co., Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.edgegallery.mecm.inventory.service;

import org.edgegallery.mecm.inventory.model.ModelType;

/**
 * Tenant service.
 */
interface TenantService {

    /**
     * Adds a given tenant, if already exists just increase the model count.
     *
     * @param id   tenant identifier
     * @param type model type
     */
    void addTenant(String id, ModelType type);

    /**
     * Reduces a count for a give model type, also removes a given tenant if count of all model count is 0.
     *
     * @param id   tenant identifier
     * @param type model type
     */
    void reduceCount(String id, ModelType type);

    /**
     * Checks if max tenant count has reached.
     *
     * @return true if reached, false otherwise
     */
    boolean isMaxTenantCountReached();

    /**
     * Clears count of a specific model type, also removes a given tenant if count of all model count is 0.
     *
     * @param tenantId tenant identifier
     * @param type     model type
     */
    void clearCount(String tenantId, ModelType type);


    /**
     * Checks if tenant exist.
     *
     * @return true if exist, false otherwise
     */
    boolean isTenantExist(String tenantId);
}
