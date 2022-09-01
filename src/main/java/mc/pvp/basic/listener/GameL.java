package mc.pvp.basic.listener;

import mc.pvp.PVP;
import mc.pvp.basic.util.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Objects;

public class GameL implements Listener {
    @EventHandler
    public void onBreakBeacon(BlockBreakEvent e) {
        if (!e.getBlock().getLocation().equals(Game.beacon)) return;
        if (!Objects.requireNonNull(PVP.mainScoreboard.getTeam("A")).getEntries().contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            return;
        }
        Game.endA();
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent e) {
        if (!e.getSource().equals(e.getDestination()) && e.getItem().getItemMeta().getCustomModelData() == 11111111)
            e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getCustomModelData() == 11111111) e.setCancelled(true);
    }
}
