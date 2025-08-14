package me.xceing.divineInterdiction;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class DivineInterdiction extends JavaPlugin {

    private static DivineInterdiction instance;

    public static DivineInterdiction getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        // Store the plugin instance
        instance = this;

        // Plugin startup logic
        ArtifactSettings.getInstance().load();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(CommandTree.getCommandTree());
        });
        getServer().getPluginManager().registerEvents(new ArtifactEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
