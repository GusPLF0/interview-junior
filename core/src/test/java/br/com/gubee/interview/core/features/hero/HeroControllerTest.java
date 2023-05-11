package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HeroController.class)
class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HeroService mockHeroServiceImpl;

    @Test
    void testCreate() throws Exception {
        // Setup
        // Configure HeroService.create(...).
        final UUID uuid = UUID.fromString("439c21b4-525e-47a5-b3fa-a899900b2bb4");
        when(mockHeroServiceImpl.create(CreateHeroRequest.builder().build())).thenReturn(uuid);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/heroes")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testFindWithId() throws Exception {
        // Setup
        // Configure HeroService.findById(...).
        final ResponseEntity<HeroDTO> heroDTOResponseEntity = new ResponseEntity<>(HeroDTO.builder().build(),
                HttpStatus.OK);
        when(mockHeroServiceImpl.findById(UUID.fromString("a981a31c-beb4-4b48-90af-08d62bf47b22")))
                .thenReturn(heroDTOResponseEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/heroes/{id}", "a981a31c-beb4-4b48-90af-08d62bf47b22")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testFindWithName() throws Exception {
        // Setup
        // Configure HeroService.findByName(...).
        final ResponseEntity<List<HeroDTO>> listResponseEntity = new ResponseEntity<>(
                List.of(HeroDTO.builder().build()), HttpStatus.OK);
        when(mockHeroServiceImpl.findByName("name")).thenReturn(listResponseEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/heroes")
                        .param("name", "name")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testFindWithName_HeroServiceReturnsNoItems() throws Exception {
        // Setup
        // Configure HeroService.findByName(...).
        final ResponseEntity<List<HeroDTO>> listResponseEntity = ResponseEntity.ok(Collections.emptyList());
        when(mockHeroServiceImpl.findByName("name")).thenReturn(listResponseEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/heroes")
                        .param("name", "name")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testUpdate() throws Exception {
        // Setup
        // Configure HeroService.update(...).
        final ResponseEntity<ChangeInHeroResponse> changeInHeroResponseEntity = new ResponseEntity<>(
                ChangeInHeroResponse.builder().build(), HttpStatus.OK);
        when(mockHeroServiceImpl.update(UUID.fromString("554412f2-db96-4818-9e8c-b151caf2480c"),
                UpdateHeroRequest.builder().build())).thenReturn(changeInHeroResponseEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        put("/api/v1/heroes/{id}", "554412f2-db96-4818-9e8c-b151caf2480c")
                                .content("content").contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testDelete() throws Exception {
        // Setup
        doReturn(new ResponseEntity<>(null, HttpStatus.OK)).when(mockHeroServiceImpl).delete(
                UUID.fromString("da08a041-85e7-4c1f-b4f0-44a4e6afadce"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        delete("/api/v1/heroes/{id}", "da08a041-85e7-4c1f-b4f0-44a4e6afadce")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testCompare() throws Exception {
        // Setup
        doReturn(new ResponseEntity<>(null, HttpStatus.OK)).when(mockHeroServiceImpl).compare(
                UUID.fromString("15d12110-4696-4f93-905b-b33c639c2f54"),
                UUID.fromString("5a0b7143-803a-46ce-928f-ba7c8b84e65d"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/heroes/{principalHero}/compare/{comparedHero}", "15d12110-4696-4f93-905b-b33c639c2f54",
                                "5a0b7143-803a-46ce-928f-ba7c8b84e65d")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
