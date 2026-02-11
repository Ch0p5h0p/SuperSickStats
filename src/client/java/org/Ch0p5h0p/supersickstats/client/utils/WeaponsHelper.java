package org.Ch0p5h0p.supersickstats.client.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class WeaponsHelper {
    public static String getMaterial(ItemStack stack) {
        String id = stack.getItem().getTranslationKey();
        String mat;
        if (id.contains("wooden")) mat = "WOOD";
        else if (id.contains("stone")) mat = "STONE";
        else if (id.contains("copper")) mat = "COPPER";
        else if (id.contains("golden")) mat = "GOLD";
        else if (id.contains("iron")) mat = "IRON";
        else if (id.contains("diamond")) mat = "DIAMOND";
        else if (id.contains("netherite")) mat = "NETHERITE";
        else mat = "OTHER";
        return mat;
    }

    public static String getWeaponType(ItemStack stack) {
        String id = stack.getItem().getTranslationKey();
        String type;
        if (id.contains("sword")) type = "SWORD";
        else if (id.contains("axe")) type = "AXE";
        else if (id.contains("pickaxe")) type = "PICKAXE";
        else if (id.contains("hoe")) type = "HOE";
        else if (id.contains("shovel")) type = "SHOVEL";
        else if (id.contains("spear")) type = "SPEAR";
        else if (id.contains("mace")) type = "MACE";
        else if (id.contains("trident")) type = "TRIDENT";
        else type = "OTHER";
        return type;
    }

    public static float getBaseDamage(String material, String type) {
        switch (type) {
            case "SWORD":
                if (material == "WOOD") return 4;
                if (material == "GOLD") return 4;
                if (material == "STONE") return 5;
                if (material == "COPPER") return 5;
                if (material == "IRON") return 6;
                if (material == "DIAMOND") return 7;
                if (material == "NETHERITE") return 8;
                else return 1;
            case "AXE":
                if (material == "WOOD") return 7;
                if (material == "GOLD") return 7;
                if (material == "STONE") return 9;
                if (material == "COPPER") return 9;
                if (material == "IRON") return 9;
                if (material == "DIAMOND") return 9;
                if (material == "NETHERITE") return 10;
                else return 1;
            case "PICKAXE":
                if (material == "WOOD") return 2;
                if (material == "GOLD") return 2;
                if (material == "STONE") return 3;
                if (material == "COPPER") return 3;
                if (material == "IRON") return 4;
                if (material == "DIAMOND") return 5;
                if (material == "NETHERITE") return 6;
                else return 1;
            case "HOE":
                return 1;
            case "SHOVEL":
                if (material == "WOOD") return 2.5f;
                if (material == "GOLD") return 2.5f;
                if (material == "STONE") return 3.5f;
                if (material == "COPPER") return 3.5f;
                if (material == "IRON") return 4.5f;
                if (material == "DIAMOND") return 5.5f;
                if (material == "NETHERITE") return 6.5f;
                else return 1;
            case "SPEAR":
                if (material == "WOOD") return 1;
                if (material == "GOLD") return 1;
                if (material == "STONE") return 2;
                if (material == "COPPER") return 2;
                if (material == "IRON") return 3;
                if (material == "DIAMOND") return 4;
                if (material == "NETHERITE") return 5;
            case "MACE":
                return 6;
            case "TRIDENT":
                return 9;
            default:
                return 1;
        }
    }

    public static float calcSpearChargeDamage(PlayerEntity p, ItemStack spear) {
        String mat = getMaterial(spear);
        float baseDamage;
        float multiplier;
        switch (mat) {
            case "WOOD":
            case "GOLD":
            default:
                baseDamage = 4;
                multiplier = 0.7f;
                break;
            case "STONE":
            case "COPPER":
                baseDamage = 5;
                multiplier = 8.82f;
                break;
            case "IRON":
                baseDamage = 6;
                multiplier = 0.95f;
                break;
            case "DIAMOND":
                baseDamage = 6;
                multiplier = 1.075f;
                break;
            case "NETHERITE":
                baseDamage = 7;
                multiplier = 1.2f;
                break;
        }

        double BPS = p.getVelocity().length()*20;
        float finalDamage = (float) (baseDamage+(BPS*multiplier));
        if (BPS<5.1) return 0; else return finalDamage;

    }
}
