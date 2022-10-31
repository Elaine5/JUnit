package br.ce.wcaquino.servicos;
 

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuaio;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
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
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		
		Usuario usuario = umUsuaio().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		
		//error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		//error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
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
		Usuario usuario = umUsuaio().agora();;
		List<Filme> filmes = Arrays.asList(umFilme().agora());
				
		//acao				
		service.alugarFilme(usuario, filmes);		
	}
	
	@Test
	//(FORMA ROUBUSTA) DE REALIZAR OS TESTES
	public void naoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//cenario

		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		}catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		//System.out.println("Forma robusta");
	}
	
	@Test
	//(FORMA NOVA) DE REALIZAR OS TESTES ** ESTÁ É A FORMA MAIS RECOMENDADA
	public void naoDeveAlugarFilmeSemFilme () throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		

		Usuario usuario = umUsuaio().agora();;

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		service.alugarFilme(usuario, null);

	}
	/**
	//ADICIONANDO DESCONTOS 25%
	@Test
	public void pagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuaio().agora();;
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
		Usuario usuario = umUsuaio().agora();;
		List<Filme> filmes = Arrays.asList(
				new Filme("Aventuras", 5, 4.0), new Filme("Sucesso", 5, 4.0), new Filme("Terror", 5, 4.0), new Filme("Comedia", 5, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2=13
		
		assertThat(resultado.getValor(), is(13.0));
			
		}
		//ADICIONANDO DESCONTOS 75%
		@Test
		public void pagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
			//cenario
			Usuario usuario = umUsuaio().agora();;
			List<Filme> filmes = Arrays.asList(
					new Filme("Aventuras", 5, 4.0), new Filme("Sucesso", 5, 4.0), 
					new Filme("Terror", 5, 4.0), new Filme("Comedia", 5, 4.0), 
					new Filme("Amor", 5, 4.0));
			
			//acao
			Locacao resultado = service.alugarFilme(usuario, filmes);
			
			//verificacao
			//4+4+3+2+1=14
			
			assertThat(resultado.getValor(), is(14.0));	
		}
		//ADICIONANDO DESCONTOS 75%
		@Test
		public void pagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
			//cenario
			Usuario usuario = umUsuaio().agora();;
			List<Filme> filmes = Arrays.asList(
					new Filme("Aventuras", 5, 4.0), new Filme("Sucesso", 5, 4.0), 
					new Filme("Terror", 5, 4.0), new Filme("Comedia", 5, 4.0), 
					new Filme("Amor", 5, 4.0), new Filme("AmorFinal", 5, 4.0));
			
			//acao
			Locacao resultado = service.alugarFilme(usuario, filmes);
			
			//verificacao
			//4+4+3+2+1=14
			
			assertThat(resultado.getValor(), is(14.0));	
		}*/
		
		@Test
		public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
			Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
			
			//ESTE TESTE FUNCIONA APENAS NO SABADO
			//CENARIO
			Usuario usuario = umUsuaio().agora();;
			List<Filme> filmes = Arrays.asList(umFilme().agora());
						
			//AÇÃO
			Locacao retorno = service.alugarFilme(usuario, filmes);
						
			//VERIFICAÇÃO
			assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
			assertThat(retorno.getDataRetorno(), caiNumaSegunda());
			
			//asserttThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
			// asserttThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		}

	

}








