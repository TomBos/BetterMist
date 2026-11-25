package com.tombos.bettermist;

import net.fabricmc.api.ClientModInitializer;

public class BetterMistClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkyblockState.init();
    }
}
