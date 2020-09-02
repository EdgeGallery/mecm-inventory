package org.edgegallery.mecm.inventory.apihandler.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppLcmDtoTest {

	@InjectMocks
	AppLcmDto appLcmDto;

	@Before
	public void setUp(){
		appLcmDto.setApplcmIp("applcmIp");
		appLcmDto.setApplcmPort("applcmPort");
		appLcmDto.setPassword("password");
		appLcmDto.setUserName("userName");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("applcmIp",appLcmDto.getApplcmIp());
		Assert.assertEquals("applcmPort",appLcmDto.getApplcmPort());
		Assert.assertEquals("password",appLcmDto.getPassword());
		Assert.assertEquals("userName",appLcmDto.getUserName());
	}
}
