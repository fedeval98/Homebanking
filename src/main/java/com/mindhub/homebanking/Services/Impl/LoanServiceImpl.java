package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;


    @Override
    public List<Loan> getLoans(){
        return loanRepository.findAll();
    }

    @Override
    public ResponseEntity<String> applyForLoan(LoanApplicationDTO loanApplicationDTO, String email) {
        Client client = clientRepository.findByEmail(email);


        if (client == null) {
            return new ResponseEntity<>("Error applying for loan: Client not found or has no account.",HttpStatus.BAD_REQUEST);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if (loan == null) {
            return new ResponseEntity<>("Error applying for loan: Loan not found.", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el cliente ya tiene un préstamo con el mismo ID
        if (client.getClientLoans().stream().anyMatch(clientLoan -> clientLoan.getLoan().getId().equals(loan.getId()))) {
            return new ResponseEntity<>("Error applying for loan: Client already has this loan.", HttpStatus.BAD_REQUEST);
        }

        double requestedAmount = loanApplicationDTO.getAmount();
        int requestedPayments = loanApplicationDTO.getPayments();
        double amountInterest = requestedAmount*1.2;

        if (requestedAmount <= 0 || requestedAmount > loan.getMaxAmount() || !loan.getPayments().contains(requestedPayments)) {
            return new ResponseEntity<>("Error applying for loan: Invalid loan application data.", HttpStatus.BAD_REQUEST);
        }

        // solicitud del prestamo
        ClientLoan clientLoan = new ClientLoan(amountInterest, requestedPayments);
        client.addClientLoan(clientLoan);
        loan.ClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Loan application successful", HttpStatus.OK);
    }

    @Override
    public List<LoanDTO> getLoansDTO(){
        return getLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public LoanDTO findById(Long id) {
        return loanRepository.findById(id).map(LoanDTO::new).orElse(null);
    }

}
