package org.Ch0p5h0p.supersickstats.client.calc;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.Ch0p5h0p.supersickstats.client.utils.EnchantmentUtil;
import org.Ch0p5h0p.supersickstats.client.utils.ProtectionEnchantment;

import java.util.Map;
import java.util.Set;

public class DefenseCalculator {

    public static ProtectionEnchantment getProt(ItemStack armorPiece) {
        int prot = EnchantmentUtil.getLevel(armorPiece, Enchantments.PROTECTION);
        int fire = EnchantmentUtil.getLevel(armorPiece, Enchantments.FIRE_PROTECTION);
        int proj = EnchantmentUtil.getLevel(armorPiece, Enchantments.PROJECTILE_PROTECTION);
        int blast = EnchantmentUtil.getLevel(armorPiece, Enchantments.BLAST_PROTECTION);
        if ( prot > 0) {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!! PROT ENCHANTMENT FOUND");
            return new ProtectionEnchantment("PROT", prot);
        }
        if ( fire > 0) {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!! FIREPROT ENCHANTMENT FOUND");
            return new ProtectionEnchantment("FIRE", fire);
        }
        if ( proj > 0) {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!! PROJ PROT ENCHANTMENT FOUND");
            return new ProtectionEnchantment("PROJ", proj);
        }
        if ( blast > 0) {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!! BLAST PROT ENCHANTMENT FOUND");
            return new ProtectionEnchantment("BLAST", blast);
        }
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!! NO PROT ENCHANTMENT FOUND");
        return new ProtectionEnchantment("NONE", 0);
    }

    public static DefenseBreakdown calculate(PlayerEntity p) {
        float totalReduction;

        // Handle armor
        int armorPoints=p.getArmor();
        float armorToughness = (float) p.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS);

        float armor = Math.min(20, Math.max(armorPoints/5, armorPoints-(4/(armorToughness+8))))/25;

        // Handle enchantments

        ItemStack head = p.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = p.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = p.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = p.getEquippedStack(EquipmentSlot.FEET);

        float prot = 0.0f;
        float projProt = 0.0f;
        float fireProt = 0.0f;
        float blastProt = 0.0f;

        ProtectionEnchantment headEnch=getProt(head);
        ProtectionEnchantment chestEnch=getProt(chest);
        ProtectionEnchantment legsEnch=getProt(legs);
        ProtectionEnchantment feetEnch=getProt(feet);
        ProtectionEnchantment[] armorEnchantments = {headEnch, chestEnch, legsEnch, feetEnch};
        for (ProtectionEnchantment armorEnch : armorEnchantments) {
            switch (armorEnch.type) {
                case "PROT":
                    prot+=(4*armorEnch.level);
                    projProt+=(4*armorEnch.level);
                    fireProt+=(4*armorEnch.level);
                    blastProt+=(4*armorEnch.level);
                    break;
                case "PROJ":
                    projProt+=(8*armorEnch.level);
                    break;
                case "FIRE":
                    fireProt+=(8*armorEnch.level);
                    break;
                case "BLAST":
                    blastProt+=(8*armorEnch.level);
                    break;
            }
        }

        prot = prot/100;
        projProt = Math.min(1, projProt/100);
        fireProt = Math.min(1, fireProt/100);
        blastProt = Math.min(1, blastProt/100);

        // Handle effects

        float effectsReduction = 0.0f; // We only have resistance right now, but this is here if we get more

        int resistanceLevel = 0;
        if (p.hasStatusEffect(StatusEffects.RESISTANCE)) {
            resistanceLevel=p.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier();
            effectsReduction+=Math.min(100, 20*resistanceLevel);
        }
        effectsReduction=Math.min(1, effectsReduction/100);

        totalReduction = armor+((1-armor)*prot);
        totalReduction += (1-totalReduction)*effectsReduction;

        return new DefenseBreakdown(
                armor*100,
                prot*100,
                fireProt*100,
                projProt*100,
                blastProt*100,
                effectsReduction*100,
                totalReduction*100
        );

    }
}
