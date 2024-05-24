package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter unit cost value: ");
        double unitCost = scanner.nextDouble();

        System.out.println("Enter lead time in months: ");
        double leadTimeInMonths = scanner.nextDouble();

        System.out.println("Enter annual interest rate: ");
        double annualInterestRate = scanner.nextDouble();

        System.out.println("Enter penalty cost per unit: ");
        double penaltyCostPerUnit = scanner.nextDouble();

        System.out.println("Enter ordering cost: ");
        double orderingCost = scanner.nextDouble();

        System.out.println("Enter average monthly demand (4 months): ");
        double averageMonthlyDemand = scanner.nextDouble();

        System.out.println("Enter standard deviation of demand: ");
        double standardDeviationOfDemand = scanner.nextDouble();


        double annualDemand = averageMonthlyDemand * 12;


        InventoryManagement inventoryManagement = new InventoryManagement(unitCost, leadTimeInMonths, annualInterestRate,
                penaltyCostPerUnit, orderingCost, annualDemand, standardDeviationOfDemand);
        inventoryManagement.printResults();
    }
}