package org.edgegallery.mecm.inventory.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;


import org.edgegallery.mecm.inventory.common.InventoryConstantsTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RunWith(MockitoJUnitRunner.class)
public class InventoryExceptionHandlerTest {

    @InjectMocks
    InventoryExceptionHandler inventoryExceptionHandler = new InventoryExceptionHandler();

    @Mock
    InventoryException inventoryException;

    @Mock
    MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    MethodParameter parameter;

    @Mock
    BindingResult bindingResult;

    @Mock
    ObjectError var1;

    @Mock
    IllegalArgumentException illegalArgumentException;

    @Mock
    NoSuchElementException noSuchElementException;

    @Mock
    ConstraintViolationException constraintViolationException;

    @Mock
    RuntimeException runtimeException;

    @Mock
    org.springframework.security.access.AccessDeniedException accessDeniedException;

    @Test
    public void testInventoryException() {
        inventoryException = new InventoryException(InventoryConstantsTest.MESSAGE);
        ResponseEntity<String> inventoryString = inventoryExceptionHandler.handleInventoryException(inventoryException);
        assertNotNull(inventoryString);
    }

    @Test
    public void testMethodArgumentException() {
        bindingResult.addError(var1);
        methodArgumentNotValidException = new MethodArgumentNotValidException(parameter, bindingResult);
        ResponseEntity<InventoryExceptionResponse> inventoryResponse =
                inventoryExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException);
        assertNotNull(inventoryResponse);
    }

    @Test
    public void testIllegalArgumentException() {
        illegalArgumentException = new IllegalArgumentException();
        ResponseEntity<InventoryExceptionResponse> inventoryExceptionHandlerResponse = inventoryExceptionHandler
                .handleIllegalArgException(illegalArgumentException);
        assertNotNull(inventoryExceptionHandlerResponse);
    }

    @Test
    public void testNoSuchElementException() {
        noSuchElementException = new NoSuchElementException();
        ResponseEntity<InventoryExceptionResponse> inventoryElementResponse =
                inventoryExceptionHandler.handleNoSuchElementException(noSuchElementException);
        assertNotNull(inventoryElementResponse);
    }

    @Test
    public void testHandleConstraintViolationException() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation mockedViolation = mock(ConstraintViolation.class);
        violations.add(mockedViolation);
        constraintViolationException = new ConstraintViolationException("message", violations);
        ResponseEntity<InventoryExceptionResponse> inventoryConstraintViolationResponse =
                inventoryExceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertNotNull(inventoryConstraintViolationResponse);
    }

    @Test
    public void testHandleRuntimeException() {
        runtimeException = new RuntimeException();
        ResponseEntity<InventoryExceptionResponse> inventoryRuntimeResponse =
                inventoryExceptionHandler.handleRuntimeException(runtimeException);
        assertNotNull(inventoryRuntimeResponse);
    }

    @Test
    public void testHandleAccessDeniedException() {
        accessDeniedException = new AccessDeniedException(InventoryConstantsTest.MESSAGE);
        ResponseEntity<InventoryExceptionResponse> appoAccessDeniedResponse =
                inventoryExceptionHandler.handleAccessDeniedException(accessDeniedException);
        assertNotNull(appoAccessDeniedResponse);
    }
}
