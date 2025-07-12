package br.com.ldias.services;


import br.com.ldias.domain.Cliente;
import br.com.ldias.exception.DAOException;
import br.com.ldias.services.generic.IGenericService;

public interface IClienteService extends IGenericService<Cliente, Long> {
	
	Cliente buscarPorCPF(Long cpf) throws DAOException;
	
}
