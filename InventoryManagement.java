package org.example;

import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InventoryManagement {

    private final double orderingCost;
    private final double holdingCostPerUnit;
    private final double annualDemand;
    private final double penaltyCostPerUnit;
    private final double leadTimeInMonths;
    private final double standardDeviationOfDemand;

    private double economicOrderQuantity;
    private double reorderPoint;
    private double actualSafetyStock;
    private double averageLeadTime;

    public InventoryManagement(double unitCost, double leadTimeInMonths, double annualInterestRate,
                               double penaltyCostPerUnit, double orderingCost, double annualDemand,
                               double standardDeviationOfDemand) {

        this.orderingCost = orderingCost;
        this.holdingCostPerUnit = unitCost * annualInterestRate; // Calculate holding cost
        this.annualDemand = annualDemand;
        this.penaltyCostPerUnit = penaltyCostPerUnit;
        this.leadTimeInMonths = leadTimeInMonths;
        this.standardDeviationOfDemand = standardDeviationOfDemand;

        calculateEconomicOrderQuantity(); // Calculate EOQ during initialization
    }

    private void calculateEconomicOrderQuantity() {
        economicOrderQuantity = Math.ceil(Math.sqrt((2 * annualDemand * orderingCost) / holdingCostPerUnit));
    }

    public double calculateOrderUpToLevel() {
        double averageDemandPerLeadTime = annualDemand / leadTimeInMonths;
        reorderPoint = averageDemandPerLeadTime + (standardDeviationOfDemand * serviceLevelZ());
        return reorderPoint;
    }

    private double serviceLevelZ() {
        double orderUpToLevelFraction = calculateOrderUpToLevelFraction();
        DataReader reader = new DataReader(orderUpToLevelFraction);
        try {
            reader.readDataFile();
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
            return 0;
        }
        return reader.getStandardNormalVariable();
    }

    private double calculateOrderUpToLevelFraction() {
        return 1 - (economicOrderQuantity * holdingCostPerUnit) / (penaltyCostPerUnit * annualDemand);
    }

    public double calculateSafetyStock() {
        actualSafetyStock = calculateOrderUpToLevel() - annualDemand / 12;
        return actualSafetyStock;
    }

    public double calculateAverageLeadTimeInMonths() {
        averageLeadTime = 12 * economicOrderQuantity / annualDemand;
        return averageLeadTime;
    }

    public void printResults() {
        DecimalFormat df = new DecimalFormat("#.####");

        System.out.println("Economic Order Quantity (EOQ) = " + economicOrderQuantity + " units");
        System.out.println("Reorder Point (R) = " + calculateOrderUpToLevel() + " units");
        System.out.println("Actual Safety Stock = " + calculateSafetyStock() + " units");
        System.out.println("Average Lead Time = " + calculateAverageLeadTimeInMonths() + " months");
        System.out.println("----------------------------------------");

        System.out.println("Average Annual Holding Cost = $" + df.format(holdingCostPerUnit * (economicOrderQuantity / 2 + actualSafetyStock)));
        System.out.println("Average Annual Ordering Cost = $" + df.format(annualDemand * orderingCost / economicOrderQuantity));
        System.out.println("Average Annual Penalty Cost = $" + df.format(penaltyCostPerUnit * annualDemand * serviceLevelZ() / economicOrderQuantity));
        System.out.println("The Average Time Between The Placement of Orders = " + df.format(averageLeadTime) + " months");
        System.out.println("The Proportion of Order Cycles In Which No Stock Out Occurs = %" + df.format(calculateOrderUpToLevelFraction() * 100));
        System.out.println("The Proportion of Demands That Are Not Met = " + serviceLevelZ() * standardDeviationOfDemand / annualDemand);
    }
}