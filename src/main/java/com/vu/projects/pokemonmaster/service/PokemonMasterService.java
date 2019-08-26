package com.vu.projects.pokemonmaster.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vu.projects.pokemonmaster.data.ItemTemplates;
import com.vu.projects.pokemonmaster.data.POGOMaster;
import com.vu.projects.pokemonmaster.data.pokemon.Evolution;
import com.vu.projects.pokemonmaster.pokemon.*;
import lombok.extern.java.Log;
import org.apache.commons.lang3.Range;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

@Service
@Log
public class PokemonMasterService {

    @Value("${pokemongo.master.file}")
    private String masterFile;

    @Value("${missing.moves.on.smeagleitem}")
    private
    List<String> missingMoves;

    @Autowired
    private PokemonMasterRepo pokemonMasterRepo;

    @Autowired
    private QuickMoveRepo quickMoveRepo;

    @Autowired
    private CinematicMoveRepo cinematicMoveRepo;

    @Autowired
    private
    RestTemplate restTemplate;

    @Autowired
    private
    ObjectMapper om;

    public final List<PokeGen> pokeGens = Arrays.asList(PokeGen.GEN_1, PokeGen.GEN_2, PokeGen.GEN_3, PokeGen.GEN_4,
            PokeGen.GEN_5, PokeGen.GEN_6, PokeGen.GEN_7);

    //// SERVICE METHODS /////////////////////

    public  List<Pokemon> listAllPokemons() {
        return pokemonMasterRepo.findAll();
    }

    public List<Pokemon> savePokemons(List<Pokemon> pokemons) {
        return pokemons.stream().map(pokemonMasterRepo::save).collect(Collectors.toList());
    }

    public List<QuickMove> saveQuickMoves(List<QuickMove> qMoves) {
        return qMoves.stream().map(quickMoveRepo::save).collect(Collectors.toList());
    }

    public List<CinematicMove> saveCinematicMoves(List<CinematicMove> cMoves) {
        return cMoves.stream().map(cinematicMoveRepo::save).collect(Collectors.toList());
    }

    public Pokemon getPokemon(String pokemonId) {
        return pokemonMasterRepo.findById(pokemonId).get();
    }

    public List<Pokemon> getPokemonByDexId(String dexId) {
        return pokemonMasterRepo.findByDexId(dexId);
    }

    public List<Pokemon> getPokemonByType(Type type) {
        return pokemonMasterRepo.findByType(type);
    }

    public List<Pokemon> getPokemonByQuickMove(QuickMove qMove) {
        return pokemonMasterRepo.findByQuickMove(qMove);
    }

    public List<Pokemon> getPokemonByCinematicMove(CinematicMove cinematicMove) {
        return pokemonMasterRepo.findByCinematicMove(cinematicMove);
    }

    public List<Pokemon> getPokemonByForm(String form) {
        return pokemonMasterRepo.findByForm(form);
    }


    /// Functional Interfaces

    public final Supplier<ResponseEntity<String>> masterData = this::readDataFromURL;

    public final BiFunction<List<ItemTemplates>, String, Optional<List<String>>> movesFromSmeargleMoves =
                                                                                        this::loadSmeargleMoves;

    ////////////////

    private Optional<List<String>> loadSmeargleMoves(List<ItemTemplates> templates, String moveType) {
        return templates.stream()
                .filter(t -> t.getSmeargleMovesSettings() != null)
                .map(t -> (moveType.equalsIgnoreCase("quick") ?
                        t.getSmeargleMovesSettings().getQuickMoves() :
                        t.getSmeargleMovesSettings().getCinematicMoves()))
                .findFirst();
    }

    public List<POGOMaster> parsingAllItems() throws IOException {
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return om.readValue(masterData.get().getBody(),
                om.getTypeFactory().constructCollectionType(List.class, POGOMaster.class));
    }

