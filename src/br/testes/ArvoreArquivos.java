package br.testes;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.sun.javafx.event.EventQueue;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;

public class ArvoreArquivos {

	public static void main(String[] args) {

		/// List<Integer> lista;

		/// Map<Integer, String> mapaBasico;

		Map<Cliente, List<Arquivo>> mapa = new HashMap<Cliente, List<Arquivo>>();

		for (int ik = 1; ik <= 100; ik++) {

			Cliente c = new Cliente();
			c.setIp("192.168.0." + ik);
			c.setNome("Cliente " + ik);
			c.setPorta(1818);

			List<Arquivo> lista = new ArrayList<>();
			for (int iv = 1; iv <= 100; iv++) {
				Arquivo arq = new Arquivo();
				arq.setNome("Arquivo " + iv);
				arq.setTamanho((long) (Math.random() * 100));
				lista.add(arq);
			}

			mapa.put(c, lista);
		}

		TableModel tb = new MeuModelo(mapa);

		JFrame jf = new JFrame();
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		JTable jt = new JTable(tb);
		
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(jt);
		jp.add(sp, BorderLayout.CENTER);
		jf.setContentPane(jp);

		jf.setBounds(0, 0, 640, 480);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SwingUtilities.invokeLater(() -> jf.setVisible(true));

	}
}
