package org.edgegallery.mecm.inventory.apihandler.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MecHostDtoTest {

	@InjectMocks
	MecHostDto mecHostDto;

	@Before
	public void setUp(){

		mecHostDto.setAddress("address");
		mecHostDto.setAffinity("affinity");
		mecHostDto.setApplcmIp("AppLcmIp");
		mecHostDto.setCity("city");
		mecHostDto.setEdgeName("edgeName");
		mecHostDto.setEdgeRepoPort("edgerepoPort");
		mecHostDto.setPassword("password");
		mecHostDto.setUserName("UserName");
		mecHostDto.setZipCode("ZipCode");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		Assert.assertEquals("address",mecHostDto.getAddress());
		Assert.assertEquals("affinity",mecHostDto.getAffinity());
		Assert.assertEquals("AppLcmIp",mecHostDto.getApplcmIp());
		Assert.assertEquals("city",mecHostDto.getCity());
		Assert.assertEquals("edgeName",mecHostDto.getEdgeName());
		Assert.assertEquals("edgerepoPort",mecHostDto.getEdgeRepoPort());
		Assert.assertEquals("password",mecHostDto.getPassword());
		Assert.assertEquals("UserName",mecHostDto.getUserName());
		Assert.assertEquals("ZipCode",mecHostDto.getZipCode());
	}
}



