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

package org.edgegallery.mecm.inventory.service.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * MEC host repository.
 */
public interface MecHostRepository extends CrudRepository<MecHost, String>, BaseRepository<MecHost> {

    @Transactional
    @Modifying
    @Query("delete from MecHost m where m.tenantId=:tenantId")
    void deleteByTenantId(@Param("tenantId") String tenantId);

    @Query(value = "SELECT * FROM mechostinventory m WHERE m.tenant_id=:tenantId", nativeQuery = true)
    List<MecHost> findByTenantId(@Param("tenantId") String tenantId);

    @Query(value = "SELECT * FROM mechostinventory m WHERE m.role=:role", nativeQuery = true)
    List<MecHost> findByUserRole(@Param("role") String role);
}

