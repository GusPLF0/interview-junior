package br.com.gubee.interview.core.features.powerstats.interfaces;

import br.com.gubee.interview.model.PowerStats;

import java.util.UUID;

public interface PowerStatsRepository {
    UUID create(PowerStats powerStats);

    UUID findByPowerStats(PowerStats powerStats);

    PowerStats findPowerStatsById(UUID id);
}
