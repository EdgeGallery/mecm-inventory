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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * MEC host registry input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
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
}
