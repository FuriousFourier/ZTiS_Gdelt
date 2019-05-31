package spring.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.zip.ZipInputStream;

public class Sandbox {

    private static final File PAWEL_DATA_DIRECTORY = new File("/media/pawel/Diacetylomorfina/ztis/dataFiles");
    private static final File DEFAULT_DATA_DIRECTORY = new File("dataFiles/");

    private Sandbox(){}

    public static void pursueSampleAnalysis(String[] args) throws FileNotFoundException {
        File dataFile = null;
        for (File file : Objects.requireNonNull(DEFAULT_DATA_DIRECTORY.listFiles())) {
            if (file.getName().endsWith(".csv.zip")) {
                dataFile = file;
                break;
            }
        }
        if (dataFile == null) {
            System.err.println("No data files");
            System.exit(2);
        }

        System.out.println("Datafile: " + dataFile.getAbsolutePath());
        LinkedList<String[]> input = new LinkedList<>();
        Map<Long, List<String>> map = new HashMap<>();
        long lol =0;
        try(Scanner inputScanner = new Scanner(new ZipInputStream(new FileInputStream(dataFile)))){
            while (true) {
                if (inputScanner.hasNextLine()) {
                    input.add(inputScanner.nextLine().split("\t"));
                    ++lol;
                } else {
                    System.out.println("dupa: " + lol);
                    break;
                }
            }
        }
        if (!input.getFirst()[8].equals("CAMEOEVENTIDS")) {
            System.err.println("Something went wrong");
            System.exit(3);
        }
        input.forEach(strings -> {
            String[] ids = strings[8].split(",");
            for (String id : ids) {
                long idLong = Long.parseLong(id);
                map.merge(idLong, Collections.singletonList(strings[10]), (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
            }
        });
        int counter = 0;
        for (Map.Entry<Long, List<String>> entry : map.entrySet()) {
            if (counter > 10) {
                break;
            }
            System.out.println(entry.getClass());
            for (String s : entry.getValue()) {
                System.out.println("\t" + s);
            }
            counter++;
        }
        System.out.println("Bye");
    }
}
