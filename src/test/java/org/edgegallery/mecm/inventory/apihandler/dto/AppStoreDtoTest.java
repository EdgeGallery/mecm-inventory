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

package org.edgegallery.mecm.inventory.apihandler.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppStoreDtoTest {

	@InjectMocks
	AppStoreDto appStoreDto;

	@Before
	public void setUp(){
		appStoreDto.setAppstoreIp("appStoreIp");
		appStoreDto.setAppstoreName("appStorePortName");
		appStoreDto.setAppstorePort("appStorePort");
		appStoreDto.setUserName("userName");
		appStoreDto.setUri("uri");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("appStoreIp",appStoreDto.getAppstoreIp());
		Assert.assertEquals("appStorePortName",appStoreDto.getAppstoreName());
		Assert.assertEquals("appStorePort",appStoreDto.getAppstorePort());
		Assert.assertEquals("userName",appStoreDto.getUserName());
		Assert.assertEquals("uri",appStoreDto.getUri());
	}
}
