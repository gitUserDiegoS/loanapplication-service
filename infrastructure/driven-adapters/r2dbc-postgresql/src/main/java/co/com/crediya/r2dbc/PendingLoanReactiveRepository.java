package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


import reactor.core.publisher.Mono;

public interface PendingLoanReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Long>, PendingLoanRepositoryCustom {

    @Query("SELECT COUNT(*) FROM solicitud WHERE id_estado = :status")
    Mono<Long> countByStatus(int status);

}
