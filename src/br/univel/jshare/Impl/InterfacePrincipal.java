package br.univel.jshare.Impl;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.Md5Util;
import br.univel.jshare.comum.MeuModelo;
import br.univel.jshare.comum.TipoFiltro;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InterfacePrincipal extends JFrame implements IServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -668465204012771020L;
	private final Integer PORTA_SERVER = 1818;
	private JPanel contentPane;
	private JTextField fieldQuery;
	private JTextField fieldIp;
	private JTextField fieldPorta;
	private JTextArea fieldStatus;
	private JButton btnParar;
	private JButton btnConectar;
	private JButton btnSair;
	private JButton btnBaixar;
	// private JButton btnUpar;
	private JTextArea fieldStatusCliente;
	private JLabel lblTipofiltro;
	private JComboBox<TipoFiltro> comboTipoFiltro;
	private JLabel lblFiltro;
	private JTextField fieldFiltro;
	private JButton btnProcurar;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	Map<Cliente, List<Arquivo>> mapaArquivos = new HashMap<>();

	IServer conexaoServidor;
	Registry registryConexaoServidor;

	IServer conexaoCliente;
	Registry registryConexaoCliente;
	private JTable tbArquivos;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JButton btnIniciar;
	private Thread up;
	private JScrollPane scrollPane_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfacePrincipal frame = new InterfacePrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfacePrincipal() {
		mapaArquivos = new HashMap<>();

		setTitle(getClienteLocal().getIp());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		contentPane.add(splitPane, gbc_splitPane);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnIniciar = new JButton("Iniciar");
		btnIniciar.setEnabled(false);
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciaServico();
			}
		});
		GridBagConstraints gbc_btnIniciar = new GridBagConstraints();
		gbc_btnIniciar.fill = GridBagConstraints.BOTH;
		gbc_btnIniciar.gridheight = 2;
		gbc_btnIniciar.insets = new Insets(0, 0, 5, 5);
		gbc_btnIniciar.gridx = 0;
		gbc_btnIniciar.gridy = 0;
		panel.add(btnIniciar, gbc_btnIniciar);

		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.gridheight = 3;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		fieldStatus = new JTextArea();
		scrollPane_1.setViewportView(fieldStatus);
		fieldStatus.setEditable(false);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pararServico();
			}
		});

		scrollPane_3 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.gridheight = 3;
		gbc_scrollPane_3.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.gridx = 2;
		gbc_scrollPane_3.gridy = 0;
		panel.add(scrollPane_3, gbc_scrollPane_3);

		fieldStatusCliente = new JTextArea();
		scrollPane_3.setViewportView(fieldStatusCliente);
		fieldStatusCliente.setEditable(false);
		btnParar.setEnabled(false);
		GridBagConstraints gbc_btnParar = new GridBagConstraints();
		gbc_btnParar.fill = GridBagConstraints.BOTH;
		gbc_btnParar.insets = new Insets(0, 0, 0, 5);
		gbc_btnParar.gridx = 0;
		gbc_btnParar.gridy = 2;
		panel.add(btnParar, gbc_btnParar);

		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0, 75, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JLabel lblQuery = new JLabel("Query");
		GridBagConstraints gbc_lblQuery = new GridBagConstraints();
		gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuery.gridx = 0;
		gbc_lblQuery.gridy = 0;
		panel_1.add(lblQuery, gbc_lblQuery);

		fieldQuery = new JTextField();
		GridBagConstraints gbc_fieldQuery = new GridBagConstraints();
		gbc_fieldQuery.gridwidth = 7;
		gbc_fieldQuery.insets = new Insets(0, 0, 5, 5);
		gbc_fieldQuery.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldQuery.gridx = 1;
		gbc_fieldQuery.gridy = 0;
		panel_1.add(fieldQuery, gbc_fieldQuery);
		fieldQuery.setColumns(10);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectar();
			}
		});

		btnProcurar = new JButton("Procurar");
		btnProcurar.setEnabled(false);
		btnProcurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				procurarArquivo();
			}
		});
		GridBagConstraints gbc_btnProcurar = new GridBagConstraints();
		gbc_btnProcurar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnProcurar.insets = new Insets(0, 0, 5, 5);
		gbc_btnProcurar.gridx = 8;
		gbc_btnProcurar.gridy = 0;
		panel_1.add(btnProcurar, gbc_btnProcurar);

		JLabel lblip = new JLabel("IP");
		GridBagConstraints gbc_lblip = new GridBagConstraints();
		gbc_lblip.insets = new Insets(0, 0, 5, 5);
		gbc_lblip.gridx = 10;
		gbc_lblip.gridy = 0;
		panel_1.add(lblip, gbc_lblip);

		fieldIp = new JTextField();
		fieldIp.setText("localhost");
		GridBagConstraints gbc_fieldIp = new GridBagConstraints();
		gbc_fieldIp.insets = new Insets(0, 0, 5, 5);
		gbc_fieldIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldIp.gridx = 11;
		gbc_fieldIp.gridy = 0;
		panel_1.add(fieldIp, gbc_fieldIp);
		fieldIp.setColumns(10);
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConectar.insets = new Insets(0, 0, 5, 0);
		gbc_btnConectar.gridx = 12;
		gbc_btnConectar.gridy = 0;
		panel_1.add(btnConectar, gbc_btnConectar);

		// btnUpar = new JButton("Upar");
		// btnUpar.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// uparArquivos();
		// }
		// });
		// btnUpar.setEnabled(false);
		// GridBagConstraints gbc_btnUpar = new GridBagConstraints();
		// gbc_btnUpar.fill = GridBagConstraints.HORIZONTAL;
		// gbc_btnUpar.insets = new Insets(0, 0, 5, 0);
		// gbc_btnUpar.gridx = 5;
		// gbc_btnUpar.gridy = 1;
		// panel_1.add(btnUpar, gbc_btnUpar);

		lblTipofiltro = new JLabel("TipoFiltro");
		GridBagConstraints gbc_lblTipofiltro = new GridBagConstraints();
		gbc_lblTipofiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipofiltro.gridx = 0;
		gbc_lblTipofiltro.gridy = 1;
		panel_1.add(lblTipofiltro, gbc_lblTipofiltro);

		comboTipoFiltro = new JComboBox<TipoFiltro>();
		comboTipoFiltro.setModel(new DefaultComboBoxModel<TipoFiltro>(TipoFiltro.values()));
		GridBagConstraints gbc_comboTipoFiltro = new GridBagConstraints();
		gbc_comboTipoFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_comboTipoFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboTipoFiltro.gridx = 1;
		gbc_comboTipoFiltro.gridy = 1;
		panel_1.add(comboTipoFiltro, gbc_comboTipoFiltro);

		lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.gridx = 2;
		gbc_lblFiltro.gridy = 1;
		panel_1.add(lblFiltro, gbc_lblFiltro);

		fieldFiltro = new JTextField();
		GridBagConstraints gbc_fieldFiltro = new GridBagConstraints();
		gbc_fieldFiltro.gridwidth = 5;
		gbc_fieldFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_fieldFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldFiltro.gridx = 3;
		gbc_fieldFiltro.gridy = 1;
		panel_1.add(fieldFiltro, gbc_fieldFiltro);
		fieldFiltro.setColumns(10);

		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});

		btnBaixar = new JButton("baixar");
		btnBaixar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fazerDowload();
			}
		});
		btnBaixar.setEnabled(false);
		GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
		gbc_btnBaixar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBaixar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBaixar.gridx = 8;
		gbc_btnBaixar.gridy = 1;
		panel_1.add(btnBaixar, gbc_btnBaixar);

		JLabel lblPorta = new JLabel("Porta");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 10;
		gbc_lblPorta.gridy = 1;
		panel_1.add(lblPorta, gbc_lblPorta);

		fieldPorta = new JTextField();
		fieldPorta.setText("1818");
		GridBagConstraints gbc_fieldPorta = new GridBagConstraints();
		gbc_fieldPorta.insets = new Insets(0, 0, 5, 5);
		gbc_fieldPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldPorta.gridx = 11;
		gbc_fieldPorta.gridy = 1;
		panel_1.add(fieldPorta, gbc_fieldPorta);
		fieldPorta.setColumns(10);
		btnSair.setEnabled(false);
		GridBagConstraints gbc_btnSair = new GridBagConstraints();
		gbc_btnSair.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSair.insets = new Insets(0, 0, 5, 0);
		gbc_btnSair.gridx = 12;
		gbc_btnSair.gridy = 1;
		panel_1.add(btnSair, gbc_btnSair);

		scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridheight = 2;
		gbc_scrollPane_2.gridwidth = 13;
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 2;
		panel_1.add(scrollPane_2, gbc_scrollPane_2);

		tbArquivos = new JTable();
		scrollPane_2.setViewportView(tbArquivos);
		tbArquivos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					fazerDowload();
				}
			}
		});
		splitPane.setDividerLocation(400);

		iniciaServico();

	}

	/*
	 * 
	 * Metodos locais para Cliente
	 * 
	 */
	public void uparArquivos() {

		List<Arquivo> list = listarAquivosLocais();

		Cliente c = getClienteLocal();

		try {
			conexaoCliente.publicarListaArquivos(c, list);
			fieldStatusCliente.append("Sua lista foi atualizada com sucesso\n");
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	private List<Arquivo> listarAquivosLocais() {

		File dirStart = new File("." + File.separatorChar + "shared" + File.separatorChar);
		List<Arquivo> listaArquivos = new ArrayList<>();
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setNome(file.getName().substring(0, file.getName().lastIndexOf(".")));
				System.out.println(arq.getNome());
				arq.setTamanho(file.length());
				String extensao = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
				arq.setExtensao(extensao);
				arq.setPath(file.getPath());
				arq.setDataHoraModificacao(new Date(file.lastModified()));
				arq.setMd5(Md5Util.getMD5Checksum(arq.getPath()));
				listaArquivos.add(arq);
			}
		}
		return listaArquivos;
	}

	public void procurarArquivo() {
		String query = fieldQuery.getText();
		String filtro = fieldFiltro.getText();
		TipoFiltro tipoFiltro = TipoFiltro.valueOf(comboTipoFiltro.getSelectedItem().toString());
//		fieldStatusCliente.append("\tQuery: " + query + "\tfiltro: " + filtro + "\tTipoFiltro: " + tipoFiltro + "\n");
		try {
			Map<Cliente, List<Arquivo>> resultMap = conexaoCliente.procurarArquivo(query, tipoFiltro, filtro);

			TableModel tb = new MeuModelo(resultMap);
			tbArquivos.setModel(tb);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	private void fazerDowload() {
		Cliente c = new Cliente();
		Arquivo a = new Arquivo();

		int linha = tbArquivos.getSelectedRow();
		int i = 0;
		
		a.setNome(tbArquivos.getValueAt(linha, i++).toString());
		a.setExtensao(tbArquivos.getValueAt(linha, i++).toString());
		a.setPath(tbArquivos.getValueAt(linha, i++).toString());
		a.setTamanho(Integer.valueOf(tbArquivos.getValueAt(linha, i++).toString()));
		a.setMd5(tbArquivos.getValueAt(linha, i++).toString());
		c.setNome(tbArquivos.getValueAt(linha, i++).toString());
		c.setIp(tbArquivos.getValueAt(linha, i++).toString());
		c.setPorta(Integer.valueOf(tbArquivos.getValueAt(linha, i++).toString()));

		try {
			Registry registryConDowload = LocateRegistry.getRegistry(c.getIp(), c.getPorta());
			IServer conDownload = (IServer) registryConDowload.lookup(IServer.NOME_SERVICO);

			byte[] bytes = conDownload.baixarArquivo(getClienteLocal(), a);

			if (bytes == null) {
			} else {
				String nome = "c�pia_de_"+ a.getNome().toString() +"."+ a.getExtensao();
				escreva(new File(nome), bytes);
				String bytesBaixado = Md5Util.getMD5Checksum("." + File.separatorChar + "shared" + File.separatorChar + nome);
				if (a.getMd5().equals(bytesBaixado)) {
					fieldStatusCliente.append("Arquivo �ntegro baixado");
				} else {
					fieldStatusCliente.append("Arquivo corrompido baixado");
				}
			}

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

	public void escreva(File arq, byte[] dados) {
		String path = "." + File.separatorChar + "shared" + File.separatorChar + arq.getName();
		try {
			Files.write(Paths.get(path), dados, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public Cliente getClienteLocal() {
		String ip = "";
		String nome = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			nome = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		Cliente cliente = new Cliente();
		cliente.setIp(ip);
		cliente.setNome(nome);
		cliente.setPorta(PORTA_SERVER);

		return cliente;
	}

	public void conectar() {
		String server = fieldIp.getText();
		int porta = Integer.parseInt(fieldPorta.getText());
		try {
			registryConexaoCliente = LocateRegistry.getRegistry(server, porta);
			conexaoCliente = (IServer) registryConexaoCliente.lookup(IServer.NOME_SERVICO);

			conexaoCliente.registrarCliente(getClienteLocal());

			fieldStatusCliente.append("Conectado e registrado com sucesso\n");

		} catch (Exception e) {
			fieldStatusCliente.append("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE O SERVIDOR EST�O RODANDO, SE O IP E PORTA EST�O"
					+ " CORRETOS, SE N�O H� BLOQUEIO DE FIREWALL OU ANTIVIRUS.\n"
					+ "-------------------------------------------------------------------\n\n");
			fieldStatusCliente.append(e.toString());
		}

		btnProcurar.setEnabled(true);
		btnBaixar.setEnabled(true);
		// btnUpar.setEnabled(true);
		btnSair.setEnabled(true);
		fieldIp.setEnabled(false);
		fieldPorta.setEnabled(false);
		btnConectar.setEnabled(false);

		up = new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					uparArquivos();
					try {
						Thread.sleep(300000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		up.start();

	}

	public void desconectar() {
		try {
			Cliente c = getClienteLocal();
			conexaoCliente.desconectar(c);
			fieldStatusCliente.append("Voc� se desconectou\n");
			up.stop();
			btnProcurar.setEnabled(false);
			btnBaixar.setEnabled(false);
			// btnUpar.setEnabled(false);
			btnSair.setEnabled(false);
			fieldIp.setEnabled(true);
			fieldPorta.setEnabled(true);
			btnConectar.setEnabled(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * Metodos locais para servidor
	 * 
	 */
	public void iniciaServico() {
		try {
			fieldStatus.append("Iniciando servidor\n");
			if (conexaoServidor == null) {
				conexaoServidor = (IServer) UnicastRemoteObject.exportObject(InterfacePrincipal.this, 0);
			}
			registryConexaoServidor = LocateRegistry.createRegistry(PORTA_SERVER);
			registryConexaoServidor.rebind(IServer.NOME_SERVICO, conexaoServidor);
			fieldStatus.append("Servidor startado, aguardando Conex�es\n");
			btnParar.setEnabled(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void pararServico() {
		btnParar.setEnabled(false);
		btnConectar.setEnabled(true);
		btnIniciar.setEnabled(true);

		try {
			UnicastRemoteObject.unexportObject(registryConexaoServidor, true);
			registryConexaoServidor = null;
			fieldStatus.append("servidor parado\n");
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * Metodos Interface para servidor
	 * 
	 */

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		if (c != null) {
			if (!mapaArquivos.containsKey(c)) {
				mapaArquivos.put(c, null);
				fieldStatus.append("Cliente " + c.getNome() + "com o IP:" + c.getIp() + " se conectou...\n");
			} else {
				fieldStatus.append("Cliente j� cadastrado\n");
			}
		} else {
			fieldStatus.append("Cliente recebido Nulo\n");
		}
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		if (mapaArquivos.containsKey(c)) {
			mapaArquivos.entrySet().forEach(e -> {
				if (e.getKey().equals(c)) {
					e.setValue(lista);
					fieldStatus.append("Lista de arquivos de " + e.getKey().getNome() + " foi atualizada...\n");
				}
			});
		} else {
			fieldStatus.append("Cliente n�o encontrado\n");
		}
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		Map<Cliente, List<Arquivo>> mapaResult = new HashMap<>();

		for (Entry<Cliente, List<Arquivo>> e : mapaArquivos.entrySet()) {

			List<Arquivo> listaResult = new ArrayList<>();
			for (Arquivo arquivo : e.getValue()) {
				switch (tipoFiltro) {
				case NOME:
					if (arquivo.getNome().contains(query)) {
						listaResult.add(arquivo);
					}
					break;

				case TAMANHO_MIN:
					if (arquivo.getTamanho() >= Integer.valueOf(filtro)) {
						if (arquivo.getNome().contains(query)) {
							listaResult.add(arquivo);
						}
					}
					break;

				case TAMANHO_MAX:
					if (arquivo.getTamanho() <= Integer.valueOf(filtro)) {
						if (arquivo.getNome().contains(query)) {
							listaResult.add(arquivo);
						}
					}
					break;

				case EXTENSAO:
					if (arquivo.getExtensao().contains(filtro)) {
						if (arquivo.getNome().contains(query)) {
							listaResult.add(arquivo);
						}
					}
					break;
				}
				listaResult.add(arquivo);
			}
			mapaResult.put(e.getKey(), listaResult);
		}
		mapaArquivos.forEach((key, value) -> {
			List<Arquivo> listaResult = new ArrayList<>();
			value.forEach(e -> {
				listaResult.add(e);
			});
			mapaResult.put(key, listaResult);
		});
		return mapaResult;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		byte[] dados = null;
		Path path = Paths.get(arq.getPath());
		try {
			dados = Files.readAllBytes(path);
			fieldStatus.append("O usu�rio: " + cli.getNome() + " com o IP: " + cli.getIp() + " baixou o seu arquivo:"
					+ arq.getNome());
			return dados;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		if (c != null) {
			if (mapaArquivos.containsKey(c)) {
				mapaArquivos.remove(c);
				fieldStatus.append("Cliente " + c.getNome() + "com o IP:" + c.getIp() + " se desconectou...\n");
			} else {
				fieldStatus.append("Cliente n�o encontrado\n");
			}
		} else {
			fieldStatus.append("Cliente recebido Nulo\n");
		}
	}
}