package co.com.crediya.api.exceptionhandler;

import co.com.crediya.api.dto.ErrorResponseDto;
import co.com.crediya.usecase.loanapplication.exception.NotAllowedLoanTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GlobalExceptionHandler {

    @ExceptionHandler(NotAllowedLoanTypeException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleEmailRegistered(
            NotAllowedLoanTypeException ex, ServerWebExchange exchange) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "EMAIL_ALREADY_REGISTERED",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(org.springframework.http.ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<org.springframework.http.ResponseEntity<ErrorResponseDto>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "INTERNAL_ERROR",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(org.springframework.http.ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

}
