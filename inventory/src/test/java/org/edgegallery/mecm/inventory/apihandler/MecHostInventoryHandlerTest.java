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

import static org.edgegallery.mecm.inventory.utils.Constants.APPLCM_URI;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplicationTest.class)
@AutoConfigureMockMvc
public class MecHostInventoryHandlerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateMecHostInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Add mepm record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"mepm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\": "
                                + "\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);

        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Test MecHost record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\",\"zipCode\":\"1234\",\"userName\":\"User\", \"mepmIp\": \"1.1.1.1\", "
                                + "\"affinity\":\"part1,part2\",\"vim\":\"k8s\","
                                + " \"coordinates\": \"1,1\"}").with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Test MecHost record get by MecHost ID
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"mechostIp\":\"1.1.1.1\",\"tenantId\":\"18db0283-3c67-4042-a708-a8e4a10c6b32\",\"mechostName\":\"TestHost\",\"zipCode\":\"1234\",\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\",\"affinity\":\"part1,part2\",\"userName\":\"User\","
                        + "\"mepmIp\":\"1.1.1.1\",\"coordinates\":\"1,1\","
                        + "\"hwcapabilities\":[],\"vim\":\"k8s\",\"configUploadStatus\":null}", getByIdResponse);

        // Test MecHost record delete by MecHost ID
        // Prepare the mock REST server
        String urlmepmDelete =
                "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts" + "/" + "1.1.1"
                        + ".1";
        MockRestServiceServer mockServerHostDelete = MockRestServiceServer.createServer(restTemplate);
        mockServerHostDelete.expect(requestTo(urlmepmDelete))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);

        // Test Mepm record delete by Mepm ID
        ResultActions deleteByIdResultMepm =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultMepm = deleteByIdResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMepm = deleteByIdMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMepm);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateMecHostInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Add Mepm record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"mepm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\":\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);

        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        ;
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).with(csrf())
                .content("{\"mechostIp\": \"1.1.1.1\", \"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\","
                        + "\"coordinates\":\"1,1\"}").with(csrf())
                .header("access_token", "SampleToken"));

        // Update record
        // Prepare the mock REST server
        String urlmepmPut = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPut = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPut.expect(requestTo(urlmepmPut))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess());

        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\","
                                + "\"coordinates\":\"1,1\",\"vim\":\"k8s\",\"configUploadStatus\":null}")
                        .with(csrf())
                        .header("access_token", "SampleToken"));
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
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"mechostIp\":\"1.1.1.1\",\"tenantId\":\"18db0283-3c67-4042-a708-a8e4a10c6b32\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\",\"address\":\"Test Address\",\"affinity\":null,\"userName\":null,\"mepmIp\":\"1.1.1.1\",\"coordinates\":\"1,1\",\"hwcapabilities\":[],\"vim\":\"k8s\",\"configUploadStatus\":null}]",
                getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);

        // Test Mepm record delete by Mepm ID
        ResultActions deleteByIdResultMepm =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultMepm = deleteByIdResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMepm = deleteByIdMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMepm);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateMecHostHardwareCapabilityInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Add Mepm record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"mepm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\":\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);

        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Test MecHost record post
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\", "
                                + "\"affinity\":\"part1,part2\",\"mepmIp\": \"1.1.1.1\",\"coordinates\":\"1,1\", "
                                + "\"hwcapabilities\":[{\"hwType\": \"GPU1\","
                                + "\"hwVendor\": \"testvendor1\",\"hwModel\": \"testmodel1\"}]}").with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponse = postMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);

        // Test MecHost record get by MecHost ID
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"mechostIp\":\"1.1.1.1\",\"tenantId\":\"18db0283-3c67-4042-a708-a8e4a10c6b32\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\",\"address\":\"Test Address\",\"affinity\":\"part1,part2\",\"userName\":null,\"mepmIp\":\"1.1.1.1\",\"coordinates\":\"1,1\",\"hwcapabilities\":[{\"hwType\":\"GPU1\",\"hwVendor\":\"testvendor1\",\"hwModel\":\"testmodel1\"}],\"vim\":null,\"configUploadStatus\":null}",
                getByIdResponse);

        // Test MecHost record delete by MecHost ID
        // Prepare the mock REST server
        String urlmepmDelete =
                "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts" + "/"
                        + "1.1.1.1";
        MockRestServiceServer mockServerHostDelete = MockRestServiceServer.createServer(restTemplate);
        mockServerHostDelete.expect(requestTo(urlmepmDelete))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);

        // Test Mepm record delete by Mepm ID
        ResultActions deleteByIdResultMepm =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultMepm = deleteByIdResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMepm = deleteByIdMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMepm);
    }

    @Test
    @WithMockUser(roles = {"MECM_ADMIN", "MECM_TENANT", "MECM_GUEST"})
    public void validateMecHostHardwareCapabilityInventoryUpdate() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Add Mepm record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"mepm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\": "
                                + "\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);

        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Create record
        mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).with(csrf())
                .content("{ \"mechostIp\": \"1.1.1.1\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                        + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\", "
                        + "\"mepmIp\": \"1.1.1.1\",\"coordinates\":\"1,1\",\"hwcapabilities\":[{\"hwType\":\"GPU1\","
                        + "\"hwVendor\":\"testvendor1\",\"hwModel\":\"testmodel1\"}]}").with(csrf())
                .header("access_token", "SampleToken"));

        // Prepare the mock REST server
        String urlmepmPut = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPut = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPut.expect(requestTo(urlmepmPut))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess());

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/tenants/" + tenantId + "/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\","
                                + "\"coordinates\":\"1,1\",\"hwcapabilities\":[]}").with(csrf())
                        .header("access_token", "SampleToken"));
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
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"mechostIp\":\"1.1.1.1\",\"tenantId\":\"18db0283-3c67-4042-a708-a8e4a10c6b32\",\"mechostName\":\"TestHost\",\"zipCode\":null,\"city\":\"TestCity\",\"address\":\"Test Address\",\"affinity\":null,\"userName\":null,\"mepmIp\":\"1.1.1.1\",\"coordinates\":\"1,1\",\"hwcapabilities\":[],\"vim\":null,\"configUploadStatus\":null}]",
                getAllResponse);

        // Test Delete all records
        ResultActions deleteAllresult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteAllMvcResult = deleteAllresult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteAllResponse = deleteAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteAllResponse);

        // Test Mepm record delete by Mepm ID
        ResultActions deleteByIdResultMepm =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult deleteByIdMvcResultMepm = deleteByIdResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMepm = deleteByIdMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMepm);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateMecApplicationInventory() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";
        String hostIp = "1.1.1.1";

        // Add Mepm record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"mepm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\": "
                                + "\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);

        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts";
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        //Mec application record post
        ResultActions postMecResult =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/tenants/" + tenantId + "/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"mechostName\":\"TestHost1\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\", "
                                + "\"mepmIp\": \"1.1.1.1\", \"affinity\":\"part1,part2\",\"coordinates\":\"1,1\","
                                + "\"hwcapabilities\":[{\"hwType\": \"GPU1\",\"hwVendor\": \"testvendor1\","
                                + "\"hwModel\": \"testmodel1\"}]}").with(csrf())
                        .header("access_token", "SampleToken"));

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
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
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
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
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
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"apps\":[{\"appInstanceId\":\"4c6fb452-640d-4e73-9016-6ccec856080d\",\"appName\":\"app-name\","
                        + "\"packageId\":\"ea339be5f1044dcf9f76b05db46f0a56\","
                        + "\"capabilities\":[\"GPU1\",\"GPU2\"],\"status\":\"Created\"}]}", getAllResponse);

        // Test Updates application record entry into the Inventory.
        //prepare mock rest server
        String urlmepmPut = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts" +
                "/" + "1.1.1.1";
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo(urlmepmPut))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess());
        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/tenants/" + tenantId + "/mechosts/"
                        + hostIp + "/apps/4c6fb452-640d-4e73-9016-6ccec856080d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content(
                                "{\"appInstanceId\":\"4c6fb452-640d-4e73-9016-6ccec856080d\",\"appName\":\"app-name1\","
                                        + "\"packageId\":\"ea339be5f1044dcf9f76b05db46f0a56\","
                                        + "\"capabilities\":[\"GPU1\",\"GPU2\"],\"status\":\"Created\"}").with(csrf())
                        .header("access_token", "SampleToken"));
        MvcResult updateMvcResult = updateResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String updateResponse = updateMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Updated\"}",
                updateResponse);

        // Test Retrieves application record entry from the Inventory
        String urlmepmGet = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts" +
                "/" + "1.1.1.1";
        mockServer.expect(requestTo(urlmepmGet))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/tenants/" + tenantId + "/mechosts/"
                        + hostIp + "/apps/4c6fb452-640d-4e73-9016-6ccec856080d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"appInstanceId\":\"4c6fb452-640d-4e73-9016-6ccec856080d\",\"appName\":\"app-name1\","
                + "\"packageId\":\"ea339be5f1044dcf9f76b05db46f0a56\","
                + "\"capabilities\":[\"GPU1\",\"GPU2\"],\"status\":\"Created\"}", getByIdResponse);

        // Test MecApplication record delete by  application ID
        String urlmepmDelete =
                "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI + "/tenants/" + tenantId + "/hosts" + "/"
                        + "1.1.1.1";
        mockServer.expect(requestTo(urlmepmDelete))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResultActions deleteByIdResult =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/tenants/" + tenantId + "/mechosts/"
                        + hostIp + "/apps/4c6fb452-640d-4e73-9016-6ccec856080d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult deleteByIdMvcResult = deleteByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponse = deleteByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponse);

        // Test Mepm record delete by Mepm ID
        ResultActions deleteByIdResultMepm =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mepms/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultMepm = deleteByIdResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMepm = deleteByIdMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMepm);
    }
}
