package br.com.gubee.interview.core.features.stubs.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import br.com.gubee.interview.model.response.CompareHeroesResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Log4j2
public class HeroServiceImplStub implements HeroService {

    private final HeroRepository heroRepository = new HeroRepositoryImplStub();

    @Override
    public UUID create(CreateHeroRequest createHeroRequest) {
        Hero hero = new Hero(createHeroRequest, UUID.randomUUID());
        log.info("Sending hero to database");
        return heroRepository.create(hero);
    }

    @Override
    public ResponseEntity<HeroDTO> findById(UUID heroId) {
        HeroDTO heroFounded = heroRepository.findById(heroId);

        if (heroFounded == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(heroFounded);
    }

    @Override
    public ResponseEntity<List<HeroDTO>> findByName(String name) {
        List<HeroDTO> listOfHeroesFounded = heroRepository.findByName(name);
        if (listOfHeroesFounded == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listOfHeroesFounded);
    }

    @Override
    public ResponseEntity<ChangeInHeroResponse> update(UUID id, UpdateHeroRequest changesInHero) {
        HeroDTO hero = heroRepository.findById(id);

        if (hero == null) {
            return ResponseEntity.notFound().build();
        }

        hero.setEnabled(changesInHero.isEnabled());

        if (changesInHero.getName() != null) {
            hero.setName(changesInHero.getName());
        }
        if (changesInHero.getRace() != null) {
            hero.setRace(changesInHero.getRace());
        }
        if (changesInHero.getIntelligence() != null) {
            hero.getPowerStats().setIntelligence(changesInHero.getIntelligence());
        }
        if (changesInHero.getStrength() != null) {
            hero.getPowerStats().setStrength(changesInHero.getStrength());
        }
        if (changesInHero.getDexterity() != null) {
            hero.getPowerStats().setDexterity(changesInHero.getDexterity());
        }
        if (changesInHero.getAgility() != null) {
            hero.getPowerStats().setAgility(changesInHero.getAgility());
        }


        hero.getPowerStats().setId(hero.getPowerStats().getId());


        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> delete(UUID heroId) {
        HeroDTO hero = heroRepository.findById(heroId);

        if (hero == null) {
            return ResponseEntity.notFound().build();
        }

        heroRepository.delete(heroId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> compare(UUID principalHero, UUID comparedHero) {
        HeroDTO firstHero = heroRepository.findById(principalHero);
        HeroDTO secondHero = heroRepository.findById(comparedHero);

        return ResponseEntity.ok(buildComparation(firstHero, secondHero));

    }

    private CompareHeroesResponse buildComparation(HeroDTO firstHero, HeroDTO secondHero) {
        return CompareHeroesResponse.builder()
                .principalHeroId(firstHero.getId())
                .comparedHeroId(secondHero.getId())
                .intelligence(verifyTheDifference(firstHero.getPowerStats().getIntelligence(), secondHero.getPowerStats().getIntelligence()))
                .strength(verifyTheDifference(firstHero.getPowerStats().getStrength(), secondHero.getPowerStats().getStrength()))
                .agility(verifyTheDifference(firstHero.getPowerStats().getAgility(), secondHero.getPowerStats().getAgility()))
                .dextery(verifyTheDifference(firstHero.getPowerStats().getDexterity(), secondHero.getPowerStats().getDexterity())).build();
    }

    private String verifyTheDifference(Integer statusOfHeroOne, Integer statusOfHeroTwo) {
        if (statusOfHeroOne.compareTo(statusOfHeroTwo) == 0){
            return "=";
        } else if (statusOfHeroOne.compareTo(statusOfHeroTwo) > 0) {
            return "+";
        } else {
            return "-";
        }
    }
}
