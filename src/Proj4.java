import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        // Read the dataset
        ArrayList<String> dataset = new ArrayList<>();
        for (int i = 0; i < numLines && inputFileNameScanner.hasNextLine(); i++) {
            dataset.add(inputFileNameScanner.nextLine());
        }

        // Close the input file
        inputFileNameScanner.close();

        // Create sorted, shuffled, and reversed datasets
        ArrayList<String> sortedDataset = new ArrayList<>(dataset);
        Collections.sort(sortedDataset);

        ArrayList<String> shuffledDataset = new ArrayList<>(dataset);
        Collections.shuffle(shuffledDataset);

        ArrayList<String> reversedDataset = new ArrayList<>(dataset);
        Collections.sort(reversedDataset, Collections.reverseOrder());

        // Initialize the hash table
        SeparateChainingHashTable<String> hashTable = new SeparateChainingHashTable<>();

        // Time and perform operations for each dataset
        timeOperations(hashTable, sortedDataset, "Sorted");
        timeOperations(hashTable, shuffledDataset, "Shuffled");
        timeOperations(hashTable, reversedDataset, "Reversed");
    }

    private static void timeOperations(SeparateChainingHashTable<String> hashTable, ArrayList<String> dataset, String type) throws IOException {
        long startTime, endTime;

        // Insert
        startTime = System.nanoTime();
        for (String item : dataset) {
            hashTable.insert(item);
        }
        endTime = System.nanoTime();
        long insertTime = endTime - startTime;

        // Search
        startTime = System.nanoTime();
        for (String item : dataset) {
            hashTable.contains(item);
        }
        endTime = System.nanoTime();
        long searchTime = endTime - startTime;

        // Delete
        startTime = System.nanoTime();
        for (String item : dataset) {
            hashTable.remove(item);
        }
        endTime = System.nanoTime();
        long deleteTime = endTime - startTime;

        // Output results to console
        System.out.printf("%s Dataset: Insert Time = %d ns, Search Time = %d ns, Delete Time = %d ns\n",
                type, insertTime, searchTime, deleteTime);

        // Append results to analysis.txt
        try (FileOutputStream fos = new FileOutputStream("analysis.txt", true)) {
            String result = String.format("%s,%d,%d,%d\n", type, insertTime, searchTime, deleteTime);
            fos.write(result.getBytes());
        }

        // Ensure the hash table is empty
        hashTable.makeEmpty();
    }
}
