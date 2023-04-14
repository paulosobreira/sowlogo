package br.nnpe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PainelUrl extends JPanel {

	private JTextField descricao = null;
	private JTextField url = null;
	private JButton sincronizar = new JButton("Sincronizar Fonte");
	private JButton alterar = new JButton("Alterar ");
	private JButton subir = new JButton("Subir ");
	private JButton descer = new JButton("Descer ");
	private JButton inserir = new JButton("Inserir ");
	private JButton apagar = new JButton("Apagar ");
	private JButton recortar = new JButton("Recortar ");
	private JButton colar = new JButton("Colar ");
	private JButton enviar = new JButton("Enviar ");
	private JApplet applet;
	private JTree tree;
	private JTextArea textArea;
	private DefaultMutableTreeNode noRecorte;

	public PainelUrl(JTree tree, JTextArea textArea, JTextField descricao,
			JTextField url, JApplet applet) {
		this.applet = applet;
		GridLayout gridLayoutC = new GridLayout(10, 1, 5, 5);
		setLayout(gridLayoutC);
		add(new JLabel("    Operações na árvore"));
		add(alterar);
		add(inserir);
		add(recortar);
		add(colar);
		add(subir);
		add(descer);
		add(sincronizar);
		add(apagar);
		add(enviar);
		this.tree = tree;
		this.textArea = textArea;
		this.descricao = descricao;
		this.url = url;
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				PainelUrl.this.textArea.setText("");
				TreePath curPath = PainelUrl.this.tree.getPathForLocation(e
						.getX(), e.getY());
				if (curPath == null) {
					return;
				}
				Object comp = curPath.getLastPathComponent();
				if (comp instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) comp;
					Object object = node.getUserObject();
					starCampos(object);
				}

			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				PainelUrl.this.textArea.setText("");
				TreePath currPath = PainelUrl.this.tree.getSelectionPath();
				if (currPath == null) {
					return;
				}
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
						.getLastPathComponent();
				Object object = node.getUserObject();
				starCampos(object);
			}
		});

		inserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				inserirNo();
			}
		});
		apagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				apagarNo();
			}
		});
		alterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				alterarNo();
			}
		});
		subir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				subirNo();
			}
		});
		descer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				descerNo();
			}
		});
		recortar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				recortarNo();
			}
		});
		colar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PainelUrl.this.textArea.setText("");
				colarNo();
			}
		});

		sincronizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sincronizar();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		enviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sincronizar();
					enviar();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
	}

	protected void enviar() {
		JPasswordField passwordField = new JPasswordField(20);
		JOptionPane.showMessageDialog(applet, passwordField, "Senha",
				JOptionPane.QUESTION_MESSAGE);
		try {
			if ("c846d80d826291f2a6a0d7a57e540307".equals(md5(new String(
					passwordField.getPassword()))))
				enviarObjeto(textArea.getText());
			else {
				JOptionPane.showMessageDialog(applet, "Senha Incorreta",
						"Senha", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void colarNo() {
		if (noRecorte == null) {
			JOptionPane.showMessageDialog(PainelUrl.this,
					"NÃO exite nó a colar", "Area de copia vazia",
					JOptionPane.ERROR_MESSAGE);
			return;

		}
		TreePath currPath = tree.getSelectionPath();
		if (currPath == null) {
			return;
		}
		int depth = currPath.getPath().length;
		if (depth != 2) {
			JOptionPane.showMessageDialog(PainelUrl.this,
					"Não é permitido colar nó neste nivel.", "Ação impossível",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
				.getLastPathComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.insertNodeInto(noRecorte, node, node.getChildCount());
		model.reload();
		selecionarNo(noRecorte);
		noRecorte = null;

	}

	protected void recortarNo() {
		TreePath currPath = tree.getSelectionPath();
		if (currPath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
				.getLastPathComponent();
		if (!(node.getUserObject() instanceof SowNode)) {
			JOptionPane.showMessageDialog(PainelUrl.this,
					"Este nó não pode ser recortado", "Area de copia vazia",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		SowNode sowNode = (SowNode) node.getUserObject();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		SowNode newSowNode = new SowNode(sowNode.getTitle(), sowNode.getUrl());
		DefaultMutableTreeNode novoNo = new DefaultMutableTreeNode(newSowNode);
		noRecorte = novoNo;
		model.removeNodeFromParent(node);

	}

	private StringBuffer carregarTexto(String arq1) throws IOException {
		BufferedReader in1 = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(arq1)));

		String line1 = in1.readLine();
		StringBuffer texto = new StringBuffer();
		while (line1 != null) {
			texto.append(line1 + "\n");
			line1 = in1.readLine();
		}
		return texto;
	}

	public static void replaceBuffer(StringBuffer buffer, String replace,
			String valor) {
		if (valor == null) {
			valor = "";
		}
		buffer.replace(buffer.indexOf(replace), buffer.indexOf(replace)
				+ replace.length(), valor);
	}

	protected void sincronizar() throws IOException {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel()
				.getRoot();

		StringBuffer corpo = carregarTexto("menu.html");

		StringBuffer param = new StringBuffer();

		param.append("\n\t<PARAM NAME=\"Root\" VALUE=\"" + root.getUserObject()
				+ "\">");
		param.append("\n\t<PARAM NAME=\"nodecont\" VALUE=\""
				+ (root.getChildCount() - 1) + "\">");
		param.append("\n");
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode nodeLv2 = (DefaultMutableTreeNode) root
					.getChildAt(i);
			String valor = "";
			if (nodeLv2.getUserObject() instanceof String) {
				valor = substCaractHtml((String) nodeLv2.getUserObject());
			} else if (nodeLv2.getUserObject() instanceof SowNode) {
				SowNode sowNode = (SowNode) nodeLv2.getUserObject();
				valor = substCaractHtml(sowNode.getTitle());
			}
			param.append("\n\t<PARAM NAME=\"node" + i + "\" VALUE=\"" + valor
					+ "\">");
			param.append("\n\t<PARAM NAME=\"node" + i + "cont\" VALUE=\""
					+ (nodeLv2.getChildCount()) + "\">");
			for (int j = 0; j < nodeLv2.getChildCount(); j++) {
				DefaultMutableTreeNode nodeLv3 = (DefaultMutableTreeNode) nodeLv2
						.getChildAt(j);
				SowNode sowNode = (SowNode) nodeLv3.getUserObject();
				String titulo = substCaractHtml((String) sowNode.getTitle());
				param.append("\n\t\t<PARAM NAME=\"node" + i + "title" + j
						+ "\" VALUE=\"" + titulo + "\">");
				param.append("\n\t\t<PARAM NAME=\"node" + i + "location" + j
						+ "\" VALUE=\"" + sowNode.getUrl().toString() + "\">");
			}
		}
		replaceBuffer(corpo, "{params}", param.toString());
		textArea.setText(corpo.toString());

	}

	protected void starCampos(Object object) {
		if (object != null && object instanceof SowNode) {
			SowNode sowNode = (SowNode) object;
			descricao.setText(sowNode.getTitle());
			if (sowNode.getUrl() != null) {
				url.setText(sowNode.getUrl().toString());
			} else {
				PainelUrl.this.url.setText("");
			}
		} else if (object instanceof String) {
			descricao.setText((String) object);
			url.setText("");
		}

	}

	protected void descerNo() {
		TreePath currPath = tree.getSelectionPath();
		if (currPath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
				.getLastPathComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		int index = model.getIndexOfChild(node.getParent(), node);
		if (index + 1 < node.getParent().getChildCount()) {
			model.insertNodeInto(node, (DefaultMutableTreeNode) node
					.getParent(), index + 1);
		}
		model.reload();
		selecionarNo(node);
	}

	protected void subirNo() {
		TreePath currPath = tree.getSelectionPath();
		if (currPath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
				.getLastPathComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		int index = model.getIndexOfChild(node.getParent(), node);
		if (index != 0) {
			model.insertNodeInto(node, (DefaultMutableTreeNode) node
					.getParent(), index - 1);
		}
		model.reload();
		selecionarNo(node);
	}

	protected void alterarNo() {
		TreePath currPath = tree.getSelectionPath();
		if (currPath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) currPath
				.getLastPathComponent();
		if (node.getUserObject() instanceof SowNode) {
			SowNode sowNode = (SowNode) node.getUserObject();
			if ("".equals(descricao.getText())) {
				JOptionPane.showMessageDialog(PainelUrl.this,
						"A Descrição NÃO pode ser em branco",
						"Descrição em branco", JOptionPane.ERROR_MESSAGE);
				return;
			}
			sowNode.setTitle(descricao.getText());
			int depth = currPath.getPath().length;
			if ("".equals(url.getText()) && depth == 3) {
				JOptionPane.showMessageDialog(PainelUrl.this,
						"A Url NÃO pode ser em branco neste nivel.",
						"Url em branco", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (depth == 3) {
				try {
					sowNode.setUrl(new URL(url.getText()));
				} catch (MalformedURLException e) {
					JOptionPane.showMessageDialog(PainelUrl.this,
							url.getText(), "URL INVÁLIDA",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		} else {
			node.setUserObject(descricao.getText());
		}

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();
		selecionarNo(node);
	}

	protected void apagarNo() {
		if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
				PainelUrl.this, "Apagar Nó?", "Apagar Nó?",
				JOptionPane.WARNING_MESSAGE)) {
			return;
		}
		TreePath geralPath = tree.getSelectionPath();
		DefaultMutableTreeNode node = null;
		if ((geralPath != null)) {
			node = (DefaultMutableTreeNode) geralPath.getLastPathComponent();

			if (node == tree.getModel().getRoot()) {
				return;
			}

			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			model.removeNodeFromParent(node);
		}
	}

	protected void inserirNo() {
		TreePath currPath = tree.getSelectionPath();
		DefaultMutableTreeNode node = null;
		int depth = currPath.getPath().length;
		if ("".equals(descricao.getText())) {
			JOptionPane.showMessageDialog(PainelUrl.this,
					"A Descrição NÃO pode ser em branco",
					"Descrição em branco", JOptionPane.ERROR_MESSAGE);
			return;

		}
		SowNode sowNode = new SowNode(descricao.getText(), null);
		if (depth > 2) {
			JOptionPane.showMessageDialog(PainelUrl.this,
					"A profundidade máxima da arvore é 2",
					"Profundidade Máxima", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (depth == 2) {
			if ("".equals(url.getText())) {
				JOptionPane.showMessageDialog(PainelUrl.this,
						"A Url NÃO pode ser em branco neste nivel.",
						"Url em branco", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				try {
					sowNode.setUrl(new URL(url.getText()));
				} catch (MalformedURLException e) {
					JOptionPane.showMessageDialog(PainelUrl.this,
							url.getText(), "URL INVÁLIDA",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
		if (currPath != null) {
			node = (DefaultMutableTreeNode) currPath.getLastPathComponent();
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(sowNode);
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			model.insertNodeInto(newNode, node, node.getChildCount());
			selecionarNo(newNode);
		}

	}

	private void selecionarNo(DefaultMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		TreePath path = new TreePath(model.getPathToRoot(node));
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
	}

	public static String substCaractHtml(String mensagem) {
		if (mensagem == null) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < mensagem.length(); i++) {
			if ('\n' == mensagem.charAt(i)) {
				stringBuffer.append("<br>\n");
			} else if ('Á' == mensagem.charAt(i)) {
				stringBuffer.append("&Aacute;");
			} else if ('á' == mensagem.charAt(i)) {
				stringBuffer.append("&aacute;");
			} else if ('Â' == mensagem.charAt(i)) {
				stringBuffer.append("&Acirc;");
			} else if ('â' == mensagem.charAt(i)) {
				stringBuffer.append("&acirc;");
			} else if ('À' == mensagem.charAt(i)) {
				stringBuffer.append("&Agrave;");
			} else if ('à' == mensagem.charAt(i)) {
				stringBuffer.append("&agrave;");
			} else if ('Ã' == mensagem.charAt(i)) {
				stringBuffer.append("&Atilde;");
			} else if ('ã' == mensagem.charAt(i)) {
				stringBuffer.append("&atilde;");
			} else if ('É' == mensagem.charAt(i)) {
				stringBuffer.append("&Eacute;");
			} else if ('é' == mensagem.charAt(i)) {
				stringBuffer.append("&eacute;");
			} else if ('Ê' == mensagem.charAt(i)) {
				stringBuffer.append("&Ecirc;");
			} else if ('ê' == mensagem.charAt(i)) {
				stringBuffer.append("&ecirc;");
			} else if ('È' == mensagem.charAt(i)) {
				stringBuffer.append("&Egrave;");
			} else if ('è' == mensagem.charAt(i)) {
				stringBuffer.append("&egrave;");
			} else if ('Í' == mensagem.charAt(i)) {
				stringBuffer.append("&Iacute;");
			} else if ('í' == mensagem.charAt(i)) {
				stringBuffer.append("&iacute;");
			} else if ('Ì' == mensagem.charAt(i)) {
				stringBuffer.append("&Igrave;");
			} else if ('ì' == mensagem.charAt(i)) {
				stringBuffer.append("&igrave;");
			} else if ('Ó' == mensagem.charAt(i)) {
				stringBuffer.append("&Oacute;");
			} else if ('ó' == mensagem.charAt(i)) {
				stringBuffer.append("&oacute;");
			} else if ('Ô' == mensagem.charAt(i)) {
				stringBuffer.append("&Ocirc;");
			} else if ('ô' == mensagem.charAt(i)) {
				stringBuffer.append("&ocirc;");
			} else if ('Ò' == mensagem.charAt(i)) {
				stringBuffer.append("&Ograve;");
			} else if ('ò' == mensagem.charAt(i)) {
				stringBuffer.append("&ograve;");
			} else if ('Õ' == mensagem.charAt(i)) {
				stringBuffer.append("&Otilde;");
			} else if ('õ' == mensagem.charAt(i)) {
				stringBuffer.append("&otilde;");
			} else if ('Ú' == mensagem.charAt(i)) {
				stringBuffer.append("&Uacute;");
			} else if ('ú' == mensagem.charAt(i)) {
				stringBuffer.append("&uacute;");
			} else if ('Ù' == mensagem.charAt(i)) {
				stringBuffer.append("&Ugrave;");
			} else if ('ù' == mensagem.charAt(i)) {
				stringBuffer.append("&ugrave;");
			} else if ('Ç' == mensagem.charAt(i)) {
				stringBuffer.append("&Ccedil;");
			} else if ('ç' == mensagem.charAt(i)) {
				stringBuffer.append("&ccedil;");
			} else if ('Ñ' == mensagem.charAt(i)) {
				stringBuffer.append("&Ntilde;");
			} else if ('ñ' == mensagem.charAt(i)) {
				stringBuffer.append("&ntilde;");
			} else if ('"' == mensagem.charAt(i)) {
				stringBuffer.append("&quot;");
			} else if ('<' == mensagem.charAt(i)) {
				stringBuffer.append("&lt;");
			} else if ('>' == mensagem.charAt(i)) {
				stringBuffer.append("&gt;");
			} else if ('&' == mensagem.charAt(i)) {
				stringBuffer.append("&amp;");
			} else if ('‘' == mensagem.charAt(i)) {
				stringBuffer.append("&lsquo;");
			} else if ('’' == mensagem.charAt(i)) {
				stringBuffer.append("&rsquo;");
			} else if ('¿' == mensagem.charAt(i)) {
				stringBuffer.append("&iquest;");
			} else {
				stringBuffer.append(mensagem.charAt(i));
			}

		}
		return stringBuffer.toString();
	}

	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(
					1, 3));
		}

		return sb.toString();
	}

	public static String md5(String message) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");

		return hex(md.digest(message.getBytes("CP1252")));
	}

	public Object enviarObjeto(Object enviar) {
		try {
			URL url = applet.getCodeBase();
			String protocol = url.getProtocol();
			String host = url.getHost();
			int port = url.getPort();
			URL dataUrl;
			Object retorno = null;
			dataUrl = new URL(protocol, host, port, "/SowServlet");

			URLConnection connection = dataUrl.openConnection();

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(
					byteArrayOutputStream);
			stream.writeObject(enviar);
			stream.flush();
			connection.setRequestProperty("Content-Length", String
					.valueOf(byteArrayOutputStream.size()));
			connection.setRequestProperty("Content-Length",
					"application/x-www-form-urlencoded");
			connection.getOutputStream().write(
					byteArrayOutputStream.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(connection
					.getInputStream());
			retorno = ois.readObject();
			return retorno;
		} catch (Exception e) {
			StackTraceElement[] trace = e.getStackTrace();
			StringBuffer buff = new StringBuffer();
			int size = ((trace.length > 10) ? 10 : trace.length);
			for (int i = 0; i < size; i++)
				buff.append(trace[i] + "\n");
			JOptionPane.showMessageDialog(this, buff.toString(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return null;
	}
}
