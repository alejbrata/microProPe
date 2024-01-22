package com.formacionbdi.springboot.app.item.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@GetMapping("/listar")
	public List<Item> listar(){
		
		return itemService.findAll();
	}
	
	@CircuitBreaker(name = "detallado", fallbackMethod = "otroMetodo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		
		return itemService.findById(id, cantidad);
	}
	
	
	public Item otroMetodo (@PathVariable Long id, @PathVariable Integer cantidad,Throwable e) {
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setName("Producto resilience4j");
		producto.setPrecio(4500.00);
		producto.setCreateAt(new Date());
		item.setProducto(producto);
		return item;
	}
}
