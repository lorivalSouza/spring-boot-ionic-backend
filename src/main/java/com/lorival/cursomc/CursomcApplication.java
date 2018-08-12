package com.lorival.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lorival.cursomc.domain.Categoria;
import com.lorival.cursomc.domain.Cidade;
import com.lorival.cursomc.domain.Cliente;
import com.lorival.cursomc.domain.Endereco;
import com.lorival.cursomc.domain.Estado;
import com.lorival.cursomc.domain.ItemPedido;
import com.lorival.cursomc.domain.Pagamento;
import com.lorival.cursomc.domain.PagamentoComBoleto;
import com.lorival.cursomc.domain.PagamentoComCartao;
import com.lorival.cursomc.domain.Pedido;
import com.lorival.cursomc.domain.Produto;
import com.lorival.cursomc.domain.enums.EstadoPagamento;
import com.lorival.cursomc.domain.enums.TipoCliente;
import com.lorival.cursomc.repositories.CategoriaRepository;
import com.lorival.cursomc.repositories.CidadeRepository;
import com.lorival.cursomc.repositories.ClienteRepository;
import com.lorival.cursomc.repositories.EnderecoRepository;
import com.lorival.cursomc.repositories.EstadoRepository;
import com.lorival.cursomc.repositories.ItemPedidoRepository;
import com.lorival.cursomc.repositories.PagamentoRepository;
import com.lorival.cursomc.repositories.PedidoRepository;
import com.lorival.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	Categoria cat1 = new Categoria(null, "Informática");
	Categoria cat2 = new Categoria(null, "Escritório");
	
	Produto p1 = new Produto(null, "Computador", 2000.00);
	Produto p2 = new Produto(null, "Impressora", 800.00);
	Produto p3 = new Produto(null, "Mouse", 80.00);
	
	cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
	cat2.getProdutos().addAll(Arrays.asList(p2));
	
	p1.getCategorias().addAll(Arrays.asList(cat1));
	p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
	p3.getCategorias().addAll(Arrays.asList(cat1));
	
	categoriaRepository.save(Arrays.asList(cat1, cat2));
	produtoRepository.save(Arrays.asList(p1, p2, p3));
	
	Estado est1 = new Estado(null, "Minas Gerais");
	Estado est2 = new Estado(null, "São Paulo");
	
	Cidade c1 =  new Cidade(null, "Uberlândia", est1);
	Cidade c2 =  new Cidade(null, "São Paulo", est2);
	Cidade c3 =  new Cidade(null, "Campinas", est2);
	
	est1.getCidades().addAll(Arrays.asList(c1));
	est2.getCidades().addAll(Arrays.asList(c2, c3));
	
	estadoRepository.save(Arrays.asList(est1, est2));
	cidadeRepository.save(Arrays.asList(c1, c2, c3));
	
	Cliente cli1 = new Cliente(null, "Maria da Silva", "mariasilva@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
	
	cli1.getTelefones().addAll(Arrays.asList("27363323", "99998585"));
	
	Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apyo 303", "Jardim", "38220834", cli1, c1);
	Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "3877012", cli1, c2);
	
	cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
			
	clienteRepository.save(Arrays.asList(cli1));
	enderecoRepository.save(Arrays.asList(e1,e2));
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
	
	Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
	
	Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
	
	ped1.setPagamento(pagto1);
	Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
	ped2.setPagamento(pagto2);
	cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
	
	pedidoRepository.save(Arrays.asList(ped1, ped2));
	pagamentoRepository.save(Arrays.asList(pagto1, pagto2));
	
	ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
	ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
	ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
	

	ped1.getItens().addAll(Arrays.asList(ip1, ip2));
	ped2.getItens().addAll(Arrays.asList(ip3));
	
	p2.getItens().addAll(Arrays.asList(ip3));
	p3.getItens().addAll(Arrays.asList(ip3));
	
	itemPedidoRepository.save(Arrays.asList(ip1, ip2, ip3));
	
	}
}
