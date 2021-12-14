/*
 *    Copyright 2021 Huawei Technologies Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
public class MecMPackageInfo {

    @Id
    @Column(name = "mecm_package_id")
    private String mecmPackageId;

    @Column(name = "mecm_pkg_name")
    private String mecmPkgName;

    @Column(name = "mecm_pkg_version")
    private String mecmPkgVersion;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "host_ips")
    private String hostIps;

    @Column(name = "status")
    private String status;

}
