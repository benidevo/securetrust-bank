package com.stb.transactionservice.common.aspect;

import com.stb.transactionservice.common.exception.ErrorDetail;
import com.stb.transactionservice.common.exception.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ValidationAspect {
    @Before("@annotation(com.stb.transactionservice.common.annotation.HandleValidationErrors)")
    public void handleValidationErrors(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                if (bindingResult.hasErrors()) {
                    List<ErrorDetail> errorDetails = new ArrayList<>();
                    for (FieldError error : bindingResult.getFieldErrors()) {
                        ErrorDetail errorDetail = ErrorDetail.builder()
                                .field(error.getField())
                                .message(error.getDefaultMessage())
                                .build();
                        errorDetails.add(errorDetail);
                    }
                    throw new ValidationException(errorDetails);
                }
            }
        }

    }
}
