package com.formacionbdi.springboot.app.item.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
public class ItemController {
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	
	
	
	
	@Autowired
	//@Qualifier("serviceRestTemplate")
	private ItemService itemService;

	@GetMapping("/listar")
	public List<Item> listar(){
		
		return itemService.findAll();
	}
	
	//@CircuitBreaker(name = "detallado", fallbackMethod = "otroMetodo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		
		return cbFactory.create("items")
				.run(()->itemService.findById(id, cantidad), e -> otroMetodo(id, cantidad, e));
	}
	
	
	@CircuitBreaker(name = "detallado", fallbackMethod = "otroMetodo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
		
		return itemService.findById(id, cantidad);
	}
	
	@CircuitBreaker(name = "detallado", fallbackMethod = "otroMetodo")
	@TimeLimiter(name = "detallado")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
		
		return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
	}
	
	public Item otroMetodo (@PathVariable Long id, @PathVariable Integer cantidad,Throwable e) {
		logger.info(e.getMessage());
		
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Producto resilience4j");
		producto.setPrecio(4500.00);
		producto.setCreateAt(new Date());
		item.setProducto(producto);
		return item;
	}
	
	public CompletableFuture<Item> otroMetodo2 (@PathVariable Long id, @PathVariable Integer cantidad,Throwable e) {
		logger.info(e.getMessage());
		
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Producto resilience4j");
		producto.setPrecio(4500.00);
		producto.setCreateAt(new Date());
		item.setProducto(producto);
		return CompletableFuture.supplyAsync(() -> item);
	}
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear (@RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar (@RequestBody Producto producto, @PathVariable Long id) {
		
		return itemService.update(producto, id);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		itemService.delete(id);
	}
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto, @Value("${configuracion.texto}") String texto){
		Map<String, String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	
	
}
