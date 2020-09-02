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
	public void setUp(){
		mecHost.setAddress("address");
		mecHost.setAffinity("affinity");
		mecHost.setApplcmIp("AppLcmIp");
		mecHost.setCity("city");
		mecHost.setEdgeName("edgeName");
		mecHost.setEdgerepoIp("edgeRepoIp");
		mecHost.setEdgerepoPort("edgerepoPort");
		mecHost.setEdgerepoUsername("edgerepoUsername");
		mecHost.setMechostId("mechostId");
		mecHost.setMechostIp("mecHostIp");
		mecHost.setMechostName("mecHostName");
		mecHost.setTenantId("tentantId");
		mecHost.setUserName("UserName");
		mecHost.setZipCode("ZipCode");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		assertEquals("address",mecHost.getAddress());
		assertEquals("affinity",mecHost.getAffinity());
		assertEquals("AppLcmIp",mecHost.getApplcmIp());
		assertEquals("city",mecHost.getCity());
		assertEquals("edgeName",mecHost.getEdgeName());
		assertEquals("edgeRepoIp",mecHost.getEdgerepoIp());
		assertEquals("edgerepoPort",mecHost.getEdgerepoPort());
		assertEquals("edgerepoUsername",mecHost.getEdgerepoUsername());
		assertEquals("mechostId",mecHost.getMechostId());
		assertEquals("mecHostIp",mecHost.getMechostIp());
		assertEquals("mecHostName",mecHost.getMechostName());
		assertEquals("tentantId",mecHost.getTenantId());
		assertEquals("UserName",mecHost.getUserName());
		assertEquals("ZipCode",mecHost.getZipCode());
	}
}
