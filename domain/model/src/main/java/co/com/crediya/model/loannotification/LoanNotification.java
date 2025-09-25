package co.com.crediya.model.loannotification;

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
public class LoanNotification {

    private Long idApplication;

    private String status;

    private List<PaymentPlan> paymentPlan;

}
