package mc.pvp.basic.commands;

import mc.pvp.basic.util.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import mc.pvp.basic.util.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static mc.pvp.PVP.mainScoreboard;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
        Game.reset();
        Game.chooing=true;
        return true;
    }

    private void joinA(Player p) {
        Objects.requireNonNull(mainScoreboard.getTeam("A")).addEntity(p);
        p.addScoreboardTag("choosing");
        p.openInventory(Menu.aClassMenu(p));
    }

    private void joinD(Player p) {
        Objects.requireNonNull(mainScoreboard.getTeam("D")).addEntity(p);
        p.addScoreboardTag("choosing");
        p.openInventory(Menu.dClassMenu(p));
    }

}
