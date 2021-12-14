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

import javax.persistence.Column;
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
 * DNS rule schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appdnsruleinventory")
public final class AppDnsRule implements BaseModel {

    @Id
    @Column(name = "dns_rule_id")
    private String dnsRuleId;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "ip_address_type")
    private String ipAddressType;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "ttl")
    private int ttl;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "app_instance_id")
    private String appInstanceId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "appd_rule_id", nullable = false)
    private AppdRule appDRule;

    @Override
    public String getIdentifier() {
        return dnsRuleId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.DNS_RULE;
    }
}