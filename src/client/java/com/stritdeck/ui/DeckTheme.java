package com.stritdeck.ui;

/**
 * Central place for the PS5-"Game Hub" look: colors, timings, sizing.
 * Change values here to re-theme every screen at once.
 */
public final class DeckTheme {
    private DeckTheme() {}

    // Background
    public static final int BG_TOP = 0xFF0E1116;
    public static final int BG_BOTTOM = 0xFF191D24;

    // Cards
    public static final int CARD_BG = 0xFF20242D;
    public static final int CARD_BG_HOVER = 0xFF2B3140;
    public static final int CARD_BORDER_FOCUSED = 0xFF5AC8FA; // PS5-ish light blue accent
    public static final int CARD_BORDER_IDLE = 0x00000000; // transparent

    public static final int TEXT_PRIMARY = 0xFFFFFFFF;
    public static final int TEXT_SECONDARY = 0xFFA6ADBB;

    public static final int CARD_WIDTH = 150;
    public static final int CARD_HEIGHT = 150;
    public static final int CARD_GAP = 18;
    public static final int CARD_CORNER_RADIUS = 10;

    // Animation
    /** Fraction of the remaining distance covered per tick toward the target (0..1). Higher = snappier. */
    public static final float HOVER_LERP_SPEED = 0.35f;
    public static final float FOCUS_SCALE = 1.08f;
    public static final float IDLE_SCALE = 1.0f;

    // Bottom status bar (clock / mod watermark), like the PS5 control center strip
    public static final int STATUS_BAR_HEIGHT = 22;
    public static final int STATUS_BAR_BG = 0x992B2F38;
}