    private ResponseEntity<String> readDataFromURL() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return restTemplate.exchange(masterFile, HttpMethod.GET, new HttpEntity<>("header",
                headers), String.class);
    }

    private RegularStat populateRegularStats(ItemTemplates t) {
        RegularStat regStat = new RegularStat();
        regStat.setMovementId(t.getMoveSettings().getMovementId());
        regStat.setDurationMs(t.getMoveSettings().getDurationMs());
        regStat.setEnergyDelta(t.getMoveSettings().getEnergyDelta() != null ?
                t.getMoveSettings().getEnergyDelta() : 0);
        regStat.setPokemonType(t.getMoveSettings().getPokemonType());
        regStat.setPower(t.getMoveSettings().getPower() != null ?
                t.getMoveSettings().getPower() : 0);

        regStat.setDps(
                BigDecimal.valueOf(regStat.getPower() / (regStat.getDurationMs() / 1000.0)).setScale(2, BigDecimal.ROUND_FLOOR));
        regStat.setEps(
                BigDecimal.valueOf(regStat.getEnergyDelta() / (regStat.getDurationMs() / 1000.0)).setScale(2, BigDecimal.ROUND_FLOOR));
        if (regStat.getEps().doubleValue() != 0.00) {
            regStat.setDamageEnergy(
                    BigDecimal.valueOf(regStat.getDps().doubleValue() / regStat.getEps().doubleValue()).
                            setScale(2, BigDecimal.ROUND_FLOOR));
        } else {
            regStat.setDamageEnergy(BigDecimal.valueOf(0.00));
        }
        return regStat;
    }

    private PvpStat populatePvpStats(ItemTemplates t) {
        PvpStat pvpStat = new PvpStat();
        pvpStat.setUniqueId(t.getCombatMove().getUniqueId());
        pvpStat.setDurationTurn(t.getCombatMove().getDurationTurns() != null ?
                t.getCombatMove().getDurationTurns() : 6);
        pvpStat.setEnergyDelta(t.getCombatMove().getEnergyDelta() != null ? t.getCombatMove().getEnergyDelta() : 0);
        pvpStat.setPower(t.getCombatMove().getPower() != null ? t.getCombatMove().getPower()
                : 0);
        pvpStat.setType(t.getCombatMove().getType());
        //log.info("Move: " + t.getCombatMove().getUniqueId());
        pvpStat.setDpt(
                BigDecimal.valueOf(pvpStat.getPower() / pvpStat.getDurationTurn()).setScale(2, BigDecimal.ROUND_FLOOR));
        pvpStat.setEpt(
                BigDecimal.valueOf(pvpStat.getEnergyDelta() / pvpStat.getDurationTurn()).setScale(2, BigDecimal.ROUND_FLOOR));
        if (pvpStat.getDpt().doubleValue() == 0.00 || pvpStat.getEpt().doubleValue() == 0.00) {
            pvpStat.setDamageEnergy(BigDecimal.valueOf(0.00));
        } else {
            pvpStat.setDamageEnergy(
                    BigDecimal.valueOf(pvpStat.getDpt().doubleValue() / pvpStat.getEpt().doubleValue()).setScale(2,
                            BigDecimal.ROUND_FLOOR));
        }
//                    log.info("DAMAGE/ENERGY = " + qMove.getPvpStat().getDamageEnergy());
        return pvpStat;
    }



    private <T> CinematicMove checkMapAndBuildCinematicStat(Map<String,CinematicMove> map, ItemTemplates t,
                                                            Function<ItemTemplates, T> populateStat) {
        CinematicMove cm = null;
        T stat = populateStat.apply(t);

        String key = stat.getClass().isAssignableFrom(RegularStat.class) ? t.getMoveSettings().getMovementId() :
                                                                                t.getCombatMove().getUniqueId();
        if (map.containsKey(key)) {
            cm = map.get(key);
        } else {
            cm = new CinematicMove();
            map.put(key, cm);
        }
        cm.setCinematicMoveid(key);
        cm.setName(buildName(cm.getCinematicMoveid(), 0, 0));

        if (stat.getClass().isAssignableFrom(RegularStat.class)) {
            cm.setRegularStat((RegularStat)stat);
        } else {
            cm.setPvpStat((PvpStat) stat);
        }
        return cm;
    }

    private <T> QuickMove checkMapAndBuildQuickStat(Map<String,QuickMove> map, ItemTemplates t,
                                                            Function<ItemTemplates, T> populateStat) {
        QuickMove qm = null;
        T stat = populateStat.apply(t);

        String key = stat.getClass().isAssignableFrom(RegularStat.class) ? t.getMoveSettings().getMovementId() :
                t.getCombatMove().getUniqueId();
        if (map.containsKey(key)) {
            qm = map.get(key);
        } else {
            qm = new QuickMove();
            map.put(key, qm);
        }
        qm.setQuickMoveid(key);
        if (qm.getQuickMoveid().endsWith("FAST")) {
            qm.setName(buildName(qm.getQuickMoveid(), 0, 1));
        } else {
            qm.setName(buildName(qm.getQuickMoveid(), 0, 0));
        }

        if (stat.getClass().isAssignableFrom(RegularStat.class)) {
            qm.setRegularStat((RegularStat)stat);
        } else {
            qm.setPvpStat((PvpStat) stat);
        }
        return qm;
    }

    public List<CinematicMove> buildCinematicMoves(List<POGOMaster> pokeMasterList) {
        Map<String, CinematicMove> cMoveMap = new ConcurrentHashMap<>();

        // First load all the smeargle cinematic moves
        List<String> cinematicMoveList = movesFromSmeargleMoves.apply(pokeMasterList.get(0).getItemTemplates(),
                "cinematic").get();

        // Then populate their "regular stats"
        pokeMasterList.get(0).getItemTemplates().parallelStream()
                .filter(t -> t.getMoveSettings() != null)
                .filter(t -> cinematicMoveList.contains(t.getMoveSettings().getMovementId()) || missingMoves.contains(t.getMoveSettings().getMovementId()))
                .map(t-> checkMapAndBuildCinematicStat(cMoveMap, t, this::populateRegularStats))
                .count();

        // Then populates their "pvp stats"
        pokeMasterList.get(0).getItemTemplates().parallelStream()
                .filter(t -> t.getCombatMove() != null)
                .filter(t -> cinematicMoveList.contains(t.getCombatMove().getUniqueId()) || missingMoves.contains(t.getCombatMove().getUniqueId()))
                .map(t -> checkMapAndBuildCinematicStat(cMoveMap, t, this::populatePvpStats))
                .count();

        // Then extract Moves from Map and return as List
        return cMoveMap.keySet().parallelStream().map(cMoveMap::get).collect(Collectors.toList());

    }

    public List<QuickMove> buildQuickMoves(List<POGOMaster> pokeMasterList) {
        Map<String, QuickMove> qMoveMap = new ConcurrentHashMap<>();

        // First load all the smeargle quick moves
        List<String> quickMoveList =
                movesFromSmeargleMoves.apply(pokeMasterList.get(0).getItemTemplates(), "quick").get();

        // Then populate their "regular stats"
        pokeMasterList.get(0).getItemTemplates().parallelStream()
                .filter(t -> t.getMoveSettings() != null)
                .filter(t -> quickMoveList.contains(t.getMoveSettings().getMovementId()) || missingMoves.contains(t.getMoveSettings().getMovementId()))
                .map(t-> checkMapAndBuildQuickStat(qMoveMap, t, this::populateRegularStats))
                .count();

        // Then populates their "pvp stats"
        pokeMasterList.get(0).getItemTemplates().parallelStream()
                .filter(t -> t.getCombatMove() != null)
                .filter(t -> quickMoveList.contains(t.getCombatMove().getUniqueId()) || missingMoves.contains(t.getCombatMove().getUniqueId()))
                .map(t -> checkMapAndBuildQuickStat(qMoveMap, t, this::populatePvpStats))
                .count();

        // Then extract moves from the Map as List and return
        return qMoveMap.keySet().parallelStream().map(qMoveMap::get).collect(Collectors.toList());
    }

    private void populatePokemon(ItemTemplates t, List<Pokemon> pokes) {
        Pokemon pokemon = new Pokemon();
        pokemon.setDexId(extractDexNum(t.getTemplateId()));
        if (t.getPokemonSettings().getForm() != null) {
            // when a pokemon has a form, it is a different form of the same pokemon
            // use the form as the new id.
            pokemon.setPokemonId(t.getPokemonSettings().getForm());
            pokemon.setForm(t.getPokemonSettings().getForm());
        } else {
            pokemon.setPokemonId(t.getPokemonSettings().getPokemonId());
        }
        pokemon.setPokemonName(buildName(pokemon.getPokemonId(), 0, 0));
        List<Type> types = new Vector<>();
        Type type = new Type();
        type.setTypeId(t.getPokemonSettings().getType());
        type.setName(buildName(t.getPokemonSettings().getType(),2, 0));
        types.add(type);
        if (t.getPokemonSettings().getType2() != null) {
            Type type2 = new Type();
            type2.setTypeId(t.getPokemonSettings().getType2());
            type2.setName(buildName(t.getPokemonSettings().getType2(),2, 0));
            types.add(type2);
        }
        pokemon.setTypes(types);
        pokemon.setCandyToUnlockThirdMove(t.getPokemonSettings().getThirdMove().getCandyToUnlock());
        pokemon.setForm(t.getPokemonSettings().getForm());
        pokemon.setStardustToUnlockThirdMove(t.getPokemonSettings().getThirdMove().getStardustToUnlock());
        Stats stats = new Stats();
        stats.setBaseAttack(t.getPokemonSettings().getStats().getBaseAttack());
        stats.setBaseDefense(t.getPokemonSettings().getStats().getBaseDefense());
        stats.setBaseStamina(t.getPokemonSettings().getStats().getBaseStamina());
        pokemon.setStats(stats);

        List<QuickMove> qMoves = new Vector<>();
        if (t.getPokemonSettings().getQuickMoves() != null) {
            for (String m : t.getPokemonSettings().getQuickMoves()) {
                QuickMove move = new QuickMove();
                move.setQuickMoveid(m);
                move.setName(buildName(m, 0, 1));
                qMoves.add(move);
            }
        }
        pokemon.setQuickMoves(qMoves);

        List<CinematicMove> cMoves = new Vector<>();
        HashMap<String, String> mvMap = new HashMap<>();
        if (t.getPokemonSettings().getCinematicMoves() != null) {
            for (String m : t.getPokemonSettings().getCinematicMoves()) {
                // master data file has issue which contains more than one
                // of the same move for a pokemon, filter it out
                if (!mvMap.containsKey(m)) {
                    mvMap.put(m, m);
                    CinematicMove move = new CinematicMove();
                    move.setCinematicMoveid(m);
                    move.setName(buildName(m, 0, 0));
                    cMoves.add(move);
                }
            }
        }
        pokemon.setCinematicMoves(cMoves);
        pokemon.setKmBuddyDistance(t.getPokemonSettings().getKmBuddyDistance());

        Map<String, Evolution> evoMap = new ConcurrentHashMap<>();
        if (t.getPokemonSettings().getEvolutionBranch() != null) {
            List<EvolutionBranch> eBranches = new Vector<>();
            for (Evolution e : t.getPokemonSettings().getEvolutionBranch()) {
                if (e.getForm() != null) {
                    e.setEvolution(e.getForm());
                }
                EvolutionBranch eb = new EvolutionBranch();
                eb.setCandyCost(e.getCandyCost());
                eb.setEvolution(e.getEvolution());
                eb.setForm(e.getForm());
                eBranches.add(eb);
            }
            pokemon.setEvolutionBranch(eBranches);
            pokemon.setForm(t.getPokemonSettings().getForm());
        }
        pokemon.setEncounterRate(new EncounterRate());

        if (t.getPokemonSettings().getEncounter().getBaseCaptureRate() != null) {
            pokemon.getEncounterRate().setBaseCaptureRatePercent(t.getPokemonSettings().getEncounter().getBaseCaptureRate());
        }
        if (t.getPokemonSettings().getEncounter().getBaseFleeRate() != null) {
            pokemon.getEncounterRate().setBaseFleeRatePercent(t.getPokemonSettings().getEncounter().getBaseFleeRate());
        }

        pokemon.setRarity(t.getPokemonSettings().getRarity());
        pokemon.setParentPokemonId(t.getPokemonSettings().getParentPokemonId());

        pokemon.setEvolutionIds(t.getPokemonSettings().getEvolutionIds());

        pokeGens.stream()
                .filter(pg -> pg.range.contains(Integer.valueOf(pokemon.getDexId())))
                .findFirst()
                .ifPresent(g->pokemon.setGeneration(g.gen));

        pokes.add(pokemon);
    }

    @SuppressWarnings("unused")
    public List<Pokemon> buildPokemons(List<POGOMaster> pokesMasterList) {
        List<Pokemon> pokes = new Vector<>();
        Map<String, Pokemon> idMap = new ConcurrentHashMap<>();

        // build the pokemon list from the pokemonSettings data first
        pokesMasterList.get(0).getItemTemplates().parallelStream()
                .filter(t->t.getPokemonSettings() != null)
                .forEach(t-> populatePokemon(t, pokes));

        // now go through the pokemon list to populate other details
        // - weather
        pokes.forEach(p -> populateWeatherAffinities(pokesMasterList, p));

        return pokes;
    }

    private void populateWeatherAffinities(List<POGOMaster> pokesMasterList, Pokemon p) {
        // First load all the weather affinities
        List<ItemTemplates> wa = pokesMasterList.get(0).getItemTemplates().stream()
                .filter(t->t.getWeatherAffinities() != null)
                .collect(Collectors.toList());

        // Then set the proper weather affinity based on the type
        p.getTypes().stream()
                .map(Type::getTypeId)
                .flatMap(id -> wa.stream().filter((w -> w.getWeatherAffinities().getPokemonType().contains(id))))
                .findFirst()
                .ifPresent(template -> p.setWeatherConditionAffinities(template.getWeatherAffinities().getWeatherCondition()));
    }

    private String extractDexNum(String templateId) {
        String[] words = templateId.split("_");
        return words[0].substring(1);
    }

    public String buildName(String s, int wordsNumToSkipPrev, int wordsNumToSkipPost) {
        StringBuilder name = new StringBuilder();
        String[] strArray = s.split("_");

        for (int i = 0 ; i < strArray.length; i++) {
            if (i < wordsNumToSkipPrev) {
                continue;
            } else if (i < strArray.length - wordsNumToSkipPost) {
                name.append(strArray[i]).append(" ");
            }
        }
        return WordUtils.capitalizeFully(name.substring(0, name.length() - 1));
    }

    public enum PokeGen {
        GEN_1(Range.between(1, 151), 1),
        GEN_2(Range.between(152, 251), 2),
        GEN_3(Range.between(252, 386), 3),
        GEN_4(Range.between(387, 493), 4),
        GEN_5(Range.between(494, 649), 5),
        GEN_6(Range.between(650, 721), 6),
        GEN_7(Range.between(722, 809), 7);

        public final Range<Integer> range;
        final int gen;

        PokeGen(Range<Integer> range, int gen) {
            this.range = range;
            this.gen = gen;
        }
    }
}
