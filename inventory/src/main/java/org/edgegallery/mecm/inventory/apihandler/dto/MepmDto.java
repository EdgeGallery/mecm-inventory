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

package org.edgegallery.mecm.inventory.apihandler.dto;

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


@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MepmDto {

    @NotEmpty(message = "mepm name is empty")
    @Size(max = 128, message = "mepm name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "mepm name is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String mepmName;

    @NotEmpty(message = "mepm IP is empty")
    @Size(max = 15, message = "mepm IP length exceeds max size")
    @Pattern(regexp = Constants.IP_REGEX, message = "mepm IP is invalid")
    private String mepmIp;

    @NotEmpty(message = "mepm port is empty")
    @Size(max = 5, message = "mepm port length exceeds max size")
    @Pattern(regexp = Constants.PORT_REGEX, message = "mepm port is invalid")
    private String mepmPort;

    @Size(max = 128, message = "username length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "mepm username is invalid."
            + " It must start and end with alpha numeric characters and special characters"
            + " allowed are hyphen and underscore")
    private String userName;

}
