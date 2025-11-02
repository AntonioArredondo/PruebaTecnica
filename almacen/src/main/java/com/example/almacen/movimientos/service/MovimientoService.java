package com.example.almacen.movimientos.service;

import com.example.almacen.movimientos.dto.MovimientoResponse;
import com.example.almacen.movimientos.entity.Movimiento;
import com.example.almacen.movimientos.repository.MovimientoRepository;
import com.example.almacen.productos.entity.Producto;
import com.example.almacen.productos.repository.ProductoRepository;
import com.example.almacen.usuarios.Usuario;
import com.example.almacen.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public MovimientoService(MovimientoRepository movimientoRepo, UsuarioRepository usuarioRepo, ProductoRepository productoRepo) {
        this.movimientoRepository = movimientoRepo;
        this.usuarioRepository = usuarioRepo;
        this.productoRepository = productoRepo;
    }

    public Movimiento crearMovimiento(Integer idUsuario, Integer idProducto, String tipo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Movimiento movimiento = new Movimiento();
        movimiento.setUsuario(usuario);
        movimiento.setProducto(producto);
        movimiento.setTipo(tipo);

        return movimientoRepository.save(movimiento);
    }

    public List<MovimientoResponse> obtenerMovimientos() {
        return movimientoRepository.findAll().stream()
                .map(m -> new MovimientoResponse(
                        m.getIdMovimiento(),
                        m.getUsuario().getNombre(),
                        m.getProducto().getNombre(),
                        m.getTipo(),
                        m.getFecha()
                )).collect(Collectors.toList());
    }

    public List<MovimientoResponse> obtenerMovimientosPorTipo(String tipo) {
        return movimientoRepository.findByTipo(tipo).stream()
                .map(m -> new MovimientoResponse(
                        m.getIdMovimiento(),
                        m.getUsuario().getNombre(),
                        m.getProducto().getNombre(),
                        m.getTipo(),
                        m.getFecha()
                )).collect(Collectors.toList());
    }
}
