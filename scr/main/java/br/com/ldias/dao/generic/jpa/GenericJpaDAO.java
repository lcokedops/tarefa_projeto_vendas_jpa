package br.com.ldias.dao.generic.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.ldias.dao.Persistente;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.MaisDeUmRegistroException;
import br.com.ldias.exception.TableException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public abstract class GenericJpaDAO<T extends Persistente, E extends Serializable> implements IGenericJpaDAO<T,E> {


    protected EntityManagerFactory entityManagerFactory;
    
    protected EntityManager entityManager;
    
    private Class<T> persistenteClass;

    public GenericJpaDAO(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    @Override
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
    	openConnection();
    	entityManager.persist(entity);
    	entityManager.getTransaction().commit();
    	closeConnection();
    	return entity;
    }


    @Override
    public void excluir(T entity) throws DAOException {
    	openConnection();
    	entity = entityManager.merge(entity);
    	entityManager.remove(entity);
    	entityManager.getTransaction().commit();
    	closeConnection();
	}

    @Override
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
    	openConnection();
    	entity = entityManager.merge(entity);
    	entityManager.getTransaction().commit();
    	closeConnection();
    	return entity;
    }

    @Override
    public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException {
    	openConnection();
    	T entity = entityManager.find(this.persistenteClass, valor);
    	entityManager.getTransaction().commit();
    	return entity;
    }
    
    @Override
    public Collection<T> buscarTodos() throws DAOException {
		openConnection();
		List<T> list = entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
		closeConnection();
		return list;
    }
	
	protected void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}
	
	protected void closeConnection() {
		entityManager.close();
		entityManagerFactory.close();
	}
	
	private String getSelectSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM ");
		sb.append(this.persistenteClass.getSimpleName());
		sb.append(" obj");
		return sb.toString();
	}
	
}