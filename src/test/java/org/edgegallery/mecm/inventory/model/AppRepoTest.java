package org.edgegallery.mecm.inventory.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppRepoTest {

    @InjectMocks
    AppRepo appRepo = new AppRepo();

    @Before
    public void setUp() {
        appRepo.setRepoId("repoId");
        appRepo.setRepoName("repoName");
        appRepo.setRepoEndPoint("repoEndPoint");
        appRepo.setRepoUserName("repoUserName");
        appRepo.setRepoPassword("repoPassword");
    }

    @Test
    public void testAppDnsProcessFlowResponse() {
        Assert.assertEquals("repoId", appRepo.getRepoId());
        Assert.assertEquals("repoName", appRepo.getRepoName());
        Assert.assertEquals("repoEndPoint", appRepo.getRepoEndPoint());
        Assert.assertEquals("repoUserName", appRepo.getRepoUserName());
        Assert.assertEquals("repoPassword", appRepo.getRepoPassword());

    }
}
