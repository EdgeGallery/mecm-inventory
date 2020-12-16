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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of inventory service.
 */
@Service("InventoryServiceImpl")
public final class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private static final String TENANT_NOT_FOUND_MESSAGE = "Record corresponding to tenant identifier {} not exists";
    private static final String ID_NOT_FOUND_MESSAGE = "Record corresponding to identifier {} not exists";
    @Autowired
    private TenantServiceImpl tenantService;

    @Override
    public <T extends BaseModel> Status addRecord(T model, CrudRepository<T, String> repository) {
        if (repository.existsById(model.getIdentifier())) {
            LOGGER.error("{} record corresponding to identifier {} already exists", model.getType(),
                    model.getIdentifier());
            throw new IllegalArgumentException("Record already exist");
        }
        String tenantId = model.getTenantId();
        List<T> record = ((BaseRepository) repository).findByTenantId(tenantId);

        if (tenantService.isMaxTenantCountReached() && !tenantService.isTenantExist(tenantId)) {
            LOGGER.error("Max tenant count {} reached", Constants.MAX_TENANTS);
            throw new InventoryException(Constants.MAX_LIMIT_REACHED_ERROR);
        }

        if (record.size() == Constants.MAX_ENTRY_PER_TENANT_PER_MODEL) {
            LOGGER.error("Max entry per tenant per model {} reached", Constants.MAX_ENTRY_PER_TENANT_PER_MODEL);
            throw new InventoryException(Constants.MAX_LIMIT_REACHED_ERROR);
        }
        repository.save(model);
        LOGGER.info("Record added for identifier {} & type {}", model.getIdentifier(), model.getType());
        tenantService.addTenant(tenantId, model.getType());
        return new Status("Saved");
    }

    @Override
    public <T extends BaseModel> Status updateRecord(T model, CrudRepository<T, String> repository) {
        if (!repository.existsById(model.getIdentifier())) {
            LOGGER.error("{} record corresponding to identifier {} not exists", model.getType(),
                    model.getIdentifier());
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        repository.save(model);
        LOGGER.info("Record updated for identifier {} & type {}", model.getIdentifier(), model.getType());
        return new Status("Updated");
    }

    @Override
    public <T extends BaseModel> List<T> getTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        List<T> record = ((BaseRepository) repository).findByTenantId(tenantId);
        if (record == null || record.isEmpty()) {
            LOGGER.error(TENANT_NOT_FOUND_MESSAGE, tenantId);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        LOGGER.info("Records returned for tenant identifier {} & type {}", record.get(0).getTenantId(),
                record.get(0).getType());
        return record;
    }

    @Override
    public <T extends BaseModel> T getRecord(String id, CrudRepository<T, String> repository) {
        Optional<T> record = repository.findById(id);
        if (!record.isPresent()) {
            LOGGER.error(ID_NOT_FOUND_MESSAGE, id);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        LOGGER.info("Record returned for identifier {} & type {}", record.get().getIdentifier(),
                record.get().getType());
        return record.get();
    }

    @Override
    public <T extends BaseModel> Status deleteTenantRecords(String tenantId, CrudRepository<T, String> repository) {
        List<T> record = ((BaseRepository) repository).findByTenantId(tenantId);
        if (record == null || record.isEmpty()) {
            LOGGER.error(TENANT_NOT_FOUND_MESSAGE, tenantId);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        ((BaseRepository) repository).deleteByTenantId(tenantId);
        LOGGER.info("Record deleted for tenant identifier {} & type {}", record.get(0).getTenantId(),
                record.get(0).getType());
        tenantService.clearCount(tenantId, record.get(0).getType());
        return new Status("Deleted");
    }

    @Override
    public <T extends BaseModel> Status deleteRecord(String id, CrudRepository<T, String> repository) {
        Optional<T> record = repository.findById(id);
        if (!record.isPresent()) {
            LOGGER.error(ID_NOT_FOUND_MESSAGE, id);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        BaseModel model = record.get();
        String tenantId = model.getTenantId();
        repository.deleteById(id);
        LOGGER.info("Record deleted for identifier {} & type {}", tenantId, model.getType());
        tenantService.reduceCount(tenantId, model.getType());
        return new Status("Deleted");
    }
}
