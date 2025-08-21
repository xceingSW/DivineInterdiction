package me.xceing.divineInterdiction.Effects;

import io.papermc.paper.persistence.PersistentDataContainerView;
import me.xceing.divineInterdiction.Artifacts;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Util {

    private static boolean isGeneric(ItemStack item, Artifacts.ArtifactList artifact){
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        if(!pdc.has(Artifacts.ARTIFACT_KEY)){
            return false;
        }
        String artifactKeyValue = pdc.get(Artifacts.ARTIFACT_KEY, PersistentDataType.STRING);
        if(!artifactKeyValue.equals(artifact.name())) {
            return false;
        }
        return true;
    }

    private static boolean usingGeneric(Player player, Artifacts.ArtifactList artifact, EquipmentSlot effectSlot){
        ItemStack item = player.getInventory().getItem(effectSlot);
        return isGeneric(item, artifact);
    }

    public static boolean isCrown(ItemStack item){
        return isGeneric(item, Artifacts.ArtifactList.THE_GODSTHIEF_HALO);
    }
    public static boolean isVeilBreaker(ItemStack item){
        return isGeneric(item, Artifacts.ArtifactList.THE_VEIL_BREAKER_EYE);
    }
    public static boolean isSeverer(ItemStack item){
        return isGeneric(item, Artifacts.ArtifactList.SEVERER_OF_IMMORTALITY);
    }

    public static boolean usingCrown(Player player){
        return usingGeneric(player, Artifacts.ArtifactList.THE_GODSTHIEF_HALO, EquipmentSlot.HEAD);
    }
    public static boolean usingVeilBreaker(Player player){
        return usingGeneric(player, Artifacts.ArtifactList.THE_VEIL_BREAKER_EYE, EquipmentSlot.HEAD);
    }
    public static boolean usingSeverer(Player player){
        return usingGeneric(player, Artifacts.ArtifactList.SEVERER_OF_IMMORTALITY, EquipmentSlot.HAND);
    }
}
