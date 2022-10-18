package mc.pvp.event;

import mc.pvp.util.Skill;
import net.kyori.adventure.text.Component;
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

import static mc.pvp.Game.getClassID;
import static mc.pvp.Game.players;

public class SkillL implements Listener {

    @EventHandler
    public void onUseItem(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!players.contains(p)) return;
        ItemStack i = e.getItem();
        if (i == null) return;
        if (!i.getItemMeta().hasCustomModelData()) return;
        int skillID = i.getItemMeta().getCustomModelData();
        if (Skill.getCd(p, skillID) == 0) {
            if (skillID != 0) i.setAmount(i.getAmount() - 1);
            Skill.get(p, getClassID(p), skillID).execute();
        } else p.sendMessage(Component.text("[S] 技能冷却中！", TextColor.color(255, 248, 161)));
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player dealt && e.getEntity() instanceof Player taken)) return;
        if (dealt.equals(taken)) return;
        if (dealt.getScoreboardTags().contains("MikeSkill1")) {
            dealt.sendTitlePart(TitlePart.TITLE, Component.text(" "));
            dealt.sendTitlePart(TitlePart.SUBTITLE, Component.text("致残打击", TextColor.color(255, 0, 19), TextDecoration.BOLD));
            dealt.removeScoreboardTag("MikeSkill1");
            taken.spawnParticle(Particle.SWEEP_ATTACK, taken.getLocation(), 10);
            taken.sendMessage(Component.text("[S] %s对你使用了致残打击！".formatted(dealt.getName()), TextColor.color(255, 248, 161)));
            taken.damage(5);
            taken.setLastDamageCause(e);
            taken.addPotionEffects(new ArrayList<>() {{
                add(new PotionEffect(PotionEffectType.SLOW, 3, 3, false, false));
            }});
        }
    }


    private void magicDamage(Player p, double n) {
        if (n <= 0) return;
        p.setHealth(p.getHealth() - n);
        if (n > p.getHealth()) {
            p.setHealth(0);
        }
    }
}
