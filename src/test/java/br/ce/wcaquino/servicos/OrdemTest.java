package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Assert;
import org.junit.FixMethodOrder;



//organizando a ordem dos testes


@FixMethodOrder(MethodSorters.NAME_ASCENDING) //ORDEM ALFABÉTICA
public class OrdemTest {

	public static int contador = 0;
	
	@Test
	public void inicia() {
		contador = 1;
	}
	
	@Test
	public void verifica() {
		Assert.assertEquals(1, contador);
	}
	
	
	//organizando a ordem dos testes (PORÉM NÃO É O MAIS RECOMENDADO)
	/**@Test
	public void testGeral() {
		inicia();
		verifica();
	}*/
}
