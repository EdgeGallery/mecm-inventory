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

import static org.edgegallery.mecm.inventory.utils.Constants.APPLCM_HOST_URL;
import static org.edgegallery.mecm.inventory.utils.Constants.APPLCM_URI;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplicationTest.class)
@AutoConfigureMockMvc
public class ConfigHandlerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(roles = "MECM_ADMIN")
    public void validateConfigOperation() throws Exception {
        String tenantId = "18db0283-3c67-4042-a708-a8e4a10c6b32";

        // Add MEPM record post
        ResultActions postResultMepm =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")

                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"mepmName\": \"applcm123\", \"mepmIp\": \"1.1.1.1\", \"mepmPort\": "
                                + "\"10000\", "
                                + "\"userName\": \"Test\" }"));

        MvcResult postMvcResultMepm = postResultMepm.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMepm = postMvcResultMepm.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMepm);


        // Prepare the mock REST server
        String urlmepmPost = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_HOST_URL;
        MockRestServiceServer mockServerHostPost = MockRestServiceServer.createServer(restTemplate);
        mockServerHostPost.expect(requestTo(urlmepmPost))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Add MecHost record
        ResultActions postResultMecHost =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mechosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mechostIp\": \"1.1.1.1\", \"edgerepoIp\": \"1.1.1.1\", "
                                + "\"edgerepoPort\": \"10000\",\"mechostName\":\"TestHost\",\"city\":\"TestCity\","
                                + "\"address\":\"Test Address\", \"mepmIp\": \"1.1.1.1\",\"coordinates\":\"1,1\"}")
                        .with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult postMvcResultMecHost = postResultMecHost.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseMecHost = postMvcResultMecHost.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseMecHost);

        // Begin test for file upload
        // Prepare the mock REST server
        String url = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_URI;
        MockRestServiceServer mockServerUpload = MockRestServiceServer.createServer(restTemplate);
        mockServerUpload.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        // Test file upload
        File file = ResourceUtils.getFile("classpath:TestFile");
        ResultActions resultActions =
                mvc.perform(MockMvcRequestBuilders.multipart("/inventory/v1/mechosts/1.1.1.1/k8sconfig")
                        .file(new MockMultipartFile("file", "TestFile", MediaType.TEXT_PLAIN_VALUE,
                                FileUtils.openInputStream(file)))
                        .with(csrf())
                        .header("access_token", "SampleToken"));
        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();


        // Begin test for file removal
        // Prepare the mock REST server
        MockRestServiceServer mockServerRemove = MockRestServiceServer.createServer(restTemplate);
        mockServerRemove.expect(requestTo(url))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        // Test file removal
        ResultActions deleteByIdResultConfig =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mechosts/1.1.1.1/k8sconfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("access_token", "SampleToken"));

        deleteByIdResultConfig.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Test MecHost record delete by MecHost ID
        // Prepare the mock REST server
        String urlmepmDelete = "http://" + "1.1.1.1" + ":" + "10000" + APPLCM_HOST_URL + "/" + "1.1.1.1";
        MockRestServiceServer mockServerHostDelete = MockRestServiceServer.createServer(restTemplate);
        mockServerHostDelete.expect(requestTo(urlmepmDelete))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResultActions deleteByIdResultMecHost =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/mechosts/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header("access_token", "SampleToken"));

        MvcResult deleteByIdMvcResultMecHost = deleteByIdResultMecHost.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseMecHost = deleteByIdMvcResultMecHost.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseMecHost);

        // Test MEPM record delete by MEPM ID
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
