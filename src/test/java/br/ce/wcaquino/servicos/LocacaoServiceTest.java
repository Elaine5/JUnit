package br.ce.wcaquino.servicos;
 

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
		
	private LocacaoService service;
	
	
	// ajuda a encontrar vários erros ao mesmo tempo.
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		
		 service = new LocacaoService();
	}
	
	
	@Test
	public void alugarFilme() throws Exception {
		//cenario
		
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));
		
		//acao
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
		}
		
		//verificacao
		
		/** 
		 *
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		 * @throws Exception 
		*/


	@Test(expected = FilmeSemEstoqueException.class)
	//(FORMA ELEGANTE) DE REALIZAR OS TESTES
	public void lancarExcecaoFilmeSemEstoque() throws Exception {
				
		//cenario	
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));
				
		//acao				
		service.alugarFilme(usuario, filmes);
		
	}
	
	@Test
	//(FORMA ROUBUSTA) DE REALIZAR OS TESTES
	public void naoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//cenario

		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));
		
		//acao
		
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		}catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		System.out.println("Forma robusta");
		
	}
	
	@Test
	//(FORMA NOVA) DE REALIZAR OS TESTES ** ESTÁ É A FORMA MAIS RECOMENDADA
	public void naoDeveAlugarFilmeSemFilme () throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		

		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		service.alugarFilme(usuario, null);

	}
	
	//ADICIONANDO DESCONTOS 25%
	@Test
	public void pagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Aventuras", 5, 4.0), new Filme("Sucesso", 5, 4.0), new Filme("Terror", 5, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3=11
		
		assertThat(resultado.getValor(), is(11.0));
		
	}
	
	//ADICIONANDO DESCONTOS 50%
		@Test
		public void pagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
			//cenario
			Usuario usuario = new Usuario("Usuario 1");
			List<Filme> filmes = Arrays.asList(new Filme("Aventuras", 5, 4.0), new Filme("Sucesso", 5, 4.0), new Filme("Terror", 5, 4.0), new Filme("Comedia", 5, 4.0));
			
			//acao
			Locacao resultado = service.alugarFilme(usuario, filmes);
			
			//verificacao
			//4+4+4+2=14
			
			assertThat(resultado.getValor(), is(14.0));
			
		}
}








