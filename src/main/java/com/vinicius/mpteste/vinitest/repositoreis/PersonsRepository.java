package com.vinicius.mpteste.vinitest.repositoreis;

import com.vinicius.mpteste.vinitest.models.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonsRepository extends JpaRepository<Clients, Long> {
}
