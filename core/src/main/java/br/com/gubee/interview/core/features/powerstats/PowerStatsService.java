package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    public PowerStats createPowerStatsWithHeroRequest(CreateHeroRequest createHeroRequest) {
        return PowerStats.builder()
                .agility(createHeroRequest.getAgility())
                .strength(createHeroRequest.getStrength())
                .intelligence(createHeroRequest.getIntelligence())
                .dexterity(createHeroRequest.getDexterity()).build();
    }

    public UUID verifyUuidForHero(CreateHeroRequest createHeroRequest) {
        PowerStats powerStatsWithHeroRequest = createPowerStatsWithHeroRequest(createHeroRequest);

        UUID uuid = powerStatsRepository.findByPowerStats(powerStatsWithHeroRequest);

        if (uuid != null) {
            return uuid;
        }

        return powerStatsRepository.create(powerStatsWithHeroRequest);
    }

    public PowerStats findPowerStatsById(UUID id) {
        return powerStatsRepository.findPowerStatsById(id);
    }

    public UUID findUuidByPowerStats(PowerStats powerStats) {
        return powerStatsRepository.findByPowerStats(powerStats);
    }
}
