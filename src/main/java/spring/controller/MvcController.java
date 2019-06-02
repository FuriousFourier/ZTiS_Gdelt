package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spring.calculation.Aggregator;
import spring.calculation.Predictor;
import spring.form.AggregationForm;
import spring.form.PredictionForm;
import spring.repository.*;

@Controller
public class MvcController {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private Aggregator aggregator;

    @Autowired
    private Predictor predictor;

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

        model.addAttribute("results", true);
        model.addAttribute("result", aggregationForm);

        model.addAttribute("aggregationResult", aggregator.calculate(aggregationForm));

        addAllAttributes(model);

        return "aggregate";
    }

    @GetMapping("/prediction")
    public String prediction(Model model) {
        model.addAttribute("predictionForm", new PredictionForm());

        addAllAttributes(model);

        return "prediction";
    }

    @PostMapping("/prediction")
    public String predictionResult(@ModelAttribute PredictionForm predictionForm, Model model) {
        System.out.println(predictionForm);
        model.addAttribute("predictionForm", new PredictionForm());

        model.addAttribute("results", true);
        model.addAttribute("result", predictionForm);

        model.addAttribute("predictionResult", predictor.calculate(predictionForm));

        addAllAttributes(model);

        return "prediction";
    }

    private void addAllAttributes(Model model) {
        model.addAttribute("eventTypes", eventTypeRepository.getAllTypes());

        model.addAttribute("sources", sourceRepository.getAllSources());

        model.addAttribute("countries", locationRepository.getAllCountries());

        model.addAttribute("organizations", organizationRepository.getAllOrganizations());

        model.addAttribute("persons", personRepository.getAllPersons());
    }

}
