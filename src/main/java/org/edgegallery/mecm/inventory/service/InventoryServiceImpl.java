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
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.edgegallery.mecm.inventory.model.BaseModel;
import org.edgegallery.mecm.inventory.service.repository.BaseRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of inventory service.
 */
@Service("InventoryServiceImpl")
public final class InventoryServiceImpl implements InventoryService {

    @Override
    public <T extends BaseModel> Status addRecord(T model, CrudRepository<T, String> repository) {
        if (repository.existsById(model.getIdentifier())) {
            throw new IllegalArgumentException("Record already exist");
        }
        List<T> record = ((BaseRepository) repository).findByTenantId(model.getTenantId());
        if (repository.count() > Constants.MAX_ENTRY_PER_MODEL ||
                record.size() > Constants.MAX_ENTRY_PER_TENANT) {
            throw new InventoryException(Constants.MAX_LIMIT_REACHED);
        }
        repository.save(model);
        return (new Status("Saved"));
    }

    @Override
    public <T extends BaseModel> Status updateRecord(T model, CrudRepository<T, String> repository) {
        if (!repository.existsById(model.getIdentifier())) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND);
        }
        repository.save(model);
        return (new Status("Updated"));
    }

    @Override
    public <T extends BaseModel> List<T> getTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        List<T> record = ((BaseRepository) repository).findByTenantId(tenantId);
        if (record == null || record.isEmpty()) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND);
        }
        return record;
    }

    @Override
    public <T extends BaseModel> T getRecord(String id, CrudRepository<T, String> repository) {
        Optional<T> record = repository.findById(id);
        if (!record.isPresent()) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND);
        }
        return record.get();
    }

    @Override
    public <T extends BaseModel> Status deleteTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        List<T> record = ((BaseRepository) repository).findByTenantId(tenantId);
        if (record == null || record.isEmpty()) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND);
        }
        ((BaseRepository) repository).deleteByTenantId(tenantId);
        return (new Status("Deleted"));
    }

    @Override
    public <T extends BaseModel> Status deleteRecord(String id, CrudRepository<T, String> repository) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND);
        }
        repository.deleteById(id);
        return (new Status("Deleted"));
    }
}
