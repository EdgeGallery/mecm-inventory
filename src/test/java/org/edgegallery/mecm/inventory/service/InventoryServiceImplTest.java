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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.edgegallery.mecm.inventory.model.AppLcm;
import org.edgegallery.mecm.inventory.service.repository.AppLcmRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InventoryServiceImplTest {

    @InjectMocks
    InventoryServiceImpl inventoryServiceImpl;

    @Mock
    AppLcmRepository appLcmRepository;

    @Mock
    TenantServiceImpl tenantServiceImpl;

    AppLcm appLcm = new AppLcm();

    public static final String TENANT_ID = "tenantId";

    public static final String APPLCM_ID = "AppLcm_id";

    public static final String APPLCM = "appLcm";

    @Test
    public void testAddRecord() {
        appLcm.setTenantId(APPLCM_ID);
        List<AppLcm> appLcmList = new ArrayList<>();
        appLcmList.add(appLcm);
        Mockito.when(appLcmRepository.findByTenantId(APPLCM_ID)).thenReturn(appLcmList);
        Mockito.when(tenantServiceImpl.isMaxTenantCountReached()).thenReturn(false);
        inventoryServiceImpl.addRecord(appLcm, appLcmRepository);
    }

    @Test
    public void testTenantRecord() {
        appLcm.setTenantId(TENANT_ID);
        List<AppLcm> appLcmList = new ArrayList<>();
        appLcmList.add(appLcm);
        Mockito.when(appLcmRepository.findByTenantId(TENANT_ID)).thenReturn(appLcmList);
        inventoryServiceImpl.getTenantRecords(TENANT_ID, appLcmRepository);
    }

    @Test
    public void testGetRecord() {
        Mockito.when(appLcmRepository.findById(APPLCM)).thenReturn(Optional.of(appLcm));
        inventoryServiceImpl.getRecord(APPLCM, appLcmRepository);
    }

    @Test
    public void testDeleteTenantRecord() {
        appLcm.setTenantId(TENANT_ID);
        List<AppLcm> appLcmList = new ArrayList<>();
        appLcmList.add(appLcm);
        Mockito.when(appLcmRepository.findByTenantId(TENANT_ID)).thenReturn(appLcmList);
        inventoryServiceImpl.deleteTenantRecords(TENANT_ID, appLcmRepository);
    }

    @Test
    public void testDeleteRecord() {
        appLcm.setTenantId(APPLCM);
        Mockito.when(appLcmRepository.findById(APPLCM)).thenReturn(Optional.of(appLcm));
        inventoryServiceImpl.deleteRecord(APPLCM, appLcmRepository);
    }
}