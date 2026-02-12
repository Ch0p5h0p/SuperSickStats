package org.Ch0p5h0p.supersickstats.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.Ch0p5h0p.supersickstats.client.calc.DamageBreakdown;
import org.Ch0p5h0p.supersickstats.client.calc.DamageCalculator;
import org.Ch0p5h0p.supersickstats.client.calc.DefenseBreakdown;
import org.Ch0p5h0p.supersickstats.client.calc.DefenseCalculator;
import org.Ch0p5h0p.supersickstats.client.config.Config;
import org.Ch0p5h0p.supersickstats.client.damage.DamageTracker;

import java.util.ArrayList;
import java.util.List;

public abstract class StatsHudRenderer implements HudElement {
    private static float totalTick = 0f;

    public static void init() {
        HudElementRegistry.addLast(Identifier.of("supersickstats", "status_hud"), hudElement());
    }

    private static HudElement hudElement() {
        return new HudElement() {
            @Override
            public void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player == null) return;
                if (!Config.HUD_ENABLED) return;

                int sw = client.getWindow().getScaledWidth();
                int sh = client.getWindow().getScaledHeight();

                float baseScale = 2.75f;
                float scale = baseScale*sh/1080f;
                drawContext.getMatrices().pushMatrix();
                drawContext.getMatrices().scale(scale, scale);

                int armorPanelX = (int) ((3*(sw/4)-5)/scale);
                int armorPanelY = (int) ((2*(sh/5))/scale);
                int armorPanelYDiff = 10;

                DefenseBreakdown db = DefenseCalculator.calculate(client.player);

                String[] armorPanel = {
                        String.format("GENERAL REDUCTION: %.2f%%", db.finalPct),
                        String.format("ARMOR REDUCTION: %.2f%%", db.armorPct),
                        String.format("PROT: %.2f%% (%.2f%% TOTAL)", db.protPct, db.totalProt),
                        String.format("FIRE: %.2f%% (%.2f%% TOTAL)", db.fireProtPct, db.totalFireProt),
                        String.format("PROJ: %.2f%% (%.2f%% TOTAL)", db.projProtPct, db.totalProjProt),
                        String.format("BLAST: %.2f%% (%.2f%% TOTAL)", db.blastProtPct, db.totalBlastProt),
                        String.format("EFFECTS REDUCTION: %.2f%%", db.effectPct)
                };

                int[] armorPanelColors = {
                        0xFF55FFFF,
                        0xFFFFFFFF,
                        0xFF55FF55,
                        0xFF55FF55,
                        0xFF55FF55,
                        0xFF55FF55,
                        0xFFFF55FF
                };

                for (int i = 0; i<armorPanel.length; i++) {
                    drawContext.drawText(client.textRenderer, armorPanel[i], armorPanelX, armorPanelY +(armorPanelYDiff *i), armorPanelColors[i], true);
                }


                int attackPanelX = (int) ((3*(sw/4)-5)/scale);
                int attackPanelY = (int) ((3*(sh/5))/scale);
                int attackPanelYDiff = 10;

                DamageBreakdown dmg = DamageCalculator.calculate(client.player);

                List<String> attackPanelTemp = new ArrayList<>();

                attackPanelTemp.add(String.format("HIT DAMAGE: %.2f (CRIT: %.2f)",dmg.total, dmg.total*1.5));
                attackPanelTemp.add(String.format("IDEAL DPS: %.2f (CRIT: %.2f)", dmg.DPS, dmg.DPS*1.5));
                attackPanelTemp.add(String.format("REAL DPS: %.2f", DamageTracker.computeDps()));
                attackPanelTemp.add(String.format("BASE: %.2f SHARPNESS: +%.2f", dmg.base, dmg.enchantment));
                attackPanelTemp.add(String.format("EFFECT MODIFIER: %s%.2f", ((dmg.effect)>=0 ? "+" : "-"), dmg.effect));

                if (dmg.chargeDamage >= 0) {
                    attackPanelTemp.add(String.format("CHARGE DAMAGE: %.2f", dmg.chargeDamage));
                }

                int[] attackPanelColors = {
                        0xFFFF5555,
                        0xFF55FF55,
                        0xFF55FF55,
                        0xFFFFFFFF,
                        0xFFFF55FF,
                        0xFFFFAA00
                };

                String[] attackPanel = attackPanelTemp.toArray(new String[0]);

                for (int i = 0; i< attackPanel.length; i++) {
                    drawContext.drawText(client.textRenderer, attackPanel[i], attackPanelX, attackPanelY +(attackPanelYDiff *i), attackPanelColors[i], true);
                }

                //drawContext.drawText(client.textRenderer, String.format("%.2f", dmg.total), (int) (((sw/2)+15)/scale), (int) (((sh/2)-3)/scale), 0xFFFF5555, true);
                //drawContext.drawText(client.textRenderer, String.format("%.2f", dmg.total*1.5), (int) (((sw/2)-40)/scale), (int) (((sh/2)-3)/scale), 0xFFFFAA00, true);

                drawContext.getMatrices().popMatrix();

            }
        };
    }
}

