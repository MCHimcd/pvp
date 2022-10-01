package mc.pvp.basic;

import mc.pvp.PVP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
            giveItems(player);
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
        for (String tag : new String[]{"ready"}) p.removeScoreboardTag(tag);
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
                ItemStack h = new ItemStack(Material.IRON_HELMET);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("头盔", TextColor.color(255, 94, 114)));
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true);
                });
                ItemStack c = new ItemStack(Material.IRON_CHESTPLATE);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("铠甲", TextColor.color(255, 94, 114)));
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier("", 20, AttributeModifier.Operation.ADD_NUMBER));
                });
                ItemStack l = new ItemStack(Material.IRON_LEGGINGS);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("护腿", TextColor.color(255, 94, 114)));
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true);
                });
                ItemStack s = new ItemStack(Material.IRON_BOOTS);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("战靴", TextColor.color(255, 94, 114)));
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true);
                });
                ItemStack weapon = new ItemStack(Material.IRON_SWORD);
                weapon.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("迈克的宝剑", TextColor.color(6, 255, 237)));
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("", 7, AttributeModifier.Operation.ADD_NUMBER));
                });
                ItemStack skill1 = new ItemStack(Material.ARROW);
                skill1.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("肾上腺素", TextColor.color(105, 230, 255)));
                    itemMeta.setCustomModelData(10000001);
                });
                ItemStack skill2 = new ItemStack(Material.FLINT);
                skill2.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("致残打击", TextColor.color(255, 0, 19)));
                    itemMeta.setCustomModelData(10000002);
                });

                equipments[0] = h;
                equipments[1] = c;
                equipments[2] = l;
                equipments[3] = s;
                items.add(weapon);
                items.add(skill1);
                items.add(skill2);

            }
            /*
            防守
             */
            case 20000001 -> {
                ItemStack h = new ItemStack(Material.IRON_HELMET);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("头盔", TextColor.color(105, 230, 255)));
                });
                ItemStack c = new ItemStack(Material.IRON_CHESTPLATE);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("铠甲", TextColor.color(105, 230, 255)));
                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier("", 20, AttributeModifier.Operation.ADD_NUMBER));
                });
                ItemStack l = new ItemStack(Material.IRON_LEGGINGS);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("护腿", TextColor.color(105, 230, 255)));
                });
                ItemStack s = new ItemStack(Material.IRON_BOOTS);
                h.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("战靴", TextColor.color(105, 230, 255)));
                });
                ItemStack weapon = new ItemStack(Material.IRON_SWORD);
                weapon.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("史瑞克的断剑", TextColor.color(255, 0, 19)));
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("", 7, AttributeModifier.Operation.ADD_NUMBER));
                    itemMeta.setCustomModelData(10000001);
                });
                ItemStack skill1 = new ItemStack(Material.AMETHYST_SHARD);
                skill1.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("原能修复", TextColor.color(105, 230, 255)));
                    itemMeta.setCustomModelData(10000002);
                });
                equipments[0] = h;
                equipments[1] = c;
                equipments[2] = l;
                equipments[3] = s;
                items.add(weapon);
                items.add(skill1);

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
    }
}
