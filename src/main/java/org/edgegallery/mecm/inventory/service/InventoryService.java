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

/**
 * Inventory service.
 */
public interface InventoryService {

    /**
     * Adds a record to inventor.
     *
     * @param model inventory model
     * @return status
     */
    String addRecord(BaseModel model);

    /**
     * Updates a record to inventor.
     *
     * @param model inventory model
     * @return status
     */
    String updateRecord(BaseModel model);

    /**
     * Returns records of a given tenant.
     *
     * @param tenantId tenant identifier
     * @return list of records
     */
    List<BaseModel> getTenantRecords(String tenantId);

    /**
     * Returns a record.
     *
     * @param id record identifier
     * @return record
     */
    BaseModel getRecord(String id);

    /**
     * Deletes records for a given tenant.
     *
     * @param tenantId tenant identifier
     * @return status
     */
    String deleteTenantRecords(String tenantId);

    /**
     * Deletes a record.
     *
     * @param id record identifier
     * @return status
     */
    String deleteRecord(String id);
}
