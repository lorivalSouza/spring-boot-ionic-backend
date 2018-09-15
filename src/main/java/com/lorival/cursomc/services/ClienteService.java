package com.lorival.cursomc.services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lorival.cursomc.domain.Cidade;
import com.lorival.cursomc.domain.Cliente;
import com.lorival.cursomc.domain.Endereco;
import com.lorival.cursomc.domain.enums.Perfil;
import com.lorival.cursomc.domain.enums.TipoCliente;
import com.lorival.cursomc.dto.ClienteDTO;
import com.lorival.cursomc.dto.ClienteNewDTO;
import com.lorival.cursomc.repositories.ClienteRepository;
import com.lorival.cursomc.repositories.EnderecoRepository;
import com.lorival.cursomc.security.UserSS;
import com.lorival.cursomc.sevices.exceptions.AuthorizationException;
import com.lorival.cursomc.sevices.exceptions.DataIntegrityException;
import com.lorival.cursomc.sevices.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository ;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3service;
	
	@Transactional
	public Cliente insert(Cliente obj) {
		//Garante que o id é null então é um objeto novo, se tiver valor será uma atualização e não uma insersão
		obj.setId(null);
		repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
		
	}
	
	public Cliente find(Integer id) {	
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = repo.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo : " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		//Verifica se Id existe
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir o cliente há pedidos relacionados.");
		}
		
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromClienteDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
		
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null ) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null ) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	//metodo auxiliar so vai ser usado dentro da classe por isso private
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
	
	//método para enviar foto do perfil do cliente
	public URI uploadPicture(MultipartFile multipartFile) {
		
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		
		URI uri = s3service.uploadFile(multipartFile);
		
		Cliente cli = repo.findOne(user.getId());
		cli.setImageUrl(uri.toString());
		repo.save(cli);
		
		return uri;
	}
	
	

}
