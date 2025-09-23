package co.com.crediya.r2dbc.customrepository;

import co.com.crediya.r2dbc.dto.AutomaticLoanDto;
import co.com.crediya.r2dbc.dto.PendingLoanDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PendingLoanRepositoryCustom {

    Flux<PendingLoanDto> findLoansByStatus(int status, String email, int size, int offset);

}
