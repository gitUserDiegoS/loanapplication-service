package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;

import org.springframework.data.repository.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PendingLoanReactiveRepository extends Repository<LoanApplicationEntity, Long> {

    @Query("select sol.monto as monto, sol.plazo as plazo, sol.email as email, tipo.nombre as nombre, tipo.tasa_interes as interes, estado.nombre as estado " +
            "from solicitud sol " +
            "join tipo_prestamo tipo on tipo.id_tipo_prestamo = sol.id_tipo_prestamo " +
            "join estado estado on estado.id_estado = sol.id_estado " +
            "where sol.id_estado= :status LIMIT :size OFFSET :offset")
    Flux<PendingLoanDto> findLoansByStatus(int status, int size, int offset);

    @Query("SELECT COUNT(*) FROM solicitud WHERE id_estado = :status")
    Mono<Long> countByStatus(int status);

}
