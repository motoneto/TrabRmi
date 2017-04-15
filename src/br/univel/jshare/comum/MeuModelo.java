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
				matriz[linha][1] = arq.getExtensao();
				matriz[linha][2] = arq.getPath();
				matriz[linha][3] = arq.getTamanho();
				matriz[linha][4] = arq.getMd5();
				matriz[linha][5] = e.getKey().getNome();
				matriz[linha][6] = e.getKey().getIp();
				matriz[linha][7] = e.getKey().getPorta();
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
		case 5: return "Portador";
		case 6: return "IP";
		case 7: return "Porta";
		case 2: return "Path";
		case 1: return "Extensão";
		case 3: return "Tamanho";
		case 4: return "Hash";
		}
		
		return null;
	}

}
