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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

/**
 * App store registry schema.
 */
@Entity
public class AppStore {

    @JsonIgnore
    @Id
    @Column(name = "appstore_id")
    private String appStoreId;

    @NotBlank(message = "Provide the application store IP")
    @Column(name = "appstore_ip")
    private String appstoreIp;

    @NotBlank(message = "Provide the application store port")
    @Column(name = "appstore_port")
    private String appstorePort;

    @NotBlank(message = "Provide the application store uri")
    @Column(name = "appstore_uri")
    private String uri;

    @NotBlank(message = "Provide the application store user name")
    @Column(name = "appstore_username")
    private String userName;

    @NotBlank(message = "Provide the application store password")
    @Column(name = "appstore_password")
    private String password;

    @NotBlank(message = "Provide the application store name")
    @Column(name = "appstore_name")
    private String appstoreName;

    @Column(name = "appstore_producer")
    private String producer;

    @NotBlank(message = "Provide the tenant ID")
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "createTime")
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * Retrieves application store ID.
     * @return application store ID
     */
    public String getAppStoreId() {
        return appStoreId;
    }

    /**
     * Sets application store ID.
     * @param appStoreId application store ID
     */
    public void setAppStoreId(String appStoreId) {
        this.appStoreId = appStoreId;
    }

    /**
     * Retrieves application store IP.
     * @return application store IP
     */
    public String getAppstoreIp() {
        return appstoreIp;
    }

    /**
     * Sets application store IP.
     * @param appstoreIp application store IP
     */
    public void setAppstoreIp(String appstoreIp) {
        this.appstoreIp = appstoreIp;
    }

    /**
     * Retrieves application store port.
     * @return application store port
     */
    public String getAppstorePort() {
        return appstorePort;
    }

    /**
     * Sets application store port.
     * @param appstorePort application store port
     */
    public void setAppstorePort(String appstorePort) {
        this.appstorePort = appstorePort;
    }

    /**
     * Retrieves application store URI.
     * @return application store URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets application store URI.
     * @param uri application store URI
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Retrieves application store user name.
     * @return application store user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets application store user name.
     * @param userName application store user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves application store password.
     * @return application store password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets application store password.
     * @param password application store password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves application store name.
     * @return application store name
     */
    public String getAppstoreName() {
        return appstoreName;
    }

    /**
     * Sets application store name.
     * @param appstoreName application store name
     */
    public void setAppstoreName(String appstoreName) {
        this.appstoreName = appstoreName;
    }

    /**
     * Retrieves application store producer.
     * @return application store producer
     */
    public String getProducer() {
        return producer;
    }

    /**
     * Sets application store producer.
     * @param producer application store producer
     */
    public void setProducer(String producer) {
        this.producer = producer;
    }

    /**
     * Retrieves tenant ID.
     * @return tenant ID
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets tenant ID.
     * @param tenantId tenant ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Retrieves record creation time.
     * @return record creation time
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets record creation time.
     * @param createTime record creation time
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
