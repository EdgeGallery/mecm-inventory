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

package org.edgegallery.mecm.inventory.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Inventory exception handler.
 */
@ControllerAdvice
public class InventoryExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryExceptionHandler.class);

    /**
     * Returns error code and message for Inventory exception.
     *
     * @param ex exception while processing request
     * @return response entity with error code and message
     */
    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<String> handleInventoryException(InventoryException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns response entity with error details when input validation is failed.
     *
     * @param ex exception while validating input
     * @return response entity with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InventoryExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMsg = new ArrayList<>();
        if (ex.getBindingResult().hasErrors()) {
            ex.getBindingResult().getAllErrors().forEach(error -> errorMsg.add(error.getDefaultMessage()));
        }
        InventoryExceptionResponse response = new InventoryExceptionResponse(LocalDateTime.now(),
                "input validation failed", errorMsg);
        LOGGER.info("Method argument error: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns error code and message when argument is illegal.
     *
     * @param ex exception while processing request
     * @return response entity with error code and message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<InventoryExceptionResponse> handleIllegalArgException(IllegalArgumentException ex) {

        InventoryExceptionResponse response = new InventoryExceptionResponse(LocalDateTime.now(),
                "Illegal argument", Collections.singletonList(ex.getMessage()));
        LOGGER.info("Illegal argument error: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns error code and message when record not found.
     *
     * @param ex exception while processing request
     * @return response entity with error code and message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<InventoryExceptionResponse> handleNoSuchElementException(NoSuchElementException ex) {
        InventoryExceptionResponse response = new InventoryExceptionResponse(LocalDateTime.now(),
                "No such element", Collections.singletonList(ex.getMessage()));
        LOGGER.info("No such element error: {}", response);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Returns error when runtime error happen.
     *
     * @param ex exception while processing request
     * @return response entity with error code and message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InventoryExceptionResponse> handleRuntimeException(RuntimeException ex) {
        InventoryExceptionResponse response = new InventoryExceptionResponse(LocalDateTime.now(),
                "Error while processing request", Collections.singletonList("Internal server error while processing "
                + "request"));
        LOGGER.info("Internal server error: {}", response);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns response entity with error details when input validation is failed.
     *
     * @param ex exception while validating input
     * @return response entity with error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<InventoryExceptionResponse> handleConstraintViolationException(
            ConstraintViolationException ex) {
        InventoryExceptionResponse response = new InventoryExceptionResponse(LocalDateTime.now(),
                "input validation failed", Collections.singletonList("URL parameter validation failed"));
        LOGGER.info("Constraint violation error: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
