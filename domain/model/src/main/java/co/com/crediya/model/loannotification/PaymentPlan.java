package co.com.crediya.model.loannotification;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentPlan {

    private Integer month;

    private BigDecimal monthlyFee;

    private BigDecimal rate;

    private BigDecimal principalPayment;

    private BigDecimal remininBalance;

}
