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

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Traffic filter schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trafficfilterinventory")
public final class TrafficFilter implements BaseModel {

    @Id
    @Column(name = "traffic_filter_id")
    private String trafficFilterId;

    @Column(name = "src_address")
    @CollectionTable(name = "trafficfiltersrcaddressinventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> srcAddress;

    @Column(name = "src_port")
    @CollectionTable(name = "trafficfiltersrcportinventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> srcPort;

    @Column(name = "dst_address")
    @CollectionTable(name = "trafficfilterdstaddressinventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> dstAddress;

    @Column(name = "dst_port")
    @CollectionTable(name = "trafficfilterdstportinventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> dstPort;

    @Column(name = "protocol")
    @CollectionTable(name = "trafficfilterprotocolinventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> protocol;

    @Column(name = "tag")
    @CollectionTable(name = "trafficfiltertaginventory", joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> tag;

    @Column(name = "src_tunnel_address")
    @CollectionTable(name = "trafficfiltersrctunneladdressinventory",
            joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> srcTunnelAddress;

    @Column(name = "dst_tunnel_address")
    @CollectionTable(name = "trafficfilterdsttunneladdressinventory",
            joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> dstTunnelAddress;

    @Column(name = "src_tunnel_port")
    @CollectionTable(name = "trafficfiltersrctunnelportinventory",
            joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> srcTunnelPort;

    @Column(name = "dst_tunnel_port")
    @CollectionTable(name = "trafficfilterdsttunnelportinventory",
            joinColumns = @JoinColumn(name = "traffic_filter_id"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> dstTunnelPort;

    @Column(name = "qci")
    private int qci;

    @Column(name = "dscp")
    private int dscp;

    @Column(name = "tc")
    private int tc;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "traffic_rule_id", nullable = false)
    private AppTrafficRule trafficRule;

    @Override
    public String getIdentifier() {
        return trafficFilterId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.TRAFFIC_FILTER;
    }
}