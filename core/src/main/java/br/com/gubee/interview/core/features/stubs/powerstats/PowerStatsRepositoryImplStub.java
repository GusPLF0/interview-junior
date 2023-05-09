package br.com.gubee.interview.core.features.stubs.powerstats;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.model.PowerStats;

import java.util.UUID;

public class PowerStatsRepositoryImplStub implements PowerStatsRepository {

    UUID FIXED_UUID = UUID.fromString("532bf876-d1fb-4467-ab6b-32c5411f1216");

    private final PowerStats fixedPowerStats = PowerStats.builder()
            .id(FIXED_UUID)
            .strength(1)
            .agility(1)
            .intelligence(1)
            .dexterity(1)
            .build();

    @Override
    public UUID create(PowerStats powerStats) {
        return UUID.randomUUID();
    }

    @Override
    public UUID findByPowerStats(PowerStats powerStats) {
        if (powerStats.getIntelligence() == fixedPowerStats.getIntelligence() &&
                powerStats.getAgility() == fixedPowerStats.getAgility() &&
                powerStats.getDexterity() == fixedPowerStats.getDexterity() &&
                powerStats.getStrength() == fixedPowerStats.getStrength()) {
            return FIXED_UUID;
        }
        return null;
    }

    @Override
    public PowerStats findPowerStatsById(UUID id) {
        if (fixedPowerStats.getId().equals(id)) {
            return fixedPowerStats;
        }
        return null;
    }
}
