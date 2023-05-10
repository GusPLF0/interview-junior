package br.com.gubee.interview.core.features.hero.interfaces;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.dto.HeroDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface HeroRepository {
    UUID create(Hero hero);

    HeroDTO findById(UUID id);

    List<HeroDTO> findByName(String name);

    void update(UUID id, HeroDTO hero);

    void delete(UUID id);
}
