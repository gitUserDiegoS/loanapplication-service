package co.com.crediya.model.loanapplication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String idDocument;
    private String name;
    private String lastname;
    private String email;
    private BigDecimal salaryBase;


}
