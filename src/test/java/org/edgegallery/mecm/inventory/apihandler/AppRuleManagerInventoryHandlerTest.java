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
public class AppRuleManagerInventoryHandlerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(roles = "MECM_ADMIN")
    public void validateAppRuleManagerInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Test APP Rule manager record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/apprulemanagers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content(
                                "{\"appRuleName\": \"rule1\", \"appRuleIp\": \"1.1.1.1\", \"appRulePort\": \"10000\", \"userName\": \"Test\" }"));

        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Test APP Rule manager record get by App rule manager ID
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/apprulemanagers/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"appRuleName\":\"rule1\",\"appRuleIp\":\"1.1.1.1\",\"appRulePort\":\"10000\",\"userName\":\"Test\"}",
                getByIdResponse);

        // Test APP Rule manager record delete by APP Rule manager ID
        ResultActions deleteByIdResult =
                mvc.perform(
                        MockMvcRequestBuilders.delete("/inventory/v1/apprulemanagers/1.1.1.1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);
    }

    @Test
    @WithMockUser(roles = "MECM_ADMIN")
    public void validateAppLcmInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/apprulemanagers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).with(csrf())
                .content(
                        "{\"appRuleName\": \"rule1\", \"appRuleIp\": \"1.1.1.1\", \"appRulePort\": \"10000\", \"userName\": \"Test\" }"));

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/apprulemanagers/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"appRuleIp\": \"1.1.1.1\",\"appRuleName\": \"rule1\", \"appRulePort\": "
                                + "\"10001\", \"userName\": \"Test\" }"));
        MvcResult updateMvcResult = updateResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String updateResponse = updateMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Updated\"}", updateResponse);

        // Test APP Rule manager get all records
        ResultActions getAllResults =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/apprulemanagers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"appRuleName\":\"rule1\",\"appRuleIp\":\"1.1.1.1\",\"appRulePort\":\"10001\",\"userName\":\"Test\"}]",
                getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/apprulemanagers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);
    }
}
