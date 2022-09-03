package mc.pvp.basic.listener;

import mc.pvp.PVP;
import mc.pvp.basic.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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

}
