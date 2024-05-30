package me.whizvox.funwithquarries.common.block;

import me.whizvox.funwithquarries.common.block.entity.QuarryControllerBlockEntity;
import me.whizvox.funwithquarries.common.block.entity.TickableBlockEntity;
import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class QuarryControllerBlock extends BaseEntityBlock {

  public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  public QuarryControllerBlock() {
    super(FWQBlocks.MACHINE_PROPERTIES);
    registerDefaultState(stateDefinition.any().setValue(STATE, State.OFF).setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(STATE, FACING);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return FWQBlockEntities.QUARRY_CONTROLLER.get().create(pos, state);
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
    return switch (state.getValue(STATE)) {
      case ON, ERROR -> 7;
      default -> 0;
    };
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    if (type == FWQBlockEntities.QUARRY_CONTROLLER.get()) {
      return TickableBlockEntity.createTicker();
    }
    return null;
  }

  @Nullable
  @Override
  public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    return level.getBlockEntity(pos) instanceof QuarryControllerBlockEntity controller ? controller : null;
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!level.isClientSide) {
      NetworkHooks.openScreen((ServerPlayer) player, state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  public enum State implements StringRepresentable {
    OFF,
    STANDBY,
    ON,
    ERROR;

    private final String name;

    State() {
      this.name = name().toLowerCase();
    }

    @Override
    public String getSerializedName() {
      return name;
    }
  }

}
