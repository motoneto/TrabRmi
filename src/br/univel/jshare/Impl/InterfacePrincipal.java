package br.univel.jshare.Impl;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.dagostini.exemplos.ListarDiretoriosArquivos;
import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;

import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

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
	private JButton btnIniciar;
	private JTextArea fieldStatus;
	private JButton btnParar;
	private JButton btnConectar;
	private JButton btnSair;
	private JButton btnBaixar;
	private JButton btnUpar;
	private JTextArea fieldStatusCliente;
	private JLabel lblTipofiltro;
	private JComboBox<TipoFiltro> comboTipoFiltro;
	private JLabel lblFiltro;
	private JTextField fieldFiltro;
	private JButton btnProcurar;

	Map<Cliente, List<Arquivo>> mapaArquivos = new HashMap<>();

	IServer conexaoServidor;
	Registry registryConexaoServidor;

	IServer conexaoCliente;
	Registry registryConexaoCliente;


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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
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
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

//		btnIniciar = new JButton("Iniciar");
//		btnIniciar.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//
//				iniciaServico();
//
//			}
//		});
//		GridBagConstraints gbc_btnIniciar = new GridBagConstraints();
//		gbc_btnIniciar.fill = GridBagConstraints.BOTH;
//		gbc_btnIniciar.insets = new Insets(0, 0, 5, 5);
//		gbc_btnIniciar.gridx = 0;
//		gbc_btnIniciar.gridy = 0;
//		panel.add(btnIniciar, gbc_btnIniciar);

		fieldStatus = new JTextArea();
		fieldStatus.setEditable(false);
		GridBagConstraints gbc_fieldStatus = new GridBagConstraints();
		gbc_fieldStatus.gridheight = 2;
		gbc_fieldStatus.insets = new Insets(0, 0, 5, 0);
		gbc_fieldStatus.fill = GridBagConstraints.BOTH;
		gbc_fieldStatus.gridx = 1;
		gbc_fieldStatus.gridy = 0;
		panel.add(fieldStatus, gbc_fieldStatus);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pararServico();
			}
		});
		btnParar.setEnabled(false);
		GridBagConstraints gbc_btnParar = new GridBagConstraints();
		gbc_btnParar.gridheight = 2;
		gbc_btnParar.fill = GridBagConstraints.BOTH;
		gbc_btnParar.insets = new Insets(0, 0, 0, 5);
		gbc_btnParar.gridx = 0;
		gbc_btnParar.gridy = 0;
		panel.add(btnParar, gbc_btnParar);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0, 75, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JLabel lblip = new JLabel("IP");
		GridBagConstraints gbc_lblip = new GridBagConstraints();
		gbc_lblip.insets = new Insets(0, 0, 5, 5);
		gbc_lblip.gridx = 0;
		gbc_lblip.gridy = 0;
		panel_1.add(lblip, gbc_lblip);

		fieldIp = new JTextField();
		fieldIp.setText("localhost");
		GridBagConstraints gbc_fieldIp = new GridBagConstraints();
		gbc_fieldIp.insets = new Insets(0, 0, 5, 5);
		gbc_fieldIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldIp.gridx = 1;
		gbc_fieldIp.gridy = 0;
		panel_1.add(fieldIp, gbc_fieldIp);
		fieldIp.setColumns(10);

		JLabel lblPorta = new JLabel("Porta");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 2;
		gbc_lblPorta.gridy = 0;
		panel_1.add(lblPorta, gbc_lblPorta);

		fieldPorta = new JTextField();
		fieldPorta.setText("1818");
		GridBagConstraints gbc_fieldPorta = new GridBagConstraints();
		gbc_fieldPorta.insets = new Insets(0, 0, 5, 5);
		gbc_fieldPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldPorta.gridx = 3;
		gbc_fieldPorta.gridy = 0;
		panel_1.add(fieldPorta, gbc_fieldPorta);
		fieldPorta.setColumns(10);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectar();
			}
		});
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnConectar.gridx = 4;
		gbc_btnConectar.gridy = 0;
		panel_1.add(btnConectar, gbc_btnConectar);

		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		btnSair.setEnabled(false);
		GridBagConstraints gbc_btnSair = new GridBagConstraints();
		gbc_btnSair.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSair.insets = new Insets(0, 0, 5, 0);
		gbc_btnSair.gridx = 5;
		gbc_btnSair.gridy = 0;
		panel_1.add(btnSair, gbc_btnSair);

		JLabel lblQuery = new JLabel("Query");
		GridBagConstraints gbc_lblQuery = new GridBagConstraints();
		gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuery.gridx = 0;
		gbc_lblQuery.gridy = 1;
		panel_1.add(lblQuery, gbc_lblQuery);

		fieldQuery = new JTextField();
		GridBagConstraints gbc_fieldQuery = new GridBagConstraints();
		gbc_fieldQuery.gridwidth = 3;
		gbc_fieldQuery.insets = new Insets(0, 0, 5, 5);
		gbc_fieldQuery.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldQuery.gridx = 1;
		gbc_fieldQuery.gridy = 1;
		panel_1.add(fieldQuery, gbc_fieldQuery);
		fieldQuery.setColumns(10);
		
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
		gbc_btnProcurar.gridx = 4;
		gbc_btnProcurar.gridy = 1;
		panel_1.add(btnProcurar, gbc_btnProcurar);

		btnUpar = new JButton("Upar");
		btnUpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uparArquivos();
			}
		});
		btnUpar.setEnabled(false);
		GridBagConstraints gbc_btnUpar = new GridBagConstraints();
		gbc_btnUpar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUpar.insets = new Insets(0, 0, 5, 0);
		gbc_btnUpar.gridx = 5;
		gbc_btnUpar.gridy = 1;
		panel_1.add(btnUpar, gbc_btnUpar);

		lblTipofiltro = new JLabel("TipoFiltro");
		GridBagConstraints gbc_lblTipofiltro = new GridBagConstraints();
		gbc_lblTipofiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipofiltro.gridx = 0;
		gbc_lblTipofiltro.gridy = 2;
		panel_1.add(lblTipofiltro, gbc_lblTipofiltro);

		comboTipoFiltro = new JComboBox();
		comboTipoFiltro.setModel(new DefaultComboBoxModel(TipoFiltro.values()));
		GridBagConstraints gbc_comboTipoFiltro = new GridBagConstraints();
		gbc_comboTipoFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_comboTipoFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboTipoFiltro.gridx = 1;
		gbc_comboTipoFiltro.gridy = 2;
		panel_1.add(comboTipoFiltro, gbc_comboTipoFiltro);

		lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.gridx = 2;
		gbc_lblFiltro.gridy = 2;
		panel_1.add(lblFiltro, gbc_lblFiltro);

		fieldFiltro = new JTextField();
		GridBagConstraints gbc_fieldFiltro = new GridBagConstraints();
		gbc_fieldFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_fieldFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_fieldFiltro.gridx = 3;
		gbc_fieldFiltro.gridy = 2;
		panel_1.add(fieldFiltro, gbc_fieldFiltro);
		fieldFiltro.setColumns(10);
		
				btnBaixar = new JButton("baixar");
				btnBaixar.setEnabled(false);
				GridBagConstraints gbc_btnBaixar = new GridBagConstraints();
				gbc_btnBaixar.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnBaixar.gridwidth = 2;
				gbc_btnBaixar.insets = new Insets(0, 0, 5, 0);
				gbc_btnBaixar.gridx = 4;
				gbc_btnBaixar.gridy = 2;
				panel_1.add(btnBaixar, gbc_btnBaixar);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		panel_1.add(scrollPane, gbc_scrollPane);

		fieldStatusCliente = new JTextArea();
		fieldStatusCliente.setEditable(false);
		scrollPane.setViewportView(fieldStatusCliente);
		splitPane.setDividerLocation(250);
		
		iniciaServico();
	}

	/*
	 * 
	 * Metodos locais para Cliente
	 * 
	 */
	public void uparArquivos(){
		
		List<Arquivo> list = listarAquivosLocais();
		
		list.forEach(e ->{
			System.out.println(e.getNome());
		});
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
				arq.setNome(file.getName());
				arq.setTamanho(file.length());
				String extensao = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
				arq.setExtensao(extensao);
				arq.setPath(file.getPath());
				arq.setDataHoraModificacao(new Date());
				arq.setMd5("");
				
				listaArquivos.add(arq);
			}
		}
		return listaArquivos;
	}

	public void procurarArquivo(){
		String query = fieldQuery.getText();
		String filtro = fieldFiltro.getText();
		TipoFiltro tipoFiltro = TipoFiltro.valueOf(comboTipoFiltro.getSelectedItem().toString());
		fieldStatusCliente.append("\tQuery: "+query+"\tfiltro: "+filtro+"\tTipoFiltro: "+tipoFiltro+"\n");
		try {
			Map<Cliente, List<Arquivo>> resultMap = conexaoCliente.procurarArquivo(query, tipoFiltro, filtro);
			
			resultMap.keySet().forEach(e ->{
				fieldStatusCliente.append("Cliente: "+e.getNome()+
										  "\tIp: "+e.getIp()+
										  "\tPorta"+e.getPorta()+
										  "\n");
				
				List<Arquivo> listArq = resultMap.get(e);
				listArq.forEach(arq -> {
					fieldStatusCliente.append("Nome: "+arq.getNome()+
											  "Extens�o: "+arq.getExtensao()+
											  "Path: "+arq.getPath()+
											  "\n");
				});
			});
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	public Cliente getClienteLocal() {
		String ip = "";
		String nome = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			nome = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
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
			
			fieldStatusCliente.append("Conectado e registrado com sucesso");
		} catch (Exception e) {
			fieldStatusCliente.append("\n\n-------------------------------------------------------\n"
					+ "ERRO: VERIFIQUE SE O SERVIDOR EST�O RODANDO, SE O IP E PORTA EST�O"
					+ " CORRETOS, SE N�O H� BLOQUEIO DE FIREWALL OU ANTIVIRUS.\n"
					+ "-------------------------------------------------------------------\n\n");
			fieldStatusCliente.append(e.toString());
		}
		
		btnProcurar.setEnabled(true);
		btnBaixar.setEnabled(true);
		btnUpar.setEnabled(true);
		btnSair.setEnabled(true);
		fieldIp.setEnabled(false);
		fieldPorta.setEnabled(false);
		btnConectar.setEnabled(false);
	}
	
	public void desconectar() {
		try {
			Cliente c = getClienteLocal();
			conexaoCliente.desconectar(c);
			fieldStatusCliente.append("Desconectado");
			
			btnProcurar.setEnabled(false);
			btnBaixar.setEnabled(false);
			btnUpar.setEnabled(false);
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
			fieldStatus.append("Aguardando Conex�es\n");
			btnParar.setEnabled(true);
			fieldStatus.append("servidor startado\n");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void pararServico() {
		btnParar.setEnabled(false);
		btnConectar.setEnabled(true);

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
				fieldStatus.append("Cliente j� cadastrado");
			}
		} else {
			fieldStatus.append("Cliente recebido Nulo");
		}
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		if (mapaArquivos.containsKey(c)) {
			mapaArquivos.entrySet().forEach(e -> {
				if (c.equals(e)) {
					e.setValue(lista);
					fieldStatus.append("Lista de arquivos de " + c.getNome() + " foi atualizada...\n");
				}
			});
		} else {
			fieldStatus.append("Cliente n�o encontrado");
		}
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		Map<Cliente, List<Arquivo>> mapaResult = new HashMap<>();
//		List<Arquivo> listaResult = new ArrayList<>();
//
//		Set<Cliente> cliente = mapaArquivos.keySet();

		mapaArquivos.forEach((c, la) -> {
			System.out.println(c.toString());
			la.forEach(e -> {
				System.out.println(e.getNome());
			});
		});
		
//		for (Iterator<Cliente> iterator = cliente.iterator(); iterator.hasNext();) {
//			Cliente cl = iterator.next();
//			System.out.println(cl.toString());
//			if (cl != null) {
//				List<Arquivo> listaCliente = mapaArquivos.get(cl);
//				listaResult.clear();
//				listaCliente.forEach(e -> {
//					switch (tipoFiltro) {
//					case NOME:
//						if (e.getNome().contains(query)) {
//							listaResult.add(e);
//						}
//						break;
//
//					case TAMANHO_MIN:
//						if (e.getTamanho() >= Long.getLong(filtro)) {
//							if(e.getNome().contains(query)){
//								listaResult.add(e);
//							}
//						}
//						break;
//
//					case TAMANHO_MAX:
//						if (e.getTamanho() <= Long.getLong(filtro)) {
//							if (e.getNome().contains(query)) {
//								listaResult.add(e);
//							}
//						}
//						break;
//
//					case EXTENSAO:
//						if (e.getExtensao().contains(query)) {
//							if (e.getNome().contains(query)) {
//								listaResult.add(e);
//							}
//						}
//						break;
//
//					default:
//						break;
//					}
//				});
//				if (listaResult.size() > 0) {
//					mapaResult.put(cl, listaResult);
//				}
//			}
//		}
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
				fieldStatus.append("Cliente n�o encontrado");
			}
		} else {
			fieldStatus.append("Cliente recebido Nulo");
		}
	}
}