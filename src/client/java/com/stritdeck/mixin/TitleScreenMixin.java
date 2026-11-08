package com.stritdeck.mixin;

import com.stritdeck.ui.CardRow;
import com.stritdeck.ui.CardWidget;
import com.stritdeck.ui.DeckTheme;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces the default list of title-screen buttons with a horizontal
 * row of PS5-style cards, wired to the same vanilla actions. This does
 * NOT touch the vanilla logic (world loading, server connecting, etc.)
 * — it only swaps what gets drawn and clicked.
 *
 * NOTE (disclosed, not verified): the exact constructor signatures of
 * SelectWorldScreen / MultiplayerScreen / OptionsScreen / LanguageOptionsScreen
 * below are written from the 1.21.x Yarn mapping conventions but have
 * NOT been compiled against the real 1.21.4 mappings in this environment
 * (no network/build access here). Expect to need small signature
 * fixes — Loom will tell you exactly which line if so.
 *
 * The "extends Screen" + dummy constructor here is the standard Mixin
 * pattern for calling members inherited from a common superclass; this
 * constructor is never actually executed, Mixin discards it.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private static final String[] ICONS = {"⏵", "🌐", "⚙", "🌍", "⏻"};

    @Inject(method = "init", at = @At("TAIL"))
    private void stritdeck$buildDeck(CallbackInfo ci) {
        TitleScreen self = (TitleScreen) (Object) this;
        this.clearChildren();

        List<Runnable> actions = new ArrayList<>();
        List<Text> titles = new ArrayList<>();
        List<Text> subtitles = new ArrayList<>();

        titles.add(Text.translatable("menu.singleplayer"));
        subtitles.add(Text.literal("Worlds"));
        actions.add(() -> this.client.setScreen(new SelectWorldScreen(self)));

        titles.add(Text.translatable("menu.multiplayer"));
        subtitles.add(Text.literal("Servers"));
        actions.add(() -> this.client.setScreen(new MultiplayerScreen(self)));

        titles.add(Text.translatable("menu.options"));
        subtitles.add(Text.literal("Settings"));
        actions.add(() -> this.client.setScreen(new OptionsScreen(self, this.client.options)));

        titles.add(Text.translatable("options.language"));
        subtitles.add(Text.literal("Language"));
        actions.add(() -> this.client.setScreen(new LanguageOptionsScreen(self, this.client.options, this.client.getLanguageManager())));

        titles.add(Text.translatable("menu.quit"));
        subtitles.add(Text.literal("Exit"));
        actions.add(() -> this.client.scheduleStop());

        int[] xs = CardRow.centeredXPositions(this.width, titles.size());
        int y = this.height / 2 - DeckTheme.CARD_HEIGHT / 2;

        for (int i = 0; i < titles.size(); i++) {
            String icon = ICONS[Math.min(i, ICONS.length - 1)];
            Runnable action = actions.get(i);
            this.addDrawableChild(new CardWidget(xs[i], y, icon, titles.get(i), subtitles.get(i), action));
        }
    }
}
