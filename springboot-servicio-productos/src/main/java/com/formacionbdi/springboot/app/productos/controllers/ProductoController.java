package com.formacionbdi.springboot.app.productos.controllers;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {

	@Autowired
	private IProductoService productoService;
	
	@GetMapping("/listar")
	public List<Producto> listar(){
		return productoService.findAll();
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) throws InterruptedException{
		if (id.equals(10L)) {
			throw new IllegalStateException("Producto no encontrado");
		}
		
		if (id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}
		boolean ok = true;
		if (!ok) {
			throw new RuntimeException();
		}
		return productoService.findById(id);
	}
}
