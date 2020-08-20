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
 * Application lifecycle management registry schema.
 */
@Entity
public class AppLcm {

    @JsonIgnore
    @Id
    @Column(name = "applcm_id")
    private String applcmId;

    @NotBlank(message = "Provide the application lcm IP")
    @Column(name = "applcm_ip")
    private String applcmIp;

    @NotBlank(message = "Provide the application lcm port")
    @Column(name = "applcm_port")
    private String applcmPort;

    @Column(name = "applcm_username")
    private String userName;

    @Column(name = "applcm_password")
    private String password;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "createTime")
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * Retrieves applcm ID.
     *
     * @return applcm ID
     */
    public String getApplcmId() {
        return applcmId;
    }

    /**
     * Sets applcm ID.
     *
     * @param applcmId applcm ID
     */
    public void setApplcmId(String applcmId) {
        this.applcmId = applcmId;
    }

    /**
     * Retrieves applcm IP.
     *
     * @return applcm IP
     */
    public String getApplcmIp() {
        return applcmIp;
    }

    /**
     * Sets applcm IP.
     *
     * @param applcmIp applcm IP
     */
    public void setApplcmIp(String applcmIp) {
        this.applcmIp = applcmIp;
    }

    /**
     * Retrieves applcm port.
     *
     * @return applcm port
     */
    public String getApplcmPort() {
        return applcmPort;
    }

    /**
     * Set applcm port.
     *
     * @param applcmPort applcm port
     */
    public void setApplcmPort(String applcmPort) {
        this.applcmPort = applcmPort;
    }

    /**
     * Retrieves applcm user name.
     *
     * @return user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets applcm user name.
     *
     * @param userName user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves possword.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets applcm password.
     *
     * @param password applcm passowrd
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves tenant ID.
     *
     * @return tenant ID
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets tenant ID.
     * @param tenantId tenand ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Retrieves create time.
     * @return create time.
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     * @param createTime create time
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}