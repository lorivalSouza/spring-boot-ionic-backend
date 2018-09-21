package com.lorival.cursomc.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lorival.cursomc.domain.Estado;
import com.lorival.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repo;
	
	
	public List<Estado> findAllByOrderByNome() {
		return repo.findAllByOrderByNome();
	}
	
}
