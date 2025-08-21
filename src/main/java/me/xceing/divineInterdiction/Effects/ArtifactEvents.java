package me.xceing.divineInterdiction.Effects;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.xceing.divineInterdiction.ArtifactSettings;
import me.xceing.divineInterdiction.Artifacts;
import me.xceing.divineInterdiction.DivineInterdiction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class ArtifactEvents implements Listener {

    @EventHandler
    public void immortalitySevererHit(PrePlayerAttackEntityEvent event) {
        if (event.getAttacked() instanceof Player victim) {
            if (!ArtifactSettings.getInstance().EFFECTED_GAMEMODES_SEVERER.contains(victim.getGameMode())) {
                return;
            }
            Player causer = event.getPlayer();
            if (Util.usingSeverer(causer)) {
                victim.setGameMode(ArtifactSettings.getInstance().EFFECT_GAMEMODE);
            }
        }
    }

    @EventHandler
    public void veilBreakerEyeEffect(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (Util.usingVeilBreaker(player)) {
            Collection<Player> players = player.getWorld().getPlayers();
            Effects.giveEagleVision(player, players);
        }
    }


    public static Set<UUID> crownWearers = new HashSet<>();

    @EventHandler
    void equipChainingCrown(EntityEquipmentChangedEvent event) {
        Map<EquipmentSlot, EntityEquipmentChangedEvent.EquipmentChange> changes = event.getEquipmentChanges();
        EntityEquipmentChangedEvent.EquipmentChange helmetChange = changes.get(EquipmentSlot.HEAD);
        if (helmetChange == null) {
            return;
        }
        // Check if equipped item is crown
        ItemStack equippedHelmet = helmetChange.newItem();
        if (Util.isCrown(equippedHelmet)) {
            addCrownWearer(event.getEntity().getUniqueId());
            return;
        }

        // Check if unequipped item is crown
        ItemStack unEquippedHelmet = helmetChange.oldItem();
        if (Util.isCrown(unEquippedHelmet)) {
            removeCrownWearer(event.getEntity().getUniqueId());
            return;
        }

    }


    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Util.usingCrown(player)) {
            addCrownWearer(player.getUniqueId());
        }
    }

    private BukkitRunnable runnableChainingCrownEffect = null;



    private void removeCrownWearer(UUID id){
        crownWearers.remove(id);
        if(crownWearers.isEmpty() && runnableChainingCrownEffect.isCancelled()){
            runnableChainingCrownEffect.cancel();
        }
    }

    private void addCrownWearer(UUID id){
        boolean fromZero = crownWearers.isEmpty();
        crownWearers.add(id);
        if(fromZero){
            runnableChainingCrownEffect =
            new BukkitRunnable() {
                @Override
                public void run() {
                    crownWearers.forEach(uuid -> {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) { // player is offline
                            removeCrownWearer(uuid);
                            return;
                        }
                        chainingCrownTick(player);
                    });
                }
            };
            runnableChainingCrownEffect.runTaskTimer(DivineInterdiction.getInstance(),1,1);
        }
    }



    private void chainingCrownTick(Player player){
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
                PotionEffect newEffect = new PotionEffect(effectTypes.get(effectIndex),1,currentEffectAmplifier+1, false, true);
                player.addPotionEffect(newEffect);
            }
        }
    }

}
