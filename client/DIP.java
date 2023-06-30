package client;
import java.awt.image.BufferedImage;
import compute.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;


public class DIP implements Task<byte[]>, Serializable{

    private static final long serialVersionUID = 227L;
    private byte[] image;

    @Override
    public byte[] execute() {
        System.out.println("\n\nExecuting PDI...");
        BufferedImage imgBuff = DIP.byteArrayToBufferedImage(this.image); 
        byte[] transformedImgBytes = DIP.bufferedImageToByteArray(
            lapacianGaussian(
                grayScaleConvertion(imgBuff)
                )
            );
        System.out.println("PDI executed.\n\n");
        return transformedImgBytes;
    }

    public DIP(byte[] image) {
        this.image = image;
    }
    
    public static BufferedImage grayScaleConvertion(BufferedImage img)  {
        if (img == null)
        {
            System.err.println("Image is null in grayScale!");
            return null;
        }

        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int rgb = img.getRGB(i, j);
                int blue = 0xff & rgb;
                int green = 0xff & (rgb >> 8);
                int red = 0xff & (rgb >> 16);
                int lum = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
                gray.setRGB(i, j, lum | (lum << 8) | (lum << 16));
            }
        }

        System.out.println("Image converted to gray scale!!!");
        return gray;
    }

    public static BufferedImage lapacianGaussian(BufferedImage inImage) {
        if (inImage == null) //se nada estiver aberto
        {
            System.err.println("Image is null in lapacianGaussian!");
            return null;
        }

        float[][] lapalcianGaussianMask = {
                           {0, 0, -1, 0, 0},
                           {0, -1, -2, -1, 0},
                           {-1, -2, 16, -2, -1},
                           {0, -1, -2, -1, 0},
                           {0, 0, -1, 0, 0}
                          };
                

        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 2; i < outImage.getWidth() - 2; i++) 
        {
            for (int j = 2; j < outImage.getHeight() - 2; j++) 
            {

                int sum = 0;

                for (int k = -2; k < lapalcianGaussianMask.length - 2; k++) 
                {
                    for (int l = -2; l < lapalcianGaussianMask[k+2].length - 2; l++) 
                    {
                        sum += (int) (inImage.getRGB(i + k, j + l) & 255) * lapalcianGaussianMask[k + 2][l + 2];
                    }
                }
                

                sum = Math.abs(sum/16);

                outImage.setRGB(i, j, sum | (sum << 8) | (sum << 16));
            }
        }
        
        System.out.println("Image transformed by lapacianGaussian!!!");
        outImage = normalizeImg(outImage);
        System.out.println("Image normalized!!!");
        return outImage;
    }

    public static BufferedImage normalizeImg(BufferedImage inImage)
    {
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int min = 255, max = 0;
        for (int i = 0; i < inImage.getWidth(); i++)
        {
            for (int j = 0; j < inImage.getHeight(); j++)
            {
                int tom = inImage.getRGB(i, j) & 0xff;
                if (tom > max)
                    max = tom;
                if (tom < min)
                    min = tom;
            }
        }
        
        int aux = max - min;
        for (int i = 0; i < inImage.getWidth(); i++)
        {
            for (int j = 0; j < inImage.getHeight(); j++)
            {
                int tom = inImage.getRGB(i, j) & 0xff;
                
                int norm = (int) (255 * (tom - min) / aux );
                
                outImage.setRGB(i, j, norm | (norm << 8) | (norm << 16));
            }
        }
        
        return outImage;
    }

    public static byte[] bufferedImageToByteArray(BufferedImage image) {
        try {
            System.out.println("Converting image to byte array...");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            baos.flush();
            byte[] byteArray = baos.toByteArray();
            baos.close();
            System.out.println("Image converted to byte array!!!");
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage byteArrayToBufferedImage(byte[] byteArray) {
        try {
            System.out.println("Converting byte array to image...");
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(bais);
            bais.close();
            System.out.println("Byte array converted to image!!!");
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}