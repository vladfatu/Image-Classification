package com.imageclassification.test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class Test
{
    public static void main(String[] args)
    {
        try
        {
            BufferedImage img = ImageIO.read(new File("http://i.stack.imgur.com/yhCnH.png") );
            BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);

            Graphics2D g = gray.createGraphics();
            g.drawImage(img, 0, 0, null);

            HashSet<Integer> colors = new HashSet<>();
            int color = 0;
            for (int y = 0; y < gray.getHeight(); y++)
            {
                for (int x = 0; x < gray.getWidth(); x++)
                {
                    color = gray.getRGB(x, y);
                    System.out.println(color);
                    colors.add(color);
                }
            }

            System.out.println(colors.size() );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}