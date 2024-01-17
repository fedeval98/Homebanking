package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.dto.newLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoansDTO();

    List <Loan> getLoans();

    ResponseEntity<String> applyForLoan(LoanApplicationDTO loanApplicationDTO, String email);

    LoanDTO findById(Long id);

    ResponseEntity<String> createLoan(newLoan newLoan);
}
