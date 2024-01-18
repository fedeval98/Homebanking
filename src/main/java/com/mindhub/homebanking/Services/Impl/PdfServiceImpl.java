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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); // formateo la fecha a algo legible

        PDDocument document = new PDDocument(); // creo el documento
        PDPage page = new PDPage(); // creo la pagina
        document.addPage(page); //añado la pagina al documento

        PDPageContentStream contentStream = new PDPageContentStream(document, page); // creo el escritor del pdf
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14); //seteo fuente
        contentStream.beginText(); // inicio el pdf
        contentStream.newLineAtOffset(100, 750); //digo en que posicion de la hoja va estar el texto
        contentStream.setLeading(14.5f); // le doy un interlineado al texto

        contentStream.showText("Account Information: " + account.getNumber());
        contentStream.newLine();
        contentStream.showText("------------------------------");
        contentStream.newLine();

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

        contentStream.endText(); // cierro el texto
        contentStream.close(); // cierro el editor

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //creo un flujo de salida de un array de bytes
        document.save(byteArrayOutputStream); // guardo el ByteArray en el documento
        document.close(); // cierro el documento

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
// Un ByteArray es un tipo de dato en el cual se guardan bytes en formato array byte[]. ByteArrayInputStream o ByteArrayOutputStream
// se utilizan para leer o escribir datos en formato byte[] en lugar de archivos fisicos.