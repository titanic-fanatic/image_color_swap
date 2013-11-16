package com.nwwebdesign.icolorswap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IColorSwap extends Component {

  private static String folderPath;
  private static File fileObject;

public static void main(String[] args) {
	  if (args.length == 0){
		  System.out.println("You must provide a directory to traverse!");
		  System.out.println(" ");
		  System.out.println("Usage: C:\\> java IColorSwap.jar <SOURCE>");
		  System.exit(0);
	  }
	  
	  folderPath = args[0];
	  fileObject = new File(folderPath);
	  
	  if (!fileObject.exists()){
		  System.out.println("The path specified does not exist!");
		  System.exit(0);
	  }
	  
	  if (!fileObject.isDirectory()){
		  System.out.println("The path specified does not point to a directory!");
		  System.exit(0);
	  }
	  
	  new IColorSwap();
  }

private BufferedImage original;
private BufferedImage altered;
private Graphics2D g2;
private static final GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

  public void printPixelARGB(int pixel, int xPixel, int yPixel) {
    int alpha = (pixel >> 24) & 0xff;
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = (pixel) & 0xff;
    
    g2.setBackground(new Color(Color.TRANSLUCENT));
    g2.setColor(new Color(blue, green, red, alpha));
    g2.drawLine(xPixel, yPixel, xPixel, yPixel);
    
    //System.out.println("argb: " + blue + ", " + green + ", " + red + ", " + alpha);
    //System.out.println("x: " + xPixel + ", y: " + yPixel);
    //System.out.println("--------------------------------------------------------------------");
  }

  private void marchThroughImage(BufferedImage image) {
    int w = image.getWidth();
    int h = image.getHeight();

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int pixel = image.getRGB(j, i);
        printPixelARGB(pixel, j, i);
      }
    }
  }

  public IColorSwap() {
    try {
    	File dir = new File(folderPath);
    	for (File child : dir.listFiles()) {
    		
    		if (child.getName().endsWith(".png")){
    			original = ImageIO.read(new File(child.getAbsolutePath()));
	    		altered = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        	g2 = altered.createGraphics();
	        	marchThroughImage(original);
	        	ImageIO.write(altered, "PNG", new File(folderPath + "\\" + child.getName()));
    		}
    	}
    } catch (IOException e) {
    	System.err.println(e.getMessage());
    }
  }

}
