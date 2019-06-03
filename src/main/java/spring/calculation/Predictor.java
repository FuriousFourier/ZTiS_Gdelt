package spring.calculation;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import spring.controller.ResultModel;
import spring.form.PredictionForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static spring.calculation.Aggregator.*;

@Service
public class Predictor {

    public static final NormalDistribution NORMAL_DISTRIBUTION = new NormalDistribution(0, 2);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResultModel calculate(PredictionForm predictionForm) {
        LocalDate date = LocalDate.parse(predictionForm.getFormDate(), FORMATTER).minusYears(1);
        LocalDate dateStart = date.minusDays(7);
        LocalDate dateEnd = date.plusDays(7);

        StringBuilder selectBuilder = new StringBuilder("select sum(e.count), sum(e.articles_number) from event as e ");

        StringBuilder restWhereBuilder = new StringBuilder();

        if (Strings.isNotEmpty(predictionForm.getFormPerson())) {
            selectBuilder
                    .append("inner join person_event as pe on e.id = pe.event_id ")
                    .append("inner join person as p on p.id = pe.person_id ");
            restWhereBuilder.append(" AND p.name = '").append(predictionForm.getFormPerson()).append("'");
        }


        boolean hasCountry = Strings.isNotEmpty(predictionForm.getFormCountry());
        boolean hasCity = Strings.isNotEmpty(predictionForm.getFormCity());
        if (hasCountry || hasCity) {
            selectBuilder
                    .append("inner join location_event as le on e.id = le.event_id ")
                    .append("inner join location as l on l.id = le.location_id ");
        }
        if (hasCountry) {
            restWhereBuilder.append(" AND l.country = '").append(predictionForm.getFormCountry()).append("'");
        }
        if (hasCity) {
            restWhereBuilder.append(" AND l.city = '").append(predictionForm.getFormCity()).append("'");
        }


        if (Strings.isNotEmpty(predictionForm.getFormEventType())) {
            selectBuilder.append("inner join event_type as et on e.event_type_id = et.id ");
            restWhereBuilder.append(" AND et.name = '").append(predictionForm.getFormEventType()).append("'");
        }


        boolean hasSource = Strings.isNotEmpty(predictionForm.getFormSource());
        boolean hasSourceUrl = Strings.isNotEmpty(predictionForm.getFormSourceUrl());
        if (hasSource) {
            selectBuilder
                    .append("inner join source_event as se on e.id = se.event_id ")
                    .append("inner join source as s on s.id = se.source_id ");
            restWhereBuilder.append(" AND s.base_url = '").append(predictionForm.getFormSource()).append("'");
        }
        if (hasSourceUrl) {
            if (!hasSource) {
                selectBuilder
                        .append("inner join source_event as se on e.id = se.event_id ")
                        .append("inner join source as s on s.id = se.source_id ");
            }
            selectBuilder
                    .append("inner join source_url as su on su.source_id = s.id ");
            restWhereBuilder.append(" AND su.exact_url = '").append(predictionForm.getFormSourceUrl()).append("'");
        }


        if (Strings.isNotEmpty(predictionForm.getFormOrganization())) {
            selectBuilder
                    .append("inner join organization_event as oe on e.id = oe.event_id ")
                    .append("inner join organization as o on o.id = oe.organization_id ");
            restWhereBuilder.append(" AND o.name= '").append(predictionForm.getFormOrganization()).append("'");
        }

        String select = selectBuilder.toString();
        String restWhere = restWhereBuilder.toString();

        List<ResultModel> results = new ArrayList<>();
        for (LocalDate currentDate = dateStart;
             currentDate.isBefore(dateEnd) || currentDate.isEqual(dateEnd);
             currentDate = currentDate.plusDays(1)) {

            String whereDate = "where e.date = '" + H2_FORMATTER.format(currentDate) + "'";
            String query = select + whereDate + restWhere;

            List<ResultModel> dayResults = jdbcTemplate.query(query, RESULT_MODEL_ROW_MAPPER);

            if (dayResults.isEmpty()) {
                results.add(new ResultModel(0, 0));
            } else {
                results.add(dayResults.get(0));
            }
        }

        return normalize(results);
    }

    private ResultModel normalize(List<ResultModel> results) {
        if (results.isEmpty()) {
            return new ResultModel(0, 0);
        }

        double eventsResult = 0;
        double articlesResult = 0;
        int size = results.size();

        double startPoint = -(((double) size) / 2 - 1);
        double startProbability = NORMAL_DISTRIBUTION.cumulativeProbability(startPoint);
        ResultModel startResult = results.get(0);
        eventsResult += startResult.getEvents() * startProbability;
        articlesResult += startResult.getArticles() * startProbability;

        for (int i = 1; i < size - 1; i++) {
            double point = startPoint + i;
            double probability = NORMAL_DISTRIBUTION.cumulativeProbability(point) - NORMAL_DISTRIBUTION.cumulativeProbability(point - 1);
            ResultModel result = results.get(i);
            eventsResult += result.getEvents() * probability;
            articlesResult += result.getArticles() * probability;
        }

        double endPoint = ((double) size) / 2 - 1;
        double endProbability = 1 - NORMAL_DISTRIBUTION.cumulativeProbability(endPoint);
        ResultModel endResult = results.get(size - 1);
        eventsResult += endResult.getEvents() * endProbability;
        articlesResult += endResult.getArticles() * endProbability;

        return new ResultModel((long) eventsResult, (long) articlesResult);
    }

}
