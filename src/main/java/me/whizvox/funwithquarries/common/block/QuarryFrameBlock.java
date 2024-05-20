package me.whizvox.funwithquarries.common.block;

import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QuarryFrameBlock extends Block {

  public static final BooleanProperty
      NORTH = BlockStateProperties.NORTH,
      SOUTH = BlockStateProperties.SOUTH,
      EAST = BlockStateProperties.EAST,
      WEST = BlockStateProperties.WEST,
      UP = BlockStateProperties.UP,
      DOWN = BlockStateProperties.DOWN;

  public static BooleanProperty fromDirection(Direction direction) {
    return switch (direction) {
      case NORTH -> NORTH;
      case SOUTH -> SOUTH;
      case EAST -> EAST;
      case WEST -> WEST;
      case UP -> UP;
      case DOWN -> DOWN;
    };
  }

  private static final VoxelShape
      SHAPE_BASE = Block.box(4, 4, 4, 12, 12, 12),
      SHAPE_NORTH = Block.box(4, 4, 0, 12, 12, 4),
      SHAPE_SOUTH = Block.box(4, 4, 12, 12, 12, 16),
      SHAPE_EAST = Block.box(12, 4, 4, 16, 12, 12),
      SHAPE_WEST = Block.box(0, 4, 4, 4, 12, 12),
      SHAPE_UP = Block.box(4, 12, 4, 12, 16, 12),
      SHAPE_DOWN = Block.box(4, 0, 4, 12, 4, 12);
  private static final Map<BlockState, VoxelShape> SHAPES_CACHE = new HashMap<>();

  public QuarryFrameBlock() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE));
    registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));

    for (BlockState state : stateDefinition.getPossibleStates()) {
      List<VoxelShape> shapes = new ArrayList<>();
      if (state.getValue(NORTH)) {
        shapes.add(SHAPE_NORTH);
      }
      if (state.getValue(SOUTH)) {
        shapes.add(SHAPE_SOUTH);
      }
      if (state.getValue(EAST)) {
        shapes.add(SHAPE_EAST);
      }
      if (state.getValue(WEST)) {
        shapes.add(SHAPE_WEST);
      }
      if (state.getValue(UP)) {
        shapes.add(SHAPE_UP);
      }
      if (state.getValue(DOWN)) {
        shapes.add(SHAPE_DOWN);
      }
      SHAPES_CACHE.put(state, Shapes.or(SHAPE_BASE, shapes.toArray(VoxelShape[]::new)));
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState finalState = defaultBlockState();
    for (Direction direction : Direction.values()) {
      BlockPos pos = context.getClickedPos().relative(direction);
      BlockState state = context.getLevel().getBlockState(pos);
      if (state.is(FWQBlocks.QUARRY_FRAME.get())) {
        finalState = finalState.setValue(fromDirection(direction), true);
        context.getLevel().setBlockAndUpdate(pos, state.setValue(fromDirection(direction.getOpposite()), true));
      }
    }
    return finalState;
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPES_CACHE.getOrDefault(state, SHAPE_BASE);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    if (!level.isClientSide) {
      level.updateNeighborsAt(pos, this);
    }
  }

  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
    BlockState updatedState = state;
    for (Direction direction : Direction.values()) {
      BlockPos otherPos = pos.relative(direction);
      BlockState otherState = level.getBlockState(otherPos);
      updatedState = updatedState.setValue(fromDirection(direction), otherState.is(FWQBlocks.QUARRY_FRAME.get()));
    }
    if (updatedState != state) {
      level.setBlockAndUpdate(pos, updatedState);
    }
  }

}
