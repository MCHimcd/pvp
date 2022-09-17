package mc.pvp.basic.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IntroItem {
    public static ItemStack getItem(int id) {
        ItemStack item;
        List<Component> lore=new ArrayList<>();
        switch (id) {
            /*
            进攻方
             */
            case 10000000->{
                item=new ItemStack(Material.DIAMOND);
                item.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("库奇"));
                    lore.add(Component.text("test"));
                    itemMeta.lore(lore);
                    itemMeta.setCustomModelData(id);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
                });
            }
            /*
            防守方
             */
            case 20000000->{
                item=new ItemStack(Material.DIAMOND);
                item.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("帕奇"));
                    lore.add(Component.text("test2"));
                    itemMeta.lore(lore);
                    itemMeta.setCustomModelData(id);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
                });
            }
            default -> item=new ItemStack(Material.AIR);
        }
        return item;
    }
}
