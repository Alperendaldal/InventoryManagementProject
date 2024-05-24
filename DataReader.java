package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

    private final double targetFraction;
    private double standardNormalVariable;
    private double lowerTailProbability;

    public DataReader(double targetFraction) {
        this.targetFraction = targetFraction;
    }

    public double getStandardNormalVariable() {
        return standardNormalVariable;
    }

    public double getLowerTailProbability() {
        return lowerTailProbability;
    }

    public void readDataFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Data.txt"))) {
            String line;
            double currentFraction;
            double closestFraction = Double.MAX_VALUE;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length < 3) {
                    continue;
                }

                try {
                    currentFraction = Double.parseDouble(parts[1]);
                    double absoluteDifference = Math.abs(targetFraction - currentFraction);

                    if (absoluteDifference < closestFraction) {
                        closestFraction = absoluteDifference;
                        standardNormalVariable = Double.parseDouble(parts[0]);
                        lowerTailProbability = Double.parseDouble(parts[2]);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Ignoring line with parsing error: " + line);
                }
            }
        }
    }
}