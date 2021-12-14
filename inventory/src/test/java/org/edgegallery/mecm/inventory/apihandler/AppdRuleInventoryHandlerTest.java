/*
 *  Copyright 2020 Huawei Technologies Co., Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.edgegallery.mecm.inventory.apihandler;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplicationTest.class)
@AutoConfigureMockMvc
public class AppdRuleInventoryHandlerTest {

    @Autowired
    MockMvc mvc;


    @Test
    @WithMockUser(roles = "MECM_ADMIN")
    public void validateAppdRuleInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";
        String appInstanceId = "28db0283-3c67-4042-a708-a8e4a10c6b32";
        String postBody = "{\n"
                + "  \"appTrafficRule\": [\n"
                + "    {\n"
                + "      \"trafficRuleId\": \"TrafficRule1\",\n"
                + "      \"filterType\": \"FLOW\",\n"
                + "      \"priority\": 1,\n"
                + "      \"action\": \"DROP\",\n"
                + "      \"trafficFilter\": [\n"
                + "        {\n"
                + "          \"srcAddress\": [\n"
                + "            \"192.168.1.1\"\n"
                + "          ],\n"
                + "          \"srcPort\": [\n"
                + "            \"8080\"\n"
                + "          ],\n"
                + "          \"dstAddress\": [\n"
                + "            \"192.168.1.1\"\n"
                + "          ],\n"
                + "          \"dstPort\": [\n"
                + "            \"8080\"\n"
                + "          ],\n"
                + "          \"protocol\": [\n"
                + "            \"ICMP\"\n"
                + "          ],\n"
                + "          \"tag\": [\"tag\"],\n"
                + "          \"srcTunnelAddress\": [\"1.1.1.1\"],\n"
                + "          \"dstTunnelAddress\": [\"2.2.2.2\"],\n"
                + "          \"srcTunnelPort\": [\"8080\"],\n"
                + "          \"dstTunnelPort\": [\"8080\"],\n"
                + "          \"qci\": 1,\n"
                + "          \"dscp\": 0,\n"
                + "          \"tc\": 1\n"
                + "        }\n"
                + "      ],\n"
                + "      \"dstInterface\": [{\n"
                + "        \"interfaceType\" : \"tunnel\",\n"
                + "        \"tunnelInfo\" : {\n"
                + "          \"tunnelType\" : \"tunneltype\",\n"
                + "          \"tunnelDstAddress\" : \"1.1.1.1\",\n"
                + "          \"tunnelSrcAddress\" : \"2.2.2.2\",\n"
                + "          \"tunnelSpecificData\" : \"some data\"\n"
                + "        },\n"
                + "        \"srcMacAddress\" : \"08:d4:0c:20:61:e7\",\n"
                + "        \"dstMacAddress\" : \"08:d4:0c:20:61:e7\",\n"
                + "        \"dstIpAddress\" : \"3.3.3.3\"\n"
                + "      }]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"appDnsRule\": [\n"
                + "    {\n"
                + "      \"dnsRuleId\": \"dnsrule1\",\n"
                + "      \"domainName\": \"domainname\",\n"
                + "      \"ipAddressType\": \"IPv4\",\n"
                + "      \"ipAddress\": \"1.1.1.1\",\n"
                + "      \"ttl\": 86400\n"
                + "    }\n"
                + "  ],\n"
                + "  \"appSupportMp1\": false,\n"
                + "  \"appName\": \"applicationname\"\n"
                + "}";

        // Test appdRule record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId
                        + "/app_instances/" + appInstanceId + "/appd_configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content(postBody));
        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        ResultActions getResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId
                        + "/app_instances/" + appInstanceId + "/appd_configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MvcResult getMvcResult = getResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String getResponse = getMvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(getResponse);
    }
}
