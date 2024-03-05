package com.rentacar.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.rentacar.exception.ErrorType.BAD_REQUEST_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles unchecked exceptions.
     *
     * It is intended to handle unchecked exceptions, specifically runtime exceptions,
     * providing a mechanism for capturing and managing unexpected errors that occur during program execution.
     * @param runtimeException The runtime exception to be handled.
     * @return An indication of whether the exception was successfully handled.
     *         This might be a boolean value or some other indicator of success.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> globalHandler(RuntimeException runtimeException) {

        return new ResponseEntity<>(createErrorMessage(runtimeException,ErrorType.INTERNAL_ERROR)
        , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Exception handler for AuthServiceException.
     *
     * This method handles exceptions specific to the Auth Service by returning an appropriate
     * ResponseEntity<ErrorMessage> containing details about the error. It is responsible for
     * converting AuthServiceExceptions into HTTP responses with error messages.
     * @param authServiceException The exception specific to the Auth Service.
     * @return A ResponseEntity containing an ErrorMessage, which encapsulates details about the error.
     */
    @ExceptionHandler(AuthServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> authServiceHandler(AuthServiceException authServiceException){
        return new ResponseEntity<>(createErrorMessage(authServiceException,authServiceException.getErrorType()),
                authServiceException.getErrorType().getHttpStatus());
    }

    /**
     * Exception handler for ConstraintViolationException.
     *
     * This method handles ConstraintViolationException, which occurs when constraints defined by
     * Jakarta Bean Validation (JSR 380) are violated. It is specifically designed to handle validation
     * errors related to constraints such as size limits, patterns, or other validation rules.
     *
     * @param duplicateKeyException The ConstraintViolationException indicating violated constraints.
     * @return A ResponseEntity containing an ErrorMessage describing the validation error.
     * @see jakarta.validation.constraints Constraints defined by Jakarta Bean Validation.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> duplicateKeyHandler(ConstraintViolationException duplicateKeyException){
        return new ResponseEntity<>(createErrorMessage(duplicateKeyException,ErrorType.INTERNAL_ERROR)
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Exception handler for MethodArgumentNotValidException.
     *
     * This method handles MethodArgumentNotValidException, which occurs when the validation of method arguments
     * annotated with @Valid fails. It is responsible for converting validation errors into appropriate error
     * responses with detailed error messages.
     *
     * @param exception The MethodArgumentNotValidException indicating invalid method arguments.
     * @return A ResponseEntity containing an ErrorMessage describing the validation error, including details
     *         about the invalid fields and their error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        ErrorType errorType = BAD_REQUEST_ERROR;
        List<String> fields = new ArrayList<>();
        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(e -> fields.add(e.getField() + ": " + e.getDefaultMessage()));
        ErrorMessage errorMessage = createErrorMessage(exception,errorType);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage, errorType.getHttpStatus());
    }

    /**
     * Creates an error message for exception handling.
     *
     * This method is used to generate an error message object based on the provided exception and error type.
     * It constructs an ErrorMessage instance containing the error message and error code associated with
     * the specified error type.
     *
     * @param exception The exception for which an error message is being created.
     * @param errorType The type of error indicating the category of the error.
     * @return An ErrorMessage object representing the error message.
     */
    private ErrorMessage createErrorMessage(Exception exception,ErrorType errorType){
        System.out.println("Error Type...: "+ exception.getMessage());
        return ErrorMessage.builder()
                .message(errorType.getMessage())
                .code(errorType.getCode())
                .build();
    }
}
