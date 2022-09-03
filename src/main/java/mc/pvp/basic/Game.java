package mc.pvp.basic;

import mc.pvp.PVP;
import org.bukkit.*;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.structure.StructureManager;

import java.util.Objects;
import java.util.Random;

public class Game {
    public static World main_world = Bukkit.getWorlds().get(0);
    public static boolean chooing = false;
    public static Location start;
    public static Location end;
    public static Location beacon;

    public static void reset() {
        chooing = false;
        start = new Location(main_world, 1000, 0, 1000);
        end = new Location(main_world, 1000, 0, 1000);
        beacon = new Location(main_world, 1000, 0, 1000);
    }

    public static void start() {
        chooing = false;
        Bukkit.getOnlinePlayers().forEach(player -> player.removeScoreboardTag("ready"));
        loadMap(1);
        Random r = new Random();
        int x = r.nextInt(end.getBlockX() - start.getBlockX()) + start.getBlockX();
        int z = r.nextInt(end.getBlockZ() - start.getBlockZ()) + start.getBlockZ();
        int y = main_world.getHighestBlockYAt(x, z) + 1;
        beacon = new Location(main_world, x, y, z);
        main_world.getBlockAt(beacon).setType(Material.BEACON);
    }

    //进攻赢
    public static void endA() {
        reset();
    }

    //防守赢
    public static void endD() {
        reset();
    }

    public static void resetPlayer(Player p){
        for (String tag : new String[]{"ready"}) p.removeScoreboardTag(tag);
        PVP.mainScoreboard.getTeams().forEach(team -> team.removePlayer(p));
    }

    private static void loadMap(int i) {
        StructureManager sm = Bukkit.getStructureManager();
        switch (i) {
            case 1 -> {
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:11")))).place(new Location(main_world, 1000, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:12")))).place(new Location(main_world, 1048, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:13")))).place(new Location(main_world, 1000, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:14")))).place(new Location(main_world, 1048, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                end = new Location(main_world, 1048, 48, 1048);
            }
        }
    }
}
