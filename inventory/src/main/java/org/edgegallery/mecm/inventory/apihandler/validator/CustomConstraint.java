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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom constraint interface as per the validator framework.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CustomConstraintValidator.class)
@Documented
public @interface CustomConstraint {

    /**
     * Gets the default message.
     *
     * @return the default key for creating error messages in case the constraint is violated
     */
    String message() default "Input validation failed";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * @return class
     */
    Class<?>[] groups() default {};

    /**
     * Assigns custom payload objects to a constraint.
     *
     * @return class
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Returns value of constraint type.
     *
     * @return constraint type
     */
    ConstraintType value();
}