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

import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * Appd rule deleted record synchronization request schema.
 */
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class SyncDeletedRulesDto implements SyncBaseDto {

    @Size(max = 20, message = "App rule deleted records exceeds max limit 20")
    private Set<@Valid AppdRuleDeletedDto> appdRuleDeletedRecs = new HashSet<>();
}
