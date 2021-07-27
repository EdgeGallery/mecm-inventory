package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TunnelInfoTest {

    @InjectMocks
    TunnelInfo tunnelInfo = new TunnelInfo();

    @Before
    public void setUp() {
        tunnelInfo.setTunnelInfoId("tunnelInfoId");
        tunnelInfo.setTunnelType("tunnelType");
        tunnelInfo.setTenantId("tenantId");
        tunnelInfo.setTunnelDstAddress("tunnelDstAddress");
        tunnelInfo.setTunnelSrcAddress("tunnelSrcAddress");
        tunnelInfo.setTunnelSpecificData("tunnelSpecificData");
    }

    @Test
    public void testTunnelInfoProcessFlowResponse() {
        Assert.assertEquals("tunnelInfoId", tunnelInfo.getTunnelInfoId());
        Assert.assertEquals("tunnelType", tunnelInfo.getTunnelType());
        Assert.assertEquals("tunnelDstAddress", tunnelInfo.getTunnelDstAddress());
        Assert.assertEquals("tunnelSrcAddress", tunnelInfo.getTunnelSrcAddress());
        Assert.assertEquals("tenantId", tunnelInfo.getTenantId());
        Assert.assertEquals("tunnelSpecificData", tunnelInfo.getTunnelSpecificData());
        Assert.assertNotNull(tunnelInfo.getIdentifier());
        Assert.assertNotNull(tunnelInfo.getType());
    }
}
