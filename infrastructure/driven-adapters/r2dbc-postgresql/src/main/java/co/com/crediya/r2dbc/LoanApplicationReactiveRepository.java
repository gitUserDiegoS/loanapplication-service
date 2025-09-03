package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Long>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}
