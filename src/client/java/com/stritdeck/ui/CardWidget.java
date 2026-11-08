package com.stritdeck.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;

/**
 * A single "card" tile in the PS5-style deck: an icon/glyph, a label
 * underneath, and a smooth hover/focus scale + glow animation.
 *
 * This does not attempt pixel-perfect rounded corners (vanilla
 * {@link DrawContext} has no rounded-rect primitive without a custom
 * shader/texture) — it uses a flat card with an accent border and a
 * soft outer glow on focus, which reads as "PS5 card" at this size
 * without needing extra textures. Swap in real artwork later by
 * rendering a texture instead of {@link #glyph} in {@link #renderWidget}.
 */
public class CardWidget extends ClickableWidget {

    private final String glyph; // short text/emoji-style placeholder icon, e.g. "⚔" or "S"
    private final Text subtitle;
    private final Runnable onActivate;

    private float scale = DeckTheme.IDLE_SCALE;

    public CardWidget(int x, int y, String glyph, Text title, Text subtitle, Runnable onActivate) {
        super(x, y, DeckTheme.CARD_WIDTH, DeckTheme.CARD_HEIGHT, title);
        this.glyph = glyph;
        this.subtitle = subtitle;
        this.onActivate = onActivate;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        activate();
    }

    public void activate() {
        if (onActivate != null) {
            onActivate.run();
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean active = this.isHovered() || this.isFocused();
        float target = active ? DeckTheme.FOCUS_SCALE : DeckTheme.IDLE_SCALE;
        this.scale += (target - this.scale) * DeckTheme.HOVER_LERP_SPEED;

        int cx = getX() + getWidth() / 2;
        int cy = getY() + getHeight() / 2;
        int w = Math.round(getWidth() * scale);
        int h = Math.round(getHeight() * scale);
        int left = cx - w / 2;
        int top = cy - h / 2;
        int right = left + w;
        int bottom = top + h;

        // Soft outer glow when focused/hovered
        if (active) {
            int glow = 6;
            context.fill(left - glow, top - glow, right + glow, bottom + glow, 0x335AC8FA);
        }

        int bg = active ? DeckTheme.CARD_BG_HOVER : DeckTheme.CARD_BG;
        context.fill(left, top, right, bottom, bg);

        // Border accent (brighter when focused so keyboard/controller nav is readable)
        int border = active ? DeckTheme.CARD_BORDER_FOCUSED : 0x33FFFFFF;
        context.drawBorder(left, top, w, h, border);

        // Icon glyph, centered in the upper portion of the card
        var tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        int glyphWidth = tr.getWidth(glyph);
        context.drawText(tr, glyph, cx - glyphWidth / 2, top + h / 3 - 4, DeckTheme.TEXT_PRIMARY, true);

        // Title
        Text title = getMessage();
        int titleWidth = tr.getWidth(title);
        context.drawText(tr, title, cx - titleWidth / 2, bottom - 30, DeckTheme.TEXT_PRIMARY, false);

        // Subtitle (smaller, dimmer)
        if (subtitle != null) {
            int subWidth = tr.getWidth(subtitle);
            context.drawText(tr, subtitle, cx - subWidth / 2, bottom - 16, DeckTheme.TEXT_SECONDARY, false);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
