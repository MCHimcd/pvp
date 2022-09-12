package mc.pvp.basic.commands;

import mc.pvp.basic.Game;
import mc.pvp.basic.util.Menu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mc.pvp.PVP.*;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Game.reset();
        List<Player> ps = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(ps);
        if (ps.size() % 2 != 0) ps.remove(0);
        while (!ps.isEmpty()) {
            joinA(ps.get(0));
            ps.remove(0);
            if (ps.isEmpty()) break;
            joinD(ps.get(0));
            ps.remove(0);
        }
        Game.choosing =true;
        new BukkitRunnable() {
            @Override
            public void run() {
                Game.players.forEach(player -> player.addScoreboardTag("ready"));
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("pvp"), 2);
        return true;
    }

    private void joinA(Player p) {
        a.addEntity(p);
        p.addScoreboardTag("choosing");
        p.openInventory(Menu.aClassMenu(p));
        Game.players.add(p);
    }

    private void joinD(Player p) {
        d.addEntity(p);
        p.addScoreboardTag("choosing");
        p.openInventory(Menu.dClassMenu(p));
        Game.players.add(p);
    }

}
