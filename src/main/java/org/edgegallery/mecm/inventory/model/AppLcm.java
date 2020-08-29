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
@Table(name = "applcminventory")
public final class AppLcm implements BaseModel {

    @Id
    @Column(name = "applcm_id")
    private String applcmId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Column(name = "applcm_ip")
    private String applcmIp;

    @Column(name = "applcm_port")
    private String applcmPort;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Override
    public String getIdentifier() {
        return applcmId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.APP_LCM;
    }
}