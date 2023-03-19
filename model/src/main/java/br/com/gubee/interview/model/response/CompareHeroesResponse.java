package br.com.gubee.interview.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class CompareHeroesResponse {

    private UUID principalHeroId;

    private UUID comparedHeroId;

    private String strength;

    private String intelligence;

    private String dextery;

    private String agility;

}
