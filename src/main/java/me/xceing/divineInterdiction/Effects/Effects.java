package me.xceing.divineInterdiction.Effects;

import me.xceing.divineInterdiction.DivineInterdiction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;

public class Effects {
    public static void giveEagleVision(Player p, Collection<Player> targets){
        PotionEffect darknessEffect = new PotionEffect(PotionEffectType.DARKNESS, 50,0,false, false);
        p.addPotionEffect(darknessEffect);
        new BukkitRunnable(){
            @Override
            public void run() {
                Packets.sendGlowWithColour(targets,p, Packets.Alignment.Enemy);
            }
        }.runTaskLaterAsynchronously(DivineInterdiction.getInstance(), 25);

    }
}
