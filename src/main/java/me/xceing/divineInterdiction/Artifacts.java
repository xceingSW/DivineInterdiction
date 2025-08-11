package me.xceing.divineInterdiction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Artifacts {
    public static final NamespacedKey ARTIFACT_KEY = new NamespacedKey(DivineInterdiction.getPlugin(DivineInterdiction.class),"artifact");
    public enum ArtifactList
    {
        SEVERER_OF_IMMORTALITY(getDefaultSevererOfImmortality()),
        THE_GODSTHIEF_HALO(getDefaultGodsthiefHalo())

        ;

        ArtifactList(ItemStack item){
            item.editPersistentDataContainer(persistentDataContainer ->
                    persistentDataContainer.set(ARTIFACT_KEY, PersistentDataType.STRING, this.name()));
            this.artifact = item;
        }

        private final ItemStack artifact;
        public String getSimpleItemName(){
            String name = this.name().toLowerCase(Locale.ROOT).replace('_',' ');
            return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase(Locale.ROOT);
        }
        public ItemStack getArtifact(){
           return artifact.clone();
        }
    }

    private static ItemStack getDefaultGodsthiefHalo(){
        Material itemType = Material.DIAMOND_HELMET;
        String displayName = "The Godsthief’s Halo";
        TextColor itemColor = NamedTextColor.RED;

        Component LoreDescription = Component.text("A saintly halo forged to steal rather than bless");
        Component LoreQuote = Component.text("“A regal but empty symbol, granting the wearer the might to rule, but only by feeding on the seat of another’s power.“ - Dethroned King");
        List<Component> lore = List.of(LoreDescription, LoreQuote);


        Component effectComponent = Component.text(" x ").decorate(TextDecoration.OBFUSCATED);
        Component displayNameComponent = Component.text(displayName).decoration(TextDecoration.OBFUSCATED,false);
        Component fullNameComponent = effectComponent.append(displayNameComponent.append(effectComponent)).color(itemColor);

        ItemStack CrownOfEnchaining = new ItemStack(itemType);
        ItemMeta meta = CrownOfEnchaining.getItemMeta();

        meta.lore(lore);
        meta.customName(fullNameComponent);
        CrownOfEnchaining.setItemMeta(meta);

        return CrownOfEnchaining;
    }

    private static ItemStack getDefaultSevererOfImmortality(){
        Material itemType = Material.DIAMOND_SWORD;
        String displayName = "Severer Of Immortality";
        TextColor itemColor = NamedTextColor.RED;

        Component LoreDescription = Component.text("Even the sky must bow to iron older than it.");
        Component LoreQuote = Component.text("“Not to kill a god, but to remind them they can die.“ - Ancient Blacksmith");
        List<Component> lore = List.of(LoreDescription, LoreQuote);

        Component effectComponent = Component.text(" x ").decorate(TextDecoration.OBFUSCATED);
        Component displayNameComponent = Component.text(displayName).decoration(TextDecoration.OBFUSCATED,false);
        Component fullNameComponent = effectComponent.append(displayNameComponent.append(effectComponent)).color(itemColor);

        ItemStack SevererOfImmortality = new ItemStack(itemType);
        ItemMeta meta = SevererOfImmortality.getItemMeta();

        meta.lore(lore);
        meta.customName(fullNameComponent);
        SevererOfImmortality.setItemMeta(meta);

        return SevererOfImmortality;
    }
}
