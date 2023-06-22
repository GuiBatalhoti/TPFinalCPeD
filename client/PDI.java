package client;
import java.awt.image.BufferedImage;
import compute.Task;
import java.io.Serializable;


public class PDI implements Task<BufferedImage>, Serializable{

    private static final long serialVersionUID = 227L;
    private static BufferedImage img;

    public PDI(BufferedImage img) {
        PDI.img = img;
    }

    public BufferedImage execute() {
        return laplacianoGaussiana(tonsCinza(img));
    }
    

    public static BufferedImage tonsCinza(BufferedImage img)  {
        if (img == null) //se nada estiver aberto
        {
            return null;
        }

        //imagem de saída em tons de cinza
        BufferedImage cinza = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //loop duplo pela imagem
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int rgb = img.getRGB(i, j); //pegando o valor RGB do pixel

                //manipulação dos bit para pegar o valor de uma cor específica
                int blue = 0xff & rgb;
                int green = 0xff & (rgb >> 8);
                int red = 0xff & (rgb >> 16);

                //cálculo do valor em tons de cinza
                int lum = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                //montagem da imagem de saída em tons de cinza
                cinza.setRGB(i, j, lum | (lum << 8) | (lum << 16));
            }
        }
        return cinza;
    }

    public static BufferedImage laplacianoGaussiana(BufferedImage img) {
        float[][] mascara = {
                           {0, 0, -1, 0, 0},
                           {0, -1, -2, -1, 0},
                           {-1, -2, 16, -2, -1},
                           {0, -1, -2, -1, 0},
                           {0, 0, -1, 0, 0}
                          };
                

        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 2; i < imgSaida.getWidth() - 2; i++) 
        {
            for (int j = 2; j < imgSaida.getHeight() - 2; j++) 
            {

                int soma = 0;

                for (int k = -2; k < mascara.length - 2; k++) 
                {
                    for (int l = -2; l < mascara[k+2].length - 2; l++) 
                    {
                        soma += (int) (img.getRGB(i + k, j + l) & 255) * mascara[k + 2][l + 2];
                    }
                }
                

                soma = Math.abs(soma/16);

                imgSaida.setRGB(i, j, soma | (soma << 8) | (soma << 16));
            }
        }
        
        imgSaida = normalizaImg(imgSaida);

        return imgSaida;
    }

    public static BufferedImage normalizaImg(BufferedImage img)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        int min = 255, max = 0;
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                
                if (tom > max)
                    max = tom;
                if (tom < min)
                    min = tom;
            }
        }
        
        int aux = max - min;
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                
                int norm = (int) (255 * (tom - min) / aux );
                
                saida.setRGB(i, j, norm | (norm << 8) | (norm << 16));
            }
        }
        
        return saida;
    }
}