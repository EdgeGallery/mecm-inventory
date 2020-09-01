package org.edgegallery.mecm.inventory.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

public class AppStoreTest {

	@InjectMocks
	AppStore appLcmStore = new AppStore();

	@Before
	public void setUp(){
		appLcmStore.setAppstoreId("appStoreId");
		appLcmStore.setAppstoreIp("appStoreIp");
		appLcmStore.setAppstoreName("appStoreName");
		appLcmStore.setAppstorePort("appStorePort");
		appLcmStore.setPassword("password");
		appLcmStore.setProducer("producer");
		appLcmStore.setTenantId("tentantId");
		appLcmStore.setUri("uri");
		appLcmStore.setUserName("userName");
	}

	@Test
	public void testAppoProcessFlowResponse() {
		assertEquals("appStoreId",appLcmStore.getAppstoreId());
		assertEquals("appStoreIp",appLcmStore.getAppstoreIp());
		assertEquals("appStoreName",appLcmStore.getAppstoreName());
		assertEquals("appStorePort",appLcmStore.getAppstorePort());
		assertEquals("password",appLcmStore.getPassword());
		assertEquals("producer",appLcmStore.getProducer());
		assertEquals("tentantId",appLcmStore.getTenantId());
		assertEquals("uri",appLcmStore.getUri());
		assertEquals("userName",appLcmStore.getUserName());
	}
}
