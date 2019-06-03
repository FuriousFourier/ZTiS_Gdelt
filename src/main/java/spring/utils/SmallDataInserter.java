//package spring.utils;
//
//import org.apache.commons.math3.util.Pair;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import spring.model.*;
//import spring.repository.*;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.zip.ZipInputStream;
//
//@Controller
//public class DataInserter {
//
//    private static final File PAWEL_DATA_DIRECTORY = new File("/media/pawel/Diacetylomorfina/ztis/dataFiles");
//    private static final File DEFAULT_DATA_DIRECTORY = new File("dataFiles/");
//
//    private static final int DATE_INDEX = 0;
//    public static final int NUMARTS_INDEX = 1;
//    public static final int COUNTTYPE_INDEX = 2;
//    public static final int NUMBER_INDEX = 3;
//    public static final int OBJECT_TYPE_INDEX = 4;
//    public static final int GEO_FULLNAME_INDEX = 6;
//    public static final int ORGANIZATIONS_INDEX = 6;
//    public static final int TONE_INDEX = 7;
//    public static final int CAMEOEVENT_IDS_INDEX = 12;
//    public static final int SOURCES_INDEX = 13;
//    public static final int SOURCE_URLS_INDEX = 14;
//
//    @Autowired
//    private OrganizationRepository organizationRepository;
//
//    @Autowired
//    private LocationRepository locationRepository;
//
//    @Autowired
//    private PersonRepository personRepository;
//
//    @Autowired
//    private SourceRepository sourceRepository;
//
//    @Autowired
//    private SourceUrlRepository sourceUrlRepository;
//
//    @Autowired
//    private EventTypeRepository eventTypeRepository;
//
//    @Autowired
//    private EventRepository eventRepository;
//
//    @GetMapping("/initDb")
//    public String insertDataToDb() throws FileNotFoundException {
//        System.out.println("ELO");
//        final File dataDirectory = DEFAULT_DATA_DIRECTORY;
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        for (File file : Objects.requireNonNull(dataDirectory.listFiles())) {
//            if (!file.getName().endsWith(".csv")) {
//                continue;
//            }
//            System.out.println("File: " + file.getAbsolutePath());
//            try (Scanner inputScanner = new Scanner(new FileInputStream(file))) {
//                Map<String, Organization> organizationsToSave = new HashMap<>();
//                Map<Pair<String, String>, Location> locationsToSave = new HashMap<>();
//                Map<String, Person> peopleToSave = new HashMap<>();
//                Map<String, Source> sourcesToSave = new HashMap<>();
//                Map<String, SourceUrl> sourceUrlsToSave = new HashMap<>();
//                Map<String, EventType> eventTypesToSave = new HashMap<>();
//                Set<Event> eventsToSave = new HashSet<>();
//                if (!inputScanner.hasNextLine()) {
//                    continue;
//                }
//                inputScanner.nextLine();
//                while (inputScanner.hasNextLine()) {
//                    String nextLine = inputScanner.nextLine();
//                    String[] splittedLine = nextLine.split("\t");
//
//                    if (!splittedLine[CAMEOEVENT_IDS_INDEX].isEmpty() && !splittedLine[COUNTS_INDEX].isEmpty()) {
//
//                        //organizations
//                        Set<Organization> organizations = new HashSet<>();
//                        for (String organizationString: splittedLine[ORGANIZATIONS_INDEX].split(";")){
//                            Optional<Organization> organizationOptional = organizationRepository.findByName(organizationString);
//                            Organization organization;
//                            if (organizationOptional.isPresent()) {
//                                organization = organizationOptional.get();
//                            } else {
//                                organization = organizationsToSave.get(organizationString);
//                                if (organization == null) {
//                                    organization = new Organization(organizationString);
//                                }
//                            }
//                            organizations.add(organization);
//                            organizationsToSave.put(organizationString, organization);
//                        }
//
//                        //locations
//                        Set<Location> locations = new HashSet<>();
//                        String[] locationStrings = splittedLine[LOCATIONS_INDEX].split(";");
//                        for (String locationString : locationStrings) {
//                            String[] split = locationString.split("#");
//                            String[] strings = split[1].split(", ");
//                            if (strings.length == 0) {
//                                throw new RuntimeException("Something went wrong");
//                            }
//                            Location location;
//                            String country, city;
//                            if (strings.length == 1) {
//                                country = strings[0];
//                                city = LocationRepository.UNKNOWN_CITY;
//                            } else {
//                                country = strings[strings.length - 1];
//                                city = strings[0];
//                            }
//                            Pair<String, String> pair = new Pair<>(country, city);
//                            Optional<Location> locationOptional = locationRepository.findByCountryAndCity(country, city);
//                            if (locationOptional.isPresent()) {
//                                location = locationOptional.get();
//                            } else {
//                                location = locationsToSave.get(pair);
//                                if (location == null){
//                                    location = new Location(country, city);
//                                }
//                            }
//                            locations.add(location);
//                            locationsToSave.put(pair, location);
//                        }
//
//                        //people
//                        Set<Person> people = new HashSet<>();
//                        for (String personString: splittedLine[PERSONS_INDEX].split(";")){
//                            Optional<Person> personOptional = personRepository.findByName(personString);
//                            Person person;
//                            if (personOptional.isPresent()) {
//                                person = personOptional.get();
//                            } else {
//                                person = peopleToSave.get(personString);
//                                if (person == null) {
//                                    person = new Person(personString);
//                                }
//                            }
//                            people.add(person);
//                            peopleToSave.put(personString, person);
//                        }
//
//                        //sources
//                        List<Source> sources = new ArrayList<>();
//                        for (String sourceString: splittedLine[SOURCES_INDEX].split(";")){
//                            Optional<Source> sourceOptional = sourceRepository.findByBaseUrl(sourceString);
//                            Source source;
//                            if (sourceOptional.isPresent()) {
//                                source = sourceOptional.get();
//                            } else {
//                                source = sourcesToSave.get(sourceString);
//                                if (source == null) {
//                                    source = new Source(sourceString);
//                                }
//                            }
//                            sources.add(source);
//                            sourcesToSave.put(sourceString, source);
//                        }
//                        Set<Source> sourceSet = new HashSet<>(sources);
//
//                        //sourceUrls
//                        Set<SourceUrl> sourceUrls = new HashSet<>();
//                        String[] sourceUrlsStrings = splittedLine[SOURCE_URLS_INDEX].split("<UDIV>");
//                        for (int i=0; i<sourceUrlsStrings.length; ++i){
//                            Optional<SourceUrl> sourceUrlOptional = sourceUrlRepository.findByExactUrl(sourceUrlsStrings[i]);
//                            SourceUrl sourceUrl;
//                            if (sourceUrlOptional.isPresent()) {
//                                sourceUrl = sourceUrlOptional.get();
//                            } else {
//                                sourceUrl = sourceUrlsToSave.get(sourceUrlsStrings[i]);
//                                if (sourceUrl == null) {
//                                    sourceUrl = new SourceUrl(sourceUrlsStrings[i], sources.get(i));
//                                }
//                            }
//                            sourceUrls.add(sourceUrl);
//                            sourceUrlsToSave.put(sourceUrlsStrings[i], sourceUrl);
//                        }
//
//                        //eventTypes
//                        Set<EventType> eventTypes = new HashSet<>();
//                        String[] strings = splittedLine[COUNTS_INDEX].split(";");
//                        long count = 0;
//                        EventType eventType = null;
//                        for (String splittedETStrings: strings){
//                            String[] eventTypeStrings = splittedETStrings.split("#");
//                            count += Long.parseLong(eventTypeStrings[1]);
//                            Optional<EventType> eventTypeOptional = eventTypeRepository.findByName(eventTypeStrings[0]);
//                            if (eventTypeOptional.isPresent()) {
//                                eventType = eventTypeOptional.get();
//                            } else {
//                                eventType = eventTypesToSave.get(eventTypeStrings[0]);
//                                if (eventType == null) {
//                                    eventType = new EventType(eventTypeStrings[0]);
//                                }
//                            }
//                            eventTypes.add(eventType);
//                            eventTypesToSave.put(eventTypeStrings[0], eventType);
//                        }
//
//                        //events
//                        Event event = new Event(LocalDate.parse(splittedLine[DATE_INDEX], formatter), count, Integer.parseInt(splittedLine[NUMARTS_INDEX]), organizations, eventType, sourceSet, people, locations);
//                        eventsToSave.add(event);
//                        for (EventType type : eventTypes) {
//                            type.getEvents().add(event);
//                        }
//                        for (Location location : locations) {
//                            location.getEvents().add(event);
//                        }
//                        for (Organization organization : organizations) {
//                            organization.getEvents().add(event);
//                        }
//                        for (Person person : people) {
//                            person.getEvents().add(event);
//                        }
//                        for (Source source : sources) {
//                            source.getEvents().add(event);
//                            source.getSourceUrls().addAll(sourceUrls);
//                        }
//                    }
//                }
//                locationRepository.saveAll(locationsToSave.values());
//                organizationRepository.saveAll(organizationsToSave.values());
//                personRepository.saveAll(peopleToSave.values());
//                sourceRepository.saveAll(sourcesToSave.values());
//                sourceUrlRepository.saveAll(sourceUrlsToSave.values());
//                eventTypeRepository.saveAll(eventTypesToSave.values());
//                eventRepository.saveAll(eventsToSave);
//            }
//        }
//        return "greeting";
//    }
//}
