package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Response to return exceptions")
public class ErrorResponseDto {

    @Schema(description = "Code Exception", example = "EMAIL_ALREADY_REGISTERED")
    private String code;

    @Schema(description = "message Exception", example = "Email null is already registered")
    private String message;

    @Schema(description = "path Exception", example = "/api/v1/usuarios")
    private String path;
}
