package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.core.features.powerstats.PowerStatsRepositoryImpl;
import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("it") // Não Cria o banco de dados temporario (?)
@Sql(scripts = "classpath:deleteAll.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class HeroRepositoryITTest {
//   1 -  start a database
//   2 - connect your application to the database
//   3 - trigger a function within your code that writes data to the database
//   4-  check that the expected data has been written to the database by reading the data from the database

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private HeroRepository subject;

    private PowerStatsRepository  powerStatsRepository;

    @BeforeEach
    void setUp() {
        subject = new HeroRepositoryImpl(namedParameterJdbcTemplate);
        powerStatsRepository = new PowerStatsRepositoryImpl(namedParameterJdbcTemplate);
    }

    @Test
    void shouldSaveAndGetAHeroById() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        UUID powerStatsUuid = powerStatsRepository.create(powerStats);

        Hero hero = Hero.builder()
                .name("Superman")
                .race(Race.ALIEN)
                .powerStatsId(powerStatsUuid).build();

        //when
        UUID uuid = subject.create(hero);

        HeroDTO heroSavedInDB = subject.findById(uuid);

        //then
        assertThat(heroSavedInDB).isNotNull();

        assertThat(heroSavedInDB.getName()).isEqualTo(hero.getName());
        assertThat(heroSavedInDB.getRace()).isEqualTo(hero.getRace());

        assertThat(heroSavedInDB.getPowerStats().getIntelligence()).isEqualTo(powerStats.getIntelligence());
        assertThat(heroSavedInDB.getPowerStats().getAgility()).isEqualTo(powerStats.getAgility());
        assertThat(heroSavedInDB.getPowerStats().getDexterity()).isEqualTo(powerStats.getDexterity());
        assertThat(heroSavedInDB.getPowerStats().getStrength()).isEqualTo(powerStats.getStrength());
    }

    @Test
    void shouldSaveAndGetAHeroByName() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        UUID powerStatsUuid = powerStatsRepository.create(powerStats);

        Hero hero = Hero.builder()
                .name("Superman")
                .race(Race.ALIEN)
                .powerStatsId(powerStatsUuid).build();

        //when
        subject.create(hero);
        List<HeroDTO> heroesSavedInDB = subject.findByName(hero.getName());
        HeroDTO heroDTO = heroesSavedInDB.get(0);
        //then
        assertThat(heroesSavedInDB).isNotNull();
        assertThat(heroDTO).isNotNull();

        assertThat(heroDTO.getName()).isEqualTo(hero.getName());
        assertThat(heroDTO.getRace()).isEqualTo(hero.getRace());

        assertThat(heroDTO.getPowerStats().getIntelligence()).isEqualTo(powerStats.getIntelligence());
        assertThat(heroDTO.getPowerStats().getAgility()).isEqualTo(powerStats.getAgility());
        assertThat(heroDTO.getPowerStats().getDexterity()).isEqualTo(powerStats.getDexterity());
        assertThat(heroDTO.getPowerStats().getStrength()).isEqualTo(powerStats.getStrength());
    }



    @Test
    void shouldUpdateAndGetAHeroById() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        UUID powerStatsUuid = powerStatsRepository.create(powerStats);
        powerStats.setId(powerStatsUuid);

        Hero hero = Hero.builder()
                .name("Superman")
                .race(Race.ALIEN)
                .powerStatsId(powerStatsUuid).build();

        UUID heroUuid = subject.create(hero);

        HeroDTO heroDTO = HeroDTO.builder()
                .id(heroUuid)
                .name("Batman")
                .race(Race.HUMAN)
                .powerStats(powerStats)
                .build();

        //when
        subject.update(heroUuid, heroDTO);

        HeroDTO updatedHero = subject.findById(heroUuid);

        //then
        assertThat(updatedHero).isNotNull();

        assertThat(updatedHero.getRace()).isEqualTo(Race.HUMAN);
        assertThat(updatedHero.getName()).isEqualTo("Batman");
    }

    @Test
    void shouldDeleteAHero() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        UUID powerStatsUuid = powerStatsRepository.create(powerStats);

        Hero hero = Hero.builder()
                .name("Superman")
                .race(Race.ALIEN)
                .powerStatsId(powerStatsUuid).build();

        UUID heroUuid = subject.create(hero);


        //when
        subject.delete(heroUuid);

        HeroDTO heroFounded = subject.findById(heroUuid);

        //then
        assertThat(heroFounded).isNull();

    }
}