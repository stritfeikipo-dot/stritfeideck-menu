package com.stritdeck;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common entrypoint. Stritdeck is purely a client-side visual mod
 * (it reskins menus), so there is nothing to do on the common/server
 * side — this class exists only because Fabric requires a "main"
 * entrypoint to be declared in fabric.mod.json.
 */
public class Stritdeck implements ModInitializer {
    public static final String MOD_ID = "stritdeck";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Stritdeck loaded (client-only menu reskin)");
    }
}
