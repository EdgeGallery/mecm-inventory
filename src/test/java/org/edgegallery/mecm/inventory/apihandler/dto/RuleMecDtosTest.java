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
public class RuleMecDtosTest {

    @InjectMocks
    AppdRuleDeletedDto appdRuleDeletedDto = new AppdRuleDeletedDto();
    SyncDeletedRulesDto syncDeletedRulesDto = new SyncDeletedRulesDto();
    SyncDeletedMecHostDto syncDeletedMecHostDto = new SyncDeletedMecHostDto();
    SyncUpdatedRulesDto syncUpdatedRulesDto = new SyncUpdatedRulesDto();
    AppdRuleConfigDto appdRuleConfigDto = new AppdRuleConfigDto();
    SyncUpdatedMecHostDto syncUpdatedMecHostDto = new SyncUpdatedMecHostDto();
    MecHostDto mecHostDto = new MecHostDto();
    AppRuleDto appRuleDto = new AppRuleDto();
    DnsRuleDto dnsRuleDto = new DnsRuleDto();
    AppRepoDto appRepoDto = new AppRepoDto();
    DstInterfaceDto dstInterfaceDto = new DstInterfaceDto();
    MecHwCapabilityDto mecHwCapabilityDto = new MecHwCapabilityDto();
    MepmDto mepmDto = new MepmDto();
    TrafficFilterDto trafficFilterDto = new TrafficFilterDto();
    TrafficRuleDto trafficRuleDto = new TrafficRuleDto();
    TunnelInfoDto tunnelInfoDto =new TunnelInfoDto();

    @Before
    public void setUp() {

        appdRuleDeletedDto.setAppInstanceId("appInstanceId");
        appdRuleConfigDto.setAppInstanceId("appInstanceId");
        mecHostDto.setMechostIp("mecHostIp");

        List delList = new ArrayList<AppdRuleDeletedDto>();
        delList.add(appdRuleDeletedDto.getAppInstanceId());
        syncDeletedRulesDto.setAppdRuleDeletedRecs(delList);
        syncDeletedMecHostDto.setMecHostStaleRecs(delList);

        List recList = new ArrayList<AppdRuleConfigDto>();
        recList.add(appdRuleConfigDto.getAppInstanceId());
        syncUpdatedRulesDto.setAppdRuleUpdatedRecs(recList);

        List mecRecList = new ArrayList<MecHostDto>();
        recList.add(mecHostDto.getMechostIp());
        syncUpdatedMecHostDto.setMecHostUpdatedRecs(mecRecList);
    }

    @Test
    public void testAppDnsProcessFlowResponse() {
        Assert.assertEquals("appInstanceId", appdRuleDeletedDto.getAppInstanceId());
        Assert.assertNotNull(appdRuleDeletedDto.toString());

        Assert.assertNotNull(syncDeletedRulesDto.getAppdRuleDeletedRecs());
        Assert.assertNotNull(syncDeletedRulesDto.toString());

        Assert.assertNotNull(syncDeletedMecHostDto.getMecHostStaleRecs());
        Assert.assertNotNull(syncDeletedMecHostDto.toString());

        Assert.assertNotNull(syncUpdatedRulesDto.getAppdRuleUpdatedRecs());
        Assert.assertNotNull(syncUpdatedRulesDto.toString());

        Assert.assertNotNull(syncUpdatedMecHostDto.getMecHostUpdatedRecs());
        Assert.assertNotNull(syncUpdatedMecHostDto.toString());

        Assert.assertNotNull(appRuleDto.toString());
        Assert.assertNotNull(dnsRuleDto.toString());
        Assert.assertNotNull(appRepoDto.toString());
        Assert.assertNotNull(dstInterfaceDto.toString());
        Assert.assertNotNull(mecHwCapabilityDto.toString());
        Assert.assertNotNull(mepmDto.toString());
        Assert.assertNotNull(trafficFilterDto.toString());
        Assert.assertNotNull(trafficRuleDto.toString());
        Assert.assertNotNull(tunnelInfoDto.toString());
    }
}
