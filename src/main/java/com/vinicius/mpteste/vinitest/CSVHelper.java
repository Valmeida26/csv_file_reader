package com.vinicius.mpteste.vinitest;

import com.vinicius.mpteste.vinitest.models.Persons;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static ByteArrayInputStream downloadCSV(List<Persons> tutorials) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Persons persons : tutorials) {
                List<? extends Serializable> data = Arrays.asList(
                        persons.getNome(),
                        persons.getUltimoNome(),
                        persons.getIdade(),
                        persons.getEmail(),
                        persons.getSexo(),
                        persons.getIpAcesso(),
                        persons.getNascimento()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("falha ao importar dados para arquivo CSV: " + e.getMessage());
        }
    }
}
