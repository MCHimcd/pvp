package mc.pvp.command;

import mc.pvp.Game;
import mc.pvp.util.Menu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mc.pvp.PVP.attackers;
import static mc.pvp.PVP.defenders;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<Player> ps = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (ps.size() == 1) {
            sender.sendMessage("§l§b[SYSTEM] §4人数不足，无法开始");
            return true;
        }
        Game.reset();
        Collections.shuffle(ps);
        if (ps.size() % 2 != 0) ps.remove(0);
        while (!ps.isEmpty()) {
            joinA(ps.get(0));
            ps.remove(0);
            joinD(ps.get(0));
            ps.remove(0);
        }
        Game.choosing = true;
        return true;
    }

    private void joinA(Player p) {
        attackers.addEntity(p);
        p.openInventory(Menu.aClassMenu(p));
        Game.players.add(p);
    }

    private void joinD(Player p) {
        defenders.addEntity(p);
        p.openInventory(Menu.dClassMenu(p));
        Game.players.add(p);
    }

}
