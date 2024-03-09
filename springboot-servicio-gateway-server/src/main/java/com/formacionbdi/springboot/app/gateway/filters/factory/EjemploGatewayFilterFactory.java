package com.formacionbdi.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion>{

	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
	
	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
	}
	
	public class Configuracion {

	
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
	}


	@Override
	public GatewayFilter apply(Configuracion config) {
		
		return (exchange, chain) ->{
		
			logger.info("Ejecutando filtro pre: " +config.mensaje);
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				Optional.ofNullable(config.cookieValor).ifPresent(cookie ->{
					exchange.getResponse().
					addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				
				logger.info("Ejecutando filtro post: " +config.mensaje);
			}));
		};
	}


	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "EjemploCookie";
	}


	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("manesaje", "cookieNombre", "cookieValor");
	}

	
	
}
