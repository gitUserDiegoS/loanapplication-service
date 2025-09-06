package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response for a loan application created")
public class ApplicationResponseDto {

    @Schema(description = "Id application for a loan application created", example = "3")
    private Long idApplication;
}
