package mc.pvp.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EquipmentHelper {
    public static ItemStack[] armors(ItemStack helmet, ItemStack chest_plate, ItemStack leggings, ItemStack boots) {
        return new ItemStack[]{helmet, chest_plate, leggings, boots};
    }

    public static ItemStack weapon(Material type, double attack_damage, double attack_speed, String name, List<String> lore, Map<Enchantment, Integer> enchantments, int model_data) {
        ItemStack r = new ItemStack(type);
        r.editMeta(itemMeta -> {
            if (name != null) itemMeta.displayName(Component.text(name));
            if (lore != null) itemMeta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("", attack_damage, AttributeModifier.Operation.ADD_NUMBER));
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("", attack_speed, AttributeModifier.Operation.ADD_NUMBER));
            if (model_data != 0) itemMeta.setCustomModelData(model_data);
            if (enchantments != null)
                enchantments.forEach((enchantment, integer) -> itemMeta.addEnchant(enchantment, integer, true));
        });
        return r;
    }

    public static ItemStack weapon(Material type, double attack_damage, double attack_speed, String name, List<String> lore, Map<Enchantment, Integer> enchantments) {
        return weapon(type, attack_damage, attack_speed, name, lore, enchantments, 0);
    }

    public static ItemStack weapon(Material type, double attack_damage, double attack_speed, String name, List<String> lore) {
        return weapon(type, attack_damage, attack_speed, name, lore, null);
    }

    public static ItemStack weapon(Material type, double attack_damage, double attack_speed, String name) {
        return weapon(type, attack_damage, attack_speed, name, null);
    }

    public static ItemStack weapon(Material type, double attack_damage, double attack_speed) {
        return weapon(type, attack_damage, attack_speed, null);
    }

    public static ItemStack armor(Material type, String name, List<String> lore, Map<Enchantment, Integer> enchantments, double armor, double armor_toughness) {
        ItemStack r = new ItemStack(type);
        r.editMeta(itemMeta -> {
            if (name != null) itemMeta.displayName(Component.text(name));
            if (lore != null) itemMeta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            if (armor != 0) {
                itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("", armor, AttributeModifier.Operation.ADD_NUMBER));
                itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("", armor_toughness, AttributeModifier.Operation.ADD_NUMBER));
            }
            if (enchantments != null)
                enchantments.forEach((enchantment, integer) -> itemMeta.addEnchant(enchantment, integer, true));
        });
        return r;
    }

    public static ItemStack armor(Material type, String name, List<String> lore, Map<Enchantment, Integer> enchantments) {
        return armor(type, name, lore, enchantments, 0, 0);
    }

    public static ItemStack armor(Material type, String name, List<String> lore) {
        return armor(type, name, lore, null);
    }

    public static ItemStack armor(Material type, String name) {
        return armor(type, name, null);
    }

    public static ItemStack skill(Material type, String name, int index) {
        ItemStack r = new ItemStack(type);
        r.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(name));
            itemMeta.setCustomModelData(index);
        });
        return r;
    }
}
