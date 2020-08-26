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

package org.edgegallery.mecm.inventory.apihandler.validator;

import static org.edgegallery.mecm.inventory.utils.Constants.LOWER_CASE_REGEX;
import static org.edgegallery.mecm.inventory.utils.Constants.MAX_PWD_COUNT;
import static org.edgegallery.mecm.inventory.utils.Constants.MAX_PWD_SIZE;
import static org.edgegallery.mecm.inventory.utils.Constants.MIN_PWD_SIZE;
import static org.edgegallery.mecm.inventory.utils.Constants.SINGLE_DIGIT_REGEX;
import static org.edgegallery.mecm.inventory.utils.Constants.SPECIAL_CHAR_REGEX;
import static org.edgegallery.mecm.inventory.utils.Constants.UPPER_CASE_REGEX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implementation of custom constraint validator.
 */
public final class CustomConstraintValidator implements ConstraintValidator<CustomConstraint, String> {

    private ConstraintType type;

    @Override
    public void initialize(CustomConstraint constraintAnnotation) {
        this.type = constraintAnnotation.value();
    }

    /**
     * Checks if input param is valid as per the constraint.
     *
     * @param param   input parameter
     * @param context context have constraint information
     * @return true if valid, false otherwise
     */
    public boolean isValid(String param, ConstraintValidatorContext context) {
        if (param == null) {
            return true;
        }
        if (type == ConstraintType.PASSWORD) {
            return isPasswordValid(param);
        }
        return true;
    }

    private boolean isPasswordValid(String pwd) {
        if ((pwd.length() >= MIN_PWD_SIZE) && (pwd.length() <= MAX_PWD_SIZE)) {
            // password must satisfy any two conditions
            int pwdValidCount = 0;
            if (isRegexMatched(SINGLE_DIGIT_REGEX, pwd)) {
                pwdValidCount++;
            }
            if (isRegexMatched(LOWER_CASE_REGEX, pwd)) {
                pwdValidCount++;
            }
            if (isRegexMatched(UPPER_CASE_REGEX, pwd)) {
                pwdValidCount++;
            }
            if (isRegexMatched(SPECIAL_CHAR_REGEX, pwd)) {
                pwdValidCount++;
            }
            return pwdValidCount >= MAX_PWD_COUNT;
        } else {
            return false;
        }
    }

    private boolean isRegexMatched(String param, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(param);
        return m.matches();
    }

}
