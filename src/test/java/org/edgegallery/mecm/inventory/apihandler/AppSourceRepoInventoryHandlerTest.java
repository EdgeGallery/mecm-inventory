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
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplicationTest.class)
@AutoConfigureMockMvc
public class AppSourceRepoInventoryHandlerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateAppSourceRepoInventory() throws Exception {

        // Add MEPM record post
        ResultActions postResultAppRepo =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/apprepos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"repoEndPoint\": \"1.1.1.1\", \"repoName\": \"AppRepo1\","
                                + " \"repoUserName\": \"admin\", \"repoPassword\": \"Harbor12345\" }"));

        MvcResult postMvcResultAppRepo = postResultAppRepo.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseAppRepo = postMvcResultAppRepo.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseAppRepo);

        // Test MecHost record get by repo endpoint
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/apprepos/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"repoEndPoint\":\"1.1.1.1\",\"repoName\":\"AppRepo1\",\"repoUserName\":\"admin\",\"repoPassword\":\"Harbor12345\"}",
                getByIdResponse);

        // Test MEPM record delete by MEPM ID
        ResultActions deleteByIdResultAppRepos =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/apprepos/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultAppRepos = deleteByIdResultAppRepos.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseAppRepos = deleteByIdMvcResultAppRepos.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseAppRepos);
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void validateAppReposInventoryUpdate() throws Exception {
        // Add MEPM record post
        ResultActions postResultAppRepo =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/apprepos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"repoEndPoint\": \"1.1.1.1\", \"repoName\": \"AppRepo1\","
                                + " \"repoUserName\": \"admin\", \"repoPassword\": \"Harbor12345\" }"));

        MvcResult postMvcResultAppRepo = postResultAppRepo.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String postResponseAppRepo = postMvcResultAppRepo.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponseAppRepo);

        // Test app repo record get by repo endpoint
        ResultActions getByIdResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/apprepos/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult getByIdMvcResult = getByIdResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getByIdResponse = getByIdMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "{\"repoEndPoint\":\"1.1.1.1\",\"repoName\":\"AppRepo1\",\"repoUserName\":\"admin\",\"repoPassword\":\"Harbor12345\"}",
                getByIdResponse);

        // Update record
        ResultActions updateResult =
                mvc.perform(MockMvcRequestBuilders.put("/inventory/v1/apprepos/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{\"repoEndPoint\":\"1.1.1.1\",\"repoName\":\"AppRepo1\",\"repoUserName\":\"admin\","
                                + "\"repoPassword\":\"Harbor12346\"}")
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
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/apprepos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));
        MvcResult getAllMvcResult = getAllResults.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String getAllResponse = getAllMvcResult.getResponse().getContentAsString();
        Assert.assertEquals(
                "[{\"repoEndPoint\":\"1.1.1.1\",\"repoName\":\"AppRepo1\",\"repoUserName\":\"admin\","
                        + "\"repoPassword\":\"Harbor12346\"}]",
                getAllResponse);

        // Test MEPM record delete by MEPM ID
        ResultActions deleteByIdResultAppRepos =
                mvc.perform(MockMvcRequestBuilders.delete("/inventory/v1/apprepos/1.1.1.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf()));

        MvcResult deleteByIdMvcResultAppRepos = deleteByIdResultAppRepos.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String deleteByIdResponseAppRepos = deleteByIdMvcResultAppRepos.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Deleted\"}", deleteByIdResponseAppRepos);
    }
}
