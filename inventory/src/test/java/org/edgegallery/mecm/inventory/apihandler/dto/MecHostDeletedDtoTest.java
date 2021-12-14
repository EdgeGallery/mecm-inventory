package org.edgegallery.mecm.inventory.apihandler.dto;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MecHostDeletedDtoTest {

    @InjectMocks
    MecHostDeletedDto mecHostDeletedDto = new MecHostDeletedDto();

    @Before
    public void setUp() {

        mecHostDeletedDto.setMechostIp("mecHostIp");

        List delList = new ArrayList<MecHostDeletedDto>();
        delList.add(mecHostDeletedDto.getMechostIp());
    }

    @Test
    public void testAppDnsProcessFlowResponse() {
        Assert.assertEquals("mecHostIp", mecHostDeletedDto.getMechostIp());
        Assert.assertNotNull(mecHostDeletedDto.toString());
    }
}
