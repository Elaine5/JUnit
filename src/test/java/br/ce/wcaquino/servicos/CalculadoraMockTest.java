package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;

public class CalculadoraMockTest {

	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		//EXEMPLOS
		//Mockito.when(calc.somar(1, 2)).thenReturn(5);
		//Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(1, 8));
	}
}
