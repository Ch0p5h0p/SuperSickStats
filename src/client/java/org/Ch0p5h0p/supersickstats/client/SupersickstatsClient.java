package org.Ch0p5h0p.supersickstats.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.Ch0p5h0p.supersickstats.client.calc.DefenseCalculator;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environemnt) -> {
            // Command: /calcDamage
            dispatcher.register(
                    CommandManager.literal("calcdamage")
                            .then(
                                    CommandManager.argument("damage", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            int dmg = IntegerArgumentType.getInteger(context, "damage");
                                            System.out.println("Damage: "+dmg);
                                            PlayerEntity p = context.getSource().getPlayer();
                                            float finalDamage = dmg*(1-DefenseCalculator.calculate(p).finalPct/100);

                                            context.getSource().sendFeedback(()->Text.literal(String.format("Damage with your current armor: %.2f (%.2f health remaining)",finalDamage, Math.max(0.0f, p.getHealth()-finalDamage))), false);
                                            return 1;
                                        })
                            )
            );
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
