package org.Ch0p5h0p.supersickstats.client.calc;

public class DefenseBreakdown {
    public final float armorPct;
    public final float protPct;
    public final float totalProt;
    public final float fireProtPct;
    public final float totalFireProt;
    public final float projProtPct;
    public final float totalProjProt;
    public final float blastProtPct;
    public final float totalBlastProt;
    public final float effectPct;
    public final float finalPct;

    public DefenseBreakdown(float armorPct, float protPct, float fireProtPct, float projProtPct, float blastProtPct, float effectPct, float finalPct) {
        this.armorPct = armorPct;
        this.protPct = protPct;
        this.totalProt = armorPct+(100*((1-armorPct/100)*protPct/100));
        this.fireProtPct = fireProtPct;
        this.totalFireProt = armorPct+(100*((1-armorPct/100)*fireProtPct/100));
        this.projProtPct = projProtPct;
        this.totalProjProt = armorPct+(100*((1-armorPct/100)*projProtPct/100));
        this.blastProtPct = blastProtPct;
        this.totalBlastProt = armorPct+(100*((1-armorPct/100)*blastProtPct/100));
        this.effectPct = effectPct;
        this.finalPct = finalPct;
    }
}
