package br.univel.jshare.comum;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class MeuModelo extends AbstractTableModel implements TableModel {

	
	private Object[][] matriz;
	private int linhas;
	
	public MeuModelo(Map<Cliente, List<Arquivo>> mapa) {
		
		linhas = 0;
		for (Entry<Cliente, List<Arquivo>> e : mapa.entrySet()) {
			linhas+=e.getValue().size();
		}
		
		matriz = new Object[linhas][8];
		
		int linha = 0;
		
		for (Entry<Cliente, List<Arquivo>> e : mapa.entrySet()) {
			for (Arquivo arq : e.getValue()) {
				matriz[linha][0] = arq.getNome();
				matriz[linha][1] = e.getKey().getNome();
				matriz[linha][2] = e.getKey().getIp();
				matriz[linha][3] = e.getKey().getPorta();
				matriz[linha][4] = arq.getPath();
				matriz[linha][5] = arq.getExtensao();
				matriz[linha][6] = arq.getTamanho();
				matriz[linha][7] = arq.getMd5();
				linha++;
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public int getRowCount() {
		return linhas;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return matriz[rowIndex][columnIndex];
	}
	
	public String getColumnName(int i){
		
		switch(i){
		case 0: return "Nome Arquivo";
		case 1: return "Nome Cliente";
		case 2: return "IP";
		case 3: return "Porta";
		case 4: return "Path";
		case 5: return "Extensão";
		case 6: return "Tamanho";
		case 7: return "Hash";
		}
		
		return null;
	}

}
