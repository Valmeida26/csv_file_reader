package com.vinicius.mpteste.vinitest.controllers;

import com.vinicius.mpteste.vinitest.models.Clients;
import com.vinicius.mpteste.vinitest.service.PersonsService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonsController {

    @Autowired
    private PersonsService personsService;

    //region funções comuns do spring
    @GetMapping("/All")
    public List<Clients> findAll(){
        return this.personsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clients> findbyId(@PathVariable Long id){
        Clients obj = this.personsService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.personsService.delete(id);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region le o CSV e persiste os dados e salva no banco
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestBody MultipartFile file) {
        try {
            // Ler o arquivo CSV
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            List<Clients> personsList = new ArrayList<>(); // Lista para armazenar as pessoas do CSV

            // Processar cada linha do CSV e adicionar à lista
            for (CSVRecord record : csvParser) {
                String name = record.get("Nome");
                String lastName = record.get("UltimoNome");
                int age = Integer.parseInt(record.get("Idade"));
                String gender = record.get("Sexo");
                String email = record.get("Email");
                String ipAccess = record.get("IpAcesso");
                String birthDate = record.get("Nascimento");

                // Corrigir data de birthDate com base na age
                birthDate = LocalDate.now().minusYears(age).toString();
                LocalDate newBirthDate = LocalDate.parse(birthDate);

                // Definir o formato desejado
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Formatando o LocalDate para uma string com o formato desejado
                String formattedDate = newBirthDate.format(formatter);

                // Adicionar a pessoa à lista
                Clients person = new Clients(name, lastName, email, gender, ipAccess, age, formattedDate);
                personsList.add(person);
            }

            // Ordenar a lista de pessoas por name e sobrenome
            personsList.sort(Comparator.comparing(Clients::getName)
                    .thenComparing(Clients::getLastName));

            // Persistir os dados no banco de dados
            for (Clients person : personsList) {
                personsService.create(person);
            }
            csvParser.close();
            return ResponseEntity.ok("CSV processado e dados salvos no banco de dados.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o CSV.");
        }
    }
    //endregion

    //region Retorna na tela em ordem alfabetica
    @GetMapping("/upload/name")
    public List<String> orderAlphabetical(@RequestParam("file") MultipartFile file) throws IOException {

        List<String> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : csvParser) {
                String line;
                String name = record.get("Nome");
                String lastName = record.get("UltimoNome");
                int age = Integer.parseInt(record.get("Idade"));
                String gender = record.get("Sexo");
                String email = record.get("Email");
                String ipAccess = record.get("IpAcesso");
                String birthDate = record.get("Nascimento");

                birthDate = LocalDate.now().minusYears(age).toString();
                LocalDate newBirthDate = LocalDate.parse(birthDate);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String formattedDate = newBirthDate.format(formatter);

                line = " name: " + name + " | lastName: " + lastName + " | age: " + age + " | gender: " + gender + " | e-mail: " + email
                        + " | ip: " + ipAccess + " | birthDate: " + formattedDate;
                data.add(line);
            }
            csvParser.close();
        }
        Collections.sort(data);
        return data;
    }
    //endregion

    //region Retorna na tela o numero de homens e mulheres e suas medias de age
    @GetMapping("/upload/genders-average")
    public ResponseEntity<String> numberOfMenAndWomen(@RequestParam("file") MultipartFile file) throws IOException{
        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            double maleCount = 0;
            double femaleCount = 0;
            double maleAgeSum = 0;
            double femaleAgeSum = 0;

            for (CSVRecord record : csvParser) {
                int age = Integer.parseInt(record.get("Idade"));
                String gender = record.get("Sexo");

                if (gender.equalsIgnoreCase("male")) {
                    maleCount++;
                    maleAgeSum += age;
                } else if (gender.equalsIgnoreCase("female")) {
                    femaleCount++;
                    femaleAgeSum += age;
                }

            }
            double mediaIdadeMale = maleAgeSum / maleCount;
            double mediaIdadeFemale = femaleAgeSum / femaleCount;

            DecimalFormat df = new DecimalFormat("#.##");

            String mediaMale = df.format(mediaIdadeMale);
            String mediafemale = df.format(mediaIdadeFemale);

            csvParser.close();
            return ResponseEntity.ok("Número de Homens: " + maleCount + " Média de idade masculina: " + mediaMale +
                    "\n" + "Número de Mulheres: " + femaleCount + " Média de idade feminina: " + mediafemale);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region Download do novo CSV
    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() {
        String filename = "csv_teste.csv";
        InputStreamResource file = new InputStreamResource(personsService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
    //endregion
}
