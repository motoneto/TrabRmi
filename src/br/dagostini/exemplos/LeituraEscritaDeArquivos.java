package br.dagostini.exemplos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LeituraEscritaDeArquivos {

	public LeituraEscritaDeArquivos(File f) {

		// 2 - Ao receber um parâmetro f lê seu conteúdo e o coloca na memória.
		// Todo o conteúdo do arquivo se encontra no array de bytes agora.
		byte[] dados = leia(f);

		// 3 - Pega o array de bytes e escreve em num novo arquivo.
		escreva(new File("Cópia de " + f.getName()), dados);
	}

	public byte[] leia(File arq) {
		Path path = Paths.get(arq.getPath());
		try {
			byte[] dados = Files.readAllBytes(path);
			return dados;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void escreva(File arq, byte[] dados) {
		try {
			Files.write(Paths.get(arq.getPath()), dados, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {

		// 1 - Instancia um objeto da classe LerArquivo.
		new LeituraEscritaDeArquivos(new File("logo.png"));
	}

}
