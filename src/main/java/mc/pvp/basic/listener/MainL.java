package mc.pvp.basic.listener;

import mc.pvp.PVP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainL implements Listener {
    Scoreboard main_scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player killer = e.getPlayer().getKiller();
        Player dead = e.getPlayer();
        addScore(dead, "death", 1);
        if (killer == null) return;
        if (killer.equals(dead)) return;
        //击杀者消息列表
        List<String> killerMes = new ArrayList<>() {{
            add("§l§6[SYSTEM] §7你击杀了%s奖励你10个金币！");
        }};
        //击杀消息列表
        List<Component> killMes = new ArrayList<>() {{
            add(Component.translatable("§l§b[SYSTEM] %s§c残忍地把%s§c的头给剁掉了！", killer.displayName().color(TextColor.color(255, 0, 19)), dead.displayName().color(TextColor.color(255, 248, 161))));
            add(Component.translatable("§l§b[SYSTEM] %s§c§c将%s§c击杀了,尸骨无存！", killer.displayName().color(TextColor.color(255, 0, 19)), dead.displayName().color(TextColor.color(255, 248, 161))));
            add(Component.translatable("§l§b[SYSTEM] %s§c被%s§c杀害！§7真可怜", dead.displayName().color(TextColor.color(255, 248, 161)), killer.displayName().color(TextColor.color(255, 0, 19))));
            add(Component.translatable("§l§b[SYSTEM] %s§c被惨无人道的杀害了！凶手是%s.", dead.displayName().color(TextColor.color(255, 248, 161)), killer.displayName().color(TextColor.color(255, 0, 19))));
            add(Component.translatable("§l§b[SYSTEM] %s§c was crushed by %s", dead.displayName().color(TextColor.color(255, 248, 161)), killer.displayName().color(TextColor.color(255, 0, 19))));
        }};
        //击杀事件
        killer.sendMessage(Component.translatable(killerMes.get((int) (Math.random() * killerMes.size())), dead.displayName()));
        addScore(killer, "money", 10);
        addScore(killer, "killplayer", 1);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(killMes.get((int) (Math.random() * killMes.size()))));
    }

    @EventHandler
    public void onHurt(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        double taken = e.getDamage();
        p.sendMessage(String.format("§l§6[SYSTEM] §c你此次受到了来自%s造成的%.2f伤害", e.getDamager().getName(), taken));
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onUseCommand(PlayerCommandPreprocessEvent e) {
//        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (String tag : new String[]{"ready"}) p.removeScoreboardTag(tag);
        PVP.mainScoreboard.getTeams().forEach(team -> team.removePlayer(p));
    }

    private void addScore(Player p, String obj, int s) {
        Score score = Objects.requireNonNull(main_scoreboard.getObjective(obj)).getScore(p);
        score.setScore(score.getScore() + s);
    }

    private void damage(Player p, double n) {
        if (n <= 0) return;
        onHurt(new EntityDamageByEntityEvent(p, p, EntityDamageEvent.DamageCause.VOID, n));
        p.setHealth(p.getHealth() - n);
        if (n > p.getHealth()) {
            p.setHealth(0);
        }
    }

    private void heal(Player p, double n) {
        if (n <= 0) return;
        double max = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        if (p.getHealth() + n > max) {
            p.setHealth(max);
            return;
        }
        p.setHealth(p.getHealth() + n);
    }

}