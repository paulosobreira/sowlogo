package br.nnpe;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;

/**
 * @author Paulo Sobreira Criado Em 21/12/2005
 */
public class SowLogo extends JFrame {
	private ImageIcon logo;
	private ThreadLogo threadLogo;

	public void init() {
		this.setSize(800, 200);
		int imgNum = (int) (1 + (Math.random() * 6));

		if (imgNum == 6) {
			imgNum = (int) (1 + (Math.random() * 6));
		}
		imgNum = 7;
		logo = new ImageIcon(this.getClass().getResource("sowlogo" + imgNum + ".png"));

		LogoPanel panel = null;

		try {
			panel = new LogoPanel(logo, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		threadLogo = new ThreadLogo(panel, this);

		String borderTxt = "Versão 2023 \u263A no rights reserved.";

		this.setTitle(borderTxt);
		if (imgNum == 6) {
			borderTxt = "Scary Guy!! Metallica yeah!!!";
		}

		TitledBorder border = new TitledBorder(borderTxt);
		border.setTitleColor(new Color(106, 139, 132));
		border.setTitleJustification(TitledBorder.RIGHT);
		border.setTitleFont(border.getTitleFont().deriveFont(14));
		panel.setBorder(border);
		panel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				threadLogo.setPausada(!threadLogo.isPausada());
			}
		});
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel);
		threadLogo.setPriority(Thread.MIN_PRIORITY);
		threadLogo.start();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

		});
	}

	public void start() {
		threadLogo.setPausada(false);
	}

	public void stop() {
		threadLogo.setPausada(true);
	}

}
