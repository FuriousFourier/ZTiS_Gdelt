package spring.calculation;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import spring.controller.ResultModel;
import spring.form.AggregationForm;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Aggregator {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter H2_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final RowMapper<ResultModel> RESULT_MODEL_ROW_MAPPER = (resultSet, i) ->
            new ResultModel(resultSet.getLong(1), resultSet.getLong(2));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static String parseDate(String date) {
        return H2_FORMATTER.format(FORMATTER.parse(date));
    }

    public ResultModel calculate(AggregationForm aggregationForm) {
        String[] dateRange = formatDate(aggregationForm.getFormDateRange());

        StringBuilder selectBuilder = new StringBuilder("select sum(e.count), sum(e.articles_number) from event as e ");

        StringBuilder whereBuilder = new StringBuilder()
                .append("where e.date between '").append(parseDate(dateRange[0])).append("' AND '").append(parseDate(dateRange[1])).append("'");


        if (Strings.isNotEmpty(aggregationForm.getFormPerson())) {
            selectBuilder
                    .append("inner join person_event as pe on e.id = pe.event_id ")
                    .append("inner join person as p on p.id = pe.person_id ");
            whereBuilder.append(" AND p.name = '").append(aggregationForm.getFormPerson()).append("'");
        }


        boolean hasCountry = Strings.isNotEmpty(aggregationForm.getFormCountry());
        boolean hasCity = Strings.isNotEmpty(aggregationForm.getFormCity());
        if (hasCountry || hasCity) {
            selectBuilder
                    .append("inner join location_event as le on e.id = le.event_id ")
                    .append("inner join location as l on l.id = le.location_id ");
        }
        if (hasCountry) {
            whereBuilder.append(" AND l.country = '").append(aggregationForm.getFormCountry()).append("'");
        }
        if (hasCity) {
            whereBuilder.append(" AND l.city = '").append(aggregationForm.getFormCity()).append("'");
        }


        if (Strings.isNotEmpty(aggregationForm.getFormEventType())) {
            selectBuilder.append("inner join event_type as et on e.event_type_id = et.id ");
            whereBuilder.append(" AND et.name = '").append(aggregationForm.getFormEventType()).append("'");
        }


        boolean hasSource = Strings.isNotEmpty(aggregationForm.getFormSource());
        boolean hasSourceUrl = Strings.isNotEmpty(aggregationForm.getFormSourceUrl());
        if (hasSource) {
            selectBuilder
                    .append("inner join source_event as se on e.id = se.event_id ")
                    .append("inner join source as s on s.id = se.source_id ");
            whereBuilder.append(" AND s.base_url = '").append(aggregationForm.getFormSource()).append("'");
        }
        if (hasSourceUrl) {
            if (!hasSource) {
                selectBuilder
                        .append("inner join source_event as se on e.id = se.event_id ")
                        .append("inner join source as s on s.id = se.source_id ");
            }
            selectBuilder
                    .append("inner join source_url as su on su.source_id = s.id ");
            whereBuilder.append(" AND su.exact_url = '").append(aggregationForm.getFormSourceUrl()).append("'");
        }


        if (Strings.isNotEmpty(aggregationForm.getFormOrganization())) {
            selectBuilder
                    .append("inner join organization_event as oe on e.id = oe.event_id ")
                    .append("inner join organization as o on o.id = oe.organization_id ");
            whereBuilder.append(" AND o.name= '").append(aggregationForm.getFormOrganization()).append("'");
        }

        String query = selectBuilder.append(whereBuilder).toString();
        System.out.println(query);

        List<ResultModel> result = jdbcTemplate.query(query, RESULT_MODEL_ROW_MAPPER);

        if (result.isEmpty()) {
            return new ResultModel(0, 0);
        } else {
            return result.get(0);
        }
    }

    public String[] formatDate(String dateRange) {
        String[] split = dateRange.split("-", 2);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }

}
