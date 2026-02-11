package org.Ch0p5h0p.supersickstats.client.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.Ch0p5h0p.supersickstats.client.calc.DamageBreakdown;
import org.Ch0p5h0p.supersickstats.client.calc.DamageCalculator;
import org.Ch0p5h0p.supersickstats.client.config.Config;
import org.Ch0p5h0p.supersickstats.client.utils.WeaponsHelper;

import java.util.ArrayDeque;
import java.util.Deque;

public class DamageTracker {
    public static final Deque<DamageEvent> dealt = new ArrayDeque<>();
    private static final Deque<DamageEvent> taken = new ArrayDeque<>();

    public static void init() {
        dealt.addLast(new DamageEvent(System.currentTimeMillis(), 0, DamageType.OTHER));
    }

    public static DamageType lastIncomingType = null;
    public static long lastIncomingTime = 0;


    public static void recordDealt(PlayerEntity p) { //old args: float dmg, DamageType type
        ItemStack stack = p.getMainHandStack();

        DamageBreakdown dmg = DamageCalculator.calculate(p);

        System.out.println(String.format("Attacked with weapon %s, dealt %.0f damage.", stack.getItemName(), dmg.total));
        dealt.addLast(new DamageEvent(System.currentTimeMillis(), dmg.total, DamageType.MELEE));
    }

    public static void recordTaken(float dmg, DamageType type) {
        taken.addLast(new DamageEvent(System.currentTimeMillis(), dmg, type));
        lastIncomingType = type;
        lastIncomingTime = System.currentTimeMillis();
    }

    public static float getRecentDealt() {
        return dealt.getLast().amount();
    }

    public static int getDealtLength() {
        return dealt.size();
    }

    public static float computeDps() {
        Deque<DamageEvent> buf = dealt;
        long windowMs = Config.DPS_WINDOW_MS;
        long now = System.currentTimeMillis();
        while (!buf.isEmpty() && now-buf.peekFirst().timeMs() > windowMs) {
            buf.removeFirst();
        }

        float sum = 0;
        for (DamageEvent e : buf) sum += e.amount();
        return sum/(windowMs/1000f);
    }


}
