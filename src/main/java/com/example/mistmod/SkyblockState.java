package com.example.mistmod;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;

public class SkyblockState {
    public static volatile boolean inDwarvenMines = false;

    public static void init() {
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, packet -> {
            boolean isSkyBlock = packet.getServerType()
                    .map(type -> "SKYBLOCK".equalsIgnoreCase(type.getName()))
                    .orElse(false);

            boolean isDwarven = packet.getMode()
                    .map(mode -> "mining_3".equalsIgnoreCase(mode))
                    .orElse(false)
                    || packet.getMap()
                    .map(map -> "Dwarven Mines".equalsIgnoreCase(map))
                    .orElse(false);

            boolean nextState = isSkyBlock && isDwarven;
            if (nextState != inDwarvenMines) {
                inDwarvenMines = nextState;
                Minecraft mc = Minecraft.getInstance();
                mc.execute(() -> {
                    if (mc.level != null
                            && mc.player != null
                            && mc.levelRenderer != null
                            && mc.gameRenderer != null) {
                        mc.levelRenderer.invalidateCompiledGeometry(
                                mc.level,
                                mc.options,
                                mc.gameRenderer.mainCamera(),
                                mc.getBlockColors()
                        );
                    }
                });
            }
        });
    }
}
