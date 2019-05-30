package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spring.form.AggregationForm;
import spring.repository.*;

@Controller
public class AggregationController {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceUrlRepository sourceUrlRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/aggregation")
    public String aggregation(Model model) {
        model.addAttribute("aggregationForm", new AggregationForm());

        addAllAttributes(model);

        return "aggregate";
    }

    @PostMapping("/aggregation")
    public String aggregationResult(@ModelAttribute AggregationForm aggregationForm, Model model) {
        System.out.println(aggregationForm);

        model.addAttribute("aggregationForm", new AggregationForm());
        addAllAttributes(model);

        return "aggregate";
    }

    private void addAllAttributes(Model model) {
        model.addAttribute("eventTypes", eventTypeRepository.getAllTypes());

        model.addAttribute("sources", sourceRepository.getAllSources());

        model.addAttribute("countries", locationRepository.getAllCountries());
//        model.addAttribute("cities", locationRepository.getAllCities());

        model.addAttribute("organizations", organizationRepository.getAllOrganizations());

        model.addAttribute("persons", personRepository.getAllPersons());
    }



}
