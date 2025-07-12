package br.com.ldias.jpa;

import java.util.Collection;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import br.com.ldias.dao.jpa.ClienteJpaDAO;
import br.com.ldias.dao.jpa.IClienteJpaDAO;
import br.com.ldias.domain.jpa.ClienteJpa;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public class ClienteJpaDAOTest {
	
	private IClienteJpaDAO clienteJpaDAO;
	
	private Random rd;
	
	public ClienteJpaDAOTest() {
		clienteJpaDAO = new ClienteJpaDAO();
		rd = new Random();
	}
	
	@After
	public void end() throws DAOException {
		Collection<ClienteJpa> list = clienteJpaDAO.buscarTodos();
		list.forEach(cli -> {
			try {
				clienteJpaDAO.excluir(cli);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Test
	public void pesquisarCliente() throws MaisDeUmRegistroException, TableException, TipoChaveNaoEncontradaException, DAOException {
		ClienteJpa cliente = criarCliente();
		clienteJpaDAO.cadastrar(cliente);
		
		ClienteJpa clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNotNull(clienteConsultado);
	}
	
	@Test
	public void salvarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		ClienteJpa cliente = criarCliente();
		ClienteJpa retorno = clienteJpaDAO.cadastrar(cliente);
		Assert.assertNotNull(retorno);
		
		ClienteJpa clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNotNull(clienteConsultado);
		
		clienteJpaDAO.excluir(cliente);
		
		ClienteJpa clienteConsultado1 = clienteJpaDAO.consultar(retorno.getId());
		Assert.assertNull(clienteConsultado1);
	}
	
	@Test
	public void excluirCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		ClienteJpa cliente = criarCliente();
		ClienteJpa retorno = clienteJpaDAO.cadastrar(cliente);
		Assert.assertNotNull(retorno);
		
		ClienteJpa clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNotNull(clienteConsultado);
		
		clienteJpaDAO.excluir(cliente);
		clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNull(clienteConsultado);
	}
	
	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
		ClienteJpa cliente = criarCliente();
		ClienteJpa retorno = clienteJpaDAO.cadastrar(cliente);
		Assert.assertNotNull(retorno);
		
		ClienteJpa clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNotNull(clienteConsultado);
		
		clienteConsultado.setNome("Leonardo Dias");
		clienteJpaDAO.alterar(clienteConsultado);
		
		ClienteJpa clienteAlterado = clienteJpaDAO.consultar(clienteConsultado.getId());
		Assert.assertNotNull(clienteAlterado);
		Assert.assertEquals("Leonardo Dias", clienteAlterado.getNome());
		
		clienteJpaDAO.excluir(cliente);
		clienteConsultado = clienteJpaDAO.consultar(cliente.getId());
		Assert.assertNull(clienteConsultado);
	}
	
	@Test
	public void buscarTodos() throws TipoChaveNaoEncontradaException, DAOException {
		ClienteJpa cliente = criarCliente();
		ClienteJpa retorno = clienteJpaDAO.cadastrar(cliente);
		Assert.assertNotNull(retorno);
		
		ClienteJpa cliente1 = criarCliente();
		ClienteJpa retorno1 = clienteJpaDAO.cadastrar(cliente1);
		Assert.assertNotNull(retorno1);
		
		Collection<ClienteJpa> list = clienteJpaDAO.buscarTodos();
		Assert.assertTrue(list != null);
		Assert.assertTrue(list.size() == 2);
		
		list.forEach(cli -> {
			try {
				clienteJpaDAO.excluir(cli);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
		
		Collection<ClienteJpa> list1 = clienteJpaDAO.buscarTodos();
		Assert.assertTrue(list1 != null);
		Assert.assertTrue(list1.size() == 0);
	}
	
	private ClienteJpa criarCliente() {
		ClienteJpa cliente = new ClienteJpa();
		cliente.setCpf(rd.nextLong());
		cliente.setNome("Leonardo");
		cliente.setCidade("Santo André");
		cliente.setEnd("End");
		cliente.setEstado("SP");
		cliente.setNumero(10);
		cliente.setTel(11900000000l);
		return cliente;
	}
	
}
