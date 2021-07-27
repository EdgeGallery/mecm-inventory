package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppTrafficRuleTest {

    @InjectMocks
    AppTrafficRule appTrafficRule = new AppTrafficRule();
    AppdRule appDRule = new AppdRule();

    @Before
    public void setUp() {
        appTrafficRule.setTrafficRuleId("trafficRuleId");
        appTrafficRule.setFilterType("filterType");
        //appTrafficRule.setPriority("priority");
        appTrafficRule.setAction("action");
        appTrafficRule.setTenantId("tenantId");
        appTrafficRule.setAppInstanceId("appInstanceId");
        appTrafficRule.setAppDRule(appDRule);
    }

    @Test
    public void testAppDnsProcessFlowResponse() {
        Assert.assertEquals("trafficRuleId", appTrafficRule.getTrafficRuleId());
        Assert.assertEquals("filterType", appTrafficRule.getFilterType());
        //Assert.assertEquals(1, appTrafficRule.getPriority());
        Assert.assertEquals("action", appTrafficRule.getAction());
        Assert.assertEquals("tenantId", appTrafficRule.getTenantId());
        Assert.assertEquals("appInstanceId", appTrafficRule.getAppInstanceId());
        Assert.assertNotNull(appTrafficRule.getIdentifier());
        Assert.assertNotNull(appTrafficRule.getType());
        Assert.assertEquals(appDRule, appTrafficRule.getAppDRule());
    }

}
