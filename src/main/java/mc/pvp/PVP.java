package mc.pvp;

import mc.pvp.basic.commands.Start;
import mc.pvp.basic.listener.MainL;
import mc.pvp.basic.listener.MenuL;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PVP extends JavaPlugin {
    public static Scoreboard mainScoreboard;
    public static HashMap<ArmorStand, Integer> show_damage = new HashMap<>();

    @Override
    public void onEnable() {
        //配置文件
//        saveDefaultConfig();
        YamlConfiguration config = (YamlConfiguration) getConfig();
        List<Map<?, ?>> list = config.getMapList("introduction.class-item.a");
        getLogger().info((String) list.get(0).get("name"));
        //计分板
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (String[] obj : new String[][]{
                new String[]{"money", "§6<金币>"},
                new String[]{"killplayer", "§7<杀敌数>"},
                new String[]{"death", "§7<死亡数>"},
                new String[]{"health", "§c<❤>"},
        })
            if (mainScoreboard.getObjective(obj[0]) == null)
                mainScoreboard.registerNewObjective(obj[0], Criteria.DUMMY, Component.text(obj[1]));
        //队伍
        for (String t : new String[]{"A", "D"})
            if (mainScoreboard.getTeam(t) == null) mainScoreboard.registerNewTeam(t);
        //tick
        new BukkitRunnable() {
            @Override
            public void run() {

                //显示血量
//                for (ArmorStand ar : show_damage.keySet()) {
//                    int time = show_damage.get(ar);
//                    if (time > 14) {
//                        ar.remove();
//                        show_damage.remove(ar);
//                        continue;
//                    }
//                    show_damage.put(ar, time + 1);
//                    ar.teleport(ar.getLocation().add(0, 0.2, 0));
//                }
//                getLogger().log(Level.CONFIG, String.valueOf(show_damage.size()));
            }
        }.runTaskTimer(this, 0, 1);
        //命令
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start());
        //事件
        Bukkit.getPluginManager().registerEvents(new MainL(), this);
        Bukkit.getPluginManager().registerEvents(new MenuL(), this);
    }
}
