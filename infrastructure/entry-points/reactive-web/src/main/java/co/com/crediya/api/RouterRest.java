package co.com.crediya.api;

import co.com.crediya.api.config.LoanApplicationPath;
import co.com.crediya.api.dto.ApplicationResponseDto;
import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.api.exceptionhandler.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Tag(name = "LoansApplication", description = "loan operations")
public class RouterRest {

    private final LoanApplicationPath loanApplicationPath;
    private final Handler loanHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenCreateLoanApplication",
                    operation = @Operation(
                            operationId = "createLoan",
                            summary = "Create loan application with data related",
                            requestBody = @RequestBody
                                    (description = "LoanApplicationRequestDto", required = true,
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = LoanApplicationRequestDto.class)
                                            )
                                    ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Loan Application created successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation =
                                                            ApplicationResponseDto.class))),
                                    @ApiResponse(responseCode = "400", description = "Bad request due to validation result",
                                            content = @Content(
                                                    schema = @Schema(implementation =
                                                            GlobalExceptionHandler.class))),
                                    @ApiResponse(responseCode = "500", description = "Internal error",
                                            content = @Content(
                                                    schema = @Schema(implementation =
                                                            GlobalExceptionHandler.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(loanApplicationPath.getLoans()), loanHandler::listenCreateLoanApplication);

    }
}
