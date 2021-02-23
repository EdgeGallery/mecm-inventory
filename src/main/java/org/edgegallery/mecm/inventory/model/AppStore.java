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
 * App store Inventory schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appstoreinventory")
public final class AppStore implements BaseModel {

    @Id
    @Column(name = "appstore_id")
    private String appstoreId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "appstore_ip")
    private String appstoreIp;

    @Column(name = "appstore_port")
    private String appstorePort;

    @Column(name = "appstore_name")
    private String appstoreName;

    @Column(name = "appstore_reponame")
    private String appstoreRepoName;

    @Column(name = "appstore_repo")
    private String appstoreRepo;

    @Column(name = "appstore_repousername")
    private String appstoreRepoUserName;

    @Column(name = "appstore_repopassword")
    private String appstoreRepoPassword;

    @Column(name = "producer")
    private String producer;

    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Override
    public String getIdentifier() {
        return appstoreId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.APP_STORE;
    }
}
