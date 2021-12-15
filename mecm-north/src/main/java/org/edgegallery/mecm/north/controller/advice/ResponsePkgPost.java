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

package org.edgegallery.mecm.north.controller.advice;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.edgegallery.mecm.north.utils.exception.ErrorMessage;

@Getter
@Setter
public class ResponsePkgPost {
    private String mecmPackageId;

    private int retCode;

    private String message;

    private List<String> params;

    /**
     * construct.
     */
    public ResponsePkgPost(String mecmPackageId, int retCode, ErrorMessage errorMsg, String detailMsg) {
        this.mecmPackageId = mecmPackageId;
        this.retCode = retCode;
        this.retCode = errorMsg.getRetCode();
        this.params = errorMsg.getParams();
        this.message = detailMsg;
    }
}
