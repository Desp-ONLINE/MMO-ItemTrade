package com.binggre.mmoitemshop.commands;

import com.binggre.binggreapi.command.BetterCommand;
import com.binggre.binggreapi.command.CommandArgument;
import com.binggre.mmoitemshop.commands.arguments.OpenArgument;
import com.binggre.mmoitemshop.commands.arguments.ReloadArgument;

import java.util.List;

public class AdminCommand extends BetterCommand {

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
}
