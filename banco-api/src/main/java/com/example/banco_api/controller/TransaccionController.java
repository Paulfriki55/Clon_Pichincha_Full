package com.example.banco_api.controller;

import com.example.banco_api.model.Transaccion;
import com.example.banco_api.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Transaccion>> obtenerTransaccionesPorUsuario(@PathVariable Long usuarioId) {
        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorUsuario(usuarioId);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<List<Transaccion>> obtenerTransaccionesPorCuenta(@PathVariable String numeroCuenta) {
        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCuenta(numeroCuenta);
        return ResponseEntity.ok(transacciones);
    }
}