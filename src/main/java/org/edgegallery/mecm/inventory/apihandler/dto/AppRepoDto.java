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

/**
 * App repo Inventory input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class AppRepoDto {

    @NotEmpty(message = "app repo endpoint is empty")
    @Size(max = 255, message = "app repo endpoint length exceeds max size")
    private String repoEndPoint;

    @NotEmpty(message = "app repo name is empty")
    @Size(max = 255, message = "app repo name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "app repo name is invalid")
    private String repoName;

    @NotEmpty(message = "app repo username is empty")
    @Size(max = 128, message = "app repo username length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "app repo username is invalid.")
    private String repoUserName;

    @NotEmpty(message = "app repo password is empty")
    @Size(max = 128, message = "app repo password length exceeds max size")
    private String repoPassword;
}
