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
    @Pattern(regexp = Constants.IP_REGEX, message = "appstore IP is invalid")
    private String appstoreIp;

    @NotEmpty(message = "appstore port is empty")
    @Size(max = 5, message = "appstore port length exceeds max size")
    @Pattern(regexp = Constants.PORT_REGEX, message = "appstore port is invalid")
    private String appstorePort;

    @Size(max = 128, message = "appstore name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "appstore name is invalid. It must start and end with alpha "
            + "numeric character and special characters allowed are hyphen and underscore")
    private String appstoreName;

    @Size(max = 128, message = "appstore repo name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "appstore repo name is invalid. It must start and end with alpha "
            + "numeric character and special characters allowed are hyphen and underscore")
    private String appstoreRepoName;

    @NotEmpty(message = "appstore repo URI is empty")
    @Size(max = 128, message = "appstore repo URI length exceeds max size")
    @Pattern(regexp = Constants.URI_REGEX, message = "appstore repo URI is invalid")
    private String appstoreRepo;

    @Size(max = 128, message = "appstore username length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "appstore username is invalid. It must start and end with "
            + "alpha numeric character and special characters allowed are hyphen and underscore")
    private String appstoreRepoUserName;

    @Size(max = 128, message = "appstore password length exceeds max size")
    @Pattern(regexp = Constants.AUTH_PASS_REGEX, message = "appstore password is invalid.")
    private String appstoreRepoPassword;

    @Size(max = 128, message = "appstore producer length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "appstore producer is invalid. It must start and end with "
            + "alpha numeric character and special characters allowed are hyphen and underscore")
    private String producer;
}
