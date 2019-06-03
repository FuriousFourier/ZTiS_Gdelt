package spring.utils;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataDownloader {

    private static final int DEFAULT_BUFFER_SIZE = 200 * 1024;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final Date DEFAULT_BEGINNING_DATE = new Date(118, Calendar.JANUARY, 1);
    private static final Date DEFAULT_ENDING_DATE = new Date(119, Calendar.JANUARY, 1);

    private final File dataDirectory;
    private Date begginningDate;
    private Date endingDate;
    private int initialBufferSize;

    public static void main(String[] args) throws IOException, ParseException {
        DataDownloader downloader = new DataDownloader(new File("/home/lukasz/Documents/ztis/data"));
        downloader.downloadData();
    }

    public DataDownloader(File dataDirectory) {
        this(dataDirectory, DEFAULT_BUFFER_SIZE);
    }

    public DataDownloader(File dataDirectory, int initialBufferSize) {
        this.dataDirectory = dataDirectory;
        this.initialBufferSize = initialBufferSize;
        this.begginningDate = DEFAULT_BEGINNING_DATE;
        this.endingDate = DEFAULT_ENDING_DATE;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    public int getInitialBufferSize() {
        return initialBufferSize;
    }

    public void setInitialBufferSize(int initialBufferSize) {
        this.initialBufferSize = initialBufferSize;
    }

    public Date getBegginningDate() {
        return begginningDate;
    }

    public void setBegginningDate(Date begginningDate) {
        this.begginningDate = begginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public void downloadData() throws ParseException, IOException {
        final Scanner input = new Scanner(System.in);
        final String filesizesUrl = "http://data.gdeltproject.org/gkg/";

        System.out.println("Beginning date: " + DATE_FORMAT.format(begginningDate));
        System.out.println("Ending date: " + DATE_FORMAT.format(endingDate));

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
            String lastLine = null;
            while (fileSizesInput.hasNextLine()){
                lastLine = fileSizesInput.nextLine();
                if (!DATE_FORMAT.parse(lastLine.split(" ")[1].split("\\.")[0]).before(begginningDate)) break;
                ++lineCounter;
            }
            if (!fileSizesInput.hasNextLine()) {
                System.err.println("No data to download, something went wrong");
                System.exit(1);
            }
            while (fileSizesInput.hasNextLine()) {
                String[] line = lastLine.split(" ");
                fileSizesInput.nextLine();
                Date thisDate = DATE_FORMAT.parse(line[1].split("\\.")[0]);
                if (thisDate.after(endingDate)) {
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
                lastLine = fileSizesInput.nextLine();
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
        ByteArrayOutputStream fis = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)){

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
