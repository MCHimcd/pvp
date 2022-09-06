package mc.pvp.basic.listener;

import mc.pvp.PVP;
import mc.pvp.basic.util.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.regex.Pattern;

import static mc.pvp.basic.Game.chosen_class;
import static mc.pvp.basic.Game.players;

public class MenuL implements Listener {
    private final HashMap<Component, Integer> menusName = new HashMap<>() {
        {
            put(Component.text("主菜单", TextColor.color(186, 255, 148)), 1);
            put(Component.text("进攻方职业菜单", TextColor.color(255, 2, 0)), 2);
            put(Component.text("防守方职业菜单", TextColor.color(6, 255, 237)), 3);
        }
    };

    @EventHandler
    //职业菜单
    public void onClickClassInv(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (menusName.get(p.getOpenInventory().title()) == null) return;
        if (e.getRawSlot() < 0) return;
        ItemStack i = e.getCurrentItem();
        if (i == null) return;
        if (i.getType().equals(Material.BARRIER)) return;
        e.setCancelled(true);
        if (!i.getItemMeta().hasCustomModelData()) return;
        int item = i.getItemMeta().getCustomModelData();
        if (chosen_class.contains(item)) return;
        p.getScoreboardTags().stream()
                .filter(tag-> Pattern.matches("chosen-\\d+",tag))
                .forEach(tag->{
                    chosen_class.remove(Integer.valueOf(tag.substring(7)));
                    p.removeScoreboardTag(tag);
                });
        chosen_class.add(item);
        p.addScoreboardTag("chosen-%d".formatted(item));
        if (PVP.a.hasPlayer(p)) players.stream().filter(player -> PVP.a.hasPlayer(p)).forEach(this::updateInv);
        if (PVP.d.hasPlayer(p)) players.stream().filter(player -> PVP.d.hasPlayer(p)).forEach(this::updateInv);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (menusName.get(e.getPlayer().getOpenInventory().title()) == null) return;
        Player p = (Player) e.getPlayer();
        if (PVP.a.hasPlayer(p)) reopen(p, 2);
        if (PVP.d.hasPlayer(p)) reopen(p, 3);
    }

    private void reopen(Player p, int menu) {
        if (menu != 0)
            menu = menusName.get(p.getOpenInventory().title());
        p.getOpenInventory().close();
        switch (menu) {
            case 2 -> p.openInventory(Menu.aClassMenu(p));
            case 3 -> p.openInventory(Menu.dClassMenu(p));
        }
    }

    private void updateInv(Player p) {
        Inventory inv = (Inventory) p.getOpenInventory();
        for (ItemStack i : inv.getContents()) {
            assert i != null;
            if (chosen_class.contains(i.getItemMeta().getCustomModelData())) i.setType(Material.BARRIER);
//            if(p.getScoreboardTags().stream()
//                    .filter(tag -> Pattern.matches("chosen-\\d+", tag)).forEach(s -> {
//                        if (chosen_class.contains(Integer.valueOf(s.substring(7)))) // TODO: 2022-9-5
//                    }))
        }
    }
}
