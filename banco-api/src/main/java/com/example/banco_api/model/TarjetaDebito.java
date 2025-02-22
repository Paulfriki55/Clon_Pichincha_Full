package com.example.banco_api.model;

import jakarta.persistence.*;
import java.time.YearMonth;
import java.util.Random;

@Entity
@Table(name = "tarjetas_debito")
public class TarjetaDebito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_tarjeta", nullable = false, unique = true)
    private String numeroTarjeta;

    @Column(name = "fecha_expiracion", nullable = false)
    private YearMonth fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_tarjeta", nullable = false)
    private StatusTarjeta statusTarjeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    public TarjetaDebito() {
        // Constructor vacío necesario para JPA
    }

    public TarjetaDebito(Cuenta cuenta) {
        this.cuenta = cuenta;
        this.numeroTarjeta = generarNumeroTarjeta();
        this.fechaExpiracion = generarFechaExpiracion();
        this.statusTarjeta = StatusTarjeta.ACTIVA;
    }

    // Métodos para generar número y fecha de expiración (puedes personalizarlos)
    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("4"); // Visa empieza con 4
        for (int i = 0; i < 15; i++) { // 16 dígitos en total
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private YearMonth generarFechaExpiracion() {
        YearMonth ahora = YearMonth.now();
        return ahora.plusYears(5); // Tarjeta expira en 5 años
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public YearMonth getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(YearMonth fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public StatusTarjeta getStatusTarjeta() {
        return statusTarjeta;
    }

    public void setStatusTarjeta(StatusTarjeta statusTarjeta) {
        this.statusTarjeta = statusTarjeta;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}