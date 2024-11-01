package mc.pvp.event;

import mc.pvp.PVP;
import mc.pvp.Game;
import mc.pvp.util.Skill;
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

import static mc.pvp.Game.*;

public class GameL implements Listener {

    @EventHandler
    public void onBreakBeacon(BlockBreakEvent e) {
        if (!e.getBlock().getLocation().equals(Game.beacon)) return;
        if (!PVP.attackers.hasPlayer(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        Game.endA();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        if (players.contains(p)) {
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        players.remove(p);
        time.removePlayer(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        resetPlayer(e.getPlayer());
    }
}
