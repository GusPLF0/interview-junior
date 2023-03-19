package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeroController.class)
class HeroControllerTest {

    private static final String API_URL = "/api/v1/heroes";

    private static final UUID HERO_ID = UUID.randomUUID();

    private static final UUID HERO2_ID = UUID.randomUUID();

    private static final PowerStats POWER_STATS = new PowerStats(5, 5, 5, 5);

    private static final HeroDTO HERO_DTO = new HeroDTO(HERO_ID, "Batman", Race.HUMAN, true, POWER_STATS);

    private static final HeroDTO HERO_DTO2 = new HeroDTO(HERO2_ID, "Superman", Race.ALIEN, true, POWER_STATS);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HeroService heroService;

    @BeforeEach
    public void initTest() {
        when(heroService.create(any())).thenReturn(UUID.randomUUID());
    }

    @Test
    void createAHeroWithAllRequiredArguments() throws Exception {
        //given
        // Convert the hero request into a string JSON format stub.
        final String body = objectMapper.writeValueAsString(createHeroRequest());

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/v1/heroes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        //then
        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verify(heroService, times(1)).create(any());
    }

    @Test
    void getHeroById_ReturnsOk() throws Exception {
        //given
        when(heroService.findById(eq(HERO_ID))).thenReturn(ResponseEntity.ok(HERO_DTO));

        //when
        final ResultActions resultActions = mockMvc.perform(get("/api/v1/heroes/{id}", HERO_ID));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(HERO_ID.toString()))
                .andExpect(jsonPath("$.name").value(HERO_DTO.getName()))
                .andExpect(jsonPath("$.race").value(HERO_DTO.getRace().toString()))
                .andExpect(jsonPath("$.power_stats.agility").value(5))
                .andExpect(jsonPath("$.power_stats.strength").value(5))
                .andExpect(jsonPath("$.power_stats.dexterity").value(5))
                .andExpect(jsonPath("$.power_stats.intelligence").value(5));
        verify(heroService, times(1)).findById(HERO_ID);
    }

    @Test
    void getHeroById_ReturnsNotFound() throws Exception {
        // Given
        when(heroService.findById(any(UUID.class))).thenReturn(ResponseEntity.notFound().build());

        // When
        final ResultActions resultActions = mockMvc.perform(get(API_URL + "/{id}", HERO_ID));

        // Then
        resultActions.andExpect(status().isNotFound());
        verify(heroService, times(1)).findById(HERO_ID);
    }

    @Test
    void getHeroesByFullName_ReturnsOk() throws Exception {
        // Given
        final ArrayList<HeroDTO> heroList = new ArrayList<>();
        heroList.add(HERO_DTO);
        when(heroService.findByName("Batman")).thenReturn(ResponseEntity.ok(heroList));

        // When
        final ResultActions resultActions = mockMvc.perform(get(API_URL)
                .queryParam("name", "Batman"));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(HERO_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Batman"))
                .andExpect(jsonPath("$[0].race").value(HERO_DTO.getRace().toString()))
                .andExpect(jsonPath("$[0].power_stats.agility").value(5))
                .andExpect(jsonPath("$[0].power_stats.strength").value(5))
                .andExpect(jsonPath("$[0].power_stats.dexterity").value(5))
                .andExpect(jsonPath("$[0].power_stats.intelligence").value(5));
        verify(heroService, times(1)).findByName("Batman");
    }

    @Test
    void getHeroesByPartOfTheName_ReturnsOkAndTwoHeroes() throws Exception {
        // Given
        final ArrayList<HeroDTO> heroList = new ArrayList<>();
        heroList.add(HERO_DTO);
        heroList.add(HERO_DTO2);
        when(heroService.findByName("man")).thenReturn(ResponseEntity.ok(heroList));

        // When
        final ResultActions resultActions = mockMvc.perform(get(API_URL)
                .queryParam("name", "man"));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(HERO_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Batman"))
                .andExpect(jsonPath("$[0].race").value(HERO_DTO.getRace().toString()))
                .andExpect(jsonPath("$[0].power_stats.agility").value(5))
                .andExpect(jsonPath("$[0].power_stats.strength").value(5))
                .andExpect(jsonPath("$[0].power_stats.dexterity").value(5))
                .andExpect(jsonPath("$[0].power_stats.intelligence").value(5))
                .andExpect(jsonPath("$[1].id").value(HERO2_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("Superman"))
                .andExpect(jsonPath("$[1].race").value(HERO_DTO2.getRace().toString()))
                .andExpect(jsonPath("$[1].power_stats.agility").value(5))
                .andExpect(jsonPath("$[1].power_stats.strength").value(5))
                .andExpect(jsonPath("$[1].power_stats.dexterity").value(5))
                .andExpect(jsonPath("$[1].power_stats.intelligence").value(5));
        verify(heroService, times(1)).findByName("man");
    }

    @Test
    void getHeroByName_ReturnsNotFound() throws Exception {
        // Given
        when(heroService.findByName(any(String.class))).thenReturn(ResponseEntity.notFound().build());

        // When
        final ResultActions resultActions = mockMvc.perform(get(API_URL)
                .queryParam("name", "borg"));

        // Then
        resultActions.andExpect(status().isNotFound());
        verify(heroService, times(1)).findByName("borg");
    }

    @Test
    void updateAHeroWithAllRequiredArguments_ReturnsNoContent() throws Exception {
        //given
        UpdateHeroRequest updateHeroRequest = UpdateHeroRequest.builder()
                .name("Batman")
                .race(Race.HUMAN)
                .strength(2)
                .agility(2)
                .dexterity(2)
                .intelligence(2)
                .enabled(true).build();

        final UUID id = UUID.randomUUID();
        final String body = objectMapper.writeValueAsString(updateHeroRequest);
        when(heroService.update(eq(id), any())).thenReturn(ResponseEntity.noContent().build());

        //when
        final ResultActions resultActions = mockMvc.perform(put("/api/v1/heroes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        //then
        resultActions.andExpect(status().isNoContent());
        verify(heroService, times(1)).update(eq(id), any());
    }

    @Test
    void deleteHero_ReturnsNoContent() throws Exception {
        //given
        when(heroService.delete(HERO_ID)).thenReturn(ResponseEntity.noContent().build());

        //when
        final ResultActions resultActions = mockMvc.perform(delete("/api/v1/heroes/{id}", HERO_ID));

        //then
        resultActions.andExpect(status().isNoContent());
        verify(heroService, times(1)).delete(HERO_ID);
    }

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }


}