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
		appLcm.setPassword("password");
		appLcm.setTenantId("tentantId");
		appLcm.setUserName("userName");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("applcmId",appLcm.getApplcmId());
		Assert.assertEquals("applcmIp",appLcm.getApplcmIp());
		Assert.assertEquals("applcmPort",appLcm.getApplcmPort());
		Assert.assertEquals("password",appLcm.getPassword());
		Assert.assertEquals("tentantId",appLcm.getTenantId());
		Assert.assertEquals("userName",appLcm.getUserName());
	}
}
