package com.lorival.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lorival.cursomc.domain.Cidade;
import com.lorival.cursomc.domain.Estado;
import com.lorival.cursomc.dto.CidadeDTO;
import com.lorival.cursomc.dto.EstadoDTO;
import com.lorival.cursomc.services.CidadeService;
import com.lorival.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {		
		List<Estado> list = estadoService.findAllByOrderByNome();
		List<EstadoDTO> objDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(objDto);
	}
	
	@RequestMapping(value="/{estadoId}/cidades", method=RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> find(@PathVariable Integer estadoId) {		
		List<Cidade> list = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> objDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(objDto);
	}

}
