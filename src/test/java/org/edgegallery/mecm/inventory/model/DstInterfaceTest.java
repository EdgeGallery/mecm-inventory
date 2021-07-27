package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DstInterfaceTest {

    @InjectMocks
    DstInterface dstInterface = new DstInterface();

    @Before
    public void setUp() {
        dstInterface.setDstInterfaceId("dstInterfaceId");
        dstInterface.setInterfaceType("interfaceType");
        dstInterface.setTenantId("tenantId");
        dstInterface.setSrcMacAddress("srcMacAddress");
        dstInterface.setDstMacAddress("dstMacAddress");
        dstInterface.setDstIpAddress("dstIpAddress");
    }

    @Test
    public void testDstInterfaceProcessFlowResponse() {
        Assert.assertEquals("dstInterfaceId", dstInterface.getDstInterfaceId());
        Assert.assertEquals("interfaceType", dstInterface.getInterfaceType());
        Assert.assertEquals("dstIpAddress", dstInterface.getDstIpAddress());
        Assert.assertEquals("srcMacAddress", dstInterface.getSrcMacAddress());
        Assert.assertEquals("tenantId", dstInterface.getTenantId());
        Assert.assertEquals("dstMacAddress", dstInterface.getDstMacAddress());
        Assert.assertNotNull(dstInterface.getIdentifier());
        Assert.assertNotNull(dstInterface.getType());

    }

}
