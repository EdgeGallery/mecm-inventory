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

import static org.edgegallery.mecm.inventory.utils.Constants.IP_REGEX;
import static org.edgegallery.mecm.inventory.utils.Constants.NAME_REGEX;
import static org.edgegallery.mecm.inventory.utils.Constants.PORT_REGEX;

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
 * Application lifecycle management input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class AppLcmDto {

    @NotEmpty(message = "applcm name is empty")
    @Size(max = 128, message = "applcm name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "applcm name is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String applcmName;

    @NotEmpty(message = "applcm IP is empty")
    @Size(max = 15, message = "applcm IP length exceeds max size")
    @Pattern(regexp = IP_REGEX, message = "applcm IP is invalid")
    private String applcmIp;

    @NotEmpty(message = "applcm port is empty")
    @Size(max = 5, message = "applcm port length exceeds max size")
    @Pattern(regexp = PORT_REGEX, message = "applcm port is invalid")
    private String applcmPort;

    @Size(max = 128, message = "username length exceeds max size")
    @Pattern(regexp = NAME_REGEX, message = "applcm username is invalid. It must start and end with alpha numeric"
            + " characters and special characters allowed are hyphen and underscore")
    private String userName;
}