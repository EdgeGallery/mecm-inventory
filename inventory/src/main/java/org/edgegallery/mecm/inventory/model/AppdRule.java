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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * APPD rule schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "appdruleinventory")
public final class AppdRule implements BaseModel {

    @Id
    @Column(name = "appd_rule_id")
    private String appdRuleId;

    @Column(name = "app_instance_id")
    private String appInstanceId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "status")
    private String status;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_support_mp1")
    private Boolean appSupportMp1;

    @OneToMany(mappedBy = "appDRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<AppDnsRule> appDNSRule;

    @OneToMany(mappedBy = "appDRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<AppTrafficRule> appTrafficRule;

    @Override
    public String getIdentifier() {
        return appdRuleId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.APPD_RULE;
    }
}