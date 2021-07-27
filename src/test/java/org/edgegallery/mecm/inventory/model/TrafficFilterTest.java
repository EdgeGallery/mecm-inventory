package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TrafficFilterTest {

    @Before
    public void setUp() {
        trafficFilter.setTrafficFilterId("trafficFilterId");
        trafficFilter.setTenantId("tenantId");

    }

    @InjectMocks
    TrafficFilter trafficFilter = new TrafficFilter();

    @Test
    public void testTrafficFilterProcessFlowResponse() {
        Assert.assertEquals("trafficFilterId", trafficFilter.getTrafficFilterId());
        Assert.assertNotNull(trafficFilter.getIdentifier());
        Assert.assertNotNull(trafficFilter.getType());
        Assert.assertNotNull(trafficFilter.getTenantId());
    }
}
