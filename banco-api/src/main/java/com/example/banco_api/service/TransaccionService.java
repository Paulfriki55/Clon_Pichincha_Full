package com.example.banco_api.service;

import com.example.banco_api.model.Cuenta;
import com.example.banco_api.model.TipoTransaccion;
import com.example.banco_api.model.Transaccion;
import com.example.banco_api.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public void registrarTransaccion(Cuenta cuentaOrigen, Long usuarioBeneficiarioId, BigDecimal monto, TipoTransaccion tipoTransaccion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuentaOrigen);
        transaccion.setUsuarioBeneficiarioId(usuarioBeneficiarioId);
        transaccion.setMontoTransferencia(monto);
        transaccion.setTipoDeTransaccion(tipoTransaccion);
        transaccion.setFechaTransferencia(LocalDateTime.now());
        transaccionRepository.save(transaccion);
    }
}