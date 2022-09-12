package mc.pvp.basic.listener;

import mc.pvp.PVP;
import mc.pvp.basic.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static mc.pvp.basic.Game.*;

public class GameL implements Listener {
    @EventHandler
    public void onUseItem(PlayerInteractEvent e) {
        ItemStack i = e.getItem();
        if (i == null) return;
        if (!i.getItemMeta().hasCustomModelData()) return;
        int item = i.getItemMeta().getCustomModelData();
        switch (item) {

        }
    }

    @EventHandler
    public void onBreakBeacon(BlockBreakEvent e) {
        if (!e.getBlock().getLocation().equals(Game.beacon)) return;
        if (!PVP.a.hasPlayer(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        Game.endA();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p=e.getPlayer();
        if(players.contains(p)){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p=e.getPlayer();
        players.remove(p);
        time.removePlayer(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        resetPlayer(e.getPlayer());
    }
}
