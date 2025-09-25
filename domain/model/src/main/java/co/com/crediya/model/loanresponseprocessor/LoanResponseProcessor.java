package co.com.crediya.model.loanresponseprocessor;

import co.com.crediya.model.loannotification.PaymentPlan;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanResponseProcessor {

    private Long idApplication;

    private String decition;

    private List<PaymentPlan> paymentPlan;


}
