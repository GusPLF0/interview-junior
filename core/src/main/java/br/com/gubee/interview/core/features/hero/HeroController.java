package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroServiceImpl;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroServiceImpl.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroDTO> findWithId(@Validated @PathVariable UUID id) {
        return heroServiceImpl.findById(id);
    }

    @GetMapping()
    public ResponseEntity<List<HeroDTO>> findWithName(@Validated @RequestParam String name) {
        return heroServiceImpl.findByName(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @PathVariable UUID id,
                                    @Validated @RequestBody UpdateHeroRequest updateHeroRequest) {
        return heroServiceImpl.update(id, updateHeroRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Validated @PathVariable UUID id) {
        return heroServiceImpl.delete(id);
    }

    @GetMapping("/{principalHero}/compare/{comparedHero}")
    public ResponseEntity<?> compare(@PathVariable UUID principalHero, @PathVariable UUID comparedHero){
        return heroServiceImpl.compare(principalHero, comparedHero);
    }
}

