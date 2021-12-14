package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppdRuleTest {

    @InjectMocks
    AppdRule appdRule = new AppdRule();

    @Before
    public void setUp() {
        appdRule.setAppdRuleId("appdRuleId");
        appdRule.setAppInstanceId("appInstanceId");
        appdRule.setTenantId("tenantId");
        appdRule.setStatus("status");
        appdRule.setAppName("appName");
        //appdRule.setAppSupportMpl("app_support_mp1");

    }
    @Test
    public void testAppdRuleProcessFlowResponse() {
        Assert.assertEquals("appdRuleId", appdRule.getAppdRuleId());
        Assert.assertEquals("appInstanceId", appdRule.getAppInstanceId());
        Assert.assertEquals("appName", appdRule.getAppName());
        Assert.assertEquals("tenantId", appdRule.getTenantId());
        Assert.assertEquals("status", appdRule.getStatus());
        Assert.assertNotNull(appdRule.toString());

    }
}
