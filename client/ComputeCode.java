package client;

import javax.imageio.ImageIO;

import compute.Compute;

import java.awt.image.BufferedImage;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputeCode {

    private static BufferedImage img;

    /*
     * @param args the command line arguments (0: the server ip; 1: the server port, 2:the image path)
     */
    public static void main(String[] args) {

        while (true) {
            int option = menuLoop();
            switch (option) {
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 1:
                    img = openImage(args[2]);
                    executeDIP(img, args[0], args[1]);
                    break;
                case 2:
                    executeCryptography(args[2], args[0], args[1]);
                case 3:
                    executeCompression(args[2], args[0], args[1]);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static int menuLoop() {
        System.out.println("Choose an option:");
        System.out.println("1 - DIP (Laplacian Gaussian)");
        System.out.println("2 - Cryptography (Encrypt the image)");
        System.out.println("3 - Compression (Compress the image)");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
        int option = Integer.parseInt(System.console().readLine());
        return option;
    }

    private static void executeDIP(BufferedImage img, String ip, String port) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            Compute comp = (Compute) registry.lookup(name);
            byte[] imgBytes = DIP.bufferedImageToByteArray(img);
            DIP task = new DIP(imgBytes);
            byte[] transformedImgBytes = comp.executeTask(task);
            BufferedImage transformedImg = DIP.byteArrayToBufferedImage(transformedImgBytes);
            saveImage(transformedImg);
        } catch (Exception e) {
            System.err.println("ComputeDIP exception:");
            e.printStackTrace();
        }
    }

    private static void executeCryptography(String imagePath, String ip, String port) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            Compute comp = (Compute) registry.lookup(name);
            Cryptography task = new Cryptography(imagePath);
            String encryptedImagePath = comp.executeTask(task);
            System.out.println("Image encrypted successfully onto \"" + encryptedImagePath + "\".");
        } catch (Exception e) {
            System.err.println("ComputeCryptography exception:");
            e.printStackTrace();
        }
    }

    private static void executeCompression(String imagePath, String ip, String port) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            Compute comp = (Compute) registry.lookup(name);
            CompressImage task = new CompressImage(imagePath);
            String compressedImagePath = comp.executeTask(task);
            System.out.println("Image compressed successfully onto \"" + compressedImagePath + "\".");
        } catch (Exception e) {
            System.err.println("ComputeCompression exception:");
            e.printStackTrace();
        }
    }

    private static BufferedImage openImage(String path) {
        System.out.println("\n\nOpening Image...");
        try {
            File arquivo = new File(path);
            BufferedImage img = ImageIO.read(arquivo);
            System.out.println("Image opened successfuly.\n\n");
            return img;
        } catch (Exception e) {
            System.err.println("Error opening image.\n");
            return null;
        }
    }

    private static void saveImage(BufferedImage img) {
        try {
            File arquivo = new File("image_borders.png");
            ImageIO.write(img, "png", arquivo);
            System.out.println("Image saved successfuly into \"image_borders.png\".");
        } catch (Exception e) {
            System.err.println("Error saving image.");
        }
    }
}