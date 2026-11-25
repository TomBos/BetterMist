package com.tombos.bettermist.mixin;

import com.tombos.bettermist.SkyblockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.world.LevelSlice", remap = false)
public class SodiumLevelSliceMixin {

    @Inject(method = "getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true, remap = false)
    private void onGetBlockStatePos(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (!SkyblockState.inAllowedArea) {
            return;
        }
        BlockState mapped = mapGlassState(cir.getReturnValue());
        if (mapped != null) {
            cir.setReturnValue(mapped);
        }
    }

    @Inject(method = "getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true, remap = false)
    private void onGetBlockStateXYZ(int x, int y, int z, CallbackInfoReturnable<BlockState> cir) {
        if (!SkyblockState.inAllowedArea) {
            return;
        }
        BlockState mapped = mapGlassState(cir.getReturnValue());
        if (mapped != null) {
            cir.setReturnValue(mapped);
        }
    }

    private static BlockState mapGlassState(BlockState state) {
        if (state == null) {
            return null;
        }
        Block block = state.getBlock();
        if (block == Blocks.STAINED_GLASS.white() || block == Blocks.STAINED_GLASS.lightGray()) {
            return Blocks.STAINED_GLASS.black().defaultBlockState();
        }
        if (block == Blocks.STAINED_GLASS_PANE.white() || block == Blocks.STAINED_GLASS_PANE.lightGray()) {
            return Blocks.STAINED_GLASS_PANE.black().defaultBlockState();
        }
        return null;
    }
}
