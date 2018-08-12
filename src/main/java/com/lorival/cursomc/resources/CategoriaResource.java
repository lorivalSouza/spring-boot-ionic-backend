package com.lorival.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lorival.cursomc.domain.Categoria;
import com.lorival.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {		
		Categoria obj = service.buscar(id);		
		return ResponseEntity.ok(obj);
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){
		obj = service.insert(obj);
		//Prepara para a proxima URI. Ex. se é categorias/1 ja traz com categorias/2 assim por diante, após salvar pega o getId() do objeto
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}
