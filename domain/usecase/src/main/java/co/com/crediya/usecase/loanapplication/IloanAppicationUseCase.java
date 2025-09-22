package co.com.crediya.usecase.loanapplication;


import co.com.crediya.model.common.PageResponse;
import co.com.crediya.model.loanapplication.LoanApplication;
import co.com.crediya.model.loanapplication.gateways.PendingLoanApplication;
import co.com.crediya.model.usersession.UserSession;

import reactor.core.publisher.Mono;

public interface IloanAppicationUseCase {

    Mono<LoanApplication> saveLoanApplication(LoanApplication loanApplication, String idDocument, String token, UserSession userSession);

    Mono<PageResponse<PendingLoanApplication>> getLoanApplications(int status, String email, int page, int size, int offset, String token);

    Mono<LoanApplication> updateLoanApplication(LoanApplication loanApplication, String token);


}
