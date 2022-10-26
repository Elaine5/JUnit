package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

//TESTE PARAMETRIZÁVEIS
//TÉCNICA DATA DRIVE TEST (TESTES ORIENTADOS A DADOS)

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	private LocacaoService service;
	
	@Before
	public void setup() {
		
		 service = new LocacaoService();
	}	
	
	private static Filme aventuras = new Filme("Aventuras", 5, 4.0);
	private static Filme sucesso = new Filme("Sucesso", 5, 4.0);
	private static Filme terror = new Filme("Terror", 5, 4.0);
	private static Filme comedia = new Filme("Comedia", 5, 4.0);
	private static Filme amor = new Filme("Amor", 5, 4.0);
	private static Filme amorFinal = new Filme("AmorFinal", 5, 4.0);
	private static Filme aventurasFinal = new Filme("aventurasFinal", 5, 4.0);
	
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(aventuras, sucesso), 8.0, "2 Filmes: Sem Desconto"},
			{Arrays.asList(aventuras, sucesso, terror), 11.0, "3 Filmes: 25%"},
			{Arrays.asList(aventuras, sucesso, terror, comedia), 13.0, "4 Filmes: 50%"},
			{Arrays.asList(aventuras, sucesso, terror, comedia, amor), 14.0, "5 Filmes: 75%"},
			{Arrays.asList(aventuras, sucesso, terror, comedia, amor, amorFinal), 14.0, "6 Filmes: 100%"},
			{Arrays.asList(aventuras, sucesso, terror, comedia, amor, amorFinal, aventurasFinal), 18.0, "7 Filmes: Sem Desconto"},
		});
	}
	
	@Test
	public void calcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
			
		assertThat(resultado.getValor(), is(valorLocacao));	
	}
}
