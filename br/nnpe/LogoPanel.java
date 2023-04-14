package br.nnpe;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Paulo Sobreira Criado Em 21/12/2005
 */
public class LogoPanel extends JPanel {
	private static Font font = new Font("serif", Font.PLAIN, 12);
	private BufferedImage logo = null;
	public AffineTransform afZoom;
	public AffineTransform afRotate;
	public int angle;
	public Point local;
	public int velocidade = 3;
	public boolean paintComponet = true;
	public ImageIcon icon = null;
	private int halfHeight;
	private int halfWidth;
	private int height;
	private int width;
	private int textLine;
	public double zoom = 1;
	public boolean efeitoFonte;
	private boolean escurecerFonte;
	private Color corFonte;
	private short iterCorFonte;

	/**
	 * Olha os nomes de variaveis da galera da sun
	 */
	private int nStrs;
	private int strH;
	private int yy;
	private List vector;
	private List texto;

	public LogoPanel(ImageIcon icon, JFrame frame) throws IOException {
		this.icon = icon;
		logo = ImageUtil.toBufferedImage(icon.getImage());
		logo = ImageUtil.geraTransparencia(logo, Color.WHITE);
		afZoom = new AffineTransform();
		afRotate = new AffineTransform();
		local = new Point(100, 10);
		width = frame.getWidth();
		height = frame.getHeight() + (frame.getHeight() / 2);
		halfHeight = icon.getIconHeight();
		halfWidth = icon.getIconWidth() / 2;

		FontMetrics fm = getFontMetrics(font);
		strH = fm.getAscent() + fm.getDescent();
		nStrs = (height / strH) + 2;
		vector = new ArrayList(nStrs);
		texto = new ArrayList();
		preecherColunasTexto();
	}

	private void preecherColunasTexto() throws IOException {
		carregarTexto("ThreadLogo.java");
		carregarTexto("LogoPanel.java");
		carregarTexto("SowLogo.java");
		carregarTexto("GeoUtil.java");
		carregarTexto("ImageUtil.java");
	}

	public Point sortearPonto() {
		int x = (int) (1 + ((Math.random() * width) / 3));
		int y = (int) (1 + ((Math.random() * height) / 3));
		Point point = new Point(x, y);

		return point;
	}

	private void carregarTexto(String arq1) throws IOException {
		BufferedReader in1 = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(arq1)));

		String line1 = in1.readLine();

		while (line1 != null) {
			texto.add("     " + line1);
			line1 = in1.readLine();
		}
	}

	protected void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;

		if (paintComponet) {
			super.paintComponent(g);

			String s = getLine();

			if ((s == null) || ((vector.size() == nStrs) && (vector.size() != 0))) {
				vector.remove(0);
			}

			graphics2D.setColor(corEfetioDeFonte());
			yy = (s == null) ? 0 : (height - (vector.size() * strH));

			for (int i = 0; i < vector.size(); i++) {
				graphics2D.drawString((String) vector.get(i), 1, yy += strH);
			}
		}
		graphics2D.drawImage(zoomRotate(), local.x, local.y, null);
	}

	private Color corEfetioDeFonte() {
		if (efeitoFonte) {
			if (corFonte == null) {
				corFonte = Color.lightGray;
				escurecerFonte = !escurecerFonte;
			}

			if (escurecerFonte) {
				corFonte.darker();
			} else {
				corFonte.brighter();
			}

			if (iterCorFonte == 300) {
				efeitoFonte = false;
				corFonte = null;
				iterCorFonte = 0;

				return Color.lightGray;
			}

			iterCorFonte++;

			return corFonte;
		} else {
			return Color.lightGray;
		}
	}

	private String getLine() {
		String linha = null;

		if (textLine < this.texto.size()) {
			linha = (String) texto.get(textLine);
			vector.add(linha);
			textLine++;
		} else {
			textLine = 0;
		}

		return linha;
	}

	private Image zoomRotate() {
		BufferedImage rotateBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		BufferedImage zoomBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AffineTransformOp op = new AffineTransformOp(afRotate, AffineTransformOp.TYPE_BILINEAR);
		op.filter(logo, zoomBuffer);

		AffineTransformOp op2 = new AffineTransformOp(afZoom, AffineTransformOp.TYPE_BILINEAR);
		op2.filter(zoomBuffer, rotateBuffer);

		return rotateBuffer;
	}

	public void increasseAngle() {
		if (angle >= 360) {
			angle = 0;
		}

		angle += velocidade;
		rotate(angle);
	}

	public void rotate(int angle) {
		double rad = Math.toRadians((double) angle);
		afRotate.setToRotation(rad, halfWidth, halfHeight);
	}

	public void zoomIn() {
		zoom += 0.01;
		afZoom.setToScale(zoom, zoom);
	}

	public void zoomOut() {
		zoom -= 0.01;
		afZoom.setToScale(zoom, zoom);
	}

	public void decreasseAngle() {
		if (angle <= 0) {
			angle = 360;
		}

		angle -= velocidade;
		rotate(angle);
	}
}
