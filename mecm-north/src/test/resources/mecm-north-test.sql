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


create table if not exists pkg_deployment_table (
    id varchar (100) default null,
    mecm_package_id varchar (100) default null,
    mecm_pkg_name varchar (100) default null,
    app_id_from_apm varchar (100) default null,
    app_pkg_id_from_apm varchar (100) default null,
    app_instance_id varchar (100) default null,
    host_ip varchar (100) default null,
    status_code integer default 0,
    status varchar (100) default null,
    start_time varchar (100) default null,
    params varchar (100) default null,
    CONSTRAINT pkg_deployment_table_pkey PRIMARY KEY (id)
    );



INSERT INTO pkg_deployment_table(id, mecm_package_id, mecm_pkg_name, app_id_from_apm, app_pkg_id_from_apm,
                                 app_instance_id, host_ip,status_code, status, start_time, params)
VALUES ('f95d34cc-e114-4f15-8ad2-2f4819542b0a', 'f95d34cc-e114-4f15-8ad2-2f4819542b0a', 'meao'
        ,'f95d34cc-e114-4f15-8ad2-2f4819542b0a', 'f95d34cc-e114-4f15-8ad2-2f4819542b0a'
        , 'f95d34cc-e114-4f15-8ad2-2f4819542b0a','127.0.0.1', '0', 'finished', '2021-12-16', '');


create table if not exists package_table (
    mecm_package_id varchar (100) default null,
    mecm_pkg_name varchar (100) default null,
    mecm_pkg_version varchar (100) default null,
    mecm_app_class varchar (100) default null,
    tenant_id varchar (100) default null,
    host_ips varchar (100) default null,
    status varchar (100) default null,
    token text default null,
    CONSTRAINT package_table_pkey PRIMARY KEY (mecm_package_id)
    );

INSERT INTO package_table(mecm_package_id, mecm_pkg_name, mecm_pkg_version, mecm_app_class,
                          tenant_id, host_ips, status, token)
VALUES ('f95d34cc-e114-4f15-8ad2-2f4819542b0a', 'meao','v1.3', 'vm', 'f95d34cc-e114-4f15-8ad2-2f4819542b0a'
         ,'127.0.0.1', 'finished', '123');