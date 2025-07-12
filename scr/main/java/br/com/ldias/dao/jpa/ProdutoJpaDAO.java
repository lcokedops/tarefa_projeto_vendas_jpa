package br.com.ldias.dao.jpa;

import br.com.ldias.dao.generic.jpa.GenericJpaDAO;
import br.com.ldias.domain.jpa.ProdutoJpa;

public class ProdutoJpaDAO extends GenericJpaDAO<ProdutoJpa, Long> implements IProdutoJpaDAO {
	public ProdutoJpaDAO() {
		super(ProdutoJpa.class);
	}
}
