package com.example.almacen.movimientos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovimientoResponse {
    private Integer idMovimiento;
    private String nombreUsuario;
    private String nombreProducto;
    private String tipo;
    private LocalDateTime fecha;
}
