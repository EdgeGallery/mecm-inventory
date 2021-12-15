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

package org.edgegallery.mecm.north.domain;

public class ResponseConst {

    /**
     * the success code.
     */
    public static final int RET_SUCCESS = 0;

    /**
     * the fail code.
     */
    public static final int RET_FAIL = 1;

    /**
     * app param is invalid.
     */
    public static final int RET_PARAM_INVALID = 10001;

    /**
     * get mec host info failed.
     */
    public static final int RET_GET_MECMHOST_FAILED = 16007;

    /**
     * Permission not allowed to delete application package.
     */
    public static final int RET_NO_ACCESS_DELETE_PACKAGE = 18002;

    /**
     * the content of manifest file is incorrect.
     */
    public static final int RET_MF_CONTENT_INVALID = 10029;

    /**
     * An exception occurred while getting the file from application package.
     */
    public static final int RET_PARSE_FILE_EXCEPTION = 10016;

    /**
     * sign package failed.
     */
    public static final int RET_SIGN_PACKAGE_FAILED = 10030;
}
