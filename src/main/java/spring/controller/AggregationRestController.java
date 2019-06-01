package spring.controller;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.model.Source;
import spring.repository.LocationRepository;
import spring.repository.SourceRepository;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/aggregation")
public class AggregationRestController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @GetMapping("/cities")
    public Select2Model getCities(@RequestParam(value = "search", defaultValue = "") String search,
                                  @RequestParam(value = "country", defaultValue = "") String country) {
        System.out.println("Cities request " + country + " " + search);

        if (Strings.isEmpty(country)) {
            return new Select2Model(Collections.emptyList());
        }

        if (Strings.isEmpty(search)) {
            return mapCities(locationRepository.getCitiesForCountry(country));
        } else {
            return mapCities(locationRepository.getCitiesForCountryWithSearch(country, search));
        }

    }

    @GetMapping("/sources/exact")
    public Select2Model getExactSources(@RequestParam(value = "source", defaultValue = "") String source) {
        System.out.println("Sources exact request " + source);

        if (Strings.isEmpty(source)) {
            return new Select2Model(Collections.emptyList());
        } else {
            return new Select2Model(
                    sourceRepository.findByBaseUrl(source)
                            .map(Source::getSourceUrls)
                            .orElse(Collections.emptySet())
                            .stream()
                            .map(sourceUrl -> new Select2Model.Option(sourceUrl.getExactUrl(), sourceUrl.getExactUrl()))
                            .collect(toList())
            );
        }
    }

    private Select2Model mapCities(List<String> cities) {
        return new Select2Model(cities.stream()
                .map(city -> new Select2Model.Option(city, city))
                .collect(toList()));
    }

}
