package mc.pvp;

import mc.pvp.basic.commands.Start;
import mc.pvp.basic.listener.MainL;
import mc.pvp.basic.listener.MenuL;
import mc.pvp.basic.util.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public class PVP extends JavaPlugin {
    public static Scoreboard mainScoreboard;
    public static YamlConfiguration config;

    private void init() {
        config = (YamlConfiguration) getConfig();
        //计分板
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (String[] obj : new String[][]{
                new String[]{"money", "§6<金币>"},
                new String[]{"killplayer", "§7<杀敌数>"},
                new String[]{"death", "§7<死亡数>"},
                new String[]{"health", "§c<❤>"},
                new String[]{"classID", ""}
        })
            if (mainScoreboard.getObjective(obj[0]) == null)
                mainScoreboard.registerNewObjective(obj[0], Criteria.DUMMY, Component.text(obj[1]));
        //队伍
        for (String t : new String[]{"A", "D"})
            if (mainScoreboard.getTeam(t) == null) mainScoreboard.registerNewTeam(t);
        //命令
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start());
        //事件
        Bukkit.getPluginManager().registerEvents(new MainL(), this);
        Bukkit.getPluginManager().registerEvents(new MenuL(), this);
        //tick
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Game.chooing) {
                    if (Bukkit.getOnlinePlayers().stream().allMatch(player -> player.getScoreboardTags().contains("ready")))
                        Game.start();
                }
            }
        }.runTaskTimer(this, 0, 1);
        PVP.mainScoreboard.getTeam("A").getEntries().forEach(e->getLogger().info(e));
    }

    private void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                File check = new File(getServer().getWorlds().get(0).getWorldFolder().getPath() + "/datapacks/pvp");
                if (!(check.exists() && check.isFile())) {
                    Logger logger = getLogger();
                    logger.info("************************");
                    logger.info("未检测到依赖地图，此插件已禁用");
                    logger.info("************************");
                    disable();
                } else init();
            }
        }.runTaskLater(this, 1);
    }
}
