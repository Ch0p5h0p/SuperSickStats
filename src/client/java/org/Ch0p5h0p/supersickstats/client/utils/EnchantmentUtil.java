package org.Ch0p5h0p.supersickstats.client.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentUtil {

    // I love when Mojang ruins modding :D
    public static int getLevel(ItemStack stack, RegistryKey<Enchantment> enchantment) {
        for (RegistryEntry<Enchantment> e : stack.getEnchantments().getEnchantments()) {
            if (e.toString().contains(enchantment.getValue().toString())) {
                return stack.getEnchantments().getLevel(e);
            }
        }
        return 0;
    }
}
