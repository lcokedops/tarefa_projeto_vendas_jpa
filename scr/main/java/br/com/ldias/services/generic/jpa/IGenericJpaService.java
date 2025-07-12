package br.com.ldias.services.generic.jpa;

import java.io.Serializable;
import java.util.Collection;

import br.com.ldias.dao.Persistente;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public interface IGenericJpaService <T extends Persistente, E extends Serializable> {
	
	public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void excluir(T entity) throws DAOException;
	
	public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
	
	public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException;
	
	public Collection<T> buscarTodos() throws DAOException;
}
