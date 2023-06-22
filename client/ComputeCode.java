package client;

import javax.imageio.ImageIO;

import compute.Compute;

import java.awt.image.BufferedImage;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputeCode {

    /*
     * @param args the command line arguments (0: the server ip; 1: the server port, 2:the image path)
     */
    public static void main(String[] args) {
        BufferedImage img = abreImg(args[2]);
        executePDI(img, args[0], args[1]);
    }

    private static void executePDI(BufferedImage img, String ip, String port) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            Compute comp = (Compute) registry.lookup(name);
            byte[] imgBytes = PDI.bufferedImageToByteArray(img);
            PDI task = new PDI(imgBytes);
            byte[] transformedImgBytes = comp.executeTask(task);
            BufferedImage transformedImg = PDI.byteArrayToBufferedImage(transformedImgBytes);
            salvarImg(transformedImg);
        } catch (Exception e) {
            System.err.println("ComputePDI exception:");
            e.printStackTrace();
        }
    }

    private static BufferedImage abreImg(String path) {
        System.out.println("\n\nAbrindo a imagem...");
        try {
            File arquivo = new File(path);
            BufferedImage img = ImageIO.read(arquivo);
            System.out.println("Imagem aberta com sucesso.\n\n");
            return img;
        } catch (Exception e) {
            System.out.println("Erro ao abrir a imagem.\n");
            return null;
        }
    }

    private static void salvarImg(BufferedImage img) {
        try {
            File arquivo = new File("imagem_nova.png");
            ImageIO.write(img, "png", arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao salvar a imagem.");
        }
    }
}