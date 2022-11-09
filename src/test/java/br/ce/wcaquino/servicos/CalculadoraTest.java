package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;


@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando...");
	}
	
	@After
	public void tearDown() {
		System.out.println("finalizando...");
	}

	@Test
	public void deveSomarDoisValores() {
		//cenario		
		int a = 5;
		int b = 3;
		
		//acao
		int resultado = calc.somar(a, b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubritairTresValores() {
		//cenario		
		int a = 5;
		int b = 3;
		int c = 2;
		
		//acao
		int resultado2 = calc.subtrair(a, b, c);
		
		//verificacao
		Assert.assertEquals(0, resultado2);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario		
		int a = 10;
		int b = 2;
		
		//acao
		int resultado3 = calc.dividir(a, b);
		
		//verificacao
		Assert.assertEquals(5, resultado3);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void lancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 0;
		
		calc.dividir(a, b);
	}
	
	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";
		
		int resultado = calc.divide(a, b);
		
		Assert.assertEquals(2, resultado);
	}
}











