package com.example.almacen.productos.controller;

import com.example.almacen.login.JwtService;
import com.example.almacen.movimientos.service.MovimientoService;
import com.example.almacen.productos.entity.Producto;
import com.example.almacen.productos.service.ProductoService;
import com.example.almacen.usuarios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final MovimientoService movimientoService;
    private final UsuarioService usuarioService;

    public ProductoController(ProductoService productoService, MovimientoService movimientoService, JwtService jwtService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @GetMapping("/estatus")
    public List<Producto> obtenerProductosPorEstatus(@RequestParam String estatus) {
        return productoService.obtenerProductosPorEstatus(estatus);
    }

    @GetMapping("/estatus/activos")
    public List<Producto> obtenerProductosActivos() {
        return productoService.obtenerProductosActivos();
    }

    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.agregarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PatchMapping("/{id}/entrada")
    public ResponseEntity<?> aumentarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad) {
        Producto productoOriginal = productoService.obtenerProductoPorId(id);
        if (cantidad > productoOriginal.getCantidad()) {
            Producto productoActualizado = productoService.aumentarCantidad(id, cantidad);
            Integer idUsuario = usuarioService.obtenerIdUsuario();
            movimientoService.crearMovimiento(idUsuario, id, "Entrada");
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizado);
        }
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "La cantidad no puede ser menos a la actual");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PatchMapping("/{id}/salida")
    public ResponseEntity<?> bajarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad) {
        Producto productoOriginal = productoService.obtenerProductoPorId(id);
        if (cantidad < productoOriginal.getCantidad()) {
            Producto productoActualizado = productoService.bajarCantidad(id, cantidad);
            Integer idUsuario = usuarioService.obtenerIdUsuario();
            movimientoService.crearMovimiento(idUsuario, id, "Salida");
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizado);
        }
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "La cantidad no puede ser mayor a la actual");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PatchMapping("/{id}/estatus")
    public ResponseEntity<Producto> actualizarEstatus(@PathVariable Integer id, @RequestParam String estatus) {
        Producto productoActualizado = productoService.actualizarEstatus(id, estatus);
        return ResponseEntity.status(HttpStatus.OK).body(productoActualizado);
    }
}
