package mc.pvp.util;

import mc.pvp.PVP;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Skill {
    private final static Skill EMPTY = new Skill() {
        @Override
        public void execute() {
        }
    };

    public static Skill get(Player p, int classId, int skillId) {
        switch (classId) {
            /*
            进攻
             */
            //麦克
            case 10000001 -> {
                if (skillId == 1) return new Skill() {
                    @Override
                    public void execute() {
                        p.addPotionEffects(new ArrayList<>() {{
                            add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5, 0, false, false));
                            add(new PotionEffect(PotionEffectType.SPEED, 5, 0, false, false));
                        }});
                        setCd(p, 1, 15);
                    }
                };
                else if (skillId == 2) return new Skill() {
                    @Override
                    public void execute() {
                        p.removeScoreboardTag("MikeSkill1");
                        p.addScoreboardTag("MikeSkill1");
                        p.spawnParticle(Particle.FLAME, p.getLocation(), 100);
                    }
                };
                else return EMPTY;
            }
            /*
            防守
             */
            //史瑞克
            case 20000001 -> {
                if (skillId == 1) return new Skill() {
                    @Override
                    public void execute() {

                    }
                };
                else return EMPTY;
            }
            default -> {
                return new Skill() {
                    @Override
                    public void execute() {
                    }
                };
            }
        }
    }

    public static void setCd(Player p, int id, int num) {
        Objects.requireNonNull(PVP.mainScoreboard.getObjective("cd%d".formatted(id))).getScore(p).setScore(num);
    }

    public abstract void execute();
}
