package br.ce.wcaquino.servicos;
 

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

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
	public void testeLocacao() throws Exception {
		//cenario
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//acao
		
		Locacao locacao = service.alugarFilme(usuario, filme);
			
		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
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
	public void testLocacao_filmeSemEstoque() throws Exception {
		//cenario
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
				
		service.alugarFilme(usuario, filme);
		
	}
	
	@Test
	//(FORMA ROUBUSTA) DE REALIZAR OS TESTES
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		//cenario

		Filme filme = new Filme("Filme 1", 1, 5.0);
		
		//acao
		
		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		}catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		System.out.println("Forma robusta");
		
	}
	
	@Test
	//(FORMA NOVA) DE REALIZAR OS TESTES ** ESTÁ É A FORMA MAIS RECOMENDADA
	public void testLocacao_FilmeVazio () throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		

		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		service.alugarFilme(usuario, null);

	}
	
}








