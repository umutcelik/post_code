package db.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class V2__insert_data extends BaseJavaMigration {

    private static final String COMMA_DELIMITER = ",";


    @Override
    public void migrate(Context context) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

        String line = null;
        log.info("Initializing post code db started");
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:ukpostcodes.csv")))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (validateData(values)) {
                    //postCodeRepository.save()
                    jdbcTemplate.execute("INSERT INTO post_code (post_code, latitude, longitude) VALUES ('" + values[1] + "', " + Double.valueOf(values[2]) + "," + Double.valueOf(values[3]) + ")");
                    count++;
                }

            }
        } catch (Exception e) {
            log.error("invalid data:{}", line);
        }

        log.info("Initializing post code db finished, {} codes added", count);
    }

    public boolean validateData(String[] values) {
        if (values.length != 4) {
            log.error("Invalid line: {}", Arrays.toString(values));
            return false;
        }

        String postCode = values[1];
        String latitude = values[2];
        String longitude = values[3];

        if (postCode.length() != 7) {
            return false;
        }

        try {
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);

            if (lat < -90 || lat > 90) {
                log.error("Invalid latitude: {}", Arrays.toString(values));
                return false;
            }
            if (lon < -180 || lon > 180) {
                log.error("Invalid longitude: {}", Arrays.toString(values));
                return false;
            }

        } catch (NumberFormatException e) {
            log.error("Invalid coordinates: {}", Arrays.toString(values));
            return false;
        }

        return true;
    }
}
