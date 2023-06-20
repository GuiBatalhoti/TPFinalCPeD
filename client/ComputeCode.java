package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ComputeCode {

    private static final String imgPath = "imagem.png";

    /*
     * @param args the command line arguments (0: the server ip; 1: the server port)
     */
    public static void main(String[] args) {
        BufferedImage img = abreImg(imgPath);

    }

    private static BufferedImage abreImg(String path) {
        System.out.println("\n\nAbrindo a imagem...");
        try {
            File arquivo = new File(path);
            BufferedImage img = ImageIO.read(arquivo);
            System.out.println("Imagem aberta com sucesso.");
            return img;
        } catch (Exception e) {
            System.out.println("Erro ao abrir a imagem.");
            return null;
        }
    }

    private static void salvarImg(BufferedImage img) {
        try {
            File arquivo = new File("imagem.png");
            ImageIO.write(img, "png", arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao salvar a imagem.");
        }
    }
}