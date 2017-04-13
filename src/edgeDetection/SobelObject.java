package edgeDetection;

import java.awt.Color;

public class SobelObject {
	
	private final int pixelX;
	private final int pixelY;
	
	private final double gx;
	private final double gy;
	private final double g;
	private final double direction;
	
	protected SobelObject(SobelObject obj) {
		this.pixelX = obj.pixelX;
		this.pixelY = obj.pixelY;
		this.gx = obj.gx;
		this.gy = obj.gy;
		this.g = obj.g;
		this.direction = obj.direction;	
	}
	
	public SobelObject(int pixelX, int pixelY, double gx, double gy) {
		super();
		
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.gx = gx;
		this.gy = gy;
		
		this.g = Math.sqrt(gx * gx + gy * gy); 
		this.direction = Math.atan(gy / gx);;		
	}
	
	public Color getGAsColor() {
		return toColor(g / 5.66); // sqrt(2) * 4
	}
	
	public Color getGxAsColor() {
		return toColor(gx / 8 + 127); // / 8 + 127 preserving gradient direction
	}
	
	public Color getGyAsColor() {
		return toColor(gy / 8 + 127); // / 8 + 127 preserving gradient direction
	}
	
	public Color getDirectionAsColor() {
		return Color.getHSBColor((float) Math.toDegrees(direction) /360f, 1.0f, 1.0f);
	}
	
	public int getPixelX() { return pixelX; }
	public int getPixelY() { return pixelY; }
	
	public double getGx() { return gx; }
	public double getGy() { return gy; }
	public double getG() { return g; }
	public double getDirection() { return direction; }
	
	private Color toColor(double color) {
		return new Color((int)color<<16 | (int)color<<8 | (int)color);
	}

}
