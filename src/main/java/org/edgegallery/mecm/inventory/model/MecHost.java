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
 * MEC host registry schema.
 */
@Entity
public class MecHost {

    @JsonIgnore
    @Id
    @Column(name = "mec_host_id")
    private String mecHostId;

    @NotBlank(message = "Provide the MEC host IP")
    @Column(name = "mec_host_ip")
    private String mecHostIp;

    @NotBlank(message = "Provide the MEC host name")
    @Column(name = "mec_host_name")
    private String mecHostName;

    @NotBlank(message = "Provide the ZIP code")
    @Column(name = "zip_code")
    private String zipCode;

    @NotBlank(message = "Provide the city name")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Provide the MEC address")
    @Column(name = "address")
    private String address;

    @Column(name = "affinity")
    private String affinity;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "edge_name")
    private String edgeName;

    @Column(name = "edge_repo_ip")
    private String edgeRepoIp;

    @Column(name = "edge_repo_port")
    private String edgeRepoPort;

    @Column(name = "edge_repo_username")
    private String edgeRepoUsername;

    @Column(name = "edge_repo_password")
    private String edgeRepoPassword;

    @Column(name = "tenent_id")
    private String tenantId;

    @Column(name = "applcm_ip")
    private String applcmIp;

    @Column(name = "createTime")
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * Retrieves MEC host ID.
     * @return MEC host ID
     */
    public String getMecHostId() {
        return mecHostId;
    }

    /**
     * Sets MEC host ID.
     * @param mecHostId host ID
     */
    public void setMecHostId(String mecHostId) {
        this.mecHostId = mecHostId;
    }

    /**
     * Retrieves MEC host ID.
     * @return MEC host ID
     */
    public String getMecHostIp() {
        return mecHostIp;
    }

    /**
     * Sets MEC host IP.
     * @param mecHostIp edge host IP
     */
    public void setMecHostIp(String mecHostIp) {
        this.mecHostIp = mecHostIp;
    }

    /**
     * Retrieves MEC host name.
     * @return MEC host name
     */
    public String getMecHostName() {
        return mecHostName;
    }

    /**
     * Sets MEC host name.
     * @param mecHostName host name
     */
    public void setMecHostName(String mecHostName) {
        this.mecHostName = mecHostName;
    }

    /**
     * Retrieves postal ZIP code.
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets ZIP code.
     * @param zipCode zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Retrieves city.
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets MEC host city.
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Retrieves MEC host address.
     *
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets MEC host address.
     * @param address address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves MEC host ID.
     * @return MEC host ID
     */
    public String getAffinity() {
        return affinity;
    }

    public void setAffinity(String affinity) {
        this.affinity = affinity;
    }

    /**
     * Retrieves MEC host ID.
     * @return MEC host ID
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     * @param userName user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves MEC host ID.
     * @return MEC host ID
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrives password.
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves egde name.
     * @return edge name
     */
    public String getEdgeName() {
        return edgeName;
    }

    /**
     * Sets edge name.
     * @param edgeName edge name
     */
    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
    }

    /**
     * Retrieves edge repo IP.
     * @return edge repo IP
     */
    public String getEdgeRepoIp() {
        return edgeRepoIp;
    }

    /**
     * Sets edge repo IP.
     *
     * @param edgeRepoIp edge repo IP
     */
    public void setEdgeRepoIp(String edgeRepoIp) {
        this.edgeRepoIp = edgeRepoIp;
    }

    /**
     * Retrieves edge repo port.
     * @return port.
     */
    public String getEdgeRepoPort() {
        return edgeRepoPort;
    }

    /**
     * Sets edge repo port.
     * @param edgeRepoPort edge repo part
     */
    public void setEdgeRepoPort(String edgeRepoPort) {
        this.edgeRepoPort = edgeRepoPort;
    }

    /**
     * Retrieve edge repo user name.
     * @return edge repo user name
     */
    public String getEdgeRepoUsername() {
        return edgeRepoUsername;
    }

    /**
     * Sets edge repo user name.
     * @param edgeRepoUsername repo user name
     */
    public void setEdgeRepoUsername(String edgeRepoUsername) {
        this.edgeRepoUsername = edgeRepoUsername;
    }

    /**
     * Retrieves edge repository pasword.
     * @return edge repo password
     */
    public String getEdgeRepoPassword() {
        return edgeRepoPassword;
    }

    /**
     * Sets edge repository password.
     * @param edgeRepoPassword password
     */
    public void setEdgeRepoPassword(String edgeRepoPassword) {
        this.edgeRepoPassword = edgeRepoPassword;
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
     * Retrieves applcm IP.
     * @return applcm IP
     */
    public String getApplcmIp() {
        return applcmIp;
    }

    /**
     * Sets applcm IP.
     * @param applcmIp applcm IP
     */
    public void setApplcmIp(String applcmIp) {
        this.applcmIp = applcmIp;
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
