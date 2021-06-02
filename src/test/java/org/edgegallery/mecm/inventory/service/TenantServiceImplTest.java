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

import static org.edgegallery.mecm.inventory.model.ModelType.APPD_RULE;
import static org.edgegallery.mecm.inventory.model.ModelType.APP_REPO;
import static org.edgegallery.mecm.inventory.model.ModelType.APP_STORE;
import static org.edgegallery.mecm.inventory.model.ModelType.DNS_RULE;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_APPLICATION;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_HOST;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_HW_CAPABILITY;
import static org.edgegallery.mecm.inventory.model.ModelType.MEPM;
import static org.edgegallery.mecm.inventory.model.ModelType.TENANT;
import static org.edgegallery.mecm.inventory.model.ModelType.TRAFFIC_FILTER;
import static org.edgegallery.mecm.inventory.model.ModelType.TRAFFIC_RULE;

import java.util.Optional;
import org.edgegallery.mecm.inventory.exception.InventoryException;
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

    public static final String MECHOST_ID = "Mec_Host_Id";
    public static final String MEPM_ID = "Mepm_id";
    public static final String APPSTORE_ID = "AppStore_id";
    @InjectMocks
    TenantServiceImpl tenantService;
    @Mock
    TenantRepository repository;
    Tenant tenant = new Tenant();

    @Test
    public void addTenantMepm() {
        tenantService.addTenant(MEPM_ID, MEPM);
    }

    @Test
    public void addTenantMepmCount() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(MEPM_ID, MEPM);
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
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, MEPM);
    }

    @Test
    public void reduceCountSave() {
        tenant.setTenantId(MEPM_ID);
        tenant.setMepms(1);
        tenant.setAppStores(1);
        tenant.setMecHosts(1);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, MEPM);
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
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, MEPM);
    }

    @Test
    public void deleteCount() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMepms(1);
        tenant.setAppStores(2);
        tenant.setMecHosts(3);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void clearCountMecHost() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, MEC_HOST);
    }

    @Test(expected = RuntimeException.class)
    public void reduceCountNullId() {
        tenantService.reduceCount(null, MEPM);
    }

    @Test(expected = RuntimeException.class)
    public void clearCountNullId() {
        tenantService.clearCount(null, MEPM);
    }

    @Test
    public void testTenantExist() {
        tenantService.isTenantExist("tenant_id");
    }

    @Test
    public void addTenantMecHwCapability() {
        tenantService.addTenant(MEPM_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void addTenantRepo() {
        tenantService.addTenant(MEPM_ID, APP_REPO);
    }

    @Test
    public void reduceCountMepm() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, MEPM);
    }

    @Test
    public void reduceCountMecHwCapability() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void reduceCountMecApplication() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, MEC_APPLICATION);
    }

    @Test
    public void reduceCountAppDRule() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, APPD_RULE);
    }

    @Test
    public void reduceCountAppRepo() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, APP_REPO);
    }

    @Test
    public void clearCountMepm() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, MEPM);
    }

    @Test
    public void clearCountMecHwCapability() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void clearCountMecApplication() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, MEC_APPLICATION);
    }

    @Test
    public void clearCountAppdRule() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, APPD_RULE);
    }

    @Test
    public void clearCountDnsRule() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, DNS_RULE);
    }

    @Test
    public void clearCountTrafficRule() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, TRAFFIC_RULE);
    }

    @Test
    public void clearCountTrafficFilter() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, TRAFFIC_FILTER);
    }

    @Test
    public void clearCountAppRepo() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, APP_REPO);
    }

    @Test
    public void subtractCountMepm() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMepms(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, MEPM);
    }

    @Test
    public void subtractCountMecHwCapability() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecHwCapabilities(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void subtractCountMecApplication() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecApplications(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, MEC_APPLICATION);
    }

    @Test
    public void subtractCountAppDRule() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppdRules(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, APPD_RULE);
    }

    @Test
    public void subtractCountAppRepo() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppRepo(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, APP_REPO);
    }

    @Test
    public void addCountMepms() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMepms(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, MEPM);
    }

    @Test
    public void addCountAppStore() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppStores(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, APP_STORE);
    }

    @Test
    public void addCountMecHost() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecHosts(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, MEC_HOST);
    }

    @Test
    public void addCountMecHwCap() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecHwCapabilities(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void addCountMecApp() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setMecApplications(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, MEC_APPLICATION);
    }

    @Test
    public void addCountAppDRule() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppdRules(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, APPD_RULE);
    }

    @Test
    public void addCountAppRepo() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppRepo(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, APP_REPO);
    }

    @Test(expected = InventoryException.class)
    public void addTenantInvalid() {
        tenantService.addTenant(APPSTORE_ID, TENANT);
    }

    @Test(expected = InventoryException.class)
    public void reduceCountInvalid() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(MEPM_ID, TENANT);
    }

    @Test(expected = InventoryException.class)
    public void clearCountInvalid() {
        tenant.setTenantId(MEPM_ID);
        Mockito.when(repository.findById(MEPM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(MEPM_ID, TENANT);
    }

}