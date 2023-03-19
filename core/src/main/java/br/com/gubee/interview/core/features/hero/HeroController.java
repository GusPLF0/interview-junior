package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroDTO> findWithId(@Validated @PathVariable UUID id) {
        return heroService.findById(id);
    }

    @GetMapping()
    public ResponseEntity<ArrayList<HeroDTO>> findWithName(@Validated @RequestParam String name) {
        return heroService.findByName(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @PathVariable UUID id,
                                    @Validated @RequestBody UpdateHeroRequest updateHeroRequest) {
        return heroService.update(id, updateHeroRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Validated @PathVariable UUID id) {
        return heroService.delete(id);
    }

    @GetMapping("/{principalHero}/compare/{comparedHero}")
    public ResponseEntity<?> compare(@PathVariable UUID principalHero, @PathVariable UUID comparedHero){
        return heroService.compare(principalHero, comparedHero);
    }
}

