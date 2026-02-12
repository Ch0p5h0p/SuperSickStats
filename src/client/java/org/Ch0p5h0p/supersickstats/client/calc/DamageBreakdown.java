package org.Ch0p5h0p.supersickstats.client.calc;

public class DamageBreakdown {
    public final float base;
    public final float enchantment;
    public final float DPS;
    public final float effect;
    public final float chargeDamage;
    public final float total;
    public final float realTotal;
    public DamageBreakdown(float base, float enchantment, float DPS, float effect, float chargeDamage, float total, float realTotal) {
        this.base = base;
        this.enchantment = enchantment;
        this.DPS = DPS;
        this.effect = effect;
        this.chargeDamage = chargeDamage;
        this.total = total;
        this.realTotal = realTotal;
    }
}
