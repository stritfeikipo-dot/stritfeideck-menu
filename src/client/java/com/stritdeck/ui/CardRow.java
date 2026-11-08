package com.stritdeck.ui;

import java.util.List;

/**
 * Pure layout helper: given a screen width and a list of cards, returns
 * the x position each card should be placed at so the row is centered,
 * PS5-hub style. Does not render or own the widgets — the screen still
 * adds each CardWidget itself via addDrawableChild so vanilla focus
 * traversal (Tab / arrow keys / controller) keeps working for free.
 */
public final class CardRow {
    private CardRow() {}

    public static int[] centeredXPositions(int screenWidth, int cardCount) {
        int totalWidth = cardCount * DeckTheme.CARD_WIDTH + (cardCount - 1) * DeckTheme.CARD_GAP;
        int startX = (screenWidth - totalWidth) / 2;

        int[] positions = new int[cardCount];
        for (int i = 0; i < cardCount; i++) {
            positions[i] = startX + i * (DeckTheme.CARD_WIDTH + DeckTheme.CARD_GAP);
        }
        return positions;
    }

    /** Convenience overload when you already have the list and just want it to read nicely at call sites. */
    public static int[] centeredXPositions(int screenWidth, List<?> cards) {
        return centeredXPositions(screenWidth, cards.size());
    }
}
