package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//TODO marcsieber: 05.03.2017 better file handling
public class FileUtil {
	
	public static BufferedImage loadImage(String filePath) {
		final File file = new File(filePath);
		BufferedImage image = null;
		if (file.exists()) {
			try {
				image = ImageIO.read(file);
				
			} catch (IOException e) { /* ignore */ }
		}
		return image;
	}
	
	
	public static BufferedImage saveImage(BufferedImage img, String format, String filePath) {
		final File file = new File(filePath);
		BufferedImage image = null;
		if (!file.exists()) {
			try {
				ImageIO.write(img, format, file);
				
			} catch (IOException e) { /* ignore */ }
		}
		return image;
	}

}
