package co.com.crediya.r2dbc;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Long>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    @Query("select sol.monto, sol.plazo, sol.email, tipo.nombre, tipo.tasa_interes, estado.nombre " +
            "from solicitud sol " +
            "join tipo_prestamo tipo on tipo.id_tipo_prestamo = sol.id_tipo_prestamo " +
            "join estado estado on estado.id_estado = sol.id_estado " +
            "where sol.id_estado= :status LIMIT :size OFFSET :offset")
    Flux<PendingLoanApplication> findLoansByStatus(int status, int size, int offset);

    @Query("SELECT COUNT(*) FROM solicitud WHERE id_estado = :status")
    Mono<Long> countByStatus(int status);

}
