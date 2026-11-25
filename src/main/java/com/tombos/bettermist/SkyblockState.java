package com.tombos.bettermist;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.Locale;

public class SkyblockState {
    public static volatile boolean inAllowedArea = false;
    private static String lastArea = "";

    public static void init() {
        // Driven by ClientTickMixin -> SkyblockState.tick(...)
    }

    public static void tick(Minecraft mc) {
        if (mc == null || mc.level == null || mc.player == null) {
            setAreaState(mc, false, "");
            return;
        }

        String rawLocation = extractScoreboardLocation(mc.level.getScoreboard());
        if (rawLocation == null) {
            return;
        }

        String cleaned = rawLocation.replaceAll("§.", "").replace("⏣", "").trim();
        String lower = cleaned.toLowerCase(Locale.ROOT);

        boolean allowed = false;
        if (!lower.contains("mineshaft")) {
            allowed = lower.contains("dwarven mines")
                    || lower.contains("glacite tunnels")
                    || lower.equals("the mist")
                    || lower.equals("mist");
        }

        setAreaState(mc, allowed, cleaned);
    }

    private static void setAreaState(Minecraft mc, boolean nextState, String area) {
        if (nextState != inAllowedArea || !area.equals(lastArea)) {
            boolean stateChanged = nextState != inAllowedArea;
            inAllowedArea = nextState;
            lastArea = area;

            if (stateChanged && mc != null) {
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
        }
    }

    private static String extractScoreboardLocation(Scoreboard scoreboard) {
        if (scoreboard == null) {
            return null;
        }
        Objective sidebar = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (sidebar == null) {
            return null;
        }

        for (PlayerScoreEntry entry : scoreboard.listPlayerScores(sidebar)) {
            if (entry.isHidden()) {
                continue;
            }
            PlayerTeam team = scoreboard.getPlayersTeam(entry.owner());
            Component line = PlayerTeam.formatNameForTeam(team, Component.literal(entry.owner()));
            String text = line.getString().replaceAll("§.", "").trim();
            if (text.contains("⏣")) {
                return text;
            }
        }
        return null;
    }
}
