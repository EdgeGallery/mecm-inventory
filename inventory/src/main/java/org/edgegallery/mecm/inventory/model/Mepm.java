/*
 *  Copyright 2021 Huawei Technologies Co., Ltd.
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
 * MEC Platform manager schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mepminventory")
public final class Mepm implements BaseModel {

    @Id
    @Column(name = "mepm_id")
    private String mepmId;

    @Column(name = "mepm_name")
    private String mepmName;

    @Column(name = "mepm_ip")
    private String mepmIp;

    @Column(name = "mepm_port")
    private String mepmPort;

    @Column(name = "user_name")
    private String userName;

    @Override
    public String getIdentifier() {
        return mepmId;
    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public ModelType getType() {
        return ModelType.MEPM;
    }
}
