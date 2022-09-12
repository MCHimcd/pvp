package mc.pvp.basic.util;

import mc.pvp.basic.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static mc.pvp.PVP.config;

public class Menu {
    public static Inventory mainMenu() {
        Inventory m = Bukkit.createInventory(null, 9 * 6, Component.text("主菜单", TextColor.color(186, 255, 148)));
        ItemStack i1 = new ItemStack(Material.DIAMOND_SWORD);
        i1.editMeta(
                meta -> {
                    meta.displayName(Component.text("-进攻方职业介绍-", TextColor.color(255, 248, 161)));
                    meta.lore(
                            new ArrayList<>() {
                                {
                                    add(Component.text("-------------------------", TextColor.color(255, 255, 255)));
                                    add(Component.text("              进攻方职业的介绍", TextColor.color(255, 94, 114)));
                                    add(Component.text("-------------------------", TextColor.color(255, 255, 255)));
                                }

                            }
                    );
                });
        ItemStack i2 = new ItemStack(Material.SHIELD);
        i2.editMeta(
                meta -> {
                    meta.displayName(Component.text("-防守方职业介绍-", TextColor.color(255, 248, 161)));
                    meta.lore(
                            new ArrayList<>() {
                                {
                                    add(Component.text("-------------------------", TextColor.color(255, 255, 255)));
                                    add(Component.text("              防守方职业的介绍", TextColor.color(255, 94, 114)));
                                    add(Component.text("-------------------------", TextColor.color(255, 255, 255)));
                                }
                            }
                    );
                }
        );
        m.setItem(12, i1);
        m.setItem(14, i2);
        return m;
    }


    public static Inventory aClassMenu(Player p) {
        Inventory m = Bukkit.createInventory(null, 9, Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)));
        for (IntroItem i : aClasses(p)) {
            m.addItem(i.getItem());
        }
        return m;
    }

    public static Inventory dClassMenu(Player p) {
        Inventory m = Bukkit.createInventory(null, 9, Component.text("防守方职业菜单", TextColor.color(6, 255, 237)));
        for (IntroItem i : dClasses(p)) {
            m.addItem(i.getItem());
        }
        return m;
    }

    private static List<IntroItem> aClasses(Player p) {
        List<IntroItem> items = new ArrayList<>();
        for (int i = 10000000; i < 10000001; ++i) {
            if (p.getScoreboardTags().contains("AClass-%d".formatted(i))) {
                int finalI = i;
                String name = config.getString("class-item.a.%d.name".formatted(i));
                String item = config.getString("class-item.a.%d.item".formatted(i));
                if (Game.chosen_class.contains(i)) item = "minecraft:barrier";
                List<String> lore = (List<String>) config.getList("class-item.a.%d.lore".formatted(i));
                items.add(new IntroItem(name, lore, item, i, false));
            }
        }
        return items;
    }

    private static List<IntroItem> dClasses(Player p) {
        List<IntroItem> items = new ArrayList<>();
        for (int i = 20000000; i < 20000001; ++i) {
            if (p.getScoreboardTags().contains("DClass-%d".formatted(i))) {
                int finalI = i;
                String name = config.getString("class-item.d.%d.name".formatted(i));
                String item = config.getString("class-item.d.%d.item".formatted(i));
                if (Game.chosen_class.contains(i)) item = "minecraft:barrier";
                List<String> lore = (List<String>) config.getList("class-item.d.%d.lore".formatted(i));
                items.add(new IntroItem(name, lore, item, i, false));
            }
        }
        return items;
    }
}
