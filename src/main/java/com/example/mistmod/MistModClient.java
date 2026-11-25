package com.example.mistmod;

import net.fabricmc.api.ClientModInitializer;

public class MistModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkyblockState.init();
    }
}
