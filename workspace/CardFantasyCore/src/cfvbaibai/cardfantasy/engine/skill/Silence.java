package cfvbaibai.cardfantasy.engine.skill;

import java.util.ArrayList;
import java.util.List;

import cfvbaibai.cardfantasy.GameUI;
import cfvbaibai.cardfantasy.data.Skill;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.CardStatusItem;
import cfvbaibai.cardfantasy.engine.CardStatusType;
import cfvbaibai.cardfantasy.engine.EntityInfo;
import cfvbaibai.cardfantasy.engine.HeroDieSignal;
import cfvbaibai.cardfantasy.engine.Player;
import cfvbaibai.cardfantasy.engine.SkillResolver;
import cfvbaibai.cardfantasy.engine.SkillUseInfo;

public class Silence {
    public static void apply(SkillResolver resolver, SkillUseInfo skillUseInfo, EntityInfo caster, Player defenderHero, boolean isTargetAll, boolean onlyCastOnce) throws HeroDieSignal {
        if (onlyCastOnce) {
            if (resolver.getStage().hasUsed(skillUseInfo)) {
                return;
            }
            if(caster instanceof CardInfo){
                CardInfo sumCard = (CardInfo) caster;
                if(sumCard.isSummonedMinion()) {
                    SkillUseInfo sumSkillInfo = new SkillUseInfo(sumCard.getStatus().getStatusOf(CardStatusType.召唤).get(0).getCause().getOwner(), sumCard.getStatus().getStatusOf(CardStatusType.召唤).get(0).getCause().getSkill());
                    if (resolver.getStage().hasUsed(sumSkillInfo)) {
                        return;
                    }
                }
            }
        }
        List<CardInfo> victims = new ArrayList<CardInfo>();
        if (isTargetAll) {
            victims.addAll(defenderHero.getField().getAliveCards());
        } else {
            CardInfo casterCard = (CardInfo) caster;
            CardInfo victim = defenderHero.getField().getCard(casterCard.getPosition());
            if (victim == null || victim.isDead()) {
                return;
            }
            victims.add(victim);
        }
        GameUI ui = resolver.getStage().getUI();
        Skill skill = skillUseInfo.getSkill();
        ui.useSkill(caster, victims, skill, true);
        for (CardInfo victim : victims) {
            CardStatusItem statusItem = CardStatusItem.slience(skillUseInfo);
            if (!resolver.resolveAttackBlockingSkills(caster, victim, skill, 1).isAttackable()) {
                continue;
            }
            ui.addCardStatus(caster, victim, skill, statusItem);
            victim.addStatus(statusItem);
        }
        if (onlyCastOnce) {
            if(caster instanceof CardInfo){
                CardInfo summoncard = (CardInfo) caster;
                if(summoncard.isSummonedMinion()) {
                    SkillUseInfo summonSkillInfo = new SkillUseInfo(summoncard.getStatus().getStatusOf(CardStatusType.召唤).get(0).getCause().getOwner(), summoncard.getStatus().getStatusOf(CardStatusType.召唤).get(0).getCause().getSkill());
                    resolver.getStage().setUsed(summonSkillInfo, true);
                }
            }
            resolver.getStage().setUsed(skillUseInfo, true);
        }
    }
}
