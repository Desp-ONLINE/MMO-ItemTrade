package com.binggre.mmoitemshop.objects;

import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.repository.ItemTradeRepository;
import com.binggre.mongolibraryplugin.base.MongoData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class PlayerTrade implements MongoData<UUID> {

    private final UUID id;
    private String nickname;
    private final Map<Integer, List<TradeLog>> tradeLogs;

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

    public void reloadDateState() {
        ItemTradeRepository tradeRepository = MMOItemTrade.getPlugin().getTradeRepository();
        tradeLogs.forEach((id, tradeLogList) -> {
            MMOTrade mmoTrade = tradeRepository.get(id);
            if (mmoTrade == null) {
                return;
            }
            for (TradeLog log : tradeLogList) {
                int page = log.getPage();
                TradeObject trade = mmoTrade.getTrade(page);
                if (trade == null || trade.getReTradeMin() <= 0) {
                    continue;
                }
                if (isWindowExpired(log, trade.getReTradeMin())) {
                    log.clearAmount();
                }
            }
        });
    }

    private boolean isWindowExpired(TradeLog log, int reTradeMin) {
        if (log.getLastDate() == null) {
            return true;
        }
        return !log.getLastDate().plusMinutes(reTradeMin).isAfter(LocalDateTime.now());
    }

    public boolean isTradableAmount(TradeObject tradeObject, int id, int page) {
        if (tradeObject.getMaxCount() <= 0) {
            return true;
        }
        List<TradeLog> logs = this.tradeLogs.get(id);
        if (logs == null || logs.isEmpty()) {
            return true;
        }
        return logs.stream()
                .filter(log -> log.getPage() == page)
                .noneMatch(log -> log.getAmount() >= tradeObject.getMaxCount());
    }

    public boolean isTradableDate(TradeObject tradeObject, int id, int page) {
        return true;
    }

    public int getNextSeconds(TradeObject tradeObject, int id, int page) {
        if (tradeObject.getReTradeMin() <= 0) {
            return -1;
        }
        List<TradeLog> tradeLogs = this.tradeLogs.get(id);
        if (tradeLogs == null) {
            return -1;
        }
        return tradeLogs.stream()
                .filter(log -> log.getPage() == page)
                .filter(log -> log.getLastDate() != null)
                .map(log -> log.getLastDate().plusMinutes(tradeObject.getReTradeMin()))
                .findFirst()
                .map(this::getSecondsUntilNextTrade)
                .orElse(-1);
    }

    private int getSecondsUntilNextTrade(LocalDateTime nextTradeTime) {
        return Math.max(0, (int) Duration.between(LocalDateTime.now(), nextTradeTime).getSeconds());
    }

    public TradeLog findTradeLog(int id, int page) {
        List<TradeLog> tradeLogs = this.tradeLogs.get(id);
        if (tradeLogs == null) {
            return null;
        }
        return tradeLogs.stream()
                .filter(log -> log.getPage() == page)
                .findFirst()
                .orElse(null);
    }

    public void log(int id, int page) {
        this.tradeLogs.computeIfAbsent(id, k -> new ArrayList<>())
                .stream()
                .filter(tradeLog -> tradeLog.getPage() == page)
                .findFirst()
                .ifPresentOrElse(log -> {
                    if (log.getLastDate() == null) {
                        log.startNewWindow();
                    } else {
                        log.increaseAmount();
                    }
                }, () -> this.tradeLogs.get(id).add(new TradeLog(page)));
    }
}
