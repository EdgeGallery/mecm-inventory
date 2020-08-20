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


/**
 * MEC host registry schema.
 */

public class MecHost {

    private String mecHostId;

    private String mecHostIp;

    private String mecHostName;

    private String zipCode;

    private String city;

    private String address;

    private String affinity;

    private String userName;

    private String password;

    private String edgeName;

    private String edgeRepoIp;

    private String edgeRepoPort;

    private String edgeRepoUsername;

    private String edgeRepoPassword;

    private String tenantId;

    private String applcmIp;

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
