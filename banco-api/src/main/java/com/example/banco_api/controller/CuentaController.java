package com.example.banco_api.controller;

import com.example.banco_api.model.Cuenta;
import com.example.banco_api.model.TipoCuenta;
import com.example.banco_api.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping("/crear")
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody Map<String, Object> cuentaRequest) {
        Long usuarioId = Long.valueOf(cuentaRequest.get("usuarioId").toString());
        TipoCuenta tipoCuenta = TipoCuenta.valueOf(cuentaRequest.get("tipoDeCuenta").toString().toUpperCase()); // Convertir String a Enum

        Cuenta nuevaCuenta = cuentaService.crearCuenta(usuarioId, tipoCuenta);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> realizarTransferencia(@RequestBody Map<String, String> transferenciaRequest) {
        String cuentaOrigenNumero = transferenciaRequest.get("cuentaOrigen");
        String cuentaDestinoNumero = transferenciaRequest.get("cuentaDestino");
        BigDecimal monto = new BigDecimal(transferenciaRequest.get("monto"));

        try {
            cuentaService.realizarTransaccion(cuentaOrigenNumero, cuentaDestinoNumero, monto);
            return ResponseEntity.ok(Map.of("message", "Transferencia realizada con éxito."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al realizar la transferencia: " + e.getMessage());
        }
    }

    @PutMapping("/{numeroCuenta}/bloquear")
    public ResponseEntity<?> bloquearCuenta(@PathVariable String numeroCuenta) {
        try {
            cuentaService.bloquearCuenta(numeroCuenta);
            return ResponseEntity.ok(Map.of("message", "Cuenta bloqueada con éxito."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al bloquear la cuenta: " + e.getMessage());
        }
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtenerCuentaPorNumero(@PathVariable String numeroCuenta) {
        Cuenta cuenta = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
        if (cuenta != null) {
            return ResponseEntity.ok(cuenta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}