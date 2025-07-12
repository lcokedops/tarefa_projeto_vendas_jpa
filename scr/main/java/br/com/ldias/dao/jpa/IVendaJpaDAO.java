package br.com.ldias.dao.jpa;

import br.com.ldias.dao.generic.jpa.IGenericJpaDAO;
import br.com.ldias.domain.jpa.VendaJpa;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public interface IVendaJpaDAO extends IGenericJpaDAO<VendaJpa, Long>{
	public void finalizarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public VendaJpa consultarComCollection(Long id);
}
