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
import static org.edgegallery.mecm.inventory.model.ModelType.APP_RULE_MANAGER;
import static org.edgegallery.mecm.inventory.model.ModelType.APP_REPO;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_HW_CAPABILITY;
import static org.edgegallery.mecm.inventory.model.ModelType.MEC_APPLICATION;
import static org.edgegallery.mecm.inventory.model.ModelType.APPD_RULE;
import static org.edgegallery.mecm.inventory.model.ModelType.TENANT;
import static org.edgegallery.mecm.inventory.model.ModelType.DNS_RULE;
import static org.edgegallery.mecm.inventory.model.ModelType.TRAFFIC_RULE;
import static org.edgegallery.mecm.inventory.model.ModelType.TRAFFIC_FILTER;

import org.edgegallery.mecm.inventory.exception.InventoryException;
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

    public static final String MECHOST_ID = "Mec_Host_Id";
    public static final String APPLCM_ID = "AppLcm_id";
    public static final String APPSTORE_ID = "AppStore_id";
    @InjectMocks
    TenantServiceImpl tenantService;
    @Mock
    TenantRepository repository;
    Tenant tenant = new Tenant();

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

    @Test(expected = RuntimeException.class)
    public void reduceCountNullId() {
        tenantService.reduceCount(null, APP_LCM);
    }

    @Test(expected = RuntimeException.class)
    public void clearCountNullId() {
        tenantService.clearCount(null, APP_LCM);
    }

    @Test
    public void testTenantExist() {
        tenantService.isTenantExist("tenant_id");
    }

    @Test
    public void addTenantAppRuleManager() {
        tenantService.addTenant(APPLCM_ID, APP_RULE_MANAGER);
    }

    @Test
    public void addTenantMecHwCapability() {
        tenantService.addTenant(APPLCM_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void addTenantRepo() {
        tenantService.addTenant(APPLCM_ID, APP_REPO);
    }

    @Test
    public void reduceCountAppRuleManager() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, APP_RULE_MANAGER);
    }

    @Test
    public void reduceCountMecHwCapability() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, MEC_HW_CAPABILITY);
    }

    @Test
    public void reduceCountMecApplication() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, MEC_APPLICATION);
    }

    @Test
    public void reduceCountAppRule() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, APPD_RULE);
    }

    @Test
    public void reduceCountAppRepo() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, APP_REPO);
    }

    @Test
    public void clearCountAppRuleManager() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, APP_RULE_MANAGER);
    }
    @Test
    public void clearCountMecHwCapability() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, MEC_HW_CAPABILITY);
    }
    @Test
    public void clearCountMecApplication() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, MEC_APPLICATION);
    }
    @Test
    public void clearCountAppdRule() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, APPD_RULE);
    }
    @Test
    public void clearCountDnsRule() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, DNS_RULE);
    }

    @Test
    public void clearCountTrafficRule() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, TRAFFIC_RULE);
    }

    @Test
    public void clearCountTrafficFilter() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, TRAFFIC_FILTER);
    }

    @Test
    public void clearCountAppRepo() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, APP_REPO);
    }

    @Test
    public void subtractCountAppRuleManager() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppRuleManagers(1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPSTORE_ID, APP_RULE_MANAGER);
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
    public void subtractCountAppRule() {
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
    public void addCountAppLcms() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppLcms(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, APP_LCM);
    }

    @Test
    public void addCountAppRManager() {
        tenant.setTenantId(APPSTORE_ID);
        tenant.setAppRuleManagers(-1);
        Mockito.when(repository.findById(APPSTORE_ID)).thenReturn(Optional.of(tenant));
        tenantService.addTenant(APPSTORE_ID, APP_RULE_MANAGER);
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
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.reduceCount(APPLCM_ID, TENANT);
    }

    @Test(expected = InventoryException.class)
    public void clearCountInvalid() {
        tenant.setTenantId(APPLCM_ID);
        Mockito.when(repository.findById(APPLCM_ID)).thenReturn(Optional.of(tenant));
        tenantService.clearCount(APPLCM_ID, TENANT);
    }

}