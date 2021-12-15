/*
 * Copyright 2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.edgegallery.mecmNorth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@Builder
public class MecMPackageDeploymentInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mecm_package_id")
    private String mecmPackageId;

    @Column(name = "mecm_pkg_name")
    private String mecmPkgName;

    @Column(name = "app_id_from_apm")
    private String appIdFromApm;

    @Column(name = "app_pkg_id_from_apm")
    private String appPkgIdFromApm;

    @Column(name = "app_instance_id")
    private String appInstanceId;

    @Column(name = "host_ip")
    private String hostIp;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "status")
    private String status;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "params")
    private String params;
}
