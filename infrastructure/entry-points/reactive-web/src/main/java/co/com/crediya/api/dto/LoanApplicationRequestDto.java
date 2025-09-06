package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request used to create a new loan application")
public class LoanApplicationRequestDto {

    @Schema(description = "User idDocument", example = "1234567")
    private String idDocument;

    @Schema(description = "loan´s amount", example = "100000")
    private BigDecimal amount;

    @Schema(description = "loan´s term", example = "12")
    private Integer term;

    @Schema(description = "Id loan type", example = "1")
    private Integer loanType;


}
