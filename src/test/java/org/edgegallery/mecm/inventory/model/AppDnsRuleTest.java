/* Copyright 2020-2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppDnsRuleTest {

    @InjectMocks
    AppDnsRule appDnsRule = new AppDnsRule();
    AppdRule appDRule = new AppdRule();

    @Before
    public void setUp() {
        appDnsRule.setDnsRuleId("dnsRuleId");
        appDnsRule.setAppInstanceId("appInstanceId");
        appDnsRule.setDomainName("domainName");
        appDnsRule.setIpAddress("1.1.1.1");
        appDnsRule.setIpAddressType("ipAddressType");
        appDnsRule.setTenantId("tenantId");
        appDnsRule.setTtl(1);
        appDnsRule.setAppDRule(appDRule);
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
        Assert.assertEquals(appDRule, appDnsRule.getAppDRule());
    }
}
