package com.example.almacen.productos.service;

import com.example.almacen.productos.entity.Producto;
import com.example.almacen.productos.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Integer id) {
        Optional<Producto> productoEncontrado = productoRepository.findById(id);
        return productoEncontrado.orElse(null);
    }

    public List<Producto> obtenerProductosPorEstatus(String estatus) {
        return productoRepository.findByEstatus(estatus);
    }

    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByEstatus("Activo");
    }

    @Transactional
    public Producto agregarProducto(Producto producto) {
        producto.setCantidad(0);
        producto.setEstatus("Activo");
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto aumentarCantidad(Integer id, Integer nuevaCantidad) {
        Producto producto = obtenerProductoPorId(id);
        if (producto != null) {
            producto.setCantidad(nuevaCantidad);
            productoRepository.save(producto);
        }
        return producto;
    }

    @Transactional
    public Producto bajarCantidad(Integer id, Integer nuevaCantidad) {
        Producto producto = obtenerProductoPorId(id);
        if (producto != null) {
            producto.setCantidad(nuevaCantidad);
            productoRepository.save(producto);
        }
        return producto;
    }

    @Transactional
    public Producto actualizarEstatus(Integer id, String nuevoStatus) {
        Producto producto = obtenerProductoPorId(id);
        if (producto != null) {
            producto.setEstatus(nuevoStatus);
            productoRepository.save(producto);
        }
        return producto;
    }
}
