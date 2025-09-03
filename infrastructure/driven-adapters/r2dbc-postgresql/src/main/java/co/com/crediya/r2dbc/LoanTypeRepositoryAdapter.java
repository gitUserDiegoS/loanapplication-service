package co.com.crediya.r2dbc;

import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.LoanType;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.r2dbc.entity.LoanTypeEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Integer,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {

    private static final Logger log = LoggerFactory.getLogger(LoanTypeRepositoryAdapter.class);


    private final TransactionalOperator operator;

    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper, TransactionalOperator operator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, entity -> mapper.map(entity, LoanType.class));
        this.operator = operator;

    }


    @Override
    public Mono<LoanType> findByLoanType(Integer type) {
        return super.findById(type);
    }
}
