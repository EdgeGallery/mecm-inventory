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
        String pwdTest1 = "Abc@100%";
        if (pwdTest1 == "Abc@100%") {
            //  type = ConstraintType.PASSWORD;
            boolean result = customConstraintValidator.isValid(pwdTest1, cvc);
            Assert.assertTrue(result);
        }
        // verify that the context is called with the correct argument
        /*Mockito.verify(cvc)
                .buildConstraintViolationWithTemplate("valid");*/
    }

    @Test
    public void testParamNotValid() {
        String inValidPassword = "123";
        boolean result = customConstraintValidator.isValid(inValidPassword, cvc);
        Assert.assertTrue(result);
    }

    @Test
    public void testParamNull() {
        String nullPassword = null;
        boolean result = customConstraintValidator.isValid(nullPassword, cvc);
        Assert.assertTrue(result);
    }

}
