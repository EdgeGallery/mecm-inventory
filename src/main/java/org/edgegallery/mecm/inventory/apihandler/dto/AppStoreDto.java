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
import static org.edgegallery.mecm.inventory.utils.Constants.URI_REGEX;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * App store Inventory input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class AppStoreDto {

    @NotEmpty(message = "appstore IP is empty")
    @Size(max = 15, message = "appstore IP length exceeds max size")
    @Pattern(regexp = IP_REGEX, message = "appstore IP allowed pattern mismatch")
    private String appstoreIp;

    @NotEmpty(message = "appstore port is empty")
    @Size(max = 5, message = "appstore port length exceeds max size")
    @Pattern(regexp = PORT_REGEX, message = "appstore port allowed pattern mismatch")
    private String appstorePort;

    @NotEmpty(message = "appstore URI is empty")
    @Size(max = 128, message = "appstore URI length exceeds max size")
    @Pattern(regexp = URI_REGEX, message = "appstore URI allowed pattern mismatch")
    private String uri;

    @Size(max = 128, message = "appstore username length exceeds max size")
    @Pattern(regexp = NAME_REGEX, message = "appstore username allowed pattern mismatch")
    private String userName;

    // @CustomConstraint(ConstraintType.PASSWORD)
    // private String password;

    @NotEmpty(message = "appstore name is empty")
    @Size(max = 128, message = "appstore name length exceeds max size")
    @Pattern(regexp = NAME_REGEX, message = "appstore name allowed pattern mismatch")
    private String appstoreName;

    @NotEmpty(message = "producer is empty")
    @Size(max = 128, message = "appstore producer length exceeds max size")
    @Pattern(regexp = NAME_REGEX, message = "appstore producer allowed pattern mismatch")
    private String producer;
}
