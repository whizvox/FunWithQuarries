package me.whizvox.funwithquarries.common.block.entity;

import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DroneStationBlockEntity extends TickableBlockEntity {

  @Nullable
  private QuarryControllerBlockEntity controller;

  private final List<BlockPos> framePositions;
  private boolean isPlacingFrames;
  private int frameBlockIndex;

  public DroneStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(FWQBlockEntities.DRONE_STATION.get(), pPos, pBlockState);
    controller = null;
    framePositions = new ArrayList<>();
    isPlacingFrames = false;
    frameBlockIndex = 0;
  }

  private void checkForController() {
    if (controller != null && !controller.hasModule(this)) {
      controller = null;
      isPlacingFrames = false;
      frameBlockIndex = 0;
      // TODO recall drone if out
    }
  }

  public boolean hasController() {
    return controller != null;
  }

  @Nullable
  public BlockPos getNextFramePosition(boolean increment) {
    if (!hasController() || frameBlockIndex >= framePositions.size()) {
      return null;
    }
    BlockPos pos = framePositions.get(frameBlockIndex);
    if (increment) {
      frameBlockIndex++;
    }
    return pos;
  }

  public void reset() {
    isPlacingFrames = false;
    frameBlockIndex = 0;
    framePositions.clear();
    if (hasController()) {
      controller.getFramePositions().forEach(framePositions::add);
    }
  }

  public void setController(@Nullable QuarryControllerBlockEntity controller) {
    this.controller = controller;
    reset();
  }

  public void removeController() {
    setController(null);
  }

  @Override
  public void tick() {
    if (level.getGameTime() % 20 == 0) {
      checkForController();
    }
    if (!hasController()) {
      return;
    }

  }

}
