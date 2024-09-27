import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

public class Optimization {

    public static class ColorSchemeOptimization extends AbstractProblem {
        private static final int RGB = 3;
        // private final int[][] referenceScheme; // The reference color scheme
        private final HashMap<String, Integer> referenceScheme;
        private final List<String> componentName;
        private final List<int[]> dataArray;

        public ColorSchemeOptimization(HashMap<String, Integer> referenceScheme, List<String> componentName,
                                       List<int[]> dataArray) {
            super((componentName.size()) * RGB, 2);
            this.referenceScheme = referenceScheme;
            this.componentName = componentName;
            this.dataArray = dataArray;
        }

        @Override
        public Solution newSolution() {
            Solution solution = new Solution(numberOfVariables, numberOfObjectives);

            // Define bounds for RGB values
            for (int i = 0; i < numberOfVariables; i++) {
                solution.setVariable(i, new RealVariable(0, 256));
            }
            return solution;
        }

        @Override
        public void evaluate(Solution solution) {
            double[] variables = EncodingUtils.getReal(solution);
            double chargeConsumption = 0.0;
            double colorDeviation = 0.0;

            int i = 0;
            for (String key : componentName) {
                    // Extract the RGB values for the current pixel
                    int r = (int) Math.round(variables[i * RGB]);
                    int g = (int) Math.round(variables[i * RGB + 1]);
                    int b = (int) Math.round(variables[i * RGB + 2]);

                    // Calculate the charge consumption
                    chargeConsumption += ChargeConsumption.calculateChargeConsumptionPerPixel(r, g, b)
                                                                                * referenceScheme.get(key);

                    // Calculate the Euclidean distance for color deviation
                    int refR = dataArray.get(i)[0];
                    int refG = dataArray.get(i)[1];
                    int refB = dataArray.get(i)[2];
                    colorDeviation += Math.sqrt(Math.pow(r - refR, 2) + Math.pow(g - refG, 2) + Math.pow(b - refB, 2))
                                                                                * referenceScheme.get(key);
                    i++;
                }

            // Set the objectives
            solution.setObjective(0, chargeConsumption); // Objective 1: Minimize charge consumption
            solution.setObjective(1, colorDeviation); // Objective 2: Minimize color deviation
        }

    }


    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        // Draw the resized image
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }

    private static void extractRGBValues(BufferedImage image, int i, HashMap<String, Integer> calPixels,
                                         List<String> componentName) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Loop through each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                double distance = Math.sqrt(Math.pow((rgb >> 16) & 0xFF - 0, 2) + Math.pow((rgb >> 8) & 0xFF - 0, 2) +
                        Math.pow(rgb & 0xFF - 255, 2));
                if (distance < 100) {
                    String key = componentName.get(i);
                    if (calPixels.containsKey(key)) {
                        calPixels.put(key, calPixels.get(key) + 1);
                    } else {
                        calPixels.put(key, 1);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {

        String filePath = "examples/color.csv";
        // String filePath = "examples/colorSimp.csv";

        List<int[]> dataArray = new ArrayList<>();
        List<String> componentName = new ArrayList<>();
        HashMap<String, Integer> calPixels = new HashMap<>();

        ArrayList<File> fileArrayList = CalculatorFiles.CalculatorFiles();
        //ArrayList<File> fileArrayList = SimpleFiles.SimpleFiles();


        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int[] data = new int[3];
                componentName.add(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    data[i - 1] = Integer.parseInt(parts[i]);
                }
                dataArray.add(data);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        try {
            for (int i = 0; i < fileArrayList.size(); i++) {
                BufferedImage originalImage = ImageIO.read(fileArrayList.get(i));
                BufferedImage resizedImage = resizeImage(originalImage, 2560, 1440);
                // ImageIO.write(resizedImage, "PNG", new File("examples/test.png"));
                // Extract RGB values into a 2D array
                extractRGBValues(resizedImage, i, calPixels, componentName);
            }


        } catch (IOException e) {
            System.out.println("Error occurred when reading the file: " + e.getMessage());
        }


        // System.out.println(count);
        Problem problem = new ColorSchemeOptimization(calPixels, componentName, dataArray);

        // Generate first and last generations
        // Task4.StepAlgorithm.stepAlgorithm(problem);
        ExecutorProblem.executeProblem(problem, componentName);
    }
}

