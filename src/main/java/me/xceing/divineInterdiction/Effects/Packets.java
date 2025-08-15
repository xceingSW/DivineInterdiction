package me.xceing.divineInterdiction.Effects;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Packets {

    public enum Alignment {
        Ally(NamedTextColor.BLUE),
        Neutral(NamedTextColor.WHITE),
        Enemy(NamedTextColor.RED),
        Friendly(NamedTextColor.GREEN)
        ;
        
        Alignment(NamedTextColor colour){
            this.colour = colour;
        };

        private final NamedTextColor colour;
        public NamedTextColor getColour(){
            return colour;
        }
    }
    
    
    static PlayerManager playerManager = PacketEvents.getAPI().getPlayerManager();

    public static void sendSheep(Player player) {
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        Sheep sheep = new Sheep(net.minecraft.world.entity.EntityType.SHEEP, nmsPlayer.level());
        Location loc = new Location(nmsPlayer.getX(),nmsPlayer.getY()+1,nmsPlayer.getZ(),0,0);
        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(
               sheep.getId(),
                sheep.getUUID(),
                EntityTypes.SHEEP,
                loc,
                0f,
                0,
                new Vector3d()
        );
        playerManager.sendPacket(player, spawnPacket);

    }

    private static void sendGlow(Player target, Player receiver) {
        int entityId = target.getEntityId();
        List<EntityData<?>> meta = new ArrayList<>();
        meta.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) (0x40)));
        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(entityId, meta);
        playerManager.sendPacket(receiver, metadataPacket);
    }
    public static void sendGlowWithColour(List<Player> target, Player reciever, Alignment alignment){

        ScoreBoardTeamInfo info = new ScoreBoardTeamInfo(
                Component.text(alignment.name()),null,null,
                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                WrapperPlayServerTeams.CollisionRule.ALWAYS, alignment.getColour(), WrapperPlayServerTeams.OptionData.NONE );
        WrapperPlayServerTeams createRedTeamPacket = new WrapperPlayServerTeams(alignment.name(),
                WrapperPlayServerTeams.TeamMode.CREATE, info, target.stream().map(Player::getName).toList());
        playerManager.sendPacket(reciever, createRedTeamPacket);
        target.forEach(player -> {
            sendGlow(player,reciever);

        });
    }
}
