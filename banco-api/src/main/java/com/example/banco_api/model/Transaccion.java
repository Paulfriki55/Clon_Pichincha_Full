package com.example.banco_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference; // Import JsonBackReference

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDateTime fechaTransferencia;

    @Column(name = "monto_transferencia", nullable = false)
    private BigDecimal montoTransferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_transaccion", nullable = false)
    private TipoTransaccion tipoDeTransaccion;

    @Column(name = "usuario_beneficiario_id")
    private Long usuarioBeneficiarioId; // Usamos ID de usuario beneficiario, no entidad Usuario completa para simplificar

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    @JsonBackReference  // <--- **CRITICAL: Make sure @JsonBackReference is HERE**
    private Cuenta cuenta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(LocalDateTime fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    public BigDecimal getMontoTransferencia() {
        return montoTransferencia;
    }

    public void setMontoTransferencia(BigDecimal montoTransferencia) {
        this.montoTransferencia = montoTransferencia;
    }

    public TipoTransaccion getTipoDeTransaccion() {
        return tipoDeTransaccion;
    }

    public void setTipoDeTransaccion(TipoTransaccion tipoDeTransaccion) {
        this.tipoDeTransaccion = tipoDeTransaccion;
    }

    public Long getUsuarioBeneficiarioId() {
        return usuarioBeneficiarioId;
    }

    public void setUsuarioBeneficiarioId(Long usuarioBeneficiarioId) {
        this.usuarioBeneficiarioId = usuarioBeneficiarioId;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}