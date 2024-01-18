package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.PDFService;
import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Transaction;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfServiceImpl implements PDFService {
    public ByteArrayInputStream generatePdf(List<Transaction> transactions, AccountDTO account) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 750);
        contentStream.setLeading(14.5f);

        contentStream.showText("Account Information: " + account.getNumber());
        contentStream.newLine();
        contentStream.showText("------------------------------");
        contentStream.newLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (Transaction transaction : transactions) {
            contentStream.showText("Date: " + transaction.getDateTime().format(formatter));
            contentStream.newLine();
            contentStream.showText("Amount: " + transaction.getAmount());
            contentStream.newLine();
            contentStream.showText("Type: " + transaction.getType());
            contentStream.newLine();
            contentStream.showText("Description: " + transaction.getDescription());
            contentStream.newLine();
            contentStream.showText("Previous Balance: " + transaction.getPreviousBalance());
            contentStream.newLine();
            contentStream.showText("Current Balance: " + transaction.getCurrentBalance());
            contentStream.newLine();
            contentStream.showText("------------------------------");
            contentStream.newLine();
        }

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
