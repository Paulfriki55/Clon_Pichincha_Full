package com.example.banco_api.service;

import com.example.banco_api.model.Cuenta;
import com.example.banco_api.model.StatusTarjeta;
import com.example.banco_api.model.TarjetaDebito;
import com.example.banco_api.repository.CuentaRepository;
import com.example.banco_api.repository.TarjetaDebitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarjetaDebitoService {

    @Autowired
    private TarjetaDebitoRepository tarjetaDebitoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public TarjetaDebito crearTarjetaDebito(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        // Aquí podrías agregar validaciones adicionales, como verificar si la cuenta ya tiene una tarjeta activa.

        TarjetaDebito tarjetaDebito = new TarjetaDebito(cuenta);
        return tarjetaDebitoRepository.save(tarjetaDebito);
    }

    public void bloquearTarjetaDebito(String numeroTarjeta) {
        TarjetaDebito tarjetaDebito = tarjetaDebitoRepository.findByNumeroTarjeta(numeroTarjeta)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta de débito no encontrada: " + numeroTarjeta));
        tarjetaDebito.setStatusTarjeta(StatusTarjeta.BLOQUEADA);
        tarjetaDebitoRepository.save(tarjetaDebito);
    }

    public TarjetaDebito obtenerTarjetaDebitoPorNumero(String numeroTarjeta) {
        return tarjetaDebitoRepository.findByNumeroTarjeta(numeroTarjeta).orElse(null);
    }
}