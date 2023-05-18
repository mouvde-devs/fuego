import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//        The Testcase, Item, and Result classes are helper classes used for storing and representing the input
//        data and output of the package challenge using the (knapsack algorithm) .
public class KnapsackProblem {
    public static int MAX_WEIGHT = 100;
    public static int MAX_WEIGHT_ITEM = 100;
    public static int MAX_COST = 100;
    public static String OUTPUT = "";

    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("please insert an input file");
            return;
        }

        String filename = args[0];
        // String filename = "C:\\Users\\hicha\\Desktop\\Projects\\test\\fuego\\src\\main\\java\\input.txt";
        List<Testcase> testcases = readTestcasesFromFile(filename);

        for (Testcase testcase : testcases) {
            int capacity = testcase.capacity;
            List<Item> items = testcase.items;

            Result result = knapsack(capacity, items, items.size() - 1);
//            System.out.println("Max Value: " + result.maxValue);
//            System.out.println(/*"Selected Items: " + */result.selectedItems.toString().replaceAll("\\[\\s*\\]", "-").replaceAll("\\[|\\]", ""));
            OUTPUT += result.selectedItems.toString().replaceAll("\\[\\s*\\]", "-").replaceAll("\\[|\\]", "")+"\n";
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(OUTPUT);
            writer.close();
            System.out.println(OUTPUT);
            System.out.println("Text file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the text file.");
            e.printStackTrace();
        }
    }


    private static List<Testcase> readTestcasesFromFile(String filename) {
        List<Testcase> testcases = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            Scanner scanner = new Scanner(new File(filename));

            while ((line = br.readLine()) != null) {
                String[] capacityAndItems = scanner.nextLine().split(" : ");
                int capacity = Integer.parseInt(capacityAndItems[0]);

                String[] itemsString = capacityAndItems[1].split("\\) \\(");
                ArrayList<Item> items = new ArrayList<Item>();
                for (String itemString : itemsString) {
                    itemString = itemString.replaceAll("[()€]", "");
                    String[] itemValues = itemString.split(",");
                    if(Double.parseDouble(itemValues[1]) > MAX_WEIGHT_ITEM){
                        throw new Exception(itemValues[1] +"package weight is greather than 100");
                    }
                    if(Integer.parseInt(itemValues[2]) > MAX_COST){
                        throw new Exception(itemValues[2] +"cost is greather than 100");
                    }
                    if (capacity >= MAX_WEIGHT){
                        throw new Exception(capacity +" is greather than 100");

                    }
                    Item item = new Item(Integer.parseInt(itemValues[0]), Double.parseDouble(itemValues[1]), Integer.parseInt(itemValues[2]));
                    items.add(item);
                }
                testcases.add(new Testcase(capacity, items));
            }
        } catch (Exception e) {
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
        }
        return testcases;
    }
//    methode to resolve the problem using recurison concept to reduce the complexity
//    The method takes three parameters: capacity (the remaining capacity of the knapsack), items (the list of available items to choose from), and index (the current index indicating the item being considered)
    private static Result knapsack(int capacity, List<Item> items, int index) {
//        if the list is empty or the capacity is equal to 0 we return 0
        if (index < 0 || capacity == 0) {
            return new Result(0, new ArrayList<>());
        }
//      getting the item to check it
        Item currentItem = items.get(index);
//        If the weight of the current item is greater than the remaining capacity of the knapsack, it means the item cannot be included. In this case, the method recursively calls itself with the same capacity and moves to the next item by decrementing the index.
        if (currentItem.weight > capacity) {
            return knapsack(capacity, items, index - 1);
        } else {
//        If the weight of the current item is less than or equal to the remaining capacity, two recursive calls are made:
//
//        1 withoutItemResult: The method is called with the same capacity, but moves to the next item by decrementing the
//        index. This represents the case of not including the current item.
//        2 withItemResult: The method is called with the remaining capacity reduced by the weight of the current item, and
//        moves to the next item by decrementing the index. This represents the case of including the current item.

            Result withoutItemResult = knapsack(capacity, items, index - 1);
            Result withItemResult = knapsack(capacity - (int) currentItem.weight, items, index - 1);
//      The maxValue of the withItemResult is increased by the profit of the current item since the item is included
            withItemResult.maxValue += currentItem.profit;
//            System.out.println(withItemResult.selectedItems+" \n");

//        Next, a comparison is made between withItemResult.maxValue and withoutItemResult.maxValue to determine which
//        case (including or not including the current item) has a higher maximum value.
            if (withItemResult.maxValue > withoutItemResult.maxValue) {
                List<Integer> selectedItems = new ArrayList<>(withItemResult.selectedItems);
//                If including the current item results in a higher maximum value, a new list of selected items is created by adding the index
//                of the current item to withItemResult.selectedItems. A new Result object is created with the updated maximum value and the new
//                list of selected items.
                selectedItems.add(currentItem.index);
                return new Result(withItemResult.maxValue, selectedItems);
            } else {
                return withoutItemResult;
            }
        }
    }

    private static class Testcase {
        int capacity;
        List<Item> items;

        Testcase(int capacity, List<Item> items) {
            this.capacity = capacity;
            this.items = items;
        }
    }

    private static class Item {
        int index;
        double weight;
        int profit;

        Item(int index, double weight, int profit) {
            this.index = index;
            this.weight = weight;
            this.profit = profit;
        }
    }

    private static class Result {
        int maxValue;
        List<Integer> selectedItems;

        Result(int maxValue, List<Integer> selectedItems) {
            this.maxValue = maxValue;
            this.selectedItems = selectedItems;
        }
    }

}
