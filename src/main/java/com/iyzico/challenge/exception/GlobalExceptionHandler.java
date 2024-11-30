package com.iyzico.challenge.exception;

import com.iyzico.challenge.exception.model.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception exception) {
        logger.error(String.format("Exception: %s", exception.getMessage()), exception);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDTO errorDTO = new ErrorDTO(status.value(), exception.getMessage());
        return new ResponseEntity<>(errorDTO, status);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDTO> handleBaseException(BaseException exception) {
        logger.warn(String.format("BaseException: %s", exception.getMessage()), exception);

        String resolvedMessage = messageSource.getMessage(exception.getMessage(), null, Locale.getDefault());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO errorDTO = new ErrorDTO(status.value(), resolvedMessage);
        return new ResponseEntity<>(errorDTO, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError.getDefaultMessage(), objectError.getArguments(), LocaleContextHolder.getLocale()))
                .collect(Collectors.joining(", "));
        logger.warn(message, ex);
        return new ResponseEntity<>(new ErrorDTO(status.value(), message), status);
    }
}
