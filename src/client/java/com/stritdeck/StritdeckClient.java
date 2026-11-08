package com.stritdeck;

import net.fabricmc.api.ClientModInitializer;

public class StritdeckClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Stritdeck.LOGGER.info("Stritdeck client UI active — menus will render in deck/card style");
    }
}
