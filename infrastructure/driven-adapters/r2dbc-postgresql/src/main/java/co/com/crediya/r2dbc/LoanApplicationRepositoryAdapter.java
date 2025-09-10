package co.com.crediya.r2dbc;

import co.com.crediya.model.common.PageRequest;
import co.com.crediya.model.common.PageResponse;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class LoanApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication,
        LoanApplicationEntity,
        Long,
        LoanApplicationReactiveRepository
        > implements LoanApplicationRepository {

    private static final Logger log = LoggerFactory.getLogger(LoanApplicationRepositoryAdapter.class);


    private final TransactionalOperator operator;

    public LoanApplicationRepositoryAdapter(LoanApplicationReactiveRepository repository, ObjectMapper mapper, TransactionalOperator operator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, entity -> mapper.map(entity, LoanApplication.class));
        this.operator = operator;

    }

    @Override
    public Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication) {
        log.trace("Create loan application with email: {}", loanApplication.getEmail());
        return super.save(loanApplication)
                .as(operator::transactional)
                .doOnNext(savedLoanApplication -> log.trace("Loan application created successfully with id: {}", savedLoanApplication.getIdApplication()))
                .doOnError(error -> log.error("Error loan application creation, failed with message: {}", error.getMessage()));
    }


    @Override
    public Flux<PendingLoanApplication> findByStatus(int status, PageRequest pageable) {

        int offset = pageable.offset();
        return repository.findLoansByStatus(status, pageable.size(), offset);

/*
        return repository.countByStatus(status)
                .flatMap(total ->
                        repository.findLoansByStatus(status, pageable.page(), offset)
                                .collectList()
                                .map(data -> new PageResponse<>(data,
                                        pageable.page(),
                                        pageable.size(),
                                        total
                                ))

                );


 */

    }

    @Override
    public Mono<Long> countByStatus(int status) {
        return repository.countByStatus(status);
    }

}
