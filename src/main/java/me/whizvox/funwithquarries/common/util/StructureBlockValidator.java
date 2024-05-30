package me.whizvox.funwithquarries.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface StructureBlockValidator {

  boolean isValid(Level level, BlockPos pos, BlockState state);

  StructureBlockValidator IS_AIR = (level, pos, state) -> state.isAir();

}
