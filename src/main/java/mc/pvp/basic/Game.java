package mc.pvp.basic;

import mc.pvp.PVP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import org.bukkit.structure.StructureManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    public static World main_world = Bukkit.getWorlds().get(0);
    public static Location hub = new Location(main_world, 0, -60, 0);
    public static boolean choosing = false;
    public static Location start;
    public static Location end;
    public static Location beacon;
    public static List<Player> players;
    public static List<Integer> chosen_class;
    public static BossBar time = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
    ;

    static {
        reset();
    }

    public static void start() {
        choosing = false;
        Bukkit.getOnlinePlayers().forEach(player -> player.removeScoreboardTag("ready"));
        loadMap(1);
        Random r = new Random();
        int x = r.nextInt(end.getBlockX() - start.getBlockX()) + start.getBlockX();
        int z = r.nextInt(end.getBlockZ() - start.getBlockZ()) + start.getBlockZ();
        int y = main_world.getHighestBlockYAt(x, z) + 1;
        beacon = new Location(main_world, x, y, z);
        main_world.getBlockAt(beacon).setType(Material.BEACON);
        players.stream()
                .filter(player -> PVP.d.hasPlayer(player))
                .forEach(player -> player.teleport(beacon.clone().add(0, 1, 0)));
        players.forEach(player -> {
            time.addPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            giveItems(player);
        });
    }

    //进攻赢
    public static void endA() {
        reset();
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendTitlePart(TitlePart.TITLE, Component.text("进攻方", NamedTextColor.DARK_RED).append(Component.text("获胜", TextColor.color(104, 162, 189)))));
        Bukkit.getOnlinePlayers().stream().filter(player -> PVP.a.hasPlayer(player)).forEach(player -> {
            player.sendMessage("§l§6[SYSTEM] §7你获胜了，%s奖励你100个金币！");
            Score money = Objects.requireNonNull(PVP.mainScoreboard.getObjective("money")).getScore(player);
            money.setScore(money.getScore() + 100);
        });
    }

    //防守赢
    public static void endD() {
        reset();
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendTitlePart(TitlePart.TITLE, Component.text("防守方", NamedTextColor.DARK_BLUE).append(Component.text("获胜", TextColor.color(104, 162, 189)))));
        Bukkit.getOnlinePlayers().stream().filter(player -> PVP.d.hasPlayer(player)).forEach(player -> {
            player.sendMessage("§l§6[SYSTEM] §7你获胜了，%s奖励你100个金币！");
            Score money = Objects.requireNonNull(PVP.mainScoreboard.getObjective("money")).getScore(player);
            money.setScore(money.getScore() + 100);
        });
    }

    public static void reset() {
        Bukkit.getOnlinePlayers().forEach(Game::resetPlayer);
        choosing = false;
        start = new Location(main_world, 1000, 0, 1000);
        end = new Location(main_world, 1000, 0, 1000);
        beacon = new Location(main_world, 1000, 0, 1000);
        players = new ArrayList<>();
        chosen_class = new ArrayList<>();
        time.removeAll();
        time.setColor(BarColor.GREEN);
        time.setProgress(1);
    }

    public static int getClassID(Player p) {
        return Objects.requireNonNull(PVP.mainScoreboard.getObjective("class_id")).getScore(p).getScore();
    }

    public static void setClassID(Player p, int id) {
        Objects.requireNonNull(PVP.mainScoreboard.getObjective("class_id")).getScore(p).setScore(id);
    }

    public static void resetPlayer(Player p) {
        p.teleport(hub);
        p.setBedSpawnLocation(hub, true);
        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);
        for (PotionEffectType type : PotionEffectType.values()) p.removePotionEffect(type);
        for (String tag : new String[]{"ready"}) p.removeScoreboardTag(tag);
        PVP.mainScoreboard.getTeams().forEach(team -> team.removePlayer(p));
        Objects.requireNonNull(PVP.mainScoreboard.getObjective("class_id")).getScore(p).setScore(0);
    }

    private static void loadMap(int i) {
        StructureManager sm = Bukkit.getStructureManager();
        switch (i) {
            case 1 -> {
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:11")))).place(new Location(main_world, 1000, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:12")))).place(new Location(main_world, 1048, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:13")))).place(new Location(main_world, 1000, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:14")))).place(new Location(main_world, 1048, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                end = new Location(main_world, 1096, 48, 1096);
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> Objects.requireNonNull(PVP.mainScoreboard.getTeam("A")).hasPlayer(player))
                        .forEach(player -> player.teleport(new Location(main_world, 1048, 1000, 1048)));
            }
        }
    }

    private static void giveItems(Player p) {
        int id = getClassID(p);
        PlayerInventory inv = p.getInventory();
        List<ItemStack> items = new ArrayList<>();
        ItemStack[] equipments = new ItemStack[]{
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR)
        };
        switch (id) {
            /*
            进攻
             */
            case 10000000 -> {
                items.add(new ItemStack(Material.DIAMOND_SWORD));
            }
            /*
            防守
             */
            case 20000000 -> {
                items.add(new ItemStack(Material.SHIELD));
            }
        }
        items.forEach(inv::addItem);
        EntityEquipment equipment = p.getEquipment();
        equipment.setHelmet(equipments[0]);
        equipment.setChestplate(equipments[1]);
        equipment.setLeggings(equipments[2]);
        equipment.setBoots(equipments[3]);
    }
}
