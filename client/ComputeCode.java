package client;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import compute.Compute;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ComputeCode {

    private static String name = "Compute";

    /*
     * @param args the command line arguments (0: the server ip; 1: the server port, 2:the image path, 3:the text to encrypt path, 4:the dataset path)
     */
    public static void main(String[] args) {

        // main loop
        while (true) {
            int option = menu();
            switch (option) {
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 1:
                    executeDIP(args[2], args[0], args[1]);
                    break;
                case 2:
                    executeCryptography(args[3], args[0], args[1]);
                    break;
                case 3:
                    executeDataAnalysis(args[4], args[0], args[1]);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static int menu() {
        //menu wwith options
        System.out.println("Choose an option:");
        System.out.println("1 - DIP (Laplacian Gaussian)");
        System.out.println("2 - Cryptography (Encrypt text file)");
        System.out.println("3 - Chart (Data Analysis)");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
        int option = Integer.parseInt(System.console().readLine());
        return option;
    }

    private static void executeDIP(String imgPath, String ip, String port) {
        //execute laplacian gaussian filter in image

        //set security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //get registry
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            //get compute engine
            Compute computeEng = (Compute) registry.lookup(ComputeCode.name);

            //open image
            BufferedImage img = openImage(imgPath);
            //convert image to byte array, so it can be sent to the server
            byte[] imgBytes = DIP.bufferedImageToByteArray(img);
            //create task
            DIP task = new DIP(imgBytes);
            //execute task and get the transformed image in byte array form
            byte[] transformedImgBytes = computeEng.executeTask(task);
            //convert byte array to image
            BufferedImage transformedImg = DIP.byteArrayToBufferedImage(transformedImgBytes);
            //save output image
            saveImage(transformedImg);
            System.out.println("Image saved successfuly into \"image_borders.png\".");
            System.out.println("\n\n");
        } catch (Exception e) {
            System.err.println("ComputeDIP exception:");
            e.printStackTrace();
        }
    }

    private static void executeCryptography(String textPath, String ip, String port) {
        //execeute cryptography in text file

        //set security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //get registry
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            //get compute engine
            Compute computeEng = (Compute) registry.lookup(ComputeCode.name);

            //get text from file
            String text = openText(textPath);
            //create task
            Cryptography task = new Cryptography(text);
            //get the encrypted text
            String encryptedText = computeEng.executeTask(task);
            //save encrypted text
            saveText(encryptedText);
            System.out.println("Text saved successfuly into \"text_encrypted.txt\".");
            System.out.println("\n\n");
        } catch (Exception e) {
            System.err.println("ComputeCryptography exception:");
            e.printStackTrace();
        }
    }

    private static void executeDataAnalysis(String imagePath, String ip, String port) {
        //execeute cryptography in text file

        //set security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //get registry
            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port));
            //get compute engine
            Compute computeEng = (Compute) registry.lookup(ComputeCode.name);

            //open csv
            DefaultCategoryDataset csv = openCSV(imagePath);
            //create task
            DataAnalysis task = new DataAnalysis(csv);
            //get the chart
            JFreeChart chart = computeEng.executeTask(task);
            //save chart
            saveChart(chart);
            System.out.println("Chart saved successfuly into \"chart.txt\".");
            System.out.println("\n\n");
        } catch (Exception e) {
            System.err.println("ComputeDataAnalisys exception:");
            e.printStackTrace();
        }
    }

    private static BufferedImage openImage(String path) {
        System.out.println("Opening Image...");
        try {
            //open image
            File arquivo = new File(path);
            //read image
            BufferedImage img = ImageIO.read(arquivo);
            System.out.println("Image opened successfuly.\n");
            return img;
        } catch (Exception e) {
            System.err.println("Error opening image.\n");
            return null;
        }
    }

    private static void saveImage(BufferedImage img) {
        try {
            //save image
            File arquivo = new File("outputs/image_borders.png");
            //write image
            ImageIO.write(img, "png", arquivo);
        } catch (Exception e) {
            System.err.println("Error saving image.");
        }
    }
    
    private static String openText(String path) {
        System.out.println("\nOpening Text file...");
        try {
            //open text file
            File arquivo = new File(path);
            //read text file
            Scanner scanner = new Scanner(arquivo);
            //get text
            String text = scanner.nextLine();
            //close text file
            scanner.close();
            System.out.println("Text file opened successfuly.\n");
            return text;
        } catch (Exception e) {
            System.err.println("Error opening text file.\n");
            return null;
        }
    }
    
    private static void saveText(String text) {
        try {
            //save text file
            File arquivo = new File("outputs/text_encrypted.txt");
            //write text file
            PrintWriter writer = new PrintWriter(arquivo);
            writer.println(text);
            writer.close();
        } catch (Exception e) {
            System.err.println("Error saving text file.");
        }
    }

    private static DefaultCategoryDataset openCSV(String csvPath) throws FileNotFoundException, IOException {
        System.out.println("Opening CSV file...");

        //create dataset        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //read csv file
        //start the reader
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            // Skip the header line
            br.readLine();

            //read line by line
            while ((line = br.readLine()) != null) {
                //split line by comma
                String[] columns = line.split(",");
                //separate the string columns
                String country = columns[0];
                String coffeeType = columns[1];

                //for the remaining columns, add the data to the dataset
                for (int i = 2; i < columns.length; i++) {
                    int year = 1990 + (i - 2);
                    long consumption = Long.parseLong(columns[i]);

                    dataset.addValue(consumption, country + "-" + coffeeType, String.valueOf(year));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("CSV file opened successfuly.\n");
        //dataset created
        return dataset;
    }

    private static void saveChart(JFreeChart chart) {
        
        try {
            // ChartFrame frame = new ChartFrame("Coffee Consumption Chart", chart);
            // frame.pack();
            // frame.setVisible(true);

            BufferedImage chartImage = chart.createBufferedImage(800, 600);
            //save image
            File arquivo2 = new File("outputs/chart.png");
            //write image
            ImageIO.write(chartImage, "png", arquivo2);
        } catch (Exception e) {
            System.err.println("Error saving chart.");
            System.err.println(e);
        }
    }
}