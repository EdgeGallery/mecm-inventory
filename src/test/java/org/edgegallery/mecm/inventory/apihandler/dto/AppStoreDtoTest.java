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
		appStoreDto.setAppstoreName("appStorePort");
		appStoreDto.setAppstorePort("appStorePort");
		appStoreDto.setPassword("password");
		appStoreDto.setUserName("userName");
		appStoreDto.setUri("uri");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("appStoreIp",appStoreDto.getAppstoreIp());
		Assert.assertEquals("appStorePort",appStoreDto.getAppstorePort());
		Assert.assertEquals("password",appStoreDto.getPassword());
		Assert.assertEquals("userName",appStoreDto.getUserName());
		Assert.assertEquals("uri",appStoreDto.getUri());
	}
}
