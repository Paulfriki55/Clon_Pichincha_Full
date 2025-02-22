package com.example.banco_api.service;

import com.example.banco_api.model.*;
import com.example.banco_api.repository.CuentaRepository;
import com.example.banco_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransaccionService transaccionService;

    public Cuenta crearCuenta(Long usuarioId, TipoCuenta tipoDeCuenta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setNumeroCuenta(generarNumeroCuenta());
        cuenta.setTipoDeCuenta(tipoDeCuenta);
        cuenta.setStatus(StatusCuenta.ACTIVA);
        cuenta.setSaldo(BigDecimal.ZERO); // Saldo inicial 0
        return cuentaRepository.save(cuenta);
    }

    private String generarNumeroCuenta() {
        // Genera un número de cuenta aleatorio de 10 dígitos
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Transactional
    public void realizarTransaccion(String cuentaOrigenNumero, String cuentaDestinoNumero, BigDecimal monto) {
        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuenta(cuentaOrigenNumero)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de origen no encontrada: " + cuentaOrigenNumero));
        Cuenta cuentaDestino = cuentaRepository.findByNumeroCuenta(cuentaDestinoNumero)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de destino no encontrada: " + cuentaDestinoNumero));

        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new IllegalStateException("Saldo insuficiente en la cuenta de origen.");
        }

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        // Registrar la transacción (puedes mover esto a un servicio de transacciones separado si lo prefieres)
        transaccionService.registrarTransaccion(cuentaOrigen, cuentaDestino.getUsuario().getId(), monto, TipoTransaccion.TRANSFERENCIA);
    }

    public void bloquearCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
        cuenta.setStatus(StatusCuenta.INACTIVA);
        cuentaRepository.save(cuenta);
    }

    public Cuenta obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElse(null);
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }

    @Transactional
    public void depositar(String numeroCuenta, BigDecimal monto) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        cuentaRepository.save(cuenta);
        // Registrar la transacción como un tipo de depósito o similar si lo deseas en el futuro
        // Por ahora, solo actualizamos el saldo.
    }
}