package br.dagostini.exemplos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.dagostini.jshare.comum.pojos.Diretorio;
import br.univel.jshare.comum.Arquivo;

public class ListarDiretoriosArquivos {

	public static void main(String[] args) {

		File dirStart = new File("." + File.separatorChar);

		List<Arquivo> listaArquivos = new ArrayList<>();
		List<Diretorio> listaDiretorios = new ArrayList<>();
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setNome(file.getName());
				arq.setTamanho(file.length());
				listaArquivos.add(arq);
			} else {
				Diretorio dir = new Diretorio();
				dir.setNome(file.getName());
				listaDiretorios.add(dir);				
			}
		}

		System.out.println("Diretórios");
		for (Diretorio dir : listaDiretorios) {
			System.out.println("\t" + dir.getNome());
		}
		
		System.out.println("Arquivos");
		for (Arquivo arq : listaArquivos) {
			System.out.println("\t" + arq.getTamanho() + "\t" + arq.getNome());
		}

	}
}
