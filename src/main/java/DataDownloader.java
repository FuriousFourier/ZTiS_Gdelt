import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DataDownloader {

    private static final File PAWEL_DATA_DIRECTORY = new File("/media/pawel/diacetylomorfina/ztis/datafiles");
    private static final File DEFAULT_DATA_DIRECTORY = new File("dataFiles/");
    private static final int INITIAL_BUFFER_SIZE = 200 * 1024;

    public static void main(String[] args) throws ParseException, IOException {
        final Scanner input = new Scanner(System.in);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        final Date DEFAULT_BEGINNING_DATE = dateFormat.parse("20180401");
        final Date DEFAULT_ENDING_DATE = dateFormat.parse("20190406");
        final String filesizesUrl = "http://data.gdeltproject.org/gkg/";

        System.out.println("Default beginning date: " + dateFormat.format(DEFAULT_BEGINNING_DATE));
        System.out.println("Default ending date: " + dateFormat.format(DEFAULT_ENDING_DATE));

        File dataDirectory = PAWEL_DATA_DIRECTORY;
        if (!dataDirectory.exists()) {
            System.out.println("Directory " + dataDirectory.getAbsolutePath() + " doesn't exist, I'll try to create it");
            if (!dataDirectory.mkdirs()) {
                System.err.println("I couldn't create directory, I ragequit");
                System.exit(3);
            }
        }
        long sumSizes = 0;
        long lineCounter = 0;
        List<String> filenames = new LinkedList<>();
        try (Scanner fileSizesInput = new Scanner(new ByteArrayInputStream(downloadToMemory(filesizesUrl + "filesizes")))){
            while (fileSizesInput.hasNextLine()
                    && dateFormat.parse(fileSizesInput.nextLine().split(" ")[1].split("\\.")[0]).before(DEFAULT_BEGINNING_DATE)){
                ++lineCounter;
            }
            if (!fileSizesInput.hasNextLine()) {
                System.err.println("No data to download, something went wrong");
                System.exit(1);
            }
            while (fileSizesInput.hasNextLine()) {
                String[] line = fileSizesInput.nextLine().split(" ");
                Date thisDate = dateFormat.parse(line[1].split("\\.")[0]);
                if (thisDate.after(DEFAULT_ENDING_DATE)) {
                    break;
                }
                sumSizes += Long.parseLong(line[0]);
                filenames.add(line[1]);

                ++lineCounter;
                //Each big file is preceding with small file that is ignored
                if (!fileSizesInput.hasNextLine()) {
                    System.err.println("Before big file there is no small file, line number " + lineCounter);
                    System.err.println("Date of file: " + thisDate + "\tEnding date:" + DEFAULT_ENDING_DATE);
                    System.exit(2);
                }
                ++lineCounter;
                fileSizesInput.nextLine();
            }
            System.out.println("Needed size for downloaded ZIPPED files: " + convertSizeToString(sumSizes) + ". Would you like to continue?");
            while(true) {
                String inputLine = input.nextLine();

                if (inputLine.toLowerCase().equals("n")) {
                    System.out.println("Lel K. Bye");
                    System.exit(0);
                } else if (inputLine.toLowerCase().equals("y")) {
                    break;
                }
                System.out.println("I don't understand you. Would you like to continue?");
            }
            final List<String> fails = new LinkedList<>();
            filenames.forEach(filename -> {
                try {
                    System.out.println("Downloading: " + filename);
                    downloadUsingStream(filesizesUrl + filename, dataDirectory.getAbsolutePath() +"/" + filename);
                } catch (IOException e) {
                    fails.add(filename);
                    System.err.println("Error:");
                    e.printStackTrace();
                }
            });
            System.out.println("Files that failed to be downloaded:");
            fails.forEach(System.out::println);
            System.out.println("Bye");
            System.exit(0);
        }

    }

    //inspired by https://www.journaldev.com/924/java-download-file-url
    private static byte[] downloadToMemory(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        try(BufferedInputStream bis = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream fis = new ByteArrayOutputStream(INITIAL_BUFFER_SIZE)){

            byte[] buffer = new byte[1024];
            int count;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
            return fis.toByteArray();
        }
    }

    //from https://www.journaldev.com/924/java-download-file-url
    private static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        try(BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
        }
    }

    private static String convertSizeToString(long size) {
        return new StringBuilder().append(size).append("B ")
                .append(size / 1024).append("KB ")
                .append(size / 1024 / 1024).append("MB ")
                .append(size / 1024 / 1024 / 1024).append("GB")
                .toString();
    }
}
