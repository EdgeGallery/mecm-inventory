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

import static org.edgegallery.mecm.inventory.common.Constants.IP_REGEX;
import static org.edgegallery.mecm.inventory.common.Constants.NAME_REGEX;
import static org.edgegallery.mecm.inventory.common.Constants.PORT_REGEX;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.edgegallery.mecm.inventory.common.ConstraintType;
import org.edgegallery.mecm.inventory.common.CustomConstraint;
import org.springframework.validation.annotation.Validated;

/**
 * MEC host registry input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
public class MecHostDtp {

    @NotEmpty(message = "mechost IP is empty")
    @Size(max = 15)
    @Pattern(regexp = IP_REGEX)
    private String mecHostIp;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String mecHostName;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String zipCode;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String city;

    @Size(max = 256)
    private String address;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String affinity;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String userName;

    @CustomConstraint(ConstraintType.PASSWORD)
    private String password;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String edgeName;

    @Size(max = 15)
    @Pattern(regexp = IP_REGEX)
    private String edgeRepoIp;

    @Size(max = 5)
    @Pattern(regexp = PORT_REGEX)
    private String edgeRepoPort;

    @Size(max = 128)
    @Pattern(regexp = NAME_REGEX)
    private String edgeRepoUsername;

    @CustomConstraint(ConstraintType.PASSWORD)
    private String edgeRepoPassword;

    @NotEmpty(message = "applcm IP is empty")
    @Size(max = 15)
    @Pattern(regexp = IP_REGEX)
    private String applcmIp;
}
