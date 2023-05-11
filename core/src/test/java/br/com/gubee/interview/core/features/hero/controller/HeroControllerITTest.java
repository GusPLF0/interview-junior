package br.com.gubee.interview.core.features.hero.controller;

import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HeroControllerITTest {

    UUID FIXED_UUID = UUID.fromString("124fb970-d1fb-4467-ab6b-32c5411f1216");
    UUID FIXED_UUID_FOR_PS = UUID.fromString("fb5f650c-d7f0-492c-a88c-08065daccf52");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HeroService heroService;


    @Test
    void shouldCreateNewHero() throws Exception {
        //given
        CreateHeroRequest createHeroRequest = CreateHeroRequest.builder()
                .name("Naruto")
                .race(Race.HUMAN)
                .dexterity(4)
                .intelligence(2)
                .agility(5)
                .strength(5).build();

        //when
        when(heroService
                .create(createHeroRequest))
                .thenReturn(FIXED_UUID);
        //then
        MvcResult location = this.mockMvc.perform(post("/api/v1/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": null,\"name\":\"Naruto\",\"race\":\"HUMAN\", \"dexterity\": 4, \"intelligence\":  2, \"agility\":  5, \"strength\":  5}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        assertThat(location.getResponse().getHeader("Location")).isEqualTo("/api/v1/heroes/"+ FIXED_UUID);
        assertThat(location.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void shouldFindHeroById() throws Exception {
        //given
        PowerStats powerStats = PowerStats.builder()
                .id(FIXED_UUID_FOR_PS)
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        HeroDTO heroDTO = HeroDTO.builder()
                .id(FIXED_UUID)
                .powerStats(powerStats)
                .name("Sasuke")
                .race(Race.HUMAN).build();
        //when
        when(heroService.findById(FIXED_UUID)).thenReturn(ResponseEntity.ok(heroDTO));
        //then

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/heroes/" + FIXED_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    id: '124fb970-d1fb-4467-ab6b-32c5411f1216',\n" +
                        "    name: 'Sasuke',\n" +
                        "    race: 'HUMAN',\n" +
                        "    enabled: false,\n" +
                        "    power_stats: {\n" +
                        "        id: 'fb5f650c-d7f0-492c-a88c-08065daccf52',\n" +
                        "        strength: 1,\n" +
                        "        agility: 1,\n" +
                        "        dexterity: 1,\n" +
                        "        intelligence: 1\n" +
                        "    }\n" +
                        "}"))
                .andReturn();


    }

    @Test
    void shouldFindHeroByName() throws Exception {

        //given
        PowerStats powerStats = PowerStats.builder()
                .id(FIXED_UUID_FOR_PS)
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        HeroDTO heroDTO = HeroDTO.builder()
                .id(FIXED_UUID)
                .powerStats(powerStats)
                .name("Sasuke")
                .race(Race.HUMAN).build();
        //when
        when(heroService.findByName("Sas")).thenReturn(ResponseEntity.ok(List.of(heroDTO)));
        //then

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/heroes")
                        .param("name", "Sas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\n" +
                        "    id: '124fb970-d1fb-4467-ab6b-32c5411f1216',\n" +
                        "    name: 'Sasuke',\n" +
                        "    race: 'HUMAN',\n" +
                        "    enabled: false,\n" +
                        "    power_stats: {\n" +
                        "        id: 'fb5f650c-d7f0-492c-a88c-08065daccf52',\n" +
                        "        strength: 1,\n" +
                        "        agility: 1,\n" +
                        "        dexterity: 1,\n" +
                        "        intelligence: 1\n" +
                        "    }\n" +
                        "}]"))
                .andReturn();


    }

    @Test
    void shouldUpdateAHero() throws Exception {
        //given
        UpdateHeroRequest updateHeroRequest = UpdateHeroRequest.builder()
                .name("Sasuke")
                .dexterity(1)
                .intelligence(1)
                .agility(1)
                .strength(1)
                .race(Race.HUMAN).build();

        PowerStats powerStats = PowerStats.builder()
                .id(FIXED_UUID_FOR_PS)
                .agility(1)
                .intelligence(1)
                .strength(1)
                .dexterity(1).build();

        ChangeInHeroResponse changeInHeroResponse = ChangeInHeroResponse.builder()
                .race(Race.HUMAN)
                .name("Sasuke")
                .powerStats(powerStats).build();

        //when
        when(heroService.update(FIXED_UUID, updateHeroRequest)).thenReturn(ResponseEntity.noContent().build());
        //then

        MvcResult mvcResult = this.mockMvc.perform(put("/api/v1/heroes/"+FIXED_UUID)
                        .content("{\"name\":\"Sasuke\",\"race\":\"HUMAN\", \"dexterity\": 1, \"intelligence\":  1, \"agility\":  1, \"strength\":  1}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Test
    void delete() {
    }

    @Test
    void compare() {
    }
}