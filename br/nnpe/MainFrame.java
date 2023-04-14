package br.nnpe;

import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JTree;
import javax.swing.WindowConstants;

/**
 * 
 * @author Paulo Sobreira Created on 26 de Maio de 2005, 10:12
 */
public class MainFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 8142440173811696099L;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SowLogo sowLogo = new SowLogo();
		sowLogo.init();
	}

}
