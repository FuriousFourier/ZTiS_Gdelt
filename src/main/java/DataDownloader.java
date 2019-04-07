import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class DataDownloader {

    private static final long MAX_SIZE = 15L * 1024 * 1024 * 1024;
    private static final File DATA_DIRECTORY = new File("dataFiles");

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Kasować dane?");
        if (inputScanner.nextLine().equals("tak")) {
            Process exec = Runtime.getRuntime().exec("rm -rf " + DATA_DIRECTORY.getName());
            try (Scanner removerInput = new Scanner(exec.getInputStream())) {
                while (exec.isAlive()) {
                    /*if (removerInput.hasNext()) {
                        System.out.println(removerInput.next());
                    }*/
                    Thread.sleep(100);
                }
            }
        }
        long sumSize = 0;
        try (Scanner scanner = new Scanner(new URL("http://data.gdeltproject.org/gdeltv2/masterfilelist.txt").openStream())) {
            while (sumSize < MAX_SIZE && scanner.hasNextLine()) {
                String dataUrlString = scanner.nextLine().split("\\s")[2];
                String[] split = dataUrlString.split("/");
                File dataFile = new File(DATA_DIRECTORY + "/" + split[split.length - 1]);
                Process wgetProcess = Runtime.getRuntime().exec("wget " + dataUrlString + " -O " + DATA_DIRECTORY.getName() + "/" + dataFile.getName());
                while (wgetProcess.isAlive()) {
                    try (Scanner processOutput = new Scanner(wgetProcess.getInputStream())) {
                        if (processOutput.hasNext()) {
                            System.out.println(processOutput.next());
                        }
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {
                    }
                }
                sumSize += dataFile.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
