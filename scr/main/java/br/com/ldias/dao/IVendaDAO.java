package br.com.ldias.dao;

import br.com.ldias.dao.generic.IGenericDAO;
import br.com.ldias.domain.Venda;
import br.com.ldias.exception.DAOException;
import br.com.ldias.exception.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {
	
	public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
}
