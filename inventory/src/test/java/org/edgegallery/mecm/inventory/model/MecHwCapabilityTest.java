package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MecHwCapabilityTest {

    @InjectMocks
    MecHwCapability mecHwCapability = new MecHwCapability();
    MecHost mecHost = new MecHost();

    @Before
    public void setUp() {
        mecHwCapability.setMecCapabilityId("mecCapabilityId");
        mecHwCapability.setHwType("hwType");
        mecHwCapability.setHwVendor("hwVendor");
        mecHwCapability.setHwModel("hwModel");
        //mecHwCapability.setMecHost(mecHost);

    }

    @Test
    public void testMecHwCapabilityProcessFlowResponse() {
        Assert.assertEquals("mecCapabilityId", mecHwCapability.getMecCapabilityId());
        Assert.assertEquals("hwType", mecHwCapability.getHwType());
        Assert.assertEquals("hwVendor", mecHwCapability.getHwVendor());
        Assert.assertEquals("hwModel", mecHwCapability.getHwModel());
        Assert.assertNotNull(mecHwCapability.getIdentifier());
        Assert.assertNotNull(mecHwCapability.getType());
        //Assert.assertEquals(mecHost, mecHwCapability.getMecHost());
    }
}
