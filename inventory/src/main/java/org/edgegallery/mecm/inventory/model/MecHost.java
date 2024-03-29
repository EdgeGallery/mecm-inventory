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
import org.hibernate.annotations.CreationTimestamp;

/**
 * MEC host Inventory schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mechostinventory")
public final class MecHost implements BaseModel {

    @Id
    @Column(name = "mechost_id")
    private String mechostId;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "mechost_ip")
    private String mechostIp;

    @Column(name = "role")
    private String role;

    @Column(name = "mechost_name")
    private String mechostName;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "affinity")
    private String affinity;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "mepm_ip")
    private String mepmIp;

    @Column(name = "config_upload_status")
    private String configUploadStatus;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "vim")
    private String vim;

    @OneToMany(mappedBy = "mecHost", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<MecHwCapability> hwcapabilities;

    @OneToMany(mappedBy = "mecAppHost", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<MecApplication> applications;

    @Override
    public String getIdentifier() {
        return mechostId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.MEC_HOST;
    }
}
