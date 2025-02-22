package com.example.banco_api.controller;

import com.example.banco_api.model.TarjetaDebito;
import com.example.banco_api.service.TarjetaDebitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tarjetas-debito")
public class TarjetaDebitoController {

    @Autowired
    private TarjetaDebitoService tarjetaDebitoService;

    @PostMapping("/crear")
    public ResponseEntity<TarjetaDebito> crearTarjetaDebito(@RequestBody Map<String, String> tarjetaRequest) {
        String numeroCuenta = tarjetaRequest.get("numeroCuenta");
        TarjetaDebito nuevaTarjeta = tarjetaDebitoService.crearTarjetaDebito(numeroCuenta);
        return new ResponseEntity<>(nuevaTarjeta, HttpStatus.CREATED);
    }

    @PutMapping("/{numeroTarjeta}/bloquear")
    public ResponseEntity<?> bloquearTarjetaDebito(@PathVariable String numeroTarjeta) {
        try {
            tarjetaDebitoService.bloquearTarjetaDebito(numeroTarjeta);
            return ResponseEntity.ok(Map.of("message", "Tarjeta de débito bloqueada con éxito."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al bloquear la tarjeta de débito: " + e.getMessage());
        }
    }

    @GetMapping("/{numeroTarjeta}")
    public ResponseEntity<TarjetaDebito> obtenerTarjetaDebitoPorNumero(@PathVariable String numeroTarjeta) {
        TarjetaDebito tarjeta = tarjetaDebitoService.obtenerTarjetaDebitoPorNumero(numeroTarjeta);
        if (tarjeta != null) {
            return ResponseEntity.ok(tarjeta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}