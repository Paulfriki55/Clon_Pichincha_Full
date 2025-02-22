package com.example.banco_api.repository;

import com.example.banco_api.model.Cuenta;
import com.example.banco_api.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findAllByCuenta(Cuenta cuenta);
}