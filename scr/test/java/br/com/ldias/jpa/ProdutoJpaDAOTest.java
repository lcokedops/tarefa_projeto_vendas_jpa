package br.com.ldias.jpa;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import br.com.ldias.dao.jpa.IProdutoJpaDAO;
import br.com.ldias.dao.jpa.ProdutoJpaDAO;
import br.com.ldias.domain.jpa.ProdutoJpa;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public class ProdutoJpaDAOTest {
	
	private IProdutoJpaDAO produtoJpaDAO;
	
	public ProdutoJpaDAOTest() {
		produtoJpaDAO = new ProdutoJpaDAO();
	}
	
	@After
	public void end() throws DAOException {
		Collection<ProdutoJpa> list = produtoJpaDAO.buscarTodos();
		list.forEach(prod -> {
			try {
				produtoJpaDAO.excluir(prod);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	
	@Test
	public void pesquisar() throws MaisDeUmRegistroException, TableException, DAOException, TipoChaveNaoEncontradaException {
		ProdutoJpa produto = criarProduto("A1");
		Assert.assertNotNull(produto);
		ProdutoJpa produtoDB = this.produtoJpaDAO.consultar(produto.getId());
		Assert.assertNotNull(produtoDB);
	}
	
	@Test
	public void salvar() throws TipoChaveNaoEncontradaException, DAOException {
		ProdutoJpa produto = criarProduto("A2");
		Assert.assertNotNull(produto);
	}
	
	@Test
	public void excluir() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
		ProdutoJpa produto = criarProduto("A3");
		Assert.assertNotNull(produto);
		this.produtoJpaDAO.excluir(produto);
		ProdutoJpa produtoBD = this.produtoJpaDAO.consultar(produto.getId());
		Assert.assertNull(produtoBD);
	}
	
	@Test
	public void alterar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		ProdutoJpa produto = criarProduto("A4");
		produto.setNome("Produto alterado");
		produtoJpaDAO.alterar(produto);
		ProdutoJpa produtoBD = this.produtoJpaDAO.consultar(produto.getId());
		Assert.assertNotNull(produtoBD);
		Assert.assertEquals("Produto alterado", produtoBD.getNome());
	}
	
	@Test
	public void buscarTodos() throws DAOException, TipoChaveNaoEncontradaException {
		criarProduto("A5");
		criarProduto("A6");
		Collection<ProdutoJpa> list = produtoJpaDAO.buscarTodos();
		Assert.assertTrue(list != null);
		Assert.assertTrue(list.size() == 2);
		
		for (ProdutoJpa prod : list) {
			this.produtoJpaDAO.excluir(prod);
		}
		
		list = produtoJpaDAO.buscarTodos();
		Assert.assertTrue(list != null);
		Assert.assertTrue(list.size() == 0);
		
	}
	
	private ProdutoJpa criarProduto(String codigo) throws TipoChaveNaoEncontradaException, DAOException {
		ProdutoJpa produto = new ProdutoJpa();
		produto.setCodigo(codigo);
		produto.setDescricao("Produto 1");
		produto.setNome("Produto 1");
		produto.setValor(BigDecimal.TEN);
		produtoJpaDAO.cadastrar(produto);
		return produto;
	}
	
}
