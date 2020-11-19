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

import java.util.LinkedList;
import java.util.List;
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
 * MEC Application Inventory input request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class MecApplicationDto {

    @NotEmpty(message = "application ID is empty")
    @Size(max = 64, message = "application ID length exceeds max size")
    @Pattern(regexp = Constants.APPLICATION_ID_REGEX, message = "application ID is invalid")
    private String appInstanceId;

    @NotEmpty(message = "app name is empty")
    @Size(max = 128, message = "app name length exceeds max size")
    @Pattern(regexp = Constants.NAME_REGEX, message = "app name is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String appName;

    @NotEmpty(message = "package id is empty")
    @Size(max = 64, message = "package ID length exceeds max size")
    @Pattern(regexp = Constants.APP_PKG_ID_REGEX, message = "package ID is invalid. It must start and end with alpha "
            + "numeric characters and special characters allowed are hyphen and underscore")
    private String packageId;

    @Size(max = 10, message = "capabilities exceeds max limit 10")
    private List<@Valid String> capabilities = new LinkedList<>();

    @NotEmpty(message = "status is empty")
    @Size(max = 128, message = "status length exceeds max size")
    @Pattern(regexp = Constants.APP_STATUS_REGEX, message = "status is invalid. special characters not allowed")
    private String status;
}
