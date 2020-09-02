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
		mecHost.setEdgerepoPassword("edgerepoPassword");
		mecHost.setEdgerepoPort("edgerepoPort");
		mecHost.setEdgerepoUsername("edgerepoUsername");
		mecHost.setMechostId("mechostId");
		mecHost.setMechostIp("mecHostIp");
		mecHost.setMechostName("mecHostName");
		mecHost.setPassword("password");
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
		assertEquals("edgerepoPassword",mecHost.getEdgerepoPassword());
		assertEquals("edgerepoPort",mecHost.getEdgerepoPort());
		assertEquals("edgerepoUsername",mecHost.getEdgerepoUsername());
		assertEquals("mechostId",mecHost.getMechostId());
		assertEquals("mecHostIp",mecHost.getMechostIp());
		assertEquals("mecHostName",mecHost.getMechostName());
		assertEquals("password",mecHost.getPassword());
		assertEquals("tentantId",mecHost.getTenantId());
		assertEquals("UserName",mecHost.getUserName());
		assertEquals("ZipCode",mecHost.getZipCode());
	}
}
