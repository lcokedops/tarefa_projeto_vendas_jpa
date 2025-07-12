package br.com.ldias.dao.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ldias.dao.Persistente;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public interface IGenericDAO <T extends Persistente, E extends Serializable>{
	
	 public Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
	 
	 public void excluir(E valor) throws DAOException;
	 
	 public void alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
	 
	 public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException;
	 
	 public Collection<T> buscarTodos() throws DAOException;
}
