package mc.pvp.basic.listener;

import mc.pvp.PVP;
import mc.pvp.basic.Game;
import mc.pvp.basic.util.Menu;
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

import java.util.HashMap;

import static mc.pvp.basic.Game.*;

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
        if (i.getType().equals(Material.BARRIER)) return;
        e.setCancelled(true);
        if (!i.getItemMeta().hasCustomModelData()) return;
        int item = i.getItemMeta().getCustomModelData();
        if (chosen_class.contains(item)) return;
        chosen_class.remove(Integer.valueOf(getClassID(p)));
        chosen_class.add(item);
        setClassID(p,item);
        p.addScoreboardTag("ready");
        if (PVP.a.hasPlayer(p)) players.stream().filter(player -> PVP.a.hasPlayer(p)).forEach(this::updateInventory);
        if (PVP.d.hasPlayer(p)) players.stream().filter(player -> PVP.d.hasPlayer(p)).forEach(this::updateInventory);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!choosing) return;
        if (menuNames.get(e.getPlayer().getOpenInventory().title()) == null) return;
        Player p = (Player) e.getPlayer();
        if (PVP.a.hasPlayer(p)) reopenInventory(p, 2);
        if (PVP.d.hasPlayer(p)) reopenInventory(p, 3);
    }

    private void reopenInventory(Player p, int menu) {
        if (menu != 0)
            menu = menuNames.get(p.getOpenInventory().title());
        p.getOpenInventory().close();
        switch (menu) {
            case 2 -> p.openInventory(Menu.aClassMenu(p));
            case 3 -> p.openInventory(Menu.dClassMenu(p));
        }
        updateInventory(p);
    }

    private void updateInventory(Player p) {
        Inventory inv = (Inventory) p.getOpenInventory();
        for (ItemStack i : inv.getContents()) {
            assert i != null;
            int item=i.getItemMeta().getCustomModelData();
            if (chosen_class.contains(item)) i.setType(Material.BARRIER);
            if (getClassID(p)==item) i.addEnchantment(Enchantment.CHANNELING,0);
            else if(i.getEnchantments().containsKey(Enchantment.CHANNELING)) i.removeEnchantment(Enchantment.CHANNELING);
        }
    }
}
