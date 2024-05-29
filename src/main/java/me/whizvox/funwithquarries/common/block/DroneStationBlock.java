package me.whizvox.funwithquarries.common.block;

import me.whizvox.funwithquarries.common.block.entity.DroneStationBlockEntity;
import me.whizvox.funwithquarries.common.block.entity.TickableBlockEntity;
import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class DroneStationBlock extends BaseEntityBlock {

  public static final DirectionProperty FACING = BlockStateProperties.FACING;
  public static final BooleanProperty OPEN = BooleanProperty.create("open");

  public DroneStationBlock() {
    super(FWQBlocks.MACHINE_PROPERTIES);

    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new DroneStationBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return type == FWQBlockEntities.DRONE_STATION.get() ? TickableBlockEntity.createTicker() : null;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, OPEN);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState()
        .setValue(FACING, context.getNearestLookingDirection().getOpposite())
        .setValue(OPEN, false);
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

}
