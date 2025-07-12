package br.com.ldias.services;

import br.com.ldias.dao.IProdutoDAO;
import br.com.ldias.domain.Produto;
import br.com.ldias.services.generic.GenericService;

public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {
	
	public ProdutoService(IProdutoDAO dao) {
		super(dao);
	}

}
