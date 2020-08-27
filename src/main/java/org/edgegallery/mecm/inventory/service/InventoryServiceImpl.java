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
import java.util.NoSuchElementException;
import java.util.Optional;
import org.edgegallery.mecm.inventory.model.BaseModel;
import org.edgegallery.mecm.inventory.service.repository.BaseRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of inventory service.
 */
@Service("InventoryServiceImpl")
public final class InventoryServiceImpl implements InventoryService {

    @Override
    public <T extends BaseModel> String addRecord(T model, CrudRepository<T, String> repository) {
        if (repository.existsById(model.getIdentifier())) {
            throw new IllegalArgumentException("Record already exist");
        }
        repository.save(model);
        return "Saved";
    }

    @Override
    public <T extends BaseModel> String updateRecord(T model, CrudRepository<T, String> repository) {
        if (!repository.existsById(model.getIdentifier())) {
            throw new NoSuchElementException("Record not found");
        }
        repository.save(model);
        return "Updated";
    }

    @Override
    public <T extends BaseModel> List<T> getTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        return ((BaseRepository) repository).findByTenantId(tenantId);
    }

    @Override
    public <T extends BaseModel> Optional<T> getRecord(String id, CrudRepository<T, String> repository) {
        return repository.findById(id);
    }

    @Override
    public <T extends BaseModel> String deleteTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        ((BaseRepository) repository).deleteByTenantId(tenantId);
        return "Deleted";
    }

    @Override
    public <T extends BaseModel> String deleteRecord(String id, CrudRepository<T, String> repository) {
        repository.deleteById(id);
        return "Deleted";
    }
}
