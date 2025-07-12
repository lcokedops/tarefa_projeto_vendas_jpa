package br.com.ldias.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ldias.dao.Persistente;
import br.com.ldias.dao.generic.IGenericDAO;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public abstract class GenericService<T extends Persistente, E extends Serializable> implements IGenericService<T, E> {
	
	protected IGenericDAO<T,E> dao;
	
	public GenericService(IGenericDAO<T,E> dao) {
		this.dao = dao;
	}

	@Override
	public Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
		return this.dao.cadastrar(entity);
	}
	
	@Override
	public void excluir(E valor) throws DAOException {
		this.dao.excluir(valor);
	}
	
	@Override
	public void alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
		this.dao.alterar(entity);
	}
	
	@Override
	public T consultar(E valor) throws DAOException {
		try {
			return this.dao.consultar(valor);
		} catch (MaisDeUmRegistroException | TableException e) {
			// TODO Auto-generated catch block
			//TODO levantar um erro genérico
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Collection<T> buscarTodos() throws DAOException {
		return this.dao.buscarTodos();
	}
	
}
