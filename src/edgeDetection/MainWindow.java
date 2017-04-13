package edgeDetection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import util.FileUtil;

public class MainWindow {
	
	private final JFrame 		frame;
	
	private final JPanel 		menuPanel = new JPanel(new GridBagLayout()); 
	private final JButton		loadImageB = new JButton(new LoadImageAction());
	private final JButton		applyGrayScaleB = new JButton(new ApplyGraySscaleAction());
	private final JButton		applyblurB = new JButton(new ApplyBlurAction());
	private final JButton		applySobelB = new JButton(new ApplySobelAction());
	
	private final JTabbedPane 	tabbedPane = new JTabbedPane();
	private final ImagePanel 	actualImagePane  = new ImagePanel(); 
	private final ImagePanel 	sourceImagePane  = new ImagePanel(); 
	private final ImagePanel 	blurImagePane = new ImagePanel(); 
	private final ImagePanel 	grayScaleImagePane = new ImagePanel(); 
	private final ImagePanel 	sobelAppiedImagePane  = new ImagePanel();
	private final ImagePanel 	sobelAppiedXImagePane  = new ImagePanel(); 
	private final ImagePanel 	sobelAppiedYImagePane  = new ImagePanel(); 
	
	private BufferedImage 		image;
	
	public MainWindow() {
		//Menu Panel
		menuPanel.add(loadImageB, 		new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 		new Insets(5, 5, 5, 0), 0, 0));
		menuPanel.add(applyblurB, 		new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,			new Insets(5, 5, 5, 0), 0, 0));
		menuPanel.add(applyGrayScaleB, 	new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,			new Insets(5, 5, 5, 0), 0, 0));
		menuPanel.add(applySobelB, 		new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,			new Insets(5, 5, 5, 0), 0, 0));
		menuPanel.add(new JPanel(), 	new GridBagConstraints(-1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,	new Insets(0, 0, 0, 0), 0, 0));
		
		//Tabbed Pane
		tabbedPane.add("Actual Image", 	actualImagePane);
		tabbedPane.add("Source", 		sourceImagePane);
		tabbedPane.add("Blur",      	blurImagePane);
		tabbedPane.add("Grayscale", 	grayScaleImagePane);
		tabbedPane.add("Sobel", 		sobelAppiedImagePane);
		tabbedPane.add("SobelX", 		sobelAppiedXImagePane);
		tabbedPane.add("SobelY", 		sobelAppiedYImagePane);
		
		//Frame
		frame = new JFrame("Sobel Edge Decetor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(BorderLayout.NORTH, menuPanel);
		frame.add(BorderLayout.CENTER, tabbedPane);
		
		frame.setSize(500, 150);
		frame.setLocation(0, 0);
		frame.setVisible(true);
	}
	
	private void resizeWindow() {
		frame.setSize(image.getWidth() > 500 ? image.getWidth() + 25: 500, image.getHeight() > 150 ? image.getHeight() + 105 : 150);
	}
	
	private void updateActualImage() {
		actualImagePane.setImage(image);
	}
	
	private void resetAllImagePane() {
		actualImagePane.setImage(null);
		sourceImagePane.setImage(null);
		grayScaleImagePane.setImage(null);
		blurImagePane.setImage(null);
		sobelAppiedImagePane.setImage(null);
		sobelAppiedXImagePane.setImage(null);
		sobelAppiedYImagePane.setImage(null);
	}
	
	private void loadSourceImage(File f) {
		resetAllImagePane();
		this.image = FileUtil.loadImage(f.getAbsolutePath());
		if (image != null) {
			sourceImagePane.setImage(image);
			updateActualImage();
			resizeWindow();
		}
	}
	
	private void applyBlur() {
		if (image != null) {
			image = new GaussianBlur(image).process().getImage();
			blurImagePane.setImage(image);
			updateActualImage();
		}
	}

	private void applyGrayScale() {
		if (image != null) {
		    BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		    Graphics g = img.getGraphics();
		    g.drawImage(image, 0, 0, null);
		    g.dispose();
			image = img;
			grayScaleImagePane.setImage(image);
			updateActualImage();
		}
	}
	
	private void applySobel() {
		if (image != null) {
			
			image = ImageUtil.expandImage(image, 1);
			SobelEdgeDetector sed = new SobelEdgeDetector(image, 1).process();
			
			BufferedImage sobelX = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			BufferedImage sobelY = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			for (SobelObject so : sed.getSobelObjects()) {
				image.setRGB(so.getPixelX(), so.getPixelY(), so.getGAsColor().getRGB());
				sobelX.setRGB(so.getPixelX(), so.getPixelY(), so.getGxAsColor().getRGB());
				sobelY.setRGB(so.getPixelX(), so.getPixelY(), so.getGyAsColor().getRGB());
			}
			
			CannyEdgeDetector ced = new CannyEdgeDetector(sed.getSobelObjects());
			CannyObject[][] cobjs = ced.getCannyObjects();
			
			for (int y = 0; y < cobjs.length; y++) {
				for (int x = 0; x < cobjs[y].length; x++) {
					Color c = cobjs[y][x].isLocalMaximum() ? cobjs[y][x].getGAsColor() : Color.black;
					image.setRGB(cobjs[y][x].getPixelX(), cobjs[y][x].getPixelY(), c.getRGB());
				}
			}
			
			sobelAppiedImagePane.setImage(image);
			sobelAppiedXImagePane.setImage(sobelX);
			sobelAppiedYImagePane.setImage(sobelY);
			updateActualImage();
		}
	}
	
	private final class LoadImageAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private final JFileChooser fc;
		private final File parentDirectory = new File("Enter your directory");
		
		private LoadImageAction() {
			super("Load image");
			fc = new JFileChooser(parentDirectory);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int found = fc.showOpenDialog((Component) e.getSource());
			
			if (found == JFileChooser.APPROVE_OPTION) {
				System.out.println("Loading file: " + fc.getSelectedFile());
				loadSourceImage(fc.getSelectedFile());
			}
		}
	}
	
	private final class ApplyBlurAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private ApplyBlurAction() {
			super("Apply blur");
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			applyBlur();
		}
	}

	private final class ApplyGraySscaleAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private ApplyGraySscaleAction() {
			super("Apply gray Scale");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			applyGrayScale();
		}
	}
	
	private final class ApplySobelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private ApplySobelAction() {
			super("Apply Sobel");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			applySobel();
		}
	}

	
	public static void main(String[] args) {
		new MainWindow();
	}

}
