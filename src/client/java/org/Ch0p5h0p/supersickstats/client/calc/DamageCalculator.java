package org.Ch0p5h0p.supersickstats.client.calc;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.Ch0p5h0p.supersickstats.client.utils.EnchantmentUtil;
import org.Ch0p5h0p.supersickstats.client.utils.WeaponsHelper;

public class DamageCalculator {
    public static float calculateEffects(PlayerEntity p) {
        float effectBoost = 0.0f;

        int strengthLevel=0;
        if (p.hasStatusEffect(StatusEffects.STRENGTH)) {
            strengthLevel = p.getStatusEffect(StatusEffects.STRENGTH).getAmplifier();
            effectBoost+=3*strengthLevel;
        }

        int weaknessLevel=0;
        if (p.hasStatusEffect(StatusEffects.WEAKNESS)) {
            weaknessLevel = p.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier();
            effectBoost-=4*weaknessLevel;
        }

        return effectBoost;
    }
    public static DamageBreakdown calculate(PlayerEntity p) {
        ItemStack stack = p.getMainHandStack();
        String type = WeaponsHelper.getWeaponType(stack);
        if (type.equals("OTHER")) return new DamageBreakdown(1, 0, 4, 0+ calculateEffects(p), -1, 1, 1+calculateEffects(p));

        float baseDmg = WeaponsHelper.getBaseDamage(WeaponsHelper.getMaterial(stack), type);
        int sharpnessLvl = EnchantmentUtil.getLevel(stack, Enchantments.SHARPNESS);
        float sharpnessDmg;
        if (sharpnessLvl > 1) {
            sharpnessDmg = (float) ((0.5*sharpnessLvl)+0.5);
        } else {
            sharpnessDmg = 0;
        }

        float effectBoost = calculateEffects(p);

        float chargeDamage;
        if (!type.equals("SPEAR")) {
            chargeDamage=-1;
        } else {
            chargeDamage = WeaponsHelper.calcSpearChargeDamage(p, stack);
        }

        float realBase = baseDmg;
        float realSharpness = sharpnessDmg;

        if (p.handSwingProgress > 0) {
            realBase *= (float) (0.2+(0.8*(Math.pow(p.handSwingProgress, 2))));
            realSharpness *= p.handSwingProgress;
        }


        float total = baseDmg+sharpnessDmg+effectBoost;
        float realTotal = realBase+realSharpness+effectBoost;
        if (chargeDamage>0) chargeDamage += sharpnessDmg;
        float DPS = (float) (realTotal*p.getAttributeValue(EntityAttributes.ATTACK_SPEED));


        return new DamageBreakdown(baseDmg, sharpnessDmg, DPS, effectBoost, chargeDamage, total, realTotal);
    }
}
