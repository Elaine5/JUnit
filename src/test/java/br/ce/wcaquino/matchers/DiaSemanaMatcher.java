package br.ce.wcaquino.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import java.util.Date;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

	private Integer diaSemana;
	
	public DiaSemanaMatcher(Integer diaSemana) {
		
	}
		
	public void describeTo(Description arg0) {
	
	}

	@Override
	protected boolean matchesSafely(Date arg0) {
		
		return false;
	}

}
