package com.example.banco_api.service;

import com.example.banco_api.model.Cuenta;
import com.example.banco_api.model.TipoTransaccion;
import com.example.banco_api.model.Transaccion;
import com.example.banco_api.repository.CuentaRepository;
import com.example.banco_api.repository.TransaccionRepository;
import com.example.banco_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;


    public void registrarTransaccion(Cuenta cuentaOrigen, Long usuarioBeneficiarioId, BigDecimal monto, TipoTransaccion tipoTransaccion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuentaOrigen);
        transaccion.setUsuarioBeneficiarioId(usuarioBeneficiarioId);
        transaccion.setMontoTransferencia(monto);
        transaccion.setTipoDeTransaccion(tipoTransaccion);
        transaccion.setFechaTransferencia(LocalDateTime.now());
        transaccionRepository.save(transaccion);
    }

    public List<Transaccion> obtenerTransaccionesPorUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getCuentas().stream()
                        .flatMap(cuenta -> transaccionRepository.findAllByCuenta(cuenta).stream())
                        .toList())
                .orElse(List.of()); // Retorna lista vacía si el usuario no existe
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(transaccionRepository::findAllByCuenta)
                .orElse(List.of()); // Retorna lista vacía si la cuenta no existe
    }
}