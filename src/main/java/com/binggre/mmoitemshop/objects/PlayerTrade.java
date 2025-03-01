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
    // trade Id, List<Log>
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

            for (TradeLog log : tradeLogList) {
                int page = log.getPage();
                TradeObject trade = mmoTrade.getTrade(page);

                if (trade == null) {
                    continue;
                }
                if (isTradableDate(trade, id, page)) {
                    log.clearAmount();
                }
            }
        });
    }

    public boolean isTradableAmount(TradeObject tradeObject, int id, int page) {
        List<TradeLog> tradeLogs = this.tradeLogs.get(id);
        if (tradeLogs == null || tradeLogs.isEmpty()) {
            return true;
        }
        if (tradeObject.getMaxCount() == -1) {
            return true;
        }
        return tradeLogs.stream()
                .filter(tradeLog -> tradeLog.getPage() == page)
                .noneMatch(tradeLog -> tradeLog.getAmount() >= tradeObject.getMaxCount());
    }

    public boolean isTradableDate(TradeObject tradeObject, int id, int page) {
        List<TradeLog> tradeLogs = this.tradeLogs.get(id);
        if (tradeLogs == null || tradeLogs.isEmpty()) {
            return true;
        }
        if (tradeObject.getReTradeMin() == -1) {
            return true;
        }
        return tradeLogs.stream()
                .filter(log -> log.getPage() == page)
                .map(log -> log.getLastDate().plusMinutes(tradeObject.getReTradeMin()))
                .noneMatch(nextTradeTime -> nextTradeTime.isAfter(LocalDateTime.now()));
    }

    public int getNextSeconds(TradeObject tradeObject, int id, int page) {
        List<TradeLog> tradeLogs = this.tradeLogs.get(id);
        if (tradeLogs == null) {
            return -1;
        }
        return tradeLogs.stream()
                .filter(log -> log.getPage() == page)
                .map(log -> log.getLastDate().plusMinutes(tradeObject.getReTradeMin()))
                .filter(nextTradeTime -> nextTradeTime.isAfter(LocalDateTime.now()))
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
                .ifPresentOrElse(TradeLog::increaseAmount, () -> this.tradeLogs.get(id).add(new TradeLog(page)));
    }
}
