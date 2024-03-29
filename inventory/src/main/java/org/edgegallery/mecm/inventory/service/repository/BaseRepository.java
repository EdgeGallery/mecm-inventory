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

package org.edgegallery.mecm.inventory.service.repository;

import java.util.List;
import org.edgegallery.mecm.inventory.model.BaseModel;
import org.springframework.data.repository.query.Param;

/**
 * Base repository.
 *
 * @param <T> type of model
 */
public interface BaseRepository<T extends BaseModel> {

    /**
     * Delete a record by tenant identifier.
     *
     * @param tenantId tenant identifier
     */
    void deleteByTenantId(@Param("tenantId") String tenantId);

    /**
     * Returns a record by tenant identifier.
     *
     * @param tenantId tenant identifier
     * @return list of records
     */
    List<T> findByTenantId(@Param("tenantId") String tenantId);

    /**
     * Returns records by role identifier.
     *
     * @param role tenant identifier
     * @return list of records
     */
    List<T> findByUserRole(@Param("role") String role);
}
