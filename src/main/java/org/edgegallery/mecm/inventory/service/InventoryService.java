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

import java.util.List;
import org.edgegallery.mecm.inventory.model.BaseModel;
import org.edgegallery.mecm.inventory.utils.Status;
import org.springframework.data.repository.CrudRepository;

/**
 * Inventory service.
 */
public interface InventoryService {

    /**
     * Adds a record to inventor.
     *
     * @param <T>        type of model
     * @param model      inventory model
     * @param repository operating repository
     * @return status
     */
    <T extends BaseModel> Status addRecord(T model, CrudRepository<T, String> repository);

    /**
     * Updates a record to inventor.
     *
     * @param <T>        type of model
     * @param model      inventory model
     * @param repository operating repository
     * @return status
     */
    <T extends BaseModel> Status updateRecord(T model, CrudRepository<T, String> repository);

    /**
     * Returns records of a given tenant.
     *
     * @param tenantId   tenant identifier
     * @param repository operating repository
     * @param <T>        type of model
     * @return list of records
     */
    <T extends BaseModel> List<T> getTenantRecords(String tenantId, CrudRepository<T, String> repository);

    /**
     * Returns a record.
     *
     * @param <T>        type of model
     * @param id         record identifier
     * @param repository operating repository
     * @return record with optional content
     */
    <T extends BaseModel> T getRecord(String id, CrudRepository<T, String> repository);

    /**
     * Deletes records for a given tenant.
     *
     * @param <T>        type of model
     * @param tenantId   tenant identifier
     * @param repository operating repository
     * @return status
     */
    <T extends BaseModel> Status deleteTenantRecords(String tenantId, CrudRepository<T, String> repository);

    /**
     * Deletes a record.
     *
     * @param <T>        type of model
     * @param id         record identifier
     * @param repository operating repository
     * @return status
     */
    <T extends BaseModel> Status deleteRecord(String id, CrudRepository<T, String> repository);
}
