package co.com.crediya.api;

import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.api.mapper.LoanMapperDto;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.usecase.loanapplication.IloanAppicationUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final IloanAppicationUseCase loanApplicationUseCase;

    private final LoanMapperDto loanMapperDto;


    public Mono<ServerResponse> listenCreateLoanApplication(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(LoanApplicationRequestDto.class)
                .doOnNext(loan -> log.trace("Begin request to create a loan application related with id document: {}", loan.getIdDocument()))
                .flatMap(dto -> {
                    LoanApplication loan = loanMapperDto.toModel(dto);
                    return loanApplicationUseCase.saveLoanApplication(loan, dto.getIdDocument());
                })
                .map(loanMapperDto::toResponse)
                .flatMap(saved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved))
                .doOnError(err -> log.error("Error in handler{}", err.getMessage(), err));
    }
}
