package me.xceing.divineInterdiction.Effects;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.xceing.divineInterdiction.ArtifactSettings;
import me.xceing.divineInterdiction.Artifacts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ArtifactEvents implements Listener {

    @EventHandler
    public void immortalitySevererHit(PrePlayerAttackEntityEvent event){
        if(event.getAttacked() instanceof Player victim){


            if(!ArtifactSettings.getInstance().EFFECTED_GAMEMODES_SEVERER.contains(victim.getGameMode()))
            {
                return;
            }

            Player cause = event.getPlayer();
            ItemStack item = cause.getInventory().getItem(EquipmentSlot.HAND);
            PersistentDataContainerView pdc = item.getPersistentDataContainer();
            if(!pdc.has(Artifacts.ARTIFACT_KEY)){
                return;
            }
            String artifactKeyValue = pdc.get(Artifacts.ARTIFACT_KEY, PersistentDataType.STRING);
            if(artifactKeyValue.equals(Artifacts.ArtifactList.SEVERER_OF_IMMORTALITY.name())){
                victim.setGameMode(ArtifactSettings.getInstance().EFFECT_GAMEMODE);
            }
        }

    }
    @EventHandler
    public void veilBreakerEyeEffect(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(EquipmentSlot.HEAD);
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        if(!pdc.has(Artifacts.ARTIFACT_KEY)){
            return;
        }
        String artifactKeyValue = pdc.get(Artifacts.ARTIFACT_KEY, PersistentDataType.STRING);
        if(!artifactKeyValue.equals(Artifacts.ArtifactList.THE_VEIL_BREAKER_EYE.name())) {
            return;
        }
        Collection<Player> players = player.getWorld().getPlayers();
        Effects.giveEagleVision(player, players);


    }

    @EventHandler
    public void chainingCrownEffect(PlayerMoveEvent event){ //TODO: edit this in a runnable so it runs every tick regardless of movement
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(EquipmentSlot.HEAD);
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        if(!pdc.has(Artifacts.ARTIFACT_KEY)){
            return;
        }
        String artifactKeyValue = pdc.get(Artifacts.ARTIFACT_KEY, PersistentDataType.STRING);
        if(!artifactKeyValue.equals(Artifacts.ArtifactList.THE_GODSTHIEF_HALO.name())) {
            return;
        }
        Collection<Player> players = player.getLocation().getNearbyPlayers(ArtifactSettings.getInstance().CROWN_OF_CHAINING_EFFECT_RANGE);
        long nonSurvivalPlayersCount = players.stream().filter(player1 -> ArtifactSettings.getInstance().EFFECTED_GAMEMODES_HALO.contains(player1.getGameMode())).count();

        int effectIndex = 0;
        List<PotionEffectType> effectTypes = ArtifactSettings.getInstance().EFFECT_TYPES;
        for (int i = 0; i < nonSurvivalPlayersCount; i++) {
            if(effectIndex >= effectTypes.size()){
                break;
            }

            PotionEffect effect = player.getPotionEffect(effectTypes.get(effectIndex));
            int currentEffectAmplifier = effect == null ? -1 : effect.getAmplifier();
            if(currentEffectAmplifier >= ArtifactSettings.getInstance().MAX_EFFECT_AMPLIFIER.get(effectIndex)){
                effectIndex++;
                i--;
            }else{
                PotionEffect newEffect = new PotionEffect(effectTypes.get(effectIndex),1,currentEffectAmplifier+1, true, false);
                player.addPotionEffect(newEffect);
            }
        }

    }

}
