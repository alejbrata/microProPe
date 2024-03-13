package com.formacionbdi.springboot.app.usuarios.models.dao;



import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;



@RepositoryRestResource(path = "usuarios")
public interface UsarioDao extends PagingAndSortingRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);
	
	/**Misma forma de obtener usuario por nombre a través de JPA y consulta nativa a bbdd*/
	
//	@Query("select u from Usuario u where username = ?1")
//	public Usuario obtenerPorNombreUsuario(String username);
	
//	@Query(value = "select u from usuario u where username = ?1", nativeQuery = true)
//	public Usuario obtenerPorNombreUsuario(String username);
	
	
}
