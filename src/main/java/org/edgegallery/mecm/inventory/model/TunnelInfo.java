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
 * Tunnel info.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tunnelinfoinventory")
public final class TunnelInfo implements BaseModel {

    @Id
    @Column(name = "tunnel_info_id")
    private String tunnelInfoId;

    @Column(name = "tunnel_type")
    private String tunnelType;

    @Column(name = "tunnel_dst_address")
    private String tunnelDstAddress;

    @Column(name = "tunnel_src_address")
    private String tunnelSrcAddress;

    @Column(name = "tunnel_specific_data")
    private String tunnelSpecificData;

    @Column(name = "tenant_id")
    private String tenantId;

    @Override
    public String getIdentifier() {
        return tunnelInfoId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.TUNNEL_INFO;
    }
}