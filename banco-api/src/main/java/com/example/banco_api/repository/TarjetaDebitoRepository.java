package com.example.banco_api.repository;

import com.example.banco_api.model.TarjetaDebito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarjetaDebitoRepository extends JpaRepository<TarjetaDebito, Long> {
    Optional<TarjetaDebito> findByNumeroTarjeta(String numeroTarjeta);
}