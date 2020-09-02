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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class AppLcmTest {

	@InjectMocks
	AppLcm appLcm = new AppLcm();

	@Before
	public void setUp(){
		appLcm.setApplcmId("applcmId");
		appLcm.setApplcmIp("applcmIp");
		appLcm.setApplcmPort("applcmPort");
		appLcm.setTenantId("tentantId");
		appLcm.setUserName("userName");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("applcmId",appLcm.getApplcmId());
		Assert.assertEquals("applcmIp",appLcm.getApplcmIp());
		Assert.assertEquals("applcmPort",appLcm.getApplcmPort());
		Assert.assertEquals("tentantId",appLcm.getTenantId());
		Assert.assertEquals("userName",appLcm.getUserName());
	}
}
