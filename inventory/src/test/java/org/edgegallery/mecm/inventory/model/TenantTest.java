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

package org.edgegallery.mecm.inventory.model;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class TenantTest {

    public static final int MEPM = 30101;
    public static final int APP_STORE = 90000;
    public static final int MEC_HOST = 32806;
    public static final String TENANT_ID = "tenant_id";
    @InjectMocks
    Tenant tenant = new Tenant();

    @Before
    public void setUp() {
        tenant.setMepms(MEPM);
        tenant.setAppStores(APP_STORE);
        tenant.setMecHosts(MEC_HOST);
        tenant.setTenantId(TENANT_ID);
    }

    @Test
    public void testTenantModel() {
        assertEquals(MEPM, tenant.getMepms());
        assertEquals(APP_STORE, tenant.getAppStores());
        assertEquals(MEC_HOST, tenant.getMecHosts());
        assertEquals(TENANT_ID, tenant.getTenantId());
        Assert.assertNotNull(tenant.getType());
    }
}