package mc.pvp.basic.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IntroItem {
    public static ItemStack getItem(int id) {
        ItemStack item=new ItemStack(Material.AIR);
        List<Component> lore = new ArrayList<>();
        switch (id) {
            case 10000000, 20000000 -> {
                item.setType(Material.IRON_SWORD);
                item.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("基础职业", TextColor.color(110, 169, 191)));
                    lore.add(Component.text(""));
                    itemMeta.lore(lore);
                    itemMeta.setCustomModelData(id);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
                });
            }
            /*
            进攻方
             */
            case 10000001->{
                // TODO: 2022-9-18
            }
            /*
            防守方
             */
            case 20000001->{
                // TODO: 2022-9-18
            }
        }
        return item;
    }
}
