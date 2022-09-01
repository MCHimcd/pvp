package mc.pvp.basic.util;

import org.bukkit.Bukkit;

public class Game {
    public static boolean chooing=false;
    public static void reset(){
        chooing=false;
    }
    public static void start(){
        chooing=false;
        Bukkit.getOnlinePlayers().forEach(player -> player.removeScoreboardTag("ready"));
    }
}
