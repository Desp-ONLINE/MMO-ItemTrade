package com.binggre.mmoitemshop.commands;

import com.binggre.binggreapi.command.BetterCommand;
import com.binggre.binggreapi.command.CommandArgument;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.commands.arguments.OpenArgument;
import com.binggre.mmoitemshop.commands.arguments.ReloadArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdminCommand extends BetterCommand implements TabCompleter {

    @Override
    public String getCommand() {
        return "교환";
    }

    @Override
    public boolean isSingleCommand() {
        return false;
    }

    @Override
    public List<CommandArgument> getArguments() {
        return List.of(
                new ReloadArgument(),
                new OpenArgument()
        );
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return argsMap.keySet().stream().toList();
        }
        if (strings[0].equals("오픈")) {
            return MMOItemTrade.getPlugin().getTradeRepository()
                    .values()
                    .stream()
                    .map(MMOTrade -> MMOTrade.getId() + "")
                    .toList();
        }
        return List.of();
    }
}
