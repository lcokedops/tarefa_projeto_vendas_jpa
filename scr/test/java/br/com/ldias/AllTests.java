package br.com.ldias;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.ldias.jpa.ClienteJpaDAOTest;
import br.com.ldias.jpa.ProdutoJpaDAOTest;
import br.com.ldias.jpa.VendaJpaDAOTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	ClienteJpaDAOTest.class, ProdutoJpaDAOTest.class, VendaJpaDAOTest.class, 
	ClienteDAOTest.class, ProdutoDAOTest.class, VendaDAOTest.class,
	ClienteServiceTest.class, ProdutoServiceTest.class
})
public class AllTests {
	
}
