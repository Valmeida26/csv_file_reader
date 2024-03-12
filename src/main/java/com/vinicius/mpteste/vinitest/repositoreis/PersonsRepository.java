package com.vinicius.mpteste.vinitest.repositoreis;

import com.vinicius.mpteste.vinitest.models.Persons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonsRepository extends JpaRepository<Persons, Long> {
}
