package me.xceing.divineInterdiction;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class CommandTree {

    enum CommandArgument {
        ARTIFACT,
        TARGET_PLAYER_ARGUMENT
    }

    public static LiteralCommandNode<CommandSourceStack> getCommandTree() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("divineinterdiction")
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    sender.sendMessage("This is a plugin I made");
                    sender.sendMessage("General info goes here");
                    return Command.SINGLE_SUCCESS;
                });

        root.then(getItemCommand());
        root.then(getConfigCommand());

        return root.build();
    }

    private static LiteralArgumentBuilder<CommandSourceStack> getConfigCommand(){
        LiteralArgumentBuilder<CommandSourceStack> configCommand = Commands.literal("config");
        configCommand.then(Commands.literal("reload")).executes(commandContext -> {
            ArtifactSettings.getInstance().load();
            return Command.SINGLE_SUCCESS;
        });

        return configCommand;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> getItemCommand() {
        LiteralArgumentBuilder<CommandSourceStack> itemCommand = Commands.literal("item");

        // "get" subcommand
        itemCommand.then(Commands.literal("get")
                .then(createArtifactArgument()
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            if (sender instanceof Player player) {
                                return executeGiveArtifact(ctx, player);
                            }
                            sender.sendMessage("This command cannot be executed by the console");
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );

        // "give" subcommand
        itemCommand.then(Commands.literal("give")
                .then(Commands.argument(CommandArgument.TARGET_PLAYER_ARGUMENT.name(), ArgumentTypes.players())
                        .then(createArtifactArgument()
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument(CommandArgument.TARGET_PLAYER_ARGUMENT.name(), PlayerSelectorArgumentResolver.class);
                                    Iterable<Player> target = targetResolver.resolve(ctx.getSource());
                                    return executeGiveArtifact(ctx, target);
                                })
                        )
                )
        );

        return itemCommand;
    }

    // Reusable artifact argument
    private static RequiredArgumentBuilder<CommandSourceStack, String> createArtifactArgument() {
        return Commands.argument(CommandArgument.ARTIFACT.name(), StringArgumentType.greedyString())
                .suggests((ctx, builder) -> {
                    Arrays.stream(Artifacts.ArtifactList.values()).forEach(artifact -> builder.suggest(artifact.getSimpleItemName()));

                    return builder.buildFuture();
                });
    }

    private static int executeGiveArtifact(CommandContext<CommandSourceStack> ctx, Player target) {
        giveArtifactToPlayers(ctx, Collections.singletonList(target));
        return Command.SINGLE_SUCCESS;
    }

    private static int executeGiveArtifact(CommandContext<CommandSourceStack> ctx, Iterable<Player> targets) {
        giveArtifactToPlayers(ctx, targets);
        return Command.SINGLE_SUCCESS;
    }

    private static void giveArtifactToPlayers(CommandContext<CommandSourceStack> ctx, Iterable<Player> targets) {
        String itemName = ctx.getArgument(CommandArgument.ARTIFACT.name(), String.class);
        CommandSender sender = ctx.getSource().getSender();
        targets.forEach(player -> executeGiveCommand(sender, player, itemName));
    }

    private static void executeGiveCommand(CommandSender sender, HumanEntity target, String itemName) {
        Optional<Artifacts.ArtifactList> artifact = Arrays.stream(Artifacts.ArtifactList.values()).filter(artifactListItem -> artifactListItem.getSimpleItemName().equals(itemName)).findFirst();
        if (artifact.isEmpty())
        {
            sender.sendMessage("This is not a valid Artifact");
        } else {
            sender.sendMessage("Giving " + itemName + " to " + target.getName());
            target.getInventory().addItem(artifact.get().getArtifact());
        }
    }
}
