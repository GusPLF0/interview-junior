package br.com.gubee.interview.core.features.stubs.powerstats;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsService;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;

import java.util.UUID;

public class PowerStatsServiceImplStub implements PowerStatsService {

    private final PowerStatsRepository powerStatsRepositoryImpl = new PowerStatsRepositoryImplStub();

    @Override
    public UUID create(PowerStats powerStats) {
        return null;
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
    public UUID verifyUuidForHero(CreateHeroRequest createHeroRequest) {
        PowerStats powerStatsWithHeroRequest = createPowerStatsWithHeroRequest(createHeroRequest);

        UUID uuid = powerStatsRepositoryImpl.findByPowerStats(powerStatsWithHeroRequest);

        if (uuid != null) {
            return uuid;
        }

        return powerStatsRepositoryImpl.create(powerStatsWithHeroRequest);
    }

    @Override
    public PowerStats findPowerStatsById(UUID id) {
        return null;
    }

    @Override
    public UUID findUuidByPowerStats(PowerStats powerStats) {
        return null;
    }
}
