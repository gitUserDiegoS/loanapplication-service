package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoanStatusRequestDto;
import co.com.crediya.api.dto.StatusLoanResponseDto;
import co.com.crediya.model.loanapplication.LoanApplication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusLoanMapperDto {


    StatusLoanResponseDto toResponse(String status);

    LoanApplication toModel(LoanStatusRequestDto loanStatusRequestDto);


}
