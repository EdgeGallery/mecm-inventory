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
 * MEC host Inventory schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apprepoinventory")
public final class AppRepo implements BaseModel {

    @Id
    @Column(name = "repo_id")
    private String repoId;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "repo_endpoint")
    private String repoEndPoint;

    @Column(name = "repo_username")
    private String repoUserName;

    @Column(name = "repo_password")
    private String repoPassword;

    @Override
    public String getIdentifier() {
        return repoId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public ModelType getType() {
        return ModelType.APP_REPO;
    }
}
