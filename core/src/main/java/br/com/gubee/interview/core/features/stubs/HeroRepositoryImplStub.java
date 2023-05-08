package br.com.gubee.interview.core.features.stubs;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class HeroRepositoryImplStub implements HeroRepository {

    UUID FIXED_UUID = UUID.fromString("124fb970-d1fb-4467-ab6b-32c5411f1216");

    private final PowerStats powerStats = PowerStats.builder()
            .intelligence(4)
            .strength(4)
            .agility(4)
            .dexterity(4).build();

    private final HeroDTO heroDTO = HeroDTO.builder()
            .powerStats(powerStats)
            .race(Race.HUMAN)
            .name("Aleatorio")
            .id(FIXED_UUID)
            .enabled(true).build();

    private final List<HeroDTO> listOfHeroes = new ArrayList<>(Arrays.asList(heroDTO));

    @Override
    public UUID create(Hero hero) {
        log.info("Hero saved in Database");
        return UUID.randomUUID();
    }

    @Override
    public HeroDTO findById(UUID id) {
        if (id.equals(heroDTO.getId())){
            return heroDTO;
        } else {
            return null;
        }
    }

    @Override
    public List<HeroDTO> findByName(String name) {
        String pattern = ".*"+name+".*";
        for (HeroDTO hero : listOfHeroes) {
            if (hero.getName().matches(pattern)){
                return listOfHeroes;
            }
        }
        return null;

    }

    @Override
    public void update(UUID id, HeroDTO hero) {

    }

    @Override
    public void delete(UUID id) {

    }


}
