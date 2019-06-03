package spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import spring.model.*;
import spring.repository.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.zip.ZipInputStream;

@Controller
public class DataInserter {

    private static final File PAWEL_DATA_DIRECTORY = new File("/media/pawel/Diacetylomorfina/ztis/dataFiles");
    private static final File DEFAULT_DATA_DIRECTORY = new File("dataFiles/");

    private static final int DATE_INDEX = 0;
    public static final int NUMARTS_INDEX = 1;
    public static final int COUNTS_INDEX = 2;
    public static final int THEME_INDEX = 3;
    public static final int LOCATIONS_INDEX = 4;
    public static final int PERSONS_INDEX = 5;
    public static final int ORGANIZATIONS_INDEX = 6;
    public static final int TONE_INDEX = 7;
    public static final int CAMEOEVENT_IDS_INDEX = 8;
    public static final int SOURCES_INDEX = 9;
    public static final int SOURCE_URLS_INDEX = 10;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceUrlRepository sourceUrlRepository;

    private Set<Organization> organizationsToSave = new HashSet<>();
    private Set<Location> locationsToSave = new HashSet<>();
    private Set<Person> peopleToSave = new HashSet<>();
    private Set<Source> sourcesToSave = new HashSet<>();
    private Set<SourceUrl> sourceUrlsToSave = new HashSet<>();

    @GetMapping("/initDb")
    private void insertDataToDb() throws FileNotFoundException {
        final File dataDirectory = DEFAULT_DATA_DIRECTORY;
        for (File file : Objects.requireNonNull(dataDirectory.listFiles())) {
            try (Scanner inputScanner = new Scanner(new ZipInputStream(new FileInputStream(file)))) {
                while (inputScanner.hasNextLine()) {
                    String[] splittedLine = inputScanner.nextLine().split("\t");

                    //organizations
                    Set<Organization> organizations = new HashSet<>();
                    for (String organizationString: splittedLine[ORGANIZATIONS_INDEX].split(";")){
                        Optional<Organization> organizationOptional = organizationRepository.findByName(organizationString);
                        Organization organization = organizationOptional.orElseGet(() -> new Organization(splittedLine[ORGANIZATIONS_INDEX]));
                        organizations.add(organization);
                    }
                    organizationsToSave.addAll(organizations);

                    //locations
                    Set<Location> locations = new HashSet<>();
                    String[] locationStrings = splittedLine[LOCATIONS_INDEX].split(";");
                    for (String locationString : locationStrings) {
                        String[] strings = locationString.split("#")[1].split(", ");
                        if (strings.length == 0) {
                            throw new RuntimeException("Something went wrong");
                        }
                        Location location;
                        if (strings.length == 1) {
                            Optional<Location> locationOptional = locationRepository.findByCountryAndCity(strings[0], LocationRepository.UNKNOWN_CITY);
                            location = locationOptional.orElseGet(() -> new Location(strings[0], LocationRepository.UNKNOWN_CITY));
                            locations.add(location);
                        }
                    }
                    locationsToSave.addAll(locations);

                    //people
                    Set<Person> people = new HashSet<>();
                    for (String personString: splittedLine[PERSONS_INDEX].split(";")){
                        Optional<Person> personOptional = personRepository.findByName(personString);
                        Person person = personOptional.orElseGet(() -> new Person(personString));
                        people.add(person);
                    }
                    peopleToSave.addAll(people);

                    //sources
                    List<Source> sources = new ArrayList<>();
                    for (String sourceString: splittedLine[SOURCES_INDEX].split(";")){
                        Optional<Source> sourceOptional = sourceRepository.findByBaseUrl(sourceString);
                        Source source = sourceOptional.orElseGet(() -> new Source(sourceString));
                        sources.add(source);
                    }
                    sourcesToSave.addAll(sources);

                    //sourceUrls
                    Set<SourceUrl> sourceUrls = new HashSet<>();
                    String[] sourceUrlsStrings = splittedLine[SOURCE_URLS_INDEX].split("<UDIV>");
                    for (int i=0; i<sourceUrlsStrings.length; ++i){
                        Optional<SourceUrl> sourceUrlOptional = sourceUrlRepository.findByExactUrl(sourceUrlsStrings[i]);
                        SourceUrl sourceUrl;
                        if (sourceUrlOptional.isPresent()) {
                            sourceUrl = sourceUrlOptional.get();
                        } else {
                            sourceUrl = new SourceUrl(sourceUrlsStrings[i], sources.get(i));
                        }
                        sourceUrls.add(sourceUrl);
                    }
                    sourceUrlsToSave.addAll(sourceUrls);

                    //eventTypes
                    Set<EventType> eventTypes = new HashSet<>();

                }
            }
        }
    }
}
