package com.example.almacen.movimientos.controller;

import com.example.almacen.movimientos.dto.MovimientoResponse;
import com.example.almacen.movimientos.entity.Movimiento;
import com.example.almacen.movimientos.service.MovimientoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public List<MovimientoResponse> obtenerTodos() {
        return movimientoService.obtenerMovimientos();
    }

    @GetMapping("/tipo")
    public List<MovimientoResponse> obtenerPorTipo(@RequestParam String tipo) {
        return movimientoService.obtenerMovimientosPorTipo(tipo);
    }
}
