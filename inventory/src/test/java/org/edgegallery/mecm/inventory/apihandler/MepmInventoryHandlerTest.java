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
public class MepmInventoryHandlerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(roles = "MECM_ADMIN")
    public void validateMepmInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Test mepm record post
        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mepmIp\": \"1.1.1.1\", \"mepmPort\": \"10000\", \"userName\": \"Test\", "
                                + "\"mepmName\": \"mepm123\" }")).andDo(MockMvcResultHandlers.print()).andReturn();

        String postResponse = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Test mepm record get by mepm ID
        result =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())).andDo(MockMvcResultHandlers.print())
                        .andReturn();

        String getByIdResponse = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"mepmName\":\"mepm123\",\"mepmIp\":\"1.1.1.1\",\"mepmPort\":\"10000\","
                        + "\"userName\":\"Test\"}",
                getByIdResponse);

        // Test mepm record delete by mepm ID
        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
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
    public void validateMepmInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).with(csrf())
                .content("{ \"mepmIp\": \"1.1.1.1\", \"mepmPort\": \"10000\", \"userName\": \"Test\", "
                        + "\"mepmName\": \"mepm123\"  }"));

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mepmIp\": \"1.1.1.1\", \"mepmPort\": \"10001\", \"userName\": \"Test\", "
                                + "\"mepmName\": \"mepm123\" }"));
        MvcResult updateMvcResult = updateResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String updateResponse = updateMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Updated\"}", updateResponse);

        // Test mepm get all records
        ResultActions getAllResults =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);
    }
}
