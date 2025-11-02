package com.example.almacen.productos.repository;

import com.example.almacen.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository  extends JpaRepository <Producto, Integer> {
    List<Producto> findByEstatus(String estatus);
}
