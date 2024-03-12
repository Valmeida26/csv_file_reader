package com.vinicius.mpteste.vinitest.service;

import com.vinicius.mpteste.vinitest.CSVHelper;
import com.vinicius.mpteste.vinitest.models.Persons;
import com.vinicius.mpteste.vinitest.repositoreis.PersonsRepository;
import com.vinicius.mpteste.vinitest.repositoreis.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PersonsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonsRepository personsRepository;

    public List<Persons> findAll(){
        return this.personsRepository.findAll();
    }
    public Persons findById(Long id){
        Optional<Persons> persons = this.personsRepository.findById(id);
        return persons.orElseThrow(() -> new RuntimeException(
                "Pessoa não encontrada! id: " + id + ", Tipo: " + Persons.class.getName()
        ));
    }

    @Transactional
    public void create(Persons obj){
        obj.setId(null);
        obj = this.personsRepository.save(obj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.personsRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Não é possível excluir a pessoa: " + id);
        }
    }

    //Teste de download
    public ByteArrayInputStream load() {
        List<Persons> csvDownload = personsRepository.findAll();
        return CSVHelper.downloadCSV(csvDownload);
    }
}
