package edgeDetection;

import java.awt.image.BufferedImage;

// xxxxxxxxxxx
// xooooooooox
// xooooooooox
// xooooooooox
// xxxxxxxxxxx

// x won't be processed
public class SobelEdgeDetector {
	
	private final static int MATIRX_WIDTH = 3;
	private final static int[][] X_MATRIX = 
		{{-1, 0, 1}, 
		 {-2, 0, 2},
		 {-1, 0, 1}};
	
	private final static int[][] Y_MATRIX = 
		{{ 1,  2,  1},
		 { 0,  0,  0},
		 {-1, -2, -1}};
	
	private final BufferedImage image;
	private final int height;
	private final int width;
	
	private final SobelObject[] sobelObjects;
	
	
	//TODO marcsieber: 07.04.2017 image expandation yes or no
	public SobelEdgeDetector(BufferedImage image, int imageExpanded) {
		if (image == null || image.getWidth() < 4 || image.getHeight() < 4 || (imageExpanded > 1 || imageExpanded < 0)) {
			throw new RuntimeException("Image must not be null, smaller than 4x4 pixel and the Image must not be expanded more than one pixel");
		}
		if (imageExpanded == 0) {
			image = ImageUtil.expandImage(image, 1);
		}
		
		this.image = image;
		this.width = image.getWidth() -2;
		this.height = image.getHeight() -2;
		this.sobelObjects = new SobelObject[width * height];
	}
	
	public SobelEdgeDetector process() {
		for (int y = 1; y < image.getHeight()-1; y++) {
			for (int x = 1; x < image.getWidth()-1; x++) {
				int[][] a = createPixelMatrixForPixel(x, y);
				double gx = applySobelMatrix(X_MATRIX, a);
				double gy = applySobelMatrix(Y_MATRIX, a);
				
				sobelObjects[(y-1) * width + x-1] = new SobelObject(x-1, y-1, gx, gy); // -1 -> Image is Expanded by one pixel
			}
		}
		return this;
	}
	
	public SobelObject[] getSobelObjects() {
		return sobelObjects;
	}
	
	private int[][] createPixelMatrixForPixel(int x, int y) {
		int[][] pixels = new int[MATIRX_WIDTH][MATIRX_WIDTH];
		
		pixels[0][0] = image.getRGB(x-1, y-1); 	pixels[0][1] = image.getRGB(x, y-1); 	pixels[0][2] = image.getRGB(x+1, y-1);
		pixels[1][0] = image.getRGB(x-1, y); 	pixels[1][1] = image.getRGB(x, y); 		pixels[1][2] = image.getRGB(x+1, y);
		pixels[2][0] = image.getRGB(x-1, y+1); 	pixels[2][1] = image.getRGB(x, y+1); 	pixels[2][2] = image.getRGB(x+1, y+1);
		
		return pixels;
	}
	
	private int applySobelMatrix(int [][] sobelMatrix, int [][] pixelMatrix) {
		long result = 0;
		for (int y = 0; y < sobelMatrix.length; y++) {
			for (int x = 0; x < sobelMatrix[y].length; x++) {
				result += (sobelMatrix[y][x] * (pixelMatrix[y][x]&255)); // & 255 -> Use only one Color channel
			}
		}
		return (int) result;
	}

}
