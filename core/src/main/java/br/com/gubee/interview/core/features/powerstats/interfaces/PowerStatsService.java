package br.com.gubee.interview.core.features.powerstats.interfaces;

import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PowerStatsService {
    @Transactional
    UUID create(PowerStats powerStats);

    PowerStats createPowerStatsWithHeroRequest(CreateHeroRequest createHeroRequest);

    UUID verifyUuidForHero(CreateHeroRequest createHeroRequest);

    PowerStats findPowerStatsById(UUID id);

    UUID findUuidByPowerStats(PowerStats powerStats);
}
