package mc.pvp.basic.listener;

import mc.pvp.basic.other.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFactory;

import java.util.HashMap;

public class MenuL implements Listener {
    private final ItemFactory itemFactory = Bukkit.getItemFactory();
    private final HashMap<Component, Integer> menusName = new HashMap<Component, Integer>() {
        {
            put(Component.text("主菜单", TextColor.color(186, 255, 148)), 1);
            put(Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)), 2);
            put(Component.text("防守方职业菜单", TextColor.color(6, 255, 237)), 3);
        }
    };

    @EventHandler
    public void onClickInv(InventoryClickEvent e) {
        if (menusName.get(e.getWhoClicked().getOpenInventory().title()) == null) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() < 0) p.closeInventory();
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (menusName.get(e.getPlayer().getOpenInventory().title()) == null) return;
        HumanEntity p= e.getPlayer();
        switch (menusName.get(p.getOpenInventory().title())) {
            case 2->p.openInventory(Menu.aClassMenu0());
            case 3->p.openInventory(Menu.dClassMenu0());
        }
    }
}
