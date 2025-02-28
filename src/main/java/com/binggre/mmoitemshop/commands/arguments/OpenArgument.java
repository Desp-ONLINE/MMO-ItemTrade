package com.binggre.mmoitemshop.commands.arguments;

import com.binggre.binggreapi.command.CommandArgument;
import com.binggre.binggreapi.utils.NumberUtil;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.gui.TradeGUI;
import com.binggre.mmoitemshop.objects.MMOTrade;
import com.binggre.mmoitemshop.repository.ItemTradeRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenArgument implements CommandArgument {

    private final ItemTradeRepository repository = MMOItemTrade.getPlugin().getTradeRepository();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        int tradeId = NumberUtil.parseInt(args[1]);
        if (tradeId == NumberUtil.PARSE_ERROR) {
            player.sendMessage("숫자를 입력해 주세요.");
            return false;
        }
        MMOTrade mmoTrade = repository.get(tradeId);
        if (mmoTrade == null) {
            player.sendMessage("존재하지 않는 교환 상점입니다.");
            return false;
        }

        TradeGUI.open(player, mmoTrade);
        return false;
    }

    @Override
    public String getArg() {
        return "오픈";
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "[ID] - 교환 상점을 오픈합니다.";
    }

    @Override
    public String getPermission() {
        return "mmoitemtrade.admin.open";
    }

    @Override
    public String getPermissionMessage() {
        return "§c권한이 없습니다.";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }
}
