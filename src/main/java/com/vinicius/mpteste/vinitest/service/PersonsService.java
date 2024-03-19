package com.vinicius.mpteste.vinitest.service;

import com.vinicius.mpteste.vinitest.CSVHelper;
import com.vinicius.mpteste.vinitest.models.Clients;
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

    public List<Clients> findAll(){
        return this.personsRepository.findAll();
    }

    public Clients findById(Long id){
        Optional<Clients> persons = this.personsRepository.findById(id);
        return persons.orElseThrow(() -> new RuntimeException(
                "Pessoa não encontrada! id: " + id + ", Tipo: " + Clients.class.getName()
        ));
    }

    @Transactional
    public void create(Clients obj){
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

    //Download do CSV
    public ByteArrayInputStream load() {
        List<Clients> csvDownload = personsRepository.findAll();
        return CSVHelper.downloadCSV(csvDownload);
    }
}
