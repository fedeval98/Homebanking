package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.dto.newLoan;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        double amountInterest = requestedAmount*(1+(loan.getinterest_rate()/100.0));

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

    @Override
    public ResponseEntity<String> createLoan(newLoan newLoan) {

        if(newLoan.getPayments().isEmpty()){
            return new ResponseEntity<>("Payment options are empty.",HttpStatus.BAD_REQUEST);
        }

        if(newLoan.getName().isBlank()){
            return new ResponseEntity<>("Loan name can not be blank",HttpStatus.BAD_REQUEST);
        }

        if(newLoan.getInterest_rate() == 0 || newLoan.getInterest_rate() <= 0){
            return new ResponseEntity<>("Interest rate can not be 0 or lesser.",HttpStatus.BAD_REQUEST);
        }

        if(newLoan.getMaxAmount()== 0 || newLoan.getMaxAmount() <= 0){
            return new ResponseEntity<>("Max amount can not be 0 or lesser.",HttpStatus.BAD_REQUEST);
        }

        if(getLoans().stream().anyMatch(existingLoan -> existingLoan.getName().equals(newLoan.getName()))){
            return new ResponseEntity<>("Loan already created, please change the name.",HttpStatus.BAD_REQUEST);
        }

        Set<Integer> uniquePayments = new HashSet<>(newLoan.getPayments());
        if(uniquePayments.size() < newLoan.getPayments().size()){
            return  new ResponseEntity<>("Duplicate payment options are not allowed", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(newLoan.getName(), newLoan.getMaxAmount(),newLoan.getPayments(),newLoan.getInterest_rate());
        loanRepository.save(loan);

        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

}
