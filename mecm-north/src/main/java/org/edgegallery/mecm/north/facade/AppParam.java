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

package org.edgegallery.mecm.north.facade;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppParam {

    private String applicationType;

    private String showType;

    private String affinity;

    private String industry;

    private boolean experienceAble;

    private static Set<String> typeSet = new HashSet<>();

    private static Set<String> industrySet = new HashSet<>();

    private static Set<String> architectureSet = new HashSet<>();

    private static Set<String> showTypeSet = new HashSet<>();

    static {
        typeSet.add("Video Application");
        typeSet.add("Safety");
        typeSet.add("Blockchain");
        typeSet.add("Internet of Things");
        typeSet.add("Big Data");
        typeSet.add("AR/VR");
        typeSet.add("Game");
        typeSet.add("Others");
        // old types
        typeSet.add("Video Surveillance");
        typeSet.add("Smart Device");
        typeSet.add("API");
        typeSet.add("SDK");
        typeSet.add("MEP");
        typeSet.add("Other");
    }

    static {
        industrySet.add("Smart City");
        industrySet.add("Smart Park");
        industrySet.add("Smart Agriculture");
        industrySet.add("Industrial Internet");
        industrySet.add("Transportation Logistics");
        industrySet.add("Energy");
        industrySet.add("Finance");
        industrySet.add("Education");
        industrySet.add("Cultural Tourism");
        industrySet.add("Medical Health");
        industrySet.add("Digital Government");
        industrySet.add("Ecosystem");
        industrySet.add("Game Competition");
        industrySet.add("Others");
        // old industries
        industrySet.add("Smart Supermarket");
        industrySet.add("Industrial Manufacturing");
        industrySet.add("Water Conservancy");
        industrySet.add("Open Source");
        industrySet.add("Other");
    }

    static {
        architectureSet.add("X86");
        architectureSet.add("ARM64");
        architectureSet.add("ARM32");
    }

    static {
        showTypeSet.add("public");
        showTypeSet.add("inner-public");
        showTypeSet.add("private");
    }

    /**
     * check app param valid.
     */
    public boolean checkValidParam(AppParam appparam) {
        if (StringUtils.isEmpty(appparam.getShowType())) {
            appparam.setShowType("public");
        }
        return typeSet.contains(appparam.getApplicationType()) && industrySet.contains(appparam.getIndustry())
            && architectureSet.contains(appparam.getAffinity()) && showTypeSet.contains(appparam.getShowType());
    }

}

