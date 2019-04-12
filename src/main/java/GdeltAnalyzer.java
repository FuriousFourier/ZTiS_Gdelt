import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GdeltAnalyzer {

    private static final File PAWEL_DATA_DIRECTORY = new File("/media/pawel/diacetylomorfina/ztis/datafiles");
    private static final File DEFAULT_DATA_DIRECTORY = new File("dataFiles/");

    private GdeltAnalyzer(){}

    public static void main(String[] args) throws ParseException, IOException {
        DataDownloader downloader = new DataDownloader(PAWEL_DATA_DIRECTORY);
        downloader.downloadData();
    }
}
