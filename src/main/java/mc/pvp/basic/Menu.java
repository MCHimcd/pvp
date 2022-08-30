package mc.pvp.basic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
        m.setItem(12,i1);
        m.setItem(14,i2);
        return m;
    }


    public static Inventory aClassMenu0() {
        Inventory m = Bukkit.createInventory(null, 9, Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)));
        return m;
    }

    public static Inventory dClassMenu0() {
        Inventory m = Bukkit.createInventory(null, 9, Component.text("防守方职业菜单", TextColor.color(6, 255, 237)));
        return m;
    }
}
