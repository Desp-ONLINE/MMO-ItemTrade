package com.binggre.mmoitemshop.objects;

import com.binggre.mongolibraryplugin.base.MongoData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerTrade implements MongoData<UUID> {

    private final UUID id;
    private String nickname;
    // trade Id, amount
    private final Map<Integer, TradeLog> tradeLogs;

    public PlayerTrade(Player player) {
        this.id = player.getUniqueId();
        this.tradeLogs = new HashMap<>();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void updateNickname(Player player) {
        nickname = player.getName();
    }

    public boolean isTradableAmount(TradeObject tradeObject, int page) {
        TradeLog tradeLog = tradeLogs.get(page);
        if (tradeLog == null) {
            return true;
        }
        if (tradeObject.getMaxCount() == -1) {
            return true;
        }
        return tradeLog.getAmount() < tradeObject.getMaxCount();
    }

    public boolean isTradableDate(TradeObject tradeObject, int page) {
        TradeLog tradeLog = tradeLogs.get(page);
        if (tradeLog == null) {
            return true;
        }
        if (tradeObject.getReTradeMin() == -1) {
            return true;
        }
        LocalDateTime nextTradeTime = tradeLog.getDate().plusMinutes(tradeObject.getReTradeMin());
        return !nextTradeTime.isAfter(LocalDateTime.now());
    }

    public int getNextSeconds(TradeObject tradeObject, int page) {
        TradeLog tradeLog = tradeLogs.get(page);
        if (tradeLog == null) {
            return -1;
        }
        LocalDateTime nextTradeTime = tradeLog.getDate().plusMinutes(tradeObject.getReTradeMin());
        return getSecondsUntilNextTrade(nextTradeTime);
    }

    private int getSecondsUntilNextTrade(LocalDateTime nextTradeTime) {
        return Math.max(0, (int) Duration.between(LocalDateTime.now(), nextTradeTime).getSeconds());
    }

    public void log(int page) {
        tradeLogs.compute(page, (key, tradeLog) -> {
            if (tradeLog == null) {
                return new TradeLog(page);
            } else {
                tradeLog.increaseAmount();
                return tradeLog;
            }
        });
    }
}
