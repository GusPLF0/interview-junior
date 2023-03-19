package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM hero" +
            " INNER JOIN power_stats ON hero.power_stats_id = power_stats.id" +
            " WHERE hero.id = :id";

    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM hero" +
            " INNER JOIN power_stats ON hero.power_stats_id = power_stats.id" +
            " WHERE name LIKE :name";

    private static final String UPDATE_HERO_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, power_stats_id = :powerStatsId, updated_at = :updatedAt, enabled = :isEnabled" +
            " WHERE id = :id";

    private static final String DELETE_HERO_QUERY = "DELETE FROM hero" +
            " WHERE id = :id" +
            " RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }

    HeroDTO findById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    FIND_BY_ID_QUERY,
                    params,
                    (rs, rowNum) -> HeroDTO.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .name(rs.getString("name"))
                            .race(Race.valueOf(rs.getString("race")))
                            .enabled(rs.getBoolean("enabled"))
                            .powerStats(PowerStats.builder()
                                    .id(UUID.fromString(rs.getString("power_stats_id")))
                                    .strength(rs.getInt("strength"))
                                    .agility(rs.getInt("agility"))
                                    .dexterity(rs.getInt("dexterity"))
                                    .intelligence(rs.getInt("intelligence"))
                                    .build())
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    ArrayList<HeroDTO> findByName(String name) {
        final String query = "%" + name + "%";
        final Map<String, Object> params = Map.of("name", query);

        try {
            return (ArrayList<HeroDTO>) namedParameterJdbcTemplate.query(
                    FIND_BY_NAME_QUERY,
                    params,
                    (rs, rowNum) -> HeroDTO.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .name(rs.getString("name"))
                            .race(Race.valueOf(rs.getString("race")))
                            .enabled(rs.getBoolean("enabled"))
                            .powerStats(PowerStats.builder()
                                    .id(UUID.fromString(rs.getString("power_stats_id")))
                                    .strength(rs.getInt("strength"))
                                    .agility(rs.getInt("agility"))
                                    .dexterity(rs.getInt("dexterity"))
                                    .intelligence(rs.getInt("intelligence"))
                                    .build())
                            .build()
                    );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


    }

    void update(UUID id, HeroDTO hero) {
        final Map<String, Object> params = Map.of(
                "id", id,
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "updatedAt", Timestamp.from(Instant.now()),
                "isEnabled", hero.isEnabled(),
                "powerStatsId", hero.getPowerStats().getId());

        namedParameterJdbcTemplate.update(UPDATE_HERO_QUERY, params);
    }

    public void delete(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        namedParameterJdbcTemplate.queryForObject(DELETE_HERO_QUERY, params, UUID.class);
    }
}
