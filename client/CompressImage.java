package client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.GZIPOutputStream;

import compute.Task;

public class CompressImage implements Task<String>, Serializable{

    private String inputImagePath;
    private String outputImagePath;

    public String execute() {
        System.out.println("\n\nExecuting Compreesion...");
        compressImage(this.inputImagePath, this.outputImagePath);
        System.out.println("Compreesion executed.\n\n");
        return outputImagePath;
    }

    public CompressImage(String inputImagePath) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = "imagem_compressed.gz";
    }

    public static void compressImage(String imagePath, String compressedImagePath) {
        try {
            FileInputStream inputFile = new FileInputStream(imagePath);
            FileOutputStream outputFile = new FileOutputStream(compressedImagePath);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputFile);

            byte[] buffer = new byte[1000 * 1024];
            int bytesRead;
            while ((bytesRead = inputFile.read(buffer)) != -1) {
                gzipOutputStream.write(buffer, 0, bytesRead);
            }

            inputFile.close();
            gzipOutputStream.close();

            System.out.println("Image compressed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
