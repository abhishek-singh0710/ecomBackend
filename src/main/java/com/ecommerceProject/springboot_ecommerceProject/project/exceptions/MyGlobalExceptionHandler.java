package com.ecommerceProject.springboot_ecommerceProject.project.exceptions;

import com.ecommerceProject.springboot_ecommerceProject.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Custom Error Handling

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // To Create A Response Which Is Presented In A More User Friendly Way
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        // Using The Exception Message
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }


}
