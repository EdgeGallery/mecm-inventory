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

package org.edgegallery.mecm.inventory.apihandler.dto;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.springframework.validation.annotation.Validated;

/**
 * MEC host Inventory input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class MecHostDto {

    @NotEmpty(message = "mechost IP is empty")
    @Size(max = 15, message = "mechost IP length exceeds max size")
    @Pattern(regexp = Constants.IP_REGEX, message = "mechost IP is invalid")
    private String mechostIp;

    @NotEmpty(message = "mechost name is empty")
    @Size(max = 128, message = "mechost name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "mechost name is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String mechostName;

    @Size(max = 128, message = "mechost zipcode length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "mechost zipcode is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String zipCode;

    @NotEmpty(message = "mechost city is empty")
    @Size(max = 128, message = "mechost city length exceeds max size")
    @Pattern(regexp = Constants.CITY_REGEX, message = "mechost city is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are forward slash and space")
    private String city;

    @NotEmpty(message = "address is empty")
    @Size(max = 256, message = "mechost address length exceeds max size")
    private String address;

    @Size(max = 128, message = "mechost affinity length exceeds max size")
    @Pattern(regexp = Constants.AFFINITY_REGEX, message = "mechost affinity is invalid. It must start and end "
            + "with alpha numeric characters and special characters allowed are hyphen, underscore and comma")
    private String affinity;

    @Size(max = 128, message = "mechost username length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "mechost username is invalid. It must start and end with "
            + "alpha numeric characters and special characters allowed are hyphen and underscore")
    private String userName;

    @NotEmpty(message = "applcm IP is empty")
    @Size(max = 15, message = "applcm IP length exceeds max size")
    @Pattern(regexp = Constants.IP_REGEX, message = "applcm IP is invalid")
    private String applcmIp;

    @Size(max = 15, message = "app rule manager IP length exceeds max size")
    @Pattern(regexp = Constants.IP_REGEX, message = "app rule manage IP is invalid")
    private String appRuleIp;

    @NotEmpty(message = "co-ordinates is empty")
    @Size(max = 128, message = "co-ordinates length exceeds max size")
    private String coordinates;

    @Size(max = 10)
    private Set<@Valid MecHwCapabilityDto> hwcapabilities = new LinkedHashSet<>();
}
