package br.testes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testes {

	
	public static void main(String[] args) {
		
		Map<String, List<String>> mapa = new HashMap<>();
		
		List<String> lista1 = new ArrayList<>();
		lista1.add("v1");
		lista1.add("v2");
		
		mapa.put("um", lista1);
		
		
		// erro mapa.put("dois", "Aluno 2");
		
	}
	
}
