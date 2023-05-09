package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsService;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsServiceImpl implements PowerStatsService {

    private final PowerStatsRepository powerStatsRepositoryImpl;

    @Override
    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepositoryImpl.create(powerStats);
    }

    @Override
    public UUID verifyUuidForHero(CreateHeroRequest createHeroRequest) {
        PowerStats powerStatsWithHeroRequest = createPowerStatsWithHeroRequest(createHeroRequest);

        UUID uuid = this.findUuidByPowerStats(powerStatsWithHeroRequest);

        if (uuid != null) {
            return uuid;
        }

        return powerStatsRepositoryImpl.create(powerStatsWithHeroRequest);
    }

    @Override
    public PowerStats createPowerStatsWithHeroRequest(CreateHeroRequest createHeroRequest) {
        return PowerStats.builder()
                .agility(createHeroRequest.getAgility())
                .strength(createHeroRequest.getStrength())
                .intelligence(createHeroRequest.getIntelligence())
                .dexterity(createHeroRequest.getDexterity()).build();
    }

    @Override
    public PowerStats findPowerStatsById(UUID id) {
        return powerStatsRepositoryImpl.findPowerStatsById(id);
    }

    @Override
    public UUID findUuidByPowerStats(PowerStats powerStats) {
        return powerStatsRepositoryImpl.findByPowerStats(powerStats);
    }
}
