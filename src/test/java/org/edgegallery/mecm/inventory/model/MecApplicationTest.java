package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MecApplicationTest {

    @InjectMocks
    MecApplication mecApplication = new MecApplication();

    @Before
    public void setUp() {
        mecApplication.setAppInstanceId("appInstanceId");
        mecApplication.setCapabilities("capabilities");
        mecApplication.setTenantId("tenantId");
        mecApplication.setAppName("appName");
        mecApplication.setPackageId("packageId");
        mecApplication.setStatus("status");
    }

    @Test
    public void testDstInterfaceProcessFlowResponse() {
        Assert.assertEquals("appInstanceId", mecApplication.getAppInstanceId());
        Assert.assertEquals("capabilities", mecApplication.getCapabilities());
        Assert.assertEquals("appName", mecApplication.getAppName());
        Assert.assertEquals("packageId", mecApplication.getPackageId());
        Assert.assertEquals("tenantId", mecApplication.getTenantId());
        Assert.assertEquals("status", mecApplication.getStatus());

    }
}
