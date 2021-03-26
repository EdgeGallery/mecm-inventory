package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AppDnsRuleTest {

    @InjectMocks
    AppDnsRule appDnsRule = new AppDnsRule();

    @Before
    public void setUp() {
        appDnsRule.setDnsRuleId("dnsRuleId");
        appDnsRule.setAppInstanceId("appInstanceId");
        appDnsRule.setDomainName("domainName");
        appDnsRule.setIpAddress("1.1.1.1");
        appDnsRule.setIpAddressType("ipAddressType");
        appDnsRule.setTenantId("tenantId");
        appDnsRule.setTtl(1);
    }

    @Test
    public void testAppDnsProcessFlowResponse() {
            Assert.assertEquals("dnsRuleId", appDnsRule.getDnsRuleId());
            Assert.assertEquals("appInstanceId", appDnsRule.getAppInstanceId());
            Assert.assertEquals("domainName", appDnsRule.getDomainName());
            Assert.assertEquals("1.1.1.1", appDnsRule.getIpAddress());
            Assert.assertEquals("ipAddressType", appDnsRule.getIpAddressType());
            Assert.assertEquals("tenantId", appDnsRule.getTenantId());
            Assert.assertEquals(1, appDnsRule.getTtl());
            Assert.assertNotNull(appDnsRule.getIdentifier());
            Assert.assertNotNull(appDnsRule.getType());
    }
}
