package mc.pvp;

import mc.pvp.command.Start;
import mc.pvp.event.GameL;
import mc.pvp.event.MainL;
import mc.pvp.event.MenuL;
import mc.pvp.event.SkillL;
import mc.pvp.util.Timer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.logging.Logger;

import static mc.pvp.Game.*;

public class PVP extends JavaPlugin {
    public static PVP plugin;
    public static Logger l;
    public static Team attackers, defenders;
    public static Scoreboard mainScoreboard;
    public static BukkitRunnable timer;

    @Override
    public void onEnable() {
        plugin = this;
        l = getLogger();
        time = Bukkit.getBossBar(NamespacedKey.minecraft("time"));
        //命令
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start());
        //事件监听
        PluginManager pm = Bukkit.getPluginManager();
        for (Listener l : new Listener[]{
                new MainL(),
                new MenuL(),
                new GameL(),
                new SkillL()
        })
            pm.registerEvents(l, this);
        //计分板
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objects.requireNonNull(mainScoreboard.getObjective("system")).getScore("#time");
        for (String[] obj : new String[][]{
                new String[]{"money", "§6<金币>"},
                new String[]{"kill_player", "§7<杀敌数>"},
                new String[]{"death", "§7<死亡数>"},
                new String[]{"health", "§c<❤>"},
                new String[]{"class_id", ""},
                new String[]{"cd1", ""},
                new String[]{"cd2", ""},
                new String[]{"system", ""}
        }) {
            if (mainScoreboard.getObjective(obj[0]) == null)
                mainScoreboard.registerNewObjective(obj[0], Criteria.DUMMY, Component.text(obj[1]));
        }
        //队伍
        if (mainScoreboard.getTeam("A") == null) {
            attackers = mainScoreboard.registerNewTeam("A");
            attackers.setAllowFriendlyFire(false);
            attackers.color(NamedTextColor.DARK_RED);
            attackers.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        } else attackers = mainScoreboard.getTeam("A");
        if (mainScoreboard.getTeam("D") == null) {
            defenders = mainScoreboard.registerNewTeam("D");
            defenders.setAllowFriendlyFire(false);
            defenders.color(NamedTextColor.DARK_BLUE);
            defenders.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        } else defenders = mainScoreboard.getTeam("D");
        //tick
        timer = new Timer();
        timer.runTaskTimer(this, 0, 1);
    }
}
