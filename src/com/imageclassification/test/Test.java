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
    		double minDistance = Double.MAX_VALUE;
    		int closestImage = 0;
	        int[] lbpVector1 = convertToGrayAndPng("test/icon_d09.jpg");
	        printVector(lbpVector1);
	        for (int i=1;i<=25;i++)
	        {
	        	System.out.println("Sample: " + i);
	        	System.out.println();
		        int[] lbpVector2 = convertToGrayAndPng("sample/icon_s" + i + ".jpg");
		        printVector(lbpVector2);
		        
		        double euclideanDistance = euclideanDistance(lbpVector1, lbpVector2);
		        if (euclideanDistance < minDistance)
		        {
		        	minDistance = euclideanDistance;
		        	closestImage = i;
		        }
				System.out.println(euclideanDistance);
		        System.out.println();
	        }
	        
	        System.out.println("Winner is: " + closestImage + " with a distance of: " + minDistance);
	        
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static int[] convertToGrayAndPng(String path) throws IOException {
    	 
        BufferedImage sourceImg = ImageIO.read(new File(path) );
     
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
        
        int[][] pixels = compress(image);
        return generateLBPVector(pixels);
     
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
            }
        System.out.println("width: " + image_raster.getWidth() + ", Height: " +  image_raster.getHeight());
        return original;                   
    }      
    
    public static int[] generateLBPVector(int[][] pixels)
    {
    	int[] vector = new int[256];
    	for (int i=1;i<pixels.length-1;i++)
    	{
    		for (int j=1;j<pixels[i].length-1;j++)
    		{
    			int number = 0;
    			if (pixels[i][j] > pixels[i-1][j-1])
    			{
    				number += Math.pow(2, 7);
    			}
    			if (pixels[i][j] > pixels[i-1][j])
    			{
    				number += Math.pow(2, 6);
    			}
    			if (pixels[i][j] > pixels[i-1][j+1])
    			{
    				number += Math.pow(2, 5);
    			}
    			if (pixels[i][j] > pixels[i][j+1])
    			{
    				number += Math.pow(2, 4);
    			}
    			if (pixels[i][j] > pixels[i+1][j+1])
    			{
    				number += Math.pow(2, 3);
    			}
    			if (pixels[i][j] > pixels[i+1][j])
    			{
    				number += Math.pow(2, 2);
    			}
    			if (pixels[i][j] > pixels[i+1][j-1])
    			{
    				number += 2;
    			}
    			if (pixels[i][j] > pixels[i][j-1])
    			{
    				number += 1;
    			}
    			vector[number]++;
    		}
    	}
    	return vector;
    }
    
    public static double euclideanDistance(int[] a, int[] b)
    {
    	int sum = 0;
    	for (int i=0;i<a.length;i++)
    	{
    		if (a[i] > 0 || b[i] > 0)
    		{
    			sum += ((a[i] - b[i]) * (a[i] - b[i]));
    		}
    	}
    	return Math.sqrt(sum);
    }
    
    public static void printMatrix(int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
    
    public static void printVector(int[] vector)
	{
		for (int i = 0; i < vector.length; i++)
		{
			System.out.print(vector[i] + ", ");
		}
		System.out.println();
	}
}