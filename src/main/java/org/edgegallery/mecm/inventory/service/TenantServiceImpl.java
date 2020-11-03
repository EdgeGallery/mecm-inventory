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

import java.util.NoSuchElementException;
import java.util.Optional;
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.edgegallery.mecm.inventory.model.ModelType;
import org.edgegallery.mecm.inventory.model.Tenant;
import org.edgegallery.mecm.inventory.service.repository.TenantRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TenantServiceImpl")
class TenantServiceImpl implements TenantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantServiceImpl.class);
    @Autowired
    private TenantRepository repository;

    @Override
    public void addTenant(String id, ModelType type) {
        Optional<Tenant> record = repository.findById(id);
        if (!record.isPresent()) {
            Tenant nt = new Tenant();
            nt.setTenantId(id);
            addCount(nt, type);
            repository.save(nt);
            LOGGER.info("Tenant record with identifier {} added", id);
            return;
        }
        if (repository.count() == Constants.MAX_TENANTS) {
            LOGGER.error("Max tenant limit {} reached", Constants.MAX_TENANTS);
            throw new InventoryException(Constants.MAX_LIMIT_REACHED_ERROR);
        }
        Tenant ot = record.get();
        addCount(ot, type);
        repository.save(ot);
        LOGGER.info("Tenant record with identifier {} updated with new count for type {}", id, type);
    }

    @Override
    public void reduceCount(String id, ModelType type) {
        Optional<Tenant> record = repository.findById(id);
        if (!record.isPresent()) {
            LOGGER.error("Tenant record for tenant identifier {} not found", id);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        Tenant ot = record.get();
        subtractCount(ot, type);
        if (ot.getAppLcms() == 0 && ot.getAppStores() == 0 && ot.getMecHosts() == 0
                && ot.getMecHwCapabilities() == 0) {
            repository.deleteById(id);
            LOGGER.info("Tenant record with tenant identifier {} deleted", id);
        } else {
            repository.save(ot);
            LOGGER.info("Tenant record for tenant identifier {} updated with new count for type {}", id, type);
        }
    }

    @Override
    public boolean isMaxTenantCountReached() {
        return repository.count() == Constants.MAX_TENANTS;
    }

    @Override
    public void clearCount(String id, ModelType type) {
        Optional<Tenant> record = repository.findById(id);
        if (!record.isPresent()) {
            LOGGER.error("Tenant record for tenant identifier {} not found", id);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        Tenant ot = record.get();
        clearCountNumber(ot, type);
        if (ot.getAppLcms() == 0 && ot.getAppStores() == 0 && ot.getMecHosts() == 0
                && ot.getMecHwCapabilities() == 0) {
            repository.deleteById(id);
            LOGGER.info("Tenant record with tenant identifier {} deleted", id);
        } else {
            repository.save(ot);
            LOGGER.info("Tenant record for tenant identifier {} updated with new count for type {}", id, type);
        }
    }

    private void clearCountNumber(Tenant ot, ModelType type) {
        switch (type) {
            case APP_LCM:
                ot.setAppLcms(0);
                break;
            case APP_STORE:
                ot.setAppStores(0);
                break;
            case MEC_HOST:
                ot.setMecHosts(0);
                break;
            case MEC_HW_CAPABILITY:
                ot.setMecHwCapabilities(0);
                break;
            default:
                LOGGER.error(Constants.INVALID_MODEL_TYPE);
                throw new InventoryException(Constants.INVALID_MODEL_TYPE);
        }
    }

    private void addCount(Tenant t, ModelType type) {
        switch (type) {
            case APP_LCM:
                if (isNotOverflow(t.getAppLcms(), 1)) {
                    t.setAppLcms(t.getAppLcms() + 1);
                } else {
                    LOGGER.error(Constants.VAR_OVERFLOW_ERROR);
                }
                break;
            case APP_STORE:
                if (isNotOverflow(t.getAppStores(), 1)) {
                    t.setAppStores(t.getAppStores() + 1);
                } else {
                    LOGGER.error(Constants.VAR_OVERFLOW_ERROR);
                }
                break;
            case MEC_HOST:
                if (isNotOverflow(t.getMecHosts(), 1)) {
                    t.setMecHosts(t.getMecHosts() + 1);
                } else {
                    LOGGER.error(Constants.VAR_OVERFLOW_ERROR);
                }
                break;
            case MEC_HW_CAPABILITY:
                if (isNotOverflow(t.getMecHwCapabilities(), 1)) {
                    t.setMecHwCapabilities(t.getMecHwCapabilities() + 1);
                } else {
                    LOGGER.error(Constants.VAR_OVERFLOW_ERROR);
                }
                break;
            default:
                LOGGER.error(Constants.INVALID_MODEL_TYPE);
                throw new InventoryException(Constants.INVALID_MODEL_TYPE);
        }
    }

    private boolean isNotOverflow(int count, int y) {
        return (Integer.MAX_VALUE - count) >= y;
    }

    private void subtractCount(Tenant t, ModelType type) {
        switch (type) {
            case APP_LCM:
                if (isNotUnderflow(t.getAppLcms(), 1)) {
                    t.setAppLcms(t.getAppLcms() - 1);
                } else {
                    LOGGER.error("{} for applcm count", Constants.VAR_UNDERFLOW_ERROR);
                }
                break;
            case APP_STORE:
                if (isNotUnderflow(t.getAppStores(), 1)) {
                    t.setAppStores(t.getAppStores() - 1);
                } else {
                    LOGGER.error("{} for appstore count", Constants.VAR_UNDERFLOW_ERROR);
                }
                break;
            case MEC_HOST:
                if (isNotUnderflow(t.getMecHosts(), 1)) {
                    t.setMecHosts(t.getMecHosts() - 1);
                } else {
                    LOGGER.error("{} for mechost count", Constants.VAR_UNDERFLOW_ERROR);
                }
                break;
            case MEC_HW_CAPABILITY:
                if (isNotUnderflow(t.getMecHwCapabilities(), 1)) {
                    t.setMecHwCapabilities(t.getMecHwCapabilities() - 1);
                } else {
                    LOGGER.error("{} for mec hardware capability count", Constants.VAR_UNDERFLOW_ERROR);
                }
                break;
            default:
                LOGGER.error(Constants.INVALID_MODEL_TYPE);
                throw new InventoryException(Constants.INVALID_MODEL_TYPE);
        }
    }

    private boolean isNotUnderflow(int count, int y) {
        return (count - y) >= 0;
    }
}
