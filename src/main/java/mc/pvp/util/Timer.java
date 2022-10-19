package mc.pvp.util;

import mc.pvp.PVP;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static mc.pvp.Game.*;
import static mc.pvp.PVP.mainScoreboard;

public class Timer extends BukkitRunnable {
    @Override
    public void run() {
        if (choosing && players.stream().allMatch(player -> player.getScoreboardTags().contains("ready"))) start();
        if (!players.isEmpty()) {
            if (players.stream().filter(player -> PVP.attackers.hasPlayer(player)).allMatch(player -> player.getGameMode() == GameMode.SPECTATOR)) {
                endD();
                return;
            } else if (players.stream().filter(player -> PVP.defenders.hasPlayer(player)).allMatch(player -> player.getGameMode() == GameMode.SPECTATOR)) {
                endA();
                return;
            }
            int seconds = gameTime.getScore() / 20;
            if (seconds > 180) return;
            double max_seconds = 180;
            if (seconds <= 0) {
                endD();
                return;
            }
            if (seconds < max_seconds / 2) time.setColor(BarColor.YELLOW);
            if (seconds < max_seconds / 4) time.setColor(BarColor.RED);
            time.setTitle("§6剩余时间：§e%d".formatted(seconds / 20));
            time.setProgress((gameTime.getScore() - 1) / max_seconds);
        }
    }
}
