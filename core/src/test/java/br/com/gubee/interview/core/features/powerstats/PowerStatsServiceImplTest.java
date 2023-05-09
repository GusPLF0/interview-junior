package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.core.features.stubs.powerstats.PowerStatsRepositoryImplStub;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PowerStatsServiceImplTest {

    UUID FIXED_UUID = UUID.fromString("532bf876-d1fb-4467-ab6b-32c5411f1216");

    private PowerStatsRepository repository;

    private PowerStatsServiceImpl subject;

    @BeforeEach
    void setUp() {
        repository = new PowerStatsRepositoryImplStub();
        subject = new PowerStatsServiceImpl(repository);
    }

    @Test
    void shouldCreatePowerStats_WhenValidPowerStatsWasPassed() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .intelligence(4)
                .strength(4)
                .agility(4)
                .dexterity(4).build();
        //when
        UUID uuid = subject.create(powerStats);
        //then
        assertThat(uuid).isNotNull();

    }

    @Test
    void shouldCreateAPowerStats_WhenAValidHeroRequestIsPassed() {
        //given
        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .intelligence(1)
                .agility(1)
                .dexterity(1)
                .strength(1).build();
        //when
        PowerStats powerStatsWithHeroRequest = subject.createPowerStatsWithHeroRequest(heroRequest);
        //then
        assertThat(powerStatsWithHeroRequest).isNotNull();
        assertThat(powerStatsWithHeroRequest.getAgility()).isEqualTo(heroRequest.getAgility());
        assertThat(powerStatsWithHeroRequest.getDexterity()).isEqualTo(heroRequest.getDexterity());
        assertThat(powerStatsWithHeroRequest.getStrength()).isEqualTo(heroRequest.getStrength());
        assertThat(powerStatsWithHeroRequest.getIntelligence()).isEqualTo(heroRequest.getIntelligence());

    }

    @Test
    void shouldReturnPowerStats_WhenGiveAUuidThatExistsInTheDataBase() {
        //given
        //when
        PowerStats powerStats = subject.findPowerStatsById(FIXED_UUID);
        //then
        assertThat(powerStats).isNotNull();
    }

    @Test
    void shouldNotReturnPowerStats_WhenGiveAUuidThatDoesntExistsInTheDataBase() {
        //given
        //when
        PowerStats powerStats = subject.findPowerStatsById(UUID.randomUUID());
        //then
        assertThat(powerStats).isNull();
    }

    @Test
    void shouldReturnUuid_WhenGivePowerStatsThatExistsInTheDataBase() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .intelligence(1)
                .strength(1)
                .agility(1)
                .dexterity(1).build();
        //when
        UUID uuidByPowerStats = subject.findUuidByPowerStats(powerStats);
        //then
        assertThat(uuidByPowerStats).isNotNull();
    }

    @Test
    void shouldNotReturnUuid_WhenGivePowerStatsThatDoesntExistsInTheDataBase() {
        //given
        PowerStats powerStats = PowerStats.builder()
                .intelligence(2)
                .strength(3)
                .agility(5)
                .dexterity(7).build();
        //when
        UUID uuidByPowerStats = subject.findUuidByPowerStats(powerStats);
        //then
        assertThat(uuidByPowerStats).isNull();
    }

    @Test
    void shouldVerifyUuidForHero_WhenGiveAValidCreateHeroRequest() {
        //given
        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .intelligence(1)
                .agility(1)
                .dexterity(1)
                .strength(1).build();
        //when
        UUID uuidByPowerStats = subject.verifyUuidForHero(heroRequest);
        //then
        assertThat(uuidByPowerStats).isNotNull();
    }


}