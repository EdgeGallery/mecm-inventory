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

package org.edgegallery.mecm.north.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.edgegallery.mecm.north.model.MecmPackageInfo;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MecmPackageMapper {

    void insertMecmPkgInfo(MecmPackageInfo mecMPackageInfo);

    void deletePkgInfoByPkgId(String mecmPkgId);

    void updateMecmPkgInfo(MecmPackageInfo mecMPackageInfo);

    MecmPackageInfo getMecmPkgInfoByPkgId(String mecmPkgId);

}
