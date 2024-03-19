package com.vinicius.mpteste.vinitest;

import com.vinicius.mpteste.vinitest.models.Clients;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static ByteArrayInputStream downloadCSV(List<Clients> tutorials) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Clients persons : tutorials) {
                List<? extends Serializable> data = Arrays.asList(
                        persons.getName(),
                        persons.getLastName(),
                        persons.getAge(),
                        persons.getEmail(),
                        persons.getGender(),
                        persons.getIpAccess(),
                        persons.getBirthDate()
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
