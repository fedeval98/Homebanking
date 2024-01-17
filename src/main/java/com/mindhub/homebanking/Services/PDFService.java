package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface PDFService {
    ByteArrayInputStream generatePdf(List<Transaction> transactions, AccountDTO account) throws IOException;
}
