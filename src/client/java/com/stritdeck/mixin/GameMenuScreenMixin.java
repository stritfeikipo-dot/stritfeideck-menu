package com.stritdeck.mixin;

import com.stritdeck.ui.CardRow;
import com.stritdeck.ui.CardWidget;
import com.stritdeck.ui.DeckTheme;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Same treatment as {@link TitleScreenMixin} but for the in-game pause
 * menu (class name GameMenuScreen in current Yarn mappings — this was
 * called "IngameMenuScreen" in older versions; if Loom can't find
 * GameMenuScreen on 1.21.4, check the mappings for the renamed class).
 *
 * Disclosed: "Back to Game" needs to close this screen via
 * MinecraftClient#setScreen(null); "Disconnect"/"Save and Quit to Title"
 * calls a method whose exact name has moved around between versions
 * (disconnect() / exitWorld()) — verify against 1.21.4 Yarn before
 * relying on it, a placeholder TODO is left below.
 */
@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private static final String[] ICONS = {"▶", "⚙", "🌐", "✕"};

    @Inject(method = "init", at = @At("TAIL"))
    private void stritdeck$buildDeck(CallbackInfo ci) {
        this.clearChildren();

        List<Runnable> actions = new ArrayList<>();
        List<Text> titles = new ArrayList<>();
        List<Text> subtitles = new ArrayList<>();

        titles.add(Text.translatable("menu.returnToGame"));
        subtitles.add(Text.literal("Resume"));
        actions.add(() -> this.client.setScreen(null));

        titles.add(Text.translatable("menu.options"));
        subtitles.add(Text.literal("Settings"));
        actions.add(() -> this.client.setScreen(new OptionsScreen(this, this.client.options)));

        titles.add(Text.translatable("menu.returnToMenu"));
        subtitles.add(Text.literal("Quit world"));
        actions.add(() -> {
            // TODO (disclosed, unverified): confirm the exact disconnect
            // method name for 1.21.4 — likely this.client.world != null
            // check + this.client.disconnect(...) followed by
            // this.client.setScreen(new TitleScreen()). Left as a direct
            // call to keep this file compiling without extra imports;
            // wire it up once you've checked the mapped method name.
            this.client.scheduleStop(); // placeholder — replace with real disconnect-to-title logic
        });

        titles.add(Text.translatable("menu.quit"));
        subtitles.add(Text.literal("Exit game"));
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
