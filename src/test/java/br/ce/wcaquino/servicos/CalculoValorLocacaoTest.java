package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.ce.wcaquino.daos.LocacaoDAO;
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
	
	@InjectMocks
	private LocacaoService service;
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	
	@Before
	public void setup() {

	}	
	
	private static Filme aventuras = umFilme().agora();
	private static Filme sucesso = umFilme().agora();
	private static Filme terror = umFilme().agora();
	private static Filme comedia = umFilme().agora();
	private static Filme amor = umFilme().agora();
	private static Filme amorFinal = umFilme().agora();
	private static Filme aventurasFinal = umFilme().agora();
	
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
