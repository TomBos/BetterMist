package com.tombos.bettermist.mixin;

import com.tombos.bettermist.SkyblockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public class LevelSliceMixin {

    @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
    private void onGetBlockState(int x, int y, int z, CallbackInfoReturnable<BlockState> cir) {
        if (!SkyblockState.inAllowedArea) {
            return;
        }
        BlockState state = cir.getReturnValue();
        if (state == null) {
            return;
        }
        if (state.is(Blocks.STAINED_GLASS.white()) || state.is(Blocks.STAINED_GLASS.lightGray())) {
            cir.setReturnValue(Blocks.STAINED_GLASS.black().defaultBlockState());
        } else if (state.is(Blocks.STAINED_GLASS_PANE.white()) || state.is(Blocks.STAINED_GLASS_PANE.lightGray())) {
            cir.setReturnValue(Blocks.STAINED_GLASS_PANE.black().defaultBlockState());
        }
    }
}
