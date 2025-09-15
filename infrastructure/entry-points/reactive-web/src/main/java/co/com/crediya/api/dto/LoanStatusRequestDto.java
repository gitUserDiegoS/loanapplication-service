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
@Schema(description = "Request used to update the status of a loan application (Approved, Rejected)")
public class LoanStatusRequestDto {

    @Schema(description = "id of a loan application", example = "1")
    private Long idApplication;

    @Schema(description = "Status to update the loan application", example = "2 Approved; 3 Rejected")
    private Integer status;

}
