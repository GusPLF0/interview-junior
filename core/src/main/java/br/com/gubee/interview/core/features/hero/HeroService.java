package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import br.com.gubee.interview.model.response.CompareHeroesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final PowerStatsService powerStatsService;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID uuid = powerStatsService.verifyUuidForHero(createHeroRequest);

        return heroRepository.create(new Hero(createHeroRequest, uuid));
    }


    public ResponseEntity<HeroDTO> findById(UUID id) {
        HeroDTO heroFounded = heroRepository.findById(id);

        if (heroFounded == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(heroFounded);
    }


    public ResponseEntity<ArrayList<HeroDTO>> findByName(String name) {
        ArrayList<HeroDTO> listOfHeroesFounded = heroRepository.findByName(name);

        return ResponseEntity.ok(listOfHeroesFounded);
    }

    @Transactional
    public ResponseEntity<ChangeInHeroResponse> update(UUID id, UpdateHeroRequest changesInHero) {
        HeroDTO heroToAlter = heroRepository.findById(id);

        if (heroToAlter == null) {
            return ResponseEntity.notFound().build();
        }

        makeChangesInHeroFounded(changesInHero, heroToAlter);

        heroRepository.update(id, heroToAlter);

        return ResponseEntity.noContent().build();

    }
    private void makeChangesInHeroFounded(UpdateHeroRequest changesInHero, HeroDTO heroToAlter) {

        heroToAlter.setEnabled(changesInHero.isEnabled());

        if (changesInHero.getName() != null) {
            heroToAlter.setName(changesInHero.getName());
        }
        if (changesInHero.getRace() != null) {
            heroToAlter.setRace(changesInHero.getRace());
        }
        if (changesInHero.getIntelligence() != null) {
            heroToAlter.getPowerStats().setIntelligence(changesInHero.getIntelligence());
        }
        if (changesInHero.getStrength() != null) {
            heroToAlter.getPowerStats().setStrength(changesInHero.getStrength());
        }
        if (changesInHero.getDexterity() != null) {
            heroToAlter.getPowerStats().setDexterity(changesInHero.getDexterity());
        }
        if (changesInHero.getAgility() != null) {
            heroToAlter.getPowerStats().setAgility(changesInHero.getAgility());
        }


        heroToAlter.getPowerStats().setId(verifyPowerStatusUuidAttForHero(heroToAlter));

    }

    UUID verifyPowerStatusUuidAttForHero(HeroDTO heroFounded) {

        UUID byPowerStats = powerStatsService.findUuidByPowerStats(heroFounded.getPowerStats());

        if (byPowerStats == null) {
            byPowerStats = powerStatsService.create(heroFounded.getPowerStats());
        }

        return byPowerStats;
    }

    @Transactional
    public ResponseEntity<?> delete(UUID heroId) {
        HeroDTO heroFound = heroRepository.findById(heroId);

        if (heroFound == null) {
            return ResponseEntity.notFound().build();
        }

        heroRepository.delete(heroId);

        return ResponseEntity.noContent().build();

    }


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
