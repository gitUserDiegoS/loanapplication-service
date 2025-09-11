package co.com.crediya.r2dbc;

import reactor.core.publisher.Flux;

public interface PendingLoanRepositoryCustom {

    Flux<PendingLoanDto> findLoansByStatus(int status, String email, int size, int offset);
}
