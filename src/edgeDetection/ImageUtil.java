package edgeDetection;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Utility class for general image Manipulation tasks.
 * 
 * @author marcsieber
 */
public class ImageUtil {
	
	/**
	 * Expands the Image (top, left, bottom, right) with the number of the given <code>pixelCount</code><br>
	 * It's <b>not recommended</b> to expand a image more than 1 pixel because the corner of the old image (<code>pixelCount*pixelCount</code>) 
	 * is copied as it is to the new images corner.
	 * 
	 * @param image image to expand
	 * @param pixelCount How many pixel expanding
	 * @return
	 */
	public static BufferedImage expandImage(BufferedImage image, int pixelCount) {
		if (image == null && pixelCount <= 0) return image; 
		
		final BufferedImage newImage = new BufferedImage(image.getWidth() + 2 * pixelCount, image.getHeight() +2 * pixelCount, image.getType());
		int imageWidth = image.getWidth(), imageHeight = image.getHeight();
		for (int y = 0; y < pixelCount; y++) {
			for (int x = 0; x < pixelCount; x++) {
				int xRelative = x + pixelCount, yRelative = y + pixelCount;
				
				newImage.setRGB(x, y, 							image.getRGB(x, y)); //top left
				newImage.setRGB(x, yRelative + imageHeight, 	image.getRGB(x, y)); //bottom left
				
				newImage.setRGB(xRelative + imageWidth, y, 							image.getRGB(x, y)); //top right
				newImage.setRGB(xRelative + imageWidth, yRelative + imageHeight, 	image.getRGB(x, y)); //bottom right
			}
		}
		Graphics g = newImage.getGraphics();
		g.drawImage(image, pixelCount, pixelCount, null);
		return newImage;
	}
	
}
