package co.com.crediya.api.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND),
    CONFLICT(HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;

    ErrorType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static HttpStatus fromCode(String errorCode) {

        try {
            return ErrorType.valueOf(errorCode).getHttpStatus();

        } catch (IllegalArgumentException e) {
            return INTERNAL_SERVER_ERROR.getHttpStatus();
        }

    }
}
