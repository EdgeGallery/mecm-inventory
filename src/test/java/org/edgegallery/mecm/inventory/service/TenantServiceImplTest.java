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

import static org.edgegallery.mecm.inventory.model.ModelType.APP_LCM;
import static org.edgegallery.mecm.inventory.model.ModelType.APP_STORE;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_HOST;

import java.util.Optional;
import org.edgegallery.mecm.inventory.model.Tenant;
import org.edgegallery.mecm.inventory.service.repository.TenantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantServiceImplTest {

    @InjectMocks
    TenantServiceImpl tenantService;

    @Mock
    TenantRepository repository;

    Tenant tenant = new Tenant();

    public static final String MECHOST_ID = "Mec_Host_Id";

    public static final String APPLCM_ID = "AppLcm_id";

    public static final String APPSTORE_ID = "AppStore_id";

    @Test
    public void addTenantAppLcm() {
        tenantService.addTenant(APPLCM_ID, APP_LCM);
    }

    @Test
    public void addTenantAppLcmCount() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPLCM_ID, APP_LCM);
    }

    @Test
    public void addTenantAppStore() {
        tenantService.addTenant(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void addTenantAppStoreId() {
        tenant.setAppStores(4);
        tenantService.addTenant(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void addTenantMecHost() {
        tenant.setMecHosts(1);
        tenantService.addTenant(MECHOST_ID, MEC_HOST);
    }

    @Test
    public void addTenantId() {
        tenantService.addTenant(MECHOST_ID, MEC_HOST);
    }

    @Test
    public void reduceCount() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, APP_LCM);
    }

    @Test
    public void reduceCountSave() {
        tenant.setTenantId(APPLCM_ID);
        tenant.setAppLcms(1);
        tenant.setAppStores(1);
        tenant.setMecHosts(1);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, APP_LCM);
    }

    @Test
    public void subtractCount() {
        tenant.setTenantId(APPSTORE_ID);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void subtractCountAppStore() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppStores(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void subtractCountMecHost() {
        tenant.setTenantId(APPSTORE_ID);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, MEC_HOST);
    }

    @Test
    public void subtractCountId() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecHosts(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, MEC_HOST);
    }

    @Test
    public void clearCount() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, APP_LCM);
    }

    @Test
    public void deleteCount() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppLcms(1);
        tenant.setAppStores(2);
        tenant.setMecHosts(3);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void clearCountMecHost() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, MEC_HOST);
    }
}