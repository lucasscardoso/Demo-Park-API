package com.curso.demo_park_api.web.exception;

import com.curso.demo_park_api.AppExceptions.PasswordInvalidException;
import com.curso.demo_park_api.AppExceptions.UserUniqueViolationException;
import com.curso.demo_park_api.AppExceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(UserUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> dataIntegrityViolationException(RuntimeException ex, HttpServletRequest request){

        logger.error("API ERROR - "+ ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).
                contentType(MediaType.APPLICATION_JSON).
                body(new ErrorMessage(request,HttpStatus.CONFLICT,ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex, HttpServletRequest request){

        logger.error("API ERROR - "+ ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).
                body(new ErrorMessage(request,HttpStatus.BAD_REQUEST,ex.getMessage()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex, HttpServletRequest request){

        logger.error("API ERROR - "+ ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                contentType(MediaType.APPLICATION_JSON).
                body(new ErrorMessage(request,HttpStatus.NOT_FOUND,ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result){

        logger.error("API ERROR - "+ ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).
                contentType(MediaType.APPLICATION_JSON).
                body(new ErrorMessage(request,HttpStatus.UNPROCESSABLE_ENTITY,"Campo(s) invalidos!",result));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("API ERROR - "+ ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logger.error("API ERROR - "+ ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, "Usuário sem permissão necessária."));
    }
}
