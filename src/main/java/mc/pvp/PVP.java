package mc.pvp;

import mc.pvp.basic.commands.Start;
import mc.pvp.basic.listener.GameL;
import mc.pvp.basic.listener.MainL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static mc.pvp.basic.Game.*;

public class PVP extends JavaPlugin {
    public static PVP plugin;
    public static Logger l;
    public static Team a, d;
    public static Scoreboard mainScoreboard;
    public static YamlConfiguration config;
    public static BukkitRunnable timer = new BukkitRunnable() {
        @Override
        public void run() {
            if (choosing && players.stream().allMatch(player -> player.getScoreboardTags().contains("ready"))) start();
            if (!players.isEmpty()) {
                if (players.stream().filter(player -> PVP.a.hasPlayer(player)).allMatch(player -> player.getGameMode() == GameMode.SPECTATOR))
                    endD();
                else if (players.stream().filter(player -> PVP.d.hasPlayer(player)).allMatch(player -> player.getGameMode() == GameMode.SPECTATOR))
                    endA();
                final double max_time = 4600;
                int game_time = (int) (time.getProgress() * max_time);
                if (game_time <= 0) endD();
                if (game_time < max_time / 2) time.setColor(BarColor.YELLOW);
                if (game_time < max_time / 4) time.setColor(BarColor.RED);
                time.setTitle("§6剩余时间：§e%d".formatted(game_time / 20));
                time.setProgress((game_time - 1) / max_time);
            }
        }
    };

    @Override
    public void onEnable() {
        plugin = this;
        l = getLogger();
        timer.runTaskTimer(this, 0, 1);
        config = (YamlConfiguration) getConfig();
        //计分板
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objects.requireNonNull(config.getList("scoreboard")).stream().map(arr -> (List<String>) arr).forEach(obj -> {
            if (mainScoreboard.getObjective(obj.get(0)) == null)
                mainScoreboard.registerNewObjective(obj.get(0), Criteria.DUMMY, Component.text(obj.get(1)));
        });
        //队伍
        if (mainScoreboard.getTeam("A") == null) {
            a = mainScoreboard.registerNewTeam("A");
            a.setAllowFriendlyFire(false);
            a.color(NamedTextColor.DARK_RED);
            a.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        } else a = mainScoreboard.getTeam("A");
        if (mainScoreboard.getTeam("D") == null) {
            d = mainScoreboard.registerNewTeam("D");
            d.setAllowFriendlyFire(false);
            d.color(NamedTextColor.DARK_BLUE);
            d.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        } else d = mainScoreboard.getTeam("D");
        //命令
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start());
        //事件
        PluginManager pm = Bukkit.getPluginManager();
        for (Listener l : new Listener[]{
                new MainL(),
//                new MenuL(),
                new GameL()
        })
            pm.registerEvents(l, this);
    }
}
