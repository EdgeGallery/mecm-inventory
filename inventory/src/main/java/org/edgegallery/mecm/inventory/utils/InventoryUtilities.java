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

package org.edgegallery.mecm.inventory.utils;

import static org.edgegallery.mecm.inventory.utils.Constants.ROLE_ADMIN;
import static org.edgegallery.mecm.inventory.utils.Constants.ROLE_TENANT;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleConfigDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHwCapabilityDto;
import org.edgegallery.mecm.inventory.model.AppDnsRule;
import org.edgegallery.mecm.inventory.model.AppTrafficRule;
import org.edgegallery.mecm.inventory.model.AppdRule;
import org.edgegallery.mecm.inventory.model.DstInterface;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.model.MecHwCapability;
import org.edgegallery.mecm.inventory.model.TrafficFilter;
import org.edgegallery.mecm.inventory.model.TunnelInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class InventoryUtilities {

    private InventoryUtilities() {
    }

    /**
     * Checks is role exist.
     *
     * @param role user role
     * @return true if role exist, otherwise false
     */
    public static boolean hasRole(String role) {
        if ((SecurityContextHolder.getContext() == null)
                || (SecurityContextHolder.getContext().getAuthentication() == null)
                || (SecurityContextHolder.getContext().getAuthentication().getAuthorities() == null)) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(role));
    }

    /**
     * Returns model mapper.
     *
     * @return model mapper
     */
    public static ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    /**
     * Converts appd rule config dto to appd rule config.
     *
     * @param appDRuleConfigDto appd rule config dto
     * @return appd rule
     */
    public static AppdRule getAppdRule(String tenantId, String appInstanceId, AppdRuleConfigDto appDRuleConfigDto) {

        AppdRule appDRule = InventoryUtilities.getModelMapper().map(appDRuleConfigDto, AppdRule.class);
        appDRule.setTenantId(tenantId);
        appDRule.setAppInstanceId(appInstanceId);
        appDRule.setAppdRuleId(tenantId + appInstanceId);

        Set<AppDnsRule> dnsRuleSet = appDRule.getAppDNSRule();
        for (AppDnsRule dnsRule : dnsRuleSet) {
            dnsRule.setAppDRule(appDRule);
            dnsRule.setTenantId(tenantId);
            dnsRule.setAppInstanceId(appInstanceId);
        }

        Set<AppTrafficRule> trafficRuleSet = appDRule.getAppTrafficRule();
        for (AppTrafficRule trafficRule : trafficRuleSet) {
            trafficRule.setAppDRule(appDRule);
            trafficRule.setTenantId(tenantId);
            trafficRule.setAppInstanceId(appInstanceId);
            Set<TrafficFilter> trafficFilterSet = trafficRule.getTrafficFilter();
            for (TrafficFilter trafficFilter : trafficFilterSet) {
                trafficFilter.setTrafficFilterId(UUID.randomUUID().toString());
                trafficFilter.setTenantId(tenantId);
                trafficFilter.setTrafficRule(trafficRule);
            }

            Set<DstInterface> dstInterfaceSet = trafficRule.getDstInterface();
            for (DstInterface dstInterface : dstInterfaceSet) {
                dstInterface.setDstInterfaceId(UUID.randomUUID().toString());
                dstInterface.setTenantId(tenantId);
                dstInterface.setTrafficRule(trafficRule);
                TunnelInfo tunnelInfo = dstInterface.getTunnelInfo();
                if (tunnelInfo != null) {
                    tunnelInfo.setTenantId(tenantId);
                    tunnelInfo.setTunnelInfoId(UUID.randomUUID().toString());
                }
            }
        }

        return appDRule;
    }

    /**
     * To get mec host record.
     *
     * @param mecHostDto mec host dto information
     * @param mecHostIp  mec host ip address
     * @return host mechost
     */
    public static MecHost getMecHost(MecHostDto mecHostDto, String mecHostIp) {
        MecHost host = InventoryUtilities.getModelMapper().map(mecHostDto, MecHost.class);
        host.setMechostId(mecHostIp);

        Set<MecHwCapability> capabilities = new HashSet<>();
        for (MecHwCapabilityDto v : mecHostDto.getHwcapabilities()) {
            MecHwCapability capability = InventoryUtilities.getModelMapper().map(v, MecHwCapability.class);
            capability.setMecCapabilityId(v.getHwType() + host.getMechostId());
            capability.setMecHost(host);

            capabilities.add(capability);
        }
        host.setHwcapabilities(capabilities);
        return host;
    }

    /**
     * Returns MEC host ID based on host IP and tenant ID.
     * @param mecHostIp host IP
     * @param tenantId tenant ID
     * @return mec host ID
     */
    public static String getMecHostId(String mecHostIp, String tenantId) {
        String role = InventoryUtilities.hasRole(ROLE_ADMIN) ? ROLE_ADMIN : ROLE_TENANT;
        if (role.equals(ROLE_ADMIN)) {
            return mecHostIp;
        }
        return mecHostIp + "_" + tenantId;
    }
}
