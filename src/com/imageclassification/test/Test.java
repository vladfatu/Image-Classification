package com.imageclassification.test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test
{
    public static void main(String[] args)
    {
    	try
		{
			convertToGrayAndPng();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void convertToGrayAndPng() throws IOException {
    	 
        BufferedImage sourceImg = ImageIO.read(new File("source4.jpg") );
     
        // We'll be doing some gray scale magic here soon...
//        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null); 
//        BufferedImage image = op.filter(sourceImg, null);
        
        BufferedImage image = new BufferedImage( sourceImg.getWidth(), sourceImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY );  
        Graphics g = image.getGraphics();  
        g.drawImage( sourceImg, 0, 0, null );  
        g.dispose();
        
     // Write the icon as a PNG
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ImageIO.write( image, "png", out );
//        byte[] byteArray = out.toByteArray();
//        for (int i=0; i< byteArray.length; i++)
//        {
//        	System.out.println(byteArray[i]);
//        }
        
//        System.out.println(byteArray.length);
     
        ImageIO.write( image, "png", new File("output.png") );
        
//        compress(image);
        int a[] = new int[] {1, 0};
        int b[] = new int[] {0, 1};
        System.out.println(euclideanDistance(a, b));
     
    }
    
    public static int [][] compress(BufferedImage image) throws IOException
    {
        Raster image_raster = image.getData();
       
        int[][] original; // where we'll put the image
               
        //get pixel by pixel
        int[] pixel = new int[1];
        int[] buffer = new int[1];
       
        // declaring the size of arrays
        original = new int[image_raster.getWidth()][image_raster.getHeight()];

       
        //get the image in the array
        for(int i = 0 ; i < image_raster.getWidth() ; i++)
            for(int j = 0 ; j < image_raster.getHeight() ; j++)
            {
                pixel = image_raster.getPixel(i, j, buffer);
                original[i][j] = pixel[0];
                System.out.println(pixel[0]);
            }
        System.out.println("width: " + image_raster.getWidth() + ", Height: " +  image_raster.getHeight());
        return original;                   
    }      
    
    public static int[][] generateLBPMatrix(int[][] pixels)
    {
    	return pixels;
    }
    
    public static double euclideanDistance(int[] a, int[] b)
    {
    	int sum = 0;
    	for (int i=0;i<a.length;i++)
    	{
    		sum += ((a[i] - b[i]) * (a[i] - b[i]));
    	}
    	return Math.sqrt(sum);
    }
}