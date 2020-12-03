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
    @WithMockUser(roles = {"MECM_TENANT","MECM_GUEST"})
    public void validateMecHostInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Test MecHost record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                                + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\", "
                                + "\"affinity\":\"part1,part2\"}"));

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
                "{\"mechostIp\":\"1.1.1.1\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\",\"affinity\":\"part1,part2\",\"userName\":null,\"edgerepoName\":null,"
                        + "\"edgerepoIp\":\"1.1.1.1\",\"edgerepoPort\":\"10000\",\"edgerepoUsername\":null,"
                        + "\"applcmIp\":\"1.1.1.1\",\"hwcapabilities\":[]}", getByIdResponse);

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

    @Test
    @WithMockUser(roles = {"MECM_TENANT","MECM_GUEST"})
    public void validateMecHostInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                        + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\"}"));

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.2\", "
                                + "\"edgerepoPort\": \"10001\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\"}"));
        MvcResult updateMvcResult = updateResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String updateResponse = updateMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Updated\"}",
                updateResponse);

        // Test Mechost to get all records
        ResultActions getAllResults =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"mechostIp\":\"1.1.1.1\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\",\"affinity\":null,\"userName\":null,\"edgerepoName\":null,"
                        + "\"edgerepoIp\":\"1.1.1.2\",\"edgerepoPort\":\"10001\",\"edgerepoUsername\":null,"
                        + "\"applcmIp\":\"1.1.1.1\",\"hwcapabilities\":[]}]", getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT","MECM_GUEST"})
    public void validateMecHostHardwareCapabilityInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Test MecHost record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                                + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\", "
                                + "\"affinity\":\"part1,part2\",\"hwcapabilities\":[{\"hwType\": \"GPU1\",\"hwVendor\": \"testvendor1\",\"hwModel\": \"testmodel1\"}]}"));


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
                "{\"mechostIp\":\"1.1.1.1\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\",\"affinity\":\"part1,part2\",\"userName\":null,\"edgerepoName\":null,"
                        + "\"edgerepoIp\":\"1.1.1.1\",\"edgerepoPort\":\"10000\",\"edgerepoUsername\":null,"
                        + "\"applcmIp\":\"1.1.1.1\",\"hwcapabilities\":[{\"hwType\":\"GPU1\","
                        + "\"hwVendor\":\"testvendor1\",\"hwModel\":\"testmodel1\"}]}", getByIdResponse);

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

    @Test
    @WithMockUser(roles = {"MECM_TENANT","MECM_GUEST"})
    public void validateMecHostHardwareCapabilityInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                        + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\", "
                        + "\"hwcapabilities\":[{\"hwType\":\"GPU1\","
                        + "\"hwVendor\":\"testvendor1\",\"hwModel\":\"testmodel1\"}]}"));

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.2\", "
                                + "\"edgerepoPort\": \"10001\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\", "
                                + "\"hwcapabilities\":[]}"));
        MvcResult updateMvcResult = updateResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String updateResponse = updateMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Updated\"}",
                updateResponse);

        // Test Mechost to get all records
        ResultActions getAllResults =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"mechostIp\":\"1.1.1.1\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\",\"affinity\":null,\"userName\":null,\"edgerepoName\":null,"
                        + "\"edgerepoIp\":\"1.1.1.2\",\"edgerepoPort\":\"10001\",\"edgerepoUsername\":null,"
                        + "\"applcmIp\":\"1.1.1.1\",\"hwcapabilities\":[]}]", getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT","MECM_GUEST"})
    public void validateMecApplicationInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b31";
        String hostIp = "1.1.1.1";

        //Mec application record post
        ResultActions postMecResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                                + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"applcmIp\": \"1.1.1.1\", "
                                + "\"affinity\":\"part1,part2\",\"hwcapabilities\":[{\"hwType\": \"GPU1\",\"hwVendor\": \"testvendor1\",\"hwModel\": \"testmodel1\"}]}"));

        MvcResult postMecMvcResult = postMecResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postMecResponse = postMecMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postMecResponse);

        //MecApplication record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId
                        + "/mechosts/" + hostIp + "/apps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"appInstanceId\":\"4c6fb452-640d-4e73-9016-6ccec856080d\",\"appName\":\"app-name\","
                                + "\"packageId\":\"ea339be5f1044dcf9f76b05db46f0a56\","
                                + "\"capabilities\":[\"GPU1\",\"GPU2\"],\"status\":\"Created\"}"));

        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Retrieves MEC host specific record
        ResultActions getResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId
                        + "/mechosts/" + hostIp + "/capabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MvcResult getMvcResult = getResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getResponse = getMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"hwcapabilities\":[{\"hwType\":\"GPU1\",\"hwVendor\":\"testvendor1\","
                        + "\"hwModel\":\"testmodel1\"}]}", getResponse);

        // Test Mechost to get all records
        ResultActions getAllResults =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId
                        + "/mechosts/" + hostIp + "/capabilities/GPU1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"apps\":[{\"appInstanceId\":\"4c6fb452-640d-4e73-9016-6ccec856080d\",\"appName\":\"app-name\","
                        + "\"packageId\":\"ea339be5f1044dcf9f76b05db46f0a56\","
                        + "\"capabilities\":[\"GPU1\",\"GPU2\"],\"status\":\"Created\"}]}", getAllResponse);

        // Test MecApplication record delete by  application ID
        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts/"
                        + hostIp + "/apps/4c6fb452-640d-4e73-9016-6ccec856080d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);
    }
}
