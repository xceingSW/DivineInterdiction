package me.xceing.divineInterdiction;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArtifactSettings {
    private final static ArtifactSettings instance = new ArtifactSettings();
    public static ArtifactSettings getInstance(){
        return instance;
    }
    private ArtifactSettings(){}

    private File file;
    private YamlConfiguration config;

    // Settings Halo //
    private final String severerNamespace = Artifacts.ArtifactList.SEVERER_OF_IMMORTALITY.getSimpleItemName();
    public int CROWN_OF_CHAINING_EFFECT_RANGE = 20;
    public List<PotionEffectType> EFFECT_TYPES = List.of(
            PotionEffectType.HASTE,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.SPEED,
            PotionEffectType.ABSORPTION,
            PotionEffectType.REGENERATION,
            PotionEffectType.SATURATION,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.STRENGTH,
            PotionEffectType.RESISTANCE
    );
    public List<Integer> MAX_EFFECT_AMPLIFIER = List.of(
            3, 0, 2, 4, 4, 4, 1, 4, 4, 3
    );
    public List<GameMode> EFFECTED_GAMEMODES_HALO = List.of(
            GameMode.CREATIVE,
            GameMode.SPECTATOR
    );
    // Settings Severer //
    private final String haloNamespace = Artifacts.ArtifactList.THE_GODSTHIEF_HALO.getSimpleItemName();
    public List<GameMode> EFFECTED_GAMEMODES_SEVERER = List.of(
            GameMode.CREATIVE,
            GameMode.SPECTATOR
    );
    public GameMode EFFECT_GAMEMODE = GameMode.SURVIVAL;


    public void load(){
        boolean isNewFile = false;
        file = new File(DivineInterdiction.getInstance().getDataFolder(),"config.yml");
        if(!file.exists()){
            isNewFile = true;
            DivineInterdiction.getInstance().saveResource("config.yml", false);

        }
        config = new YamlConfiguration();
        config.options().parseComments(true);
        try{
            config.load(file);
            if(isNewFile){
                setDefaults();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
       loadConfigValues();

    }
    public void save(){
        try{
            config.save(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void set(String path, Object value){
        config.set(path, value);
        save();
    }
    public void setDefaults(){
        setDefaultsHalo();
        setDefaultsSeverer();
        save();
    }
    private void setDefaultsHalo(){
        String keyNamespace = Artifacts.ArtifactList.THE_GODSTHIEF_HALO.getSimpleItemName();

        config.set(keyNamespace + ".Effect-Range", CROWN_OF_CHAINING_EFFECT_RANGE);
        config.set(keyNamespace + ".Effect-Types", EFFECT_TYPES.stream()
                .map(potionEffectType -> potionEffectType.getKey().value())
                .toList());
        config.set(keyNamespace + ".Effect-Max-Amplifier", MAX_EFFECT_AMPLIFIER);
        config.set(keyNamespace + ".Effected-Gamemodes", EFFECTED_GAMEMODES_SEVERER.stream().map(gameMode -> gameMode.name()).toList());
    }
    private void setDefaultsSeverer(){

        config.set(severerNamespace + ".Effected-Gamemodes", EFFECTED_GAMEMODES_SEVERER.stream().map(gameMode -> gameMode.name()).toList());
        config.set(severerNamespace + ".Effect-Gamemode", EFFECT_GAMEMODE.name());
    }

    private void loadConfigValues(){
        loadConfigValuesHalo();
        loadConfigValuesSeverer();
    }
    private void loadConfigValuesHalo(){
        CROWN_OF_CHAINING_EFFECT_RANGE = (int) config.get(haloNamespace + ".Effect-Range");
        EFFECT_TYPES =  ((List<String>) config.get(haloNamespace + ".Effect-Types")).stream()
                .map(s -> PotionEffectType.getByName(s))
                .toList();
        MAX_EFFECT_AMPLIFIER = (List<Integer>) config.get(haloNamespace + ".Effect-Max-Amplifier");
        EFFECTED_GAMEMODES_HALO = ((List<String>) config.get(haloNamespace + ".Effected-Gamemodes")).stream()
                .map(s -> GameMode.valueOf(s))
                .toList();
    }
    private void loadConfigValuesSeverer(){
        EFFECTED_GAMEMODES_SEVERER = ((List<String>)config.get(severerNamespace + ".Effected-Gamemodes")).stream().map(s -> GameMode.valueOf(s)).toList();

        EFFECT_GAMEMODE = GameMode.valueOf(config.get(severerNamespace + ".Effect-Gamemode").toString());
    }
}
