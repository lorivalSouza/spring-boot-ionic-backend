package com.lorival.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorival.cursomc.domain.Categoria;
import com.lorival.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {		
		Categoria obj = repo.findOne(id);		
		return obj;
	}

}
