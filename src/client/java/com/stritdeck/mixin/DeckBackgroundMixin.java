package com.stritdeck.mixin;

import com.stritdeck.ui.DeckTheme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the deck-style background (dark vertical gradient + thin status
 * bar at the bottom) behind the title and pause screens, before vanilla's
 * own render runs for anything else on top.
 *
 * Targets two classes with one mixin by using a Screen-level hook instead
 * of per-screen duplication would be cleaner (e.g. an interface mixin on
 * Screen itself, gated by an instanceof check) — left as the simpler,
 * more obviously-correct two-target version here; merge later if desired.
 *
 * HONEST CAVEAT (the riskiest file in this mod, flagging clearly): this
 * injects at render/HEAD and draws first, but TitleScreen normally keeps
 * drawing its own rotating panorama + dark overlay afterward in the same
 * method, which would paint over this gradient. To really replace the
 * background you'll likely need to either:
 *   (a) @Redirect the specific panorama-rendering call inside render()
 *       (exact target method/name not verified here against 1.21.4), or
 *   (b) set the panorama's opacity/visibility off some other way and
 *       keep this gradient as what's left behind.
 * This file compiles and runs, but may currently show through or get
 * covered by vanilla's panorama until (a) or (b) is wired up — treat it
 * as a starting point, not a finished background replacement.
 */
@Mixin({TitleScreen.class, GameMenuScreen.class})
public abstract class DeckBackgroundMixin extends Screen {

    protected DeckBackgroundMixin(net.minecraft.text.Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void stritdeck$drawBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.fillGradient(0, 0, this.width, this.height, DeckTheme.BG_TOP, DeckTheme.BG_BOTTOM);

        int barTop = this.height - DeckTheme.STATUS_BAR_HEIGHT;
        context.fill(0, barTop, this.width, this.height, DeckTheme.STATUS_BAR_BG);
        var tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        context.drawText(tr, "Stritdeck", 8, barTop + (DeckTheme.STATUS_BAR_HEIGHT - 8) / 2, DeckTheme.TEXT_SECONDARY, false);
    }
}
