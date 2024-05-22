package me.whizvox.funwithquarries.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TickableBlockEntity extends BlockEntity {

  public TickableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  public abstract void tick();

  public static <T extends BlockEntity> void onTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
    ((TickableBlockEntity) blockEntity).tick();
  }

}
