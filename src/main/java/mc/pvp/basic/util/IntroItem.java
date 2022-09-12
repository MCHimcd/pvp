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
    int id;
    boolean e;
    String name, item;
    List<String> lore;
    ItemStack thisItemStack;

    public IntroItem(String name, List<String> lore, String item, int id, boolean e) {
        this.name = name;
        this.lore = new ArrayList<>(lore);
        this.item = item;
        this.id = id;
        this.e = e;
    }
    {
        thisItemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(item)));
        thisItemStack.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(name));
            itemMeta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            itemMeta.setCustomModelData(id);
            if (e) itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        });
    }

    public ItemStack getItem() {
        return thisItemStack;
    }
}
