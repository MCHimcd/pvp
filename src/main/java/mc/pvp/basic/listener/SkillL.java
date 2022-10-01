package mc.pvp.basic.listener;

import mc.pvp.basic.util.Skill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static mc.pvp.basic.Game.getClassID;
import static mc.pvp.basic.Game.players;

public class SkillL implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!players.contains(p)) return;
        ItemStack i = e.getItem();
        if (i == null) return;
        if (!i.getItemMeta().hasCustomModelData()) return;
        int id = i.getItemMeta().getCustomModelData() % 10000000;
        Skill.getSkill(p, getClassID(p), id).execute();
    }

    @EventHandler
    public void bearerAndCauser(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player causer && e.getEntity() instanceof Player bearer)) return;
        if (causer.equals(bearer)) return;
        if (causer.getScoreboardTags().contains("MikeSkill1")) {
            causer.sendTitlePart(TitlePart.TITLE,Component.text(" "));
            causer.sendTitlePart(TitlePart.SUBTITLE,Component.text("致残打击",TextColor.color(255, 0, 19), TextDecoration.BOLD));
            bearer.spawnParticle(Particle.SWEEP_ATTACK,bearer.getLocation(),10);
            bearer.sendMessage(Component.text("[S] %s对你使用了致残打击！".formatted(causer.getName()),TextColor.color(255, 248, 161)));
            bearer.damage(5);
            bearer.addPotionEffects(new ArrayList<>() {{
                add(new PotionEffect(PotionEffectType.SLOW, 3, 3, false, false));
            }});
        }
    }



















































    private void damage(Player p, double n) {
        if (n <= 0) return;
        p.setHealth(p.getHealth() - n);
        if (n > p.getHealth()) {
            p.setHealth(0);
        }
    }
}
