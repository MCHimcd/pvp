package mc.pvp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import org.bukkit.structure.StructureManager;

import java.util.*;

import static mc.pvp.util.EquipmentHelper.*;

public class Game {
    public static final BossBar TIME = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
    public static final WorldBorder WORLD_BORDER = Bukkit.getWorlds().get(0).getWorldBorder();
    public final static World WORLD = Bukkit.getWorlds().get(0);
    public final static Location HUB = new Location(WORLD, 0, -60, 0);
    public static boolean choosing = false;
    public static Location start;
    public static Location end;
    public static Location beacon;
    public static List<Player> players;
    public static List<Integer> chosen_class;

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
        int y = WORLD.getHighestBlockYAt(x, z) + 1;
        beacon = new Location(WORLD, x, y, z);
        WORLD.getBlockAt(beacon).setType(Material.BEACON);
        players.stream()
                .filter(player -> PVP.defenders.hasPlayer(player))
                .forEach(player -> player.teleport(beacon.clone().add(0, 1, 0)));
        players.forEach(player -> {
            TIME.addPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            playerInit(player);
        });
    }

    //进攻赢
    public static void endA() {
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendTitlePart(TitlePart.TITLE, Component.text("进攻方", NamedTextColor.DARK_RED).append(Component.text("获胜", TextColor.color(104, 162, 189)))));
        Bukkit.getOnlinePlayers().stream().filter(player -> PVP.attackers.hasPlayer(player)).forEach(player -> {
            player.sendMessage("§l§6[SYSTEM] §7你获胜了，奖励你100个金币！");
            Score money = Objects.requireNonNull(PVP.mainScoreboard.getObjective("money")).getScore(player);
            money.setScore(money.getScore() + 100);
        });
        reset();
    }

    //防守赢
    public static void endD() {
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendTitlePart(TitlePart.TITLE, Component.text("防守方", NamedTextColor.DARK_BLUE).append(Component.text("获胜", TextColor.color(104, 162, 189)))));
        Bukkit.getOnlinePlayers().stream().filter(player -> PVP.defenders.hasPlayer(player)).forEach(player -> {
            player.sendMessage("§l§6[SYSTEM] §7你获胜了，奖励你100个金币！");
            Score money = Objects.requireNonNull(PVP.mainScoreboard.getObjective("money")).getScore(player);
            money.setScore(money.getScore() + 100);
        });
        reset();
    }

    public static void reset() {
        Bukkit.getOnlinePlayers().forEach(Game::resetPlayer);
        choosing = false;
        start = new Location(WORLD, 1000, 0, 1000);
        end = new Location(WORLD, 1000, 0, 1000);
        beacon = new Location(WORLD, 1000, 0, 1000);
        players = new ArrayList<>();
        chosen_class = new ArrayList<>();
        TIME.removeAll();
        TIME.setColor(BarColor.GREEN);
        TIME.setProgress(1);
        WORLD_BORDER.reset();
    }

    public static int getClassID(Player p) {
        return Objects.requireNonNull(PVP.mainScoreboard.getObjective("class_id")).getScore(p).getScore();
    }

    public static void setClassID(Player p, int id) {
        Objects.requireNonNull(PVP.mainScoreboard.getObjective("class_id")).getScore(p).setScore(id);
    }

    public static void resetPlayer(Player p) {
        p.setBedSpawnLocation(HUB, true);
        p.teleportAsync(HUB);
        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        p.setFoodLevel(20);
        for (PotionEffectType type : PotionEffectType.values()) p.removePotionEffect(type);
        for (String tag : new String[]{"ready", "MikeSkill1"}) p.removeScoreboardTag(tag);
        for (String sc : new String[]{"class_id", "cd1", "cd2"})
            Objects.requireNonNull(PVP.mainScoreboard.getObjective(sc)).getScore(p).setScore(0);
        PVP.mainScoreboard.getTeams().forEach(team -> team.removePlayer(p));
    }

    private static void loadMap(int i) {
        StructureManager sm = Bukkit.getStructureManager();
        switch (i) {
            case 1 -> {
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:11")))).place(new Location(WORLD, 1000, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:12")))).place(new Location(WORLD, 1048, 0, 1000), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:13")))).place(new Location(WORLD, 1000, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                Objects.requireNonNull(sm.loadStructure(Objects.requireNonNull(NamespacedKey.fromString("minecraft:14")))).place(new Location(WORLD, 1048, 0, 1048), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
                end = new Location(WORLD, 1096, 48, 1096);
                players.forEach(player -> player.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 16), new ItemStack(Material.SANDSTONE, 32)));
                players.stream()
                        .filter(player -> PVP.attackers.hasPlayer(player))
                        .forEach(player -> player.teleport(new Location(WORLD, 1048, 1000, 1048)));
                WORLD_BORDER.setCenter(1048, 1048);
                WORLD_BORDER.setSize(96);
            }
        }
    }

    private static void playerInit(Player p) {
        int id = getClassID(p);
        PlayerInventory inv = p.getInventory();
        List<ItemStack> items = new ArrayList<>();
        int additionalHealth = 20;
        ItemStack[] equipments = new ItemStack[]{
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR)
        };
        items.add(new ItemStack(Material.IRON_AXE));
        items.add(new ItemStack(Material.IRON_PICKAXE));
        items.add(new ItemStack(Material.IRON_SHOVEL));
        switch (id) {
            case 10000000, 20000000 -> {
                items.add(new ItemStack(Material.IRON_SWORD));
                equipments[0].setType(Material.IRON_HELMET);
                equipments[1].setType(Material.IRON_CHESTPLATE);
                equipments[2].setType(Material.IRON_LEGGINGS);
                equipments[3].setType(Material.IRON_BOOTS);
            }
            /*
            进攻
             */
            case 10000001 -> {
                equipments = armors(
                        armor(Material.IRON_HELMET, "§c头盔", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_CHESTPLATE, "§c铠甲", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_LEGGINGS, "§c护腿", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_BOOTS, "§c战靴", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }})

                );
                items.add(weapon(Material.IRON_SWORD, 7, -2.4, "§b迈克的宝剑"));
                items.add(skill(Material.ARROW, "§b肾上腺素", 1));
                items.add(skill(Material.ARROW, "§c致残打击", 2));
            }
            /*
            防守
             */
            case 20000001 -> {
                equipments = armors(
                        armor(Material.IRON_HELMET, "§c头盔", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_CHESTPLATE, "§c铠甲", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_LEGGINGS, "§c护腿", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }}),
                        armor(Material.IRON_BOOTS, "§c战靴", null, new HashMap<>() {{
                            put(Enchantment.PROTECTION_PROJECTILE, 1);
                        }})

                );
                items.add(weapon(Material.IRON_SWORD, 7, -2.4, "§4史瑞克的断剑", null, null, 1));
                items.add(skill(Material.AMETHYST_SHARD, "§b原能修复", 2));
            }
        }
        items.forEach(item -> {
            item.editMeta(itemMeta -> {
                if (!itemMeta.hasCustomModelData()) itemMeta.setCustomModelData(10000000);
            });
            inv.addItem(item);
        });
        EntityEquipment equipment = p.getEquipment();
        equipment.setHelmet(equipments[0]);
        equipment.setChestplate(equipments[1]);
        equipment.setLeggings(equipments[2]);
        equipment.setBoots(equipments[3]);
        Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20 + additionalHealth);
    }
}
