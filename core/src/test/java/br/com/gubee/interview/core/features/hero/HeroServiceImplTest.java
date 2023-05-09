package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsService;
import br.com.gubee.interview.core.features.stubs.hero.HeroRepositoryImplStub;
import br.com.gubee.interview.core.features.stubs.powerstats.PowerStatsServiceImplStub;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HeroServiceImplTest {

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

    private HeroRepository heroRepository;

    private PowerStatsService powerStatsService;

    private HeroServiceImpl subject;

    @BeforeEach
    void setUp() {
        heroRepository = new HeroRepositoryImplStub();
        powerStatsService = new PowerStatsServiceImplStub();
        subject = new HeroServiceImpl(heroRepository, powerStatsService);
    }

    @Test
    void shouldBeAbleToCreateAHero_WhenAValidCreateHeroRequestWasSended() {
        //given
        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .name("Batman")
                .race(Race.HUMAN)
                .dexterity(1)
                .intelligence(1)
                .agility(1)
                .strength(1).build();

        //when
        UUID uuid = subject.create(heroRequest);

        //then
        assertThat(uuid).isNotNull();
    }

    @Test
    void shouldNotFindAHero_WhenAUuidWhoDoesntExistInDatabaseIsPassed() {
        //given
        UUID uuidToSearch = UUID.randomUUID();
        //when
        ResponseEntity<HeroDTO> heroFounded = subject.findById(uuidToSearch);
        //then
        assertThat(heroFounded.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(heroFounded.getBody()).isNull();
    }

    @Test
    void shouldFindAHero_WhenAUuidWhoExistsInDatabaseIsPassed() {
        //given
        UUID uuidToSearch = FIXED_UUID;
        //when
        ResponseEntity<HeroDTO> heroFounded = subject.findById(uuidToSearch);
        //then
        assertThat(heroFounded.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(heroFounded.getBody()).isEqualTo(heroDTO);
    }

    @Test
    void shouldGiveAEmptyListOfHeroes_WhenThereIsNoHeroWithPartOfTheNameGivedInDataBase() {
        //given
        String name = "Bat";
        //when
        ResponseEntity<List<HeroDTO>> listOfHeroes = subject.findByName(name);
        //then
        assertThat(listOfHeroes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listOfHeroes.getBody()).isNull();
    }

    @Test
    void shouldGiveListOfHeroesWithValues_WhenThereIsHeroesWithPartOfTheNameGivedInDataBase() {
        //given
        String name = "Alea";
        //when
        ResponseEntity<List<HeroDTO>> listOfHeroes = subject.findByName(name);
        //then
        System.out.println(listOfHeroes.getBody());
        assertThat(listOfHeroes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listOfHeroes.getBody()).isNotNull();
        assertThat(listOfHeroes.getBody()).contains(heroDTO);
        assertThat(listOfHeroes.getBody().size()).isEqualTo(1);
    }

    @Test
    void shouldBeAbleToUpdateAHero_WhenAllFieldsNeedToUpdate () {
        //given
        UpdateHeroRequest updateHeroRequest = UpdateHeroRequest.builder()
                .name("Flash")
                .intelligence(2)
                .strength(2)
                .dexterity(2)
                .agility(2)
                .race(Race.CYBORG).build();
        //when
        ResponseEntity<ChangeInHeroResponse> updateResponse = subject.update(FIXED_UUID, updateHeroRequest);
        //then
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(updateResponse.getBody()).isNull();
    }

    @Test
    void shouldBeAbleToUpdateAHero_WhenOneFieldNeedsToUpdate () {
        //given
        UpdateHeroRequest updateHeroRequest = UpdateHeroRequest.builder()
                .name("Flash").build();
        //when
        ResponseEntity<ChangeInHeroResponse> updateResponse = subject.update(FIXED_UUID, updateHeroRequest);
        //then
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(updateResponse.getBody()).isNull();
    }

    @Test
    void testUpdate_HeroRepositoryFindByIdReturnsNull() {
    }

    @Test
    void testUpdate_PowerStatsServiceFindUuidByPowerStatsReturnsNull() {
    }

    @Test
    void testVerifyPowerStatusUuidAttForHero() {
    }

    @Test
    void testVerifyPowerStatusUuidAttForHero_PowerStatsServiceFindUuidByPowerStatsReturnsNull() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testDelete_HeroRepositoryFindByIdReturnsNull() {
    }

    @Test
    void testCompare() {
    }
}
