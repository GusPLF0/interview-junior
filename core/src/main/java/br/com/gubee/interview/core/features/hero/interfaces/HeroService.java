package br.com.gubee.interview.core.features.hero.interfaces;

import br.com.gubee.interview.model.dto.HeroDTO;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.UpdateHeroRequest;
import br.com.gubee.interview.model.response.ChangeInHeroResponse;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface HeroService {
    UUID create(CreateHeroRequest createHeroRequest);

    ResponseEntity<HeroDTO> findById(UUID id);

    ResponseEntity<List<HeroDTO>> findByName(String name);

    ResponseEntity<ChangeInHeroResponse> update(UUID id, UpdateHeroRequest changesInHero);

    ResponseEntity<?> delete(UUID heroId);

    ResponseEntity<?> compare(UUID principalHero, UUID comparedHero);
}
