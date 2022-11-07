package br.ce.wcaquino.servicos;
 

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;
	
	// ajuda a encontrar vários erros ao mesmo tempo.
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test
	public void alugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		//acao		
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		
		}


	@Test(expected = FilmeSemEstoqueException.class)
	//(FORMA ELEGANTE) DE REALIZAR OS TESTES
	public void lancarExcecaoFilmeSemEstoque() throws Exception {
				
		//cenario	
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
				
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
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	
	@Test
	//(FORMA NOVA) DE REALIZAR OS TESTES ** ESTÁ É A FORMA MAIS RECOMENDADA
	public void naoDeveAlugarFilmeSemFilme () throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();;

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
		Usuario usuario = umUsuario().agora();;
		List<Filme> filmes = Arrays.asList(umFilme().agora());
						
		//AÇÃO
		Locacao retorno = service.alugarFilme(usuario, filmes);
						
		//VERIFICAÇÃO
		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		//CENÁRIO
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		exception.expectMessage("Usuário Negativado");
		
		//AÇÃO
		try {
			service.alugarFilme(usuario, filmes);
		//VERIFICAÇÃO
			Assert.fail();
		} catch (FilmeSemEstoqueException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}
				
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//CENARIO
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuário em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
;				List<Locacao> locacoes = Arrays.asList(
			umLocacao().atrasado().comUsuario(usuario).agora(),
			umLocacao().comUsuario(usuario2).agora(),
			umLocacao().atrasado().comUsuario(usuario3).agora(),
			umLocacao().atrasado().comUsuario(usuario3).agora());
			when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//AÇÃO
		service.notificarAtraso();
		
		//VERIFICAÇÃO
		verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		verify(email).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		//Mockito.verifyZeroInteractions(spc);
		
	}
	
	@Test
	public void deveTratarErronoSPC() throws Exception {
		//CENARIO
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha"));
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
//		exception.expectMessage("Falha");
				
		//AÇÃO
		service.alugarFilme(usuario, filmes);
		
		//VERIFICAÇÃO
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		//CENARIO
		Locacao locacao = umLocacao().agora();
				
		//AÇÃO
		service.prorrogarLocacao(locacao, 3);
		
		//VERIFICAÇÃO
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
	
	
}








