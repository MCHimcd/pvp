package mc.pvp.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IntroItem {
    public static ItemStack get(int id) {
        ItemStack item = new ItemStack(Material.AIR);
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
            case 10000001 -> {
                // 2022.10.1
                item.setType(Material.IRON_SWORD);
                item.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("迈克", TextColor.color(255, 0, 19)));
                    lore.add(Component.text("-----------------", TextColor.color(0, 0, 0)));
                    lore.add(Component.text("职业类型：近战", TextColor.color(255, 236, 7)));
                    lore.add(Component.text("职业定位：战士", TextColor.color(255, 236, 7)));
                    lore.add(Component.text("技能：", TextColor.color(255, 248, 161)));
                    lore.add(Component.text("①技能：肾上腺素", TextColor.color(255, 0, 249)));
                    lore.add(Component.text("介绍：加快自身的心率以提高自己的力量和速度！", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("持续时间：5s 冷却时间：15s", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("②技能：致残打击", TextColor.color(255, 0, 249)));
                    lore.add(Component.text("介绍：迈克将灵能输入到武器上，使下次的攻击额外造成", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("伤害并且减少目标移动速度。", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("冷却时间：15s", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("-----------------", TextColor.color(0, 0, 0)));
                    itemMeta.lore(lore);
                    itemMeta.setCustomModelData(id);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
                });
            }
            /*
            防守方
             */
            case 20000001 -> {
                item.setType(Material.IRON_SWORD);
                item.editMeta(itemMeta -> {
                    itemMeta.displayName(Component.text("史瑞克", TextColor.color(255, 0, 19)));
                    lore.add(Component.text("-----------------", TextColor.color(0, 0, 0)));
                    lore.add(Component.text("职业类型：近战", TextColor.color(255, 236, 7)));
                    lore.add(Component.text("职业定位：战士", TextColor.color(255, 236, 7)));
                    lore.add(Component.text("技能：", TextColor.color(255, 248, 161)));
                    lore.add(Component.text("①技能：格挡", TextColor.color(255, 0, 249)));
                    lore.add(Component.text("介绍：史瑞克减少下次受到伤害的80%", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("作用时间：0.6s 冷却时间：15s ", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("②技能：原能修复", TextColor.color(255, 0, 249)));
                    lore.add(Component.text("介绍：史瑞克使用元能加快回复自身和周围友军的血量。", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("作用半径：5 持续时间：30s", TextColor.color(186, 255, 148)));
                    lore.add(Component.text("-----------------", TextColor.color(0, 0, 0)));
                    itemMeta.lore(lore);
                    itemMeta.setCustomModelData(id);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
                });
            }
        }
        return item;
    }
}
