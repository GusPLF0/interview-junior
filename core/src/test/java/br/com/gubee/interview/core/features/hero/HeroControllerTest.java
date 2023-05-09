package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.core.features.stubs.hero.HeroServiceImplStub;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HeroControllerTest {

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

    private HeroController subject;

    private HeroService stub;

    @BeforeEach
    void setUp() {
        stub = new HeroServiceImplStub();
        subject = new HeroController(stub);
    }

    @Test
    void shouldReturnTheCorrectResponse_WhenCreateAHeroRequestWasSended() {
        //given
        CreateHeroRequest hero = CreateHeroRequest.builder()
                .name("Batman")
                .agility(4)
                .intelligence(4)
                .strength(4)
                .dexterity(4)
                .race(Race.HUMAN).build();
        //when
        ResponseEntity<String> response = subject.create(hero);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().containsKey("Location")).isTrue();
        assertThat(response.getHeaders().get("Location")).isNotNull();

    }

    @Test
    void shouldReturnTheCorrectResponse_WhenFindAHeroByIdRequestWasSendedWithAnIdOfASavedHeroInTheDatabaseIsPassed() {
        //given

        UUID idToSearch = FIXED_UUID;

        //when

        ResponseEntity<HeroDTO> response = subject.findWithId(idToSearch);

        //then

        assertThat(response.getBody().getId()).isEqualTo(FIXED_UUID);
        assertThat(response.getBody()).isEqualTo(heroDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnTheCorrectResponse_WhenFindAHeroByIdRequestWasSendedWithAnIdOfAnUnsavedHeroIsPassed() {
        //given

        UUID idToSearch = UUID.randomUUID();

        //when

        ResponseEntity<HeroDTO> response = subject.findWithId(idToSearch);

        //then
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnTheCorrectResponse_WhenFindAHeroByNameRequestWasSendedAndTheHeroWithNameGivedIsInTheDatabase() {
        //given
        String nameToSearch = "Aleatorio";

        //when

        ResponseEntity<List<HeroDTO>> response = subject.findWithName(nameToSearch);

        //then
        assertThat(response.getBody().get(0)).isEqualTo(heroDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnTheCorrectResponse_WhenFindAHeroByNameRequestWasSendedAndTheHeroWithPartOfTheNameGivedIsInTheDatabase() {
        //given
        String nameToSearch = "Ale";

        //when

        ResponseEntity<List<HeroDTO>> response = subject.findWithName(nameToSearch);

        //then
        assertThat(response.getBody().get(0)).isEqualTo(heroDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldFindNothing_WhenThereIsNoHeroWithPartOfTheNameGived_ThenReturnAEmptyListInTheBody() {
        //given
        String nameToSearch = "Bat";

        //when

        ResponseEntity<List<HeroDTO>> response = subject.findWithName(nameToSearch);

        //then
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateAHero_WhenAUpdateHeroRequestWasMade_ThenNoContentStatusCodeInResponse() {
        //given
      UpdateHeroRequest updateHero = UpdateHeroRequest.builder()
              .name("Aleatorio")
              .race(Race.HUMAN)
              .intelligence(5)
              .strength(5)
              .agility(5)
              .dexterity(5)
              .enabled(true).build();
        //when
        ResponseEntity<?> response = subject.update(FIXED_UUID, updateHero);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void shouldNotUpdateAHero_WhenAUpdateHeroRequestWasMadeInAInexistentHeroInDatabase_ThenNotFoundStatusCode() {
        //given
        UpdateHeroRequest updateHero = UpdateHeroRequest.builder()
                .name("Aleatorio")
                .race(Race.HUMAN)
                .intelligence(5)
                .strength(5)
                .agility(5)
                .dexterity(5)
                .enabled(true).build();
        //when
        ResponseEntity<?> response = subject.update(UUID.randomUUID(), updateHero);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void shouldDeleteAHero_WhenADeleteHeroRequestWasMadeWithAExistentUuid_ThenNoContentStatusCode() {
        // given
        // when
        ResponseEntity<?> delete = subject.delete(FIXED_UUID);
        // then
        assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(delete.getBody()).isNull();
    }

    @Test
    void shouldCompareTwoHeroes_WhenACompareHeroRequestWasMadeWithTwoValidAndExistentUuids_ThenShowTheComparationInTheBodyAndOkStatusCode() {
        // given
        UUID idToCompare = FIXED_UUID;
        UUID secondIdToCompare = FIXED_UUID;
        // when
        ResponseEntity<?> response = subject.compare(idToCompare, secondIdToCompare);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}