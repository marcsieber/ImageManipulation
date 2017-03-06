package filter;

import java.awt.image.BufferedImage;

public abstract class AFilter {
	
	protected final int width;
	protected final int height;
	protected BufferedImage image;
	
	public AFilter(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}
	
	public AFilter(int width, int height, BufferedImage image) {
		super();
		this.width = width;
		this.height = height;
		this.image = image;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public abstract AFilter apply();
	
	public abstract AFilter apply(boolean antiAliased);
	
}
