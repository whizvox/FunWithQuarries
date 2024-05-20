package me.whizvox.funwithquarries.common.block;

import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class QuarryControllerBlock extends BaseEntityBlock {

  public QuarryControllerBlock() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1.5F, 6.0F));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return FWQBlockEntities.QUARRY_CONTROLLER.get().create(pos, state);
  }

}
