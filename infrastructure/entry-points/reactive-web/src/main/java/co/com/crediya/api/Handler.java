package co.com.crediya.api;

import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.api.mapper.LoanMapperDto;
import co.com.crediya.model.common.PageRequest;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.usersession.UserSession;
import co.com.crediya.usecase.loanapplication.IloanAppicationUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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

    @PreAuthorize("hasAnyRole('CLIENT')")
    public Mono<ServerResponse> listenCreateLoanApplication(ServerRequest serverRequest) {
        String token = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);

        return getSessionContext()
                .flatMap(userSession ->
                        serverRequest.bodyToMono(LoanApplicationRequestDto.class)
                                .doOnNext(loan -> log.trace("Begin request to create a loan application related with id document: {}", loan.getIdDocument()))
                                .flatMap(dto -> {
                                    LoanApplication loan = loanMapperDto.toModel(dto);
                                    return loanApplicationUseCase.saveLoanApplication(loan, dto.getIdDocument(), token, userSession);
                                })
                                .map(loanMapperDto::toResponse)
                )
                .flatMap(saved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved))
                .doOnError(err -> log.error("Error in handler listenCreateLoanApplication {} ", err.getMessage(), err));
    }

    public Mono<ServerResponse> listenGetLoanApplications(ServerRequest serverRequest) {

        String token = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);


        int status = Integer.parseInt(serverRequest.queryParam("status").orElse("1"));
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        //String sort = serverRequest.queryParam("sort").orElse("fechaCreacion,DESC");

        PageRequest pageRequest = new PageRequest(page, size);

        return loanApplicationUseCase.getLoanApplications(status, pageRequest,token)
//                .doOnNext(user -> log.trace("Begin request to get user by email: {}", user.getEmail()))
  //              .map(userMapperDto::toFoundResponse)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(err -> log.error("Error in handler-->listenGetUserByEmail{}", err.getMessage(), err));


    }

    private static Mono<UserSession> getSessionContext() {
        return ReactiveSecurityContextHolder.getContext()
                .doOnNext(user -> log.trace("Init process, getting context"))
                .map(SecurityContext::getAuthentication)
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(auth -> (UserSession) auth.getPrincipal())
                .doOnNext(user -> log.trace("Session recovered successfully"))
                .doOnError(err -> log.error("Error in getSessionContext-->listenCreateLoanApplication{}", err.getMessage(), err));

    }
}
