package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoanApplicationRequestDto;
import co.com.crediya.model.loanapplication.LoanApplication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapperDto {

    LoanApplication toModel(LoanApplicationRequestDto loanApplicationRequestDto);


}
