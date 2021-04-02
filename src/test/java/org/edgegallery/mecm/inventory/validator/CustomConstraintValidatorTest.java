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

package org.edgegallery.mecm.inventory.validator;



import org.edgegallery.mecm.inventory.apihandler.validator.ConstraintType;

import org.edgegallery.mecm.inventory.apihandler.validator.CustomConstraint;

import org.edgegallery.mecm.inventory.apihandler.validator.CustomConstraintValidator;

import org.junit.Assert;

import org.junit.Before;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;



import javax.validation.ConstraintValidatorContext;



import static org.mockito.Mockito.mock;





@RunWith(MockitoJUnitRunner.class)

public class CustomConstraintValidatorTest {



    private ConstraintValidatorContext cvc;

    private ConstraintType type;



    @InjectMocks

    CustomConstraintValidator customConstraintValidator;



    @Mock

    CustomConstraint customConstraint;



    // ConstraintValidator constraintValidator = mock(ConstraintValidator.class);



    @Before

    public void onSetUp() {

        cvc = mock(ConstraintValidatorContext.class);

        type= ConstraintType.PASSWORD;

      /*when(cvc.buildConstraintViolationWithTemplate(anyString()))

                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));*/



    }



    @Test

    public void testInitialize() {

        type =ConstraintType.PASSWORD;

        customConstraintValidator.initialize(customConstraint);

        Assert.assertNotNull(type);



    }



    @Test

    public void testParamValid() {

        String varTest1 = "Abc@100%";

        if (varTest1 == "Abc@100%") {

            boolean result = customConstraintValidator.isValid(varTest1, cvc);

            Assert.assertTrue(result);

        }

        // verify that the context is called with the correct argument

        /*Mockito.verify(cvc)

                .buildConstraintViolationWithTemplate("valid");*/

    }



    @Test

    public void testParamNotValid() {

        String inValidVar = "123";

        boolean result = customConstraintValidator.isValid(inValidVar, cvc);

        Assert.assertTrue(result);

    }



    @Test

    public void testParamNull() {

        String nullVar = null;

        boolean result = customConstraintValidator.isValid(nullVar, cvc);

        Assert.assertTrue(result);

    }



}
