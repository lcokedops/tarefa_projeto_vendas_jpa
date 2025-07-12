package br.com.ldias.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ldias.dao.VendaExclusaoJpaDAO;
import br.com.ldias.dao.jpa.ClienteJpaDAO;
import br.com.ldias.dao.jpa.IClienteJpaDAO;
import br.com.ldias.dao.jpa.IProdutoJpaDAO;
import br.com.ldias.dao.jpa.IVendaJpaDAO;
import br.com.ldias.dao.jpa.ProdutoJpaDAO;
import br.com.ldias.dao.jpa.VendaJpaDAO;
import br.com.ldias.domain.jpa.ClienteJpa;
import br.com.ldias.domain.jpa.ProdutoJpa;
import br.com.ldias.domain.jpa.VendaJpa;
import br.com.ldias.domain.jpa.VendaJpa.Status;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public class VendaJpaDAOTest {
	
	private IVendaJpaDAO vendaDAO;
	
	private IVendaJpaDAO vendaExclusaoDAO;
	
	private IClienteJpaDAO clienteDAO;
	
	private IProdutoJpaDAO produtoDAO;
	
	private Random rd;
	
	private ClienteJpa cliente;
	
	private ProdutoJpa produto;
	
	public VendaJpaDAOTest() {
		this.vendaDAO = new VendaJpaDAO();
		vendaExclusaoDAO = new VendaExclusaoJpaDAO();
		this.clienteDAO = new ClienteJpaDAO();
		this.produtoDAO = new ProdutoJpaDAO();
		rd = new Random();
	}
	
	@Before
	public void init() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		this.cliente = cadastrarCliente();
		this.produto = cadastrarProduto("A1", BigDecimal.TEN);
	}
	
	@After
	public void end() throws DAOException {
		excluirVendas();
		excluirProdutos();
		clienteDAO.excluir(this.cliente);
	}
	
	@Test
	public void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		VendaJpa venda = criarVenda("A1");
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		VendaJpa vendaConsultada = vendaDAO.consultar(venda.getId());
		Assert.assertNotNull(vendaConsultada);
		Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
	}
	
	@Test
	public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		VendaJpa venda = criarVenda("A2");
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		
		Assert.assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
		Assert.assertTrue(venda.getStatus().equals(Status.INICIADA));
		
		VendaJpa vendaConsultada = vendaDAO.consultar(venda.getId());
		Assert.assertTrue(vendaConsultada.getId() != null);
		Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
	} 
	
	@Test
	public void cancelarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A3";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		retorno.setStatus(Status.CANCELADA);
		vendaDAO.cancelarVenda(venda);
		
		VendaJpa vendaConsultada = vendaDAO.consultar(venda.getId());
		Assert.assertEquals(codigoVenda, vendaConsultada.getCodigo());
		Assert.assertEquals(Status.CANCELADA, vendaConsultada.getStatus());
	}
	
	@Test
	public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A4";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(produto, 1);
		
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		Assert.assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A5";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		Assert.assertNotNull(prod);
		Assert.assertEquals(codigoVenda, prod.getCodigo());
		
		//TODO Usando este método apra evitar a exception org.hibernate.LazyInitializationException
		// Ele busca todos os dados da lista pois a mesma por default é lazy
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		Assert.assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	} 
	
	@Test(expected = DAOException.class)
	public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
		VendaJpa venda = criarVenda("A6");
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		
		VendaJpa retorno1 = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno1);
		Assert.assertTrue(venda.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A7";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		Assert.assertNotNull(prod);
		Assert.assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerProduto(prod, 1);
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
		valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		Assert.assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	} 
	
	@Test
	public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A8";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		Assert.assertNotNull(prod);
		Assert.assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerProduto(prod, 1);
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
		valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		Assert.assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A9";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		Assert.assertNotNull(prod);
		Assert.assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerTodosProdutos();
		Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
		Assert.assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
		Assert.assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A10";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		venda.setStatus(Status.CONCLUIDA);
		vendaDAO.finalizarVenda(venda);
		
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
		Assert.assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
	}
	
	@Test(expected = AssertionError.class)
	public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A11";
		VendaJpa venda = criarVenda(codigoVenda);
		VendaJpa retorno = vendaDAO.cadastrar(venda);
		Assert.assertNotNull(retorno);
		Assert.assertNotNull(venda);
		Assert.assertEquals(codigoVenda, venda.getCodigo());
		
		vendaDAO.finalizarVenda(venda);
		VendaJpa vendaConsultada = vendaDAO.consultarComCollection(venda.getId());
		Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
		Assert.assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
		
		vendaConsultada.adicionarProduto(this.produto, 1);
		
	}
	
	private void excluirProdutos() throws DAOException {
		Collection<ProdutoJpa> list = this.produtoDAO.buscarTodos();
		list.forEach(prod -> {
			try {
				this.produtoDAO.excluir(prod);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void excluirVendas() throws DAOException {
		Collection<VendaJpa> list = this.vendaDAO.buscarTodos();
		list.forEach(v -> {
			try {
				 this.vendaExclusaoDAO.excluir(v);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private ProdutoJpa cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		ProdutoJpa produto = new ProdutoJpa();
		produto.setCodigo(codigo);
		produto.setDescricao("Produto 1");
		produto.setNome("Produto 1");
		produto.setValor(valor);
		produtoDAO.cadastrar(produto);
		return produto;
	}
	
	private ClienteJpa cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
		ClienteJpa cliente = new ClienteJpa();
		cliente.setCpf(rd.nextLong());
		cliente.setNome("Leonardo");
		cliente.setCidade("Santo André");
		cliente.setEnd("End");
		cliente.setEstado("SP");
		cliente.setNumero(10);
		cliente.setTel(1199999999L);
		clienteDAO.cadastrar(cliente);
		return cliente;
	}
	
	private VendaJpa criarVenda(String codigo) {
		VendaJpa venda = new VendaJpa();
		venda.setCodigo(codigo);
		venda.setDataVenda(Instant.now());
		venda.setCliente(this.cliente);
		venda.setStatus(Status.INICIADA);
		venda.adicionarProduto(this.produto, 2);
		return venda;
	}
	
}
