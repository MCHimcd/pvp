package mc.pvp.event;

import mc.pvp.PVP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static mc.pvp.Game.*;

public class MenuL implements Listener {
    private final HashMap<Component, Integer> menuNames = new HashMap<>() {
        {
            put(Component.text("主菜单", TextColor.color(186, 255, 148)), 1);
            put(Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)), 2);
            put(Component.text("防守方职业菜单", TextColor.color(6, 255, 237)), 3);
        }
    };

    @EventHandler
    //职业菜单
    public void onClickClassMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (menuNames.get(p.getOpenInventory().title()) == null) return;
        if (e.getRawSlot() < 0) return;
        ItemStack i = e.getCurrentItem();
        if (i == null) return;
        e.setCancelled(true);
        if (i.getType().equals(Material.BARRIER)) return;
        if (!i.getItemMeta().hasCustomModelData()) return;
        int item = i.getItemMeta().getCustomModelData();
        if (chosen_class.contains(item)) return;
        setClassID(p, item);
        p.addScoreboardTag("ready");
        if (PVP.attackers.hasPlayer(p))
            players.stream().filter(player -> PVP.attackers.hasPlayer(p)).forEach(this::updateInventory);
        if (PVP.defenders.hasPlayer(p))
            players.stream().filter(player -> PVP.defenders.hasPlayer(p)).forEach(this::updateInventory);
        if (item == 10000000 || item == 20000000) return;
        chosen_class.remove(Integer.valueOf(getClassID(p)));
        chosen_class.add(item);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!choosing) return;
        if (!(e.getPlayer() instanceof Player p)) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                p.openInventory(e.getInventory());
            }
        }.runTaskLater(PVP.plugin, 1);
    }

//
//    private void reopenInventory(Player p, int menu) {
//        Inventory inv = p.getOpenInventory().getInventory(0);
//        if (inv != null) inv.close();
//        if (menu != 0) menu = menuNames.get(p.getOpenInventory().title());
//        switch (menu) {
//            case 2 -> p.openInventory(Menu.aClassMenu(p));
//            case 3 -> p.openInventory(Menu.dClassMenu(p));
//        }
//        updateInventory(p);
//    }

    private void updateInventory(Player p) {
        Inventory inv = p.getOpenInventory().getInventory(0);
        if (inv == null) return;
        for (ItemStack i : inv.getStorageContents()) {
            if (i == null) continue;
            int item = i.getItemMeta().getCustomModelData();
            if (getClassID(p) == item) i.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
            else if (i.getEnchantments().containsKey(Enchantment.CHANNELING))
                i.removeEnchantment(Enchantment.CHANNELING);
            if (item == 10000000 || item == 20000000) break;
            if (chosen_class.contains(item) && getClassID(p) != item) i.setType(Material.BARRIER);
        }
    }
}
