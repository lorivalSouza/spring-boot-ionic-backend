package com.lorival.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorival.cursomc.domain.Categoria;
import com.lorival.cursomc.repositories.CategoriaRepository;
import com.lorival.cursomc.sevices.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {		
		Categoria obj = repo.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo : " + Categoria.class.getName());
		}
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		//Garante que o id é null então é um objeto novo, se tiver valor será uma atualização e não uma insersão
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		//Verifica se Id existe
		find(obj.getId());
		return repo.save(obj);
	}

}
