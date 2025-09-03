package co.com.crediya.api.exceptionhandler;

import co.com.crediya.api.dto.ErrorResponseDto;
import co.com.crediya.model.loanapplication.exceptions.BusinessException;
import co.com.crediya.model.loanapplication.exceptions.NotAllowedLoanTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleBusinessException(
            BusinessException ex, ServerWebExchange exchange) {

        HttpStatus status = mapToHttpStatus(ex.getErrorCode());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                ex.getErrorCode(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(org.springframework.http.ResponseEntity
                .status(status)
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

    private HttpStatus mapToHttpStatus(String errorCode) {
        return switch (errorCode) {
            case "USER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "CONFLICT" -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

}
