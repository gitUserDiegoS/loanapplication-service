package co.com.crediya.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFoundResponseDto {

    private String idDocument;

    private String email;

    private String name;


    private String lastname;


    private BigDecimal salaryBase;


}
