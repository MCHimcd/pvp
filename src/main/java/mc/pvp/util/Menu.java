package mc.pvp.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        Inventory m = Bukkit.createInventory(p, 9, Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)));
        for (ItemStack i : aClasses(p)) m.addItem(i);
        return m;
    }

    public static Inventory dClassMenu(Player p) {
        Inventory m = Bukkit.createInventory(p, 9, Component.text("防守方职业菜单", TextColor.color(6, 255, 237)));
        for (ItemStack i : dClasses(p)) m.addItem(i);
        return m;
    }

    private static List<ItemStack> aClasses(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(IntroItem.get(10000000));
        for (int i = 10000001; i < 10000002; ++i)
            if (p.getScoreboardTags().contains("Class-%d".formatted(i))) items.add(IntroItem.get(i));
        return items;
    }

    private static List<ItemStack> dClasses(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(IntroItem.get(20000000));
        for (int i = 20000001; i < 20000002; ++i)
            if (p.getScoreboardTags().contains("Class-%d".formatted(i))) items.add(IntroItem.get(i));
        return items;
    }
}
