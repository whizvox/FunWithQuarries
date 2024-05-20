package me.whizvox.funwithquarries.common.block.entity;

import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class QuarryControllerBlockEntity extends BlockEntity {



  public QuarryControllerBlockEntity(BlockPos pos, BlockState state) {
    super(FWQBlockEntities.QUARRY_CONTROLLER.get(), pos, state);
  }

}
