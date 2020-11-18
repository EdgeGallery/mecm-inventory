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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Application lifecycle management schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenantinventory")
public final class Tenant implements BaseModel {

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "applcm_count")
    private int appLcms;

    @Column(name = "appstore_count")
    private int appStores;

    @Column(name = "mechost_count")
    private int mecHosts;

    @Column(name = "mechwcapability_count")
    private int mecHwCapabilities;

    @Column(name = "mecapplication_count")
    private int mecApplications;

    @Override
    public String getIdentifier() {
        // Return identifier
        return tenantId;
    }

    @Override
    public String getTenantId() {
        return getIdentifier();
    }

    @Override
    public ModelType getType() {
        return ModelType.TENANT;
    }

    // models are not embedded in tenant currently as topic is under discussion.
}
