/* Copyright 2020-2021 Huawei Technologies Co., Ltd.
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
 */

package org.edgegallery.mecmNorth.model.app;


/**
 * package status.
 *
 */
public enum EnumPackageStatus {
    // package is uploaded
    Upload("upload"),

    // test task is created successfully
    Test_created("created"),

    // test task is created failed
    Test_create_failed("create failed"),

    // test task is running
    Test_running("running"),

    // test task is waiting
    Test_waiting("waiting"),

    // test failed
    Test_failed("failed"),

    // test success
    Test_success("success"),

    // package is published
    Published("publish");

    // enum content
    private String text;

    EnumPackageStatus(String text) {
        this.text = text;
    }

    EnumPackageStatus() {
    }

    /**
     * transform string to enum.
     *
     * @param text input string
     * @return status
     */
    public static EnumPackageStatus fromString(String text) {
        for (EnumPackageStatus b : EnumPackageStatus.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    /**
     * check the status if can be create the test task.
     *
     * @param status status
     * @return true or false
     */
    public static boolean testAllowed(EnumPackageStatus status) {
        return status == Upload || status == Test_failed || status == Test_create_failed || status == Test_success;
    }

    public static boolean needRefresh(EnumPackageStatus status) {
        return status != Published && status != Test_success && status != Upload;
    }

    public String getText() {
        return text;
    }
}