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
public class MecHostInventoryHandlerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(roles = "MECM_TENANT")
    public void validateMecHostInventory() throws Exception {
        String tenantId = "111111";

        // Test MecHost record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", \"edgerepoPort\": "
                                + "\"10000\", \"applcmIp\": \"1.1.1.1\"}"));

        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Test MecHost record get by MecHost ID
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId
                        + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"mechostIp\":\"1.1.1.1\",\"mechostName\":null,\"zipCode\":null,\"city\":null,\"address\":null,"
                        + "\"affinity\":null,\"userName\":null,\"edgeName\":null,\"edgerepoIp\":\"1.1.1.1\","
                        + "\"edgerepoPort\":\"10000\",\"edgerepoUsername\":null,\"applcmIp\":\"1.1.1.1\"}",
                getByIdResponse);

        // Test MecHost record delete by MecHost ID
        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);
    }
}
