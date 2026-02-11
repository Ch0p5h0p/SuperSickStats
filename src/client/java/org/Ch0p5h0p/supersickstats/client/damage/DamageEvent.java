package org.Ch0p5h0p.supersickstats.client.damage;

public record DamageEvent(
    long timeMs,
    float amount,
    DamageType type
) {}
