package edgeDetection;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	public void setImage(BufferedImage image) {
		if(image != null) { 
			ColorModel cm = image.getColorModel(); // Copy images
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			WritableRaster raster = image.copyData(null);
			this.image =  new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		} else {
			this.image = null;
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}
	}

}
