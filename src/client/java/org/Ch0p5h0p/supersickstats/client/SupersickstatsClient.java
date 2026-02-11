package org.Ch0p5h0p.supersickstats.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.Ch0p5h0p.supersickstats.client.config.Config;
import org.Ch0p5h0p.supersickstats.client.damage.DamageTracker;
import org.Ch0p5h0p.supersickstats.client.hud.StatsHudRenderer;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

import static net.minecraft.client.option.KeyBinding.onKeyPressed;

public class SupersickstatsClient implements ClientModInitializer {
    public KeyBinding attackKey;
    public boolean attackKeyRegistered = false;

    @Override
    public void onInitializeClient() {
        KeyBinding ToggleKey;
        StatsHudRenderer.init();
        DamageTracker.init();

        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> {
            if (clickCount > 0) {
                System.out.println("Attack detected.");
                DamageTracker.recordDealt(player);
            }

            return false; //don't cancel
        });

        ToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.supersickstats.toggle_enabled",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                new KeyBinding.Category(Identifier.of("category.supersickstats.keys"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ToggleKey.wasPressed()) {
                Config.HUD_ENABLED=!Config.HUD_ENABLED;
            }
        });
    }
}
