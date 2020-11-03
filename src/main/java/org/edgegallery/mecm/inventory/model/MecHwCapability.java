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

import java.time.LocalDateTime;
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
 * MEC host hardware capability Inventory schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mechwcapabilityinventory")
public final class MecHwCapability implements BaseModel {

    @Id
    @Column(name = "capability_id")
    private String mecCapabilityId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Column(name = "hw_type")
    private String hwType;

    @Column(name = "hw_vendor")
    private String hwVendor;

    @Column(name = "hw_model")
    private String hwModel;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mechost_id", nullable = false)
    private MecHost mecHost;

    @Override
    public String getIdentifier() {
        return mecCapabilityId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.MEC_HW_CAPABILITY;
    }
}
