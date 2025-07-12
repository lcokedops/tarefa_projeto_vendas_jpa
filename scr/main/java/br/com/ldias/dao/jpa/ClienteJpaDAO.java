package br.com.ldias.dao.jpa;

import br.com.ldias.dao.generic.jpa.GenericJpaDAO;
import br.com.ldias.domain.jpa.ClienteJpa;

public class ClienteJpaDAO extends GenericJpaDAO<ClienteJpa, Long> implements IClienteJpaDAO {
	public ClienteJpaDAO() {
		super(ClienteJpa.class);
	}
}
