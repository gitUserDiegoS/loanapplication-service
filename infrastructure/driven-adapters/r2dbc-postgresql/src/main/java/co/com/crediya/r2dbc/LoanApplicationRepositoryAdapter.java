package co.com.crediya.r2dbc;


import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.r2dbc.entity.LoanApplicationEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class LoanApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication,
        LoanApplicationEntity,
        Long,
        LoanApplicationReactiveRepository
        > implements LoanApplicationRepository {

    private static final Logger log = LoggerFactory.getLogger(LoanApplicationRepositoryAdapter.class);

    private final TransactionalOperator operator;

    private final PendingLoanReactiveRepository pendingLoanRepo;

    public LoanApplicationRepositoryAdapter(LoanApplicationReactiveRepository repository, PendingLoanReactiveRepository pendingLoanRepo, ObjectMapper mapper, TransactionalOperator operator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, entity -> mapper.map(entity, LoanApplication.class));
        this.operator = operator;
        this.pendingLoanRepo = pendingLoanRepo;

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
    public Flux<PendingLoanApplication> findByStatus(int status, String email, int size, int offset) {


        return pendingLoanRepo.findLoansByStatus(status, email, size, offset)
                .map(proj -> PendingLoanApplication.builder()
                        .amount(proj.amount())
                        .term(proj.term())
                        .email(proj.email())
                        .loanType(proj.name())
                        .interestRate(proj.rate())
                        .status(proj.status())
                        .build());


    }

    @Override
    public Mono<Long> countByStatus(int status) {
        return pendingLoanRepo.countByStatus(status);
    }

    @Override
    public Mono<LoanApplication> updateStatusLoanApplication(LoanApplication loanApplication) {
        log.trace("Updating loan application with status: {}", loanApplication.getStatus());
        return super.findById(loanApplication.getIdApplication())
                .flatMap(loanFound -> {
                    loanFound.setStatus(loanApplication.getStatus());

                    return super.save(loanFound)
                            .as(operator::transactional)
                            .doOnNext(savedLoanApplication -> log.trace("Loan application updated successfully with id: {}", savedLoanApplication.getIdApplication()))
                            .doOnError(error -> log.error("Error when try to update the application's status, failed with message: {}", error.getMessage()));
                });


    }
}

