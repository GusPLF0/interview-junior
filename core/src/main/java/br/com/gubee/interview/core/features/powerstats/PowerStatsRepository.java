package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String FIND_BY_ID_POWERS_QUERY = "SELECT * FROM power_stats WHERE id = :id";

    private static final String FIND_BY_POWER_STATS_QUERY = "SELECT id FROM power_stats " +
            "WHERE strength = :strength AND agility = :agility AND dexterity = :dexterity AND intelligence = :intelligence";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(powerStats),
            UUID.class);
    }

    public UUID findByPowerStats(PowerStats powerStats) {
        Map<String, Object> params = new HashMap<>();
        params.put("strength", powerStats.getStrength());
        params.put("agility", powerStats.getAgility());
        params.put("dexterity", powerStats.getDexterity());
        params.put("intelligence", powerStats.getIntelligence());

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    FIND_BY_POWER_STATS_QUERY,
                    params,
                    UUID.class
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public PowerStats findPowerStatsById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    FIND_BY_ID_POWERS_QUERY,
                    params,
                    (rs, rowNum) -> new PowerStats(
                            rs.getInt("strength"),
                            rs.getInt("agility"),
                            rs.getInt("dexterity"),
                            rs.getInt("intelligence"))
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
