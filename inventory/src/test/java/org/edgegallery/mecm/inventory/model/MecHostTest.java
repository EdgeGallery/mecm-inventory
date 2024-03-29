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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class MecHostTest {

    @InjectMocks
    MecHost mecHost = new MecHost();

    @Before
    public void setUp() {
        mecHost.setAddress("address");
        mecHost.setAffinity("affinity");
        mecHost.setCity("city");
        mecHost.setMechostId("mechostId");
        mecHost.setMechostIp("mecHostIp");
        mecHost.setMechostName("mecHostName");
        mecHost.setUserName("UserName");
        mecHost.setZipCode("ZipCode");
        mecHost.setMepmIp("mepmIp");
        mecHost.setConfigUploadStatus("configUploadStatus");
        mecHost.setCoordinates("coordinates");
        mecHost.setVim("vim");
    }

    @Test
    public void testAppoProcessFlowResponse() {
        assertEquals("address", mecHost.getAddress());
        assertEquals("affinity", mecHost.getAffinity());
        assertEquals("city", mecHost.getCity());
        assertEquals("mechostId", mecHost.getMechostId());
        assertEquals("mecHostIp", mecHost.getMechostIp());
        assertEquals("mecHostName", mecHost.getMechostName());
        assertEquals("UserName", mecHost.getUserName());
        assertEquals("ZipCode", mecHost.getZipCode());
        assertEquals("mepmIp", mecHost.getMepmIp());
        assertEquals("configUploadStatus", mecHost.getConfigUploadStatus());
        assertEquals("coordinates", mecHost.getCoordinates());
        assertEquals("vim", mecHost.getVim());
    }
}
