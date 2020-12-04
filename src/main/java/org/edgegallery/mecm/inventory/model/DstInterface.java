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

package org.edgegallery.mecm.inventory.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dst interface schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dstinterfaceinventory")
public final class DstInterface implements BaseModel {

    @Id
    @Column(name = "dst_interface_id")
    private String dstInterfaceId;

    @Column(name = "interface_type")
    private String interfaceType;

    @Column(name = "src_mac_address")
    private String srcMacAddress;

    @Column(name = "dst_mac_address")
    private String dstMacAddress;

    @Column(name = "dst_ip_address")
    private String dstIpAddress;

    @Column(name = "tenant_id")
    private String tenantId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tunnel_info_id", nullable = false)
    private TunnelInfo tunnelInfo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "traffic_rule_id", nullable = false)
    private AppTrafficRule trafficRule;

    @Override
    public String getIdentifier() {
        return dstInterfaceId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.DST_INTERFACE;
    }
}