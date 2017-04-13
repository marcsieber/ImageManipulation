package edgeDetection;

import java.awt.image.BufferedImage;

public class GaussianBlur {
	
	private final static int MATIRX_WIDTH = 3;
	private final static int[][] KERNEL = new int[][]{{1,2,1},{2,4,2},{1,2,1}};
	
	private BufferedImage image;
	private BufferedImage clone;
	
	public GaussianBlur(BufferedImage image) {
		this.image = image;
		clone = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
	}
	
	public GaussianBlur process() {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
				int pixel;
				if (y > 0 && x > 0 && y < image.getHeight() -1 && x < image.getWidth() -1) {
					int[][] pixels = createMatrixFor(x, y);
					pixel = applyGaussianMatrix(KERNEL, pixels);
				} else {
					pixel = image.getRGB(x, y);
				}
				clone.setRGB(x, y, pixel);
			}
		}
		image = clone;
		return this;
	}
	
	private int[][] createMatrixFor(int x, int y) {
		int[][] pixels = new int[MATIRX_WIDTH][MATIRX_WIDTH];
		
		pixels[0][0] = image.getRGB(x-1, y-1); 	pixels[0][1] = image.getRGB(x, y-1); 	pixels[0][2] = image.getRGB(x+1, y-1);
		pixels[1][0] = image.getRGB(x-1, y); 	pixels[1][1] = image.getRGB(x, y); 		pixels[1][2] = image.getRGB(x+1, y);
		pixels[2][0] = image.getRGB(x-1, y+1); 	pixels[2][1] = image.getRGB(x, y+1); 	pixels[2][2] = image.getRGB(x+1, y+1);
		
		return pixels;
	}
	
	private int applyGaussianMatrix(int [][] gaussianMatrix, int [][] colorMatrix) {
		int r = 0, g = 0, b = 0;
		for (int y = 0; y < gaussianMatrix.length; y++) {
			for (int x = 0; x < gaussianMatrix[y].length; x++) {
				r += gaussianMatrix[y][x] * (colorMatrix[y][x] >> 16 & 255);
				g += gaussianMatrix[y][x] * (colorMatrix[y][x] >> 8  & 255);
				b += gaussianMatrix[y][x] * (colorMatrix[y][x]       & 255);
			}
		}
		r /= 16;
		g /= 16;
		b /= 16;
		return 255<< 24 | r<<16 | g<<8 | b;
	}
	
	public BufferedImage getImage() {
		return clone;
	}

}
