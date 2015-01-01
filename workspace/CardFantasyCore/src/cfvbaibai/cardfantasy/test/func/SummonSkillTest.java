package cfvbaibai.cardfantasy.test.func;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.RuneInfo;

public class SummonSkillTest extends SkillValidationTest {
    /**
     * 降临技能在符文发动和结算之前发动
     */
    @Test
    public void test降临摧毁_焚天() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "独眼巨人", "焚天", "凤凰-5*2");
        context.addToHand(0, 0).setSummonDelay(0);
        RuneInfo r焚天 = context.addToRune(0, 0);
        context.addToField(1, 1);
        context.addToField(2, 1);
        context.startGame();

        random.addNextPicks(0); // 独眼巨人摧毁
        context.proceedOneRound();

        // 降临技能先结算，杀死一只凤凰，对方场上只剩一张森林卡，无法激活焚天
        Assert.assertFalse("焚天应该未激活", r焚天.isActivated());
        Assert.assertEquals(1, context.getPlayer(1).getField().size());
    }
    
    /**
     * 有多张卡牌等待时间相同的情况下，降临传送会杀死其中最靠前的那张
     */
    @Test
    public void test降临传送_相同等待时间() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "隐世先知", "金属巨龙-5", "凤凰-5");
        context.addToHand(0, 0).setSummonDelay(0);
        CardInfo c金属巨龙 = context.addToHand(1, 1).setSummonDelay(3);
        CardInfo c凤凰 = context.addToHand(2, 1).setSummonDelay(3);
        context.startGame();

        context.proceedOneRound();

        List<CardInfo> handListB = context.getPlayer(1).getHand().toList();
        Assert.assertTrue("凤凰应该还活着", handListB.contains(c凤凰));
        Assert.assertFalse("金属巨龙应该死了", handListB.contains(c金属巨龙));
    }
}