package me.whizvox.funwithquarries.common.block.entity;

import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import me.whizvox.funwithquarries.common.util.QuarryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2i;

import java.util.*;
import java.util.stream.Stream;

public class QuarryControllerBlockEntity extends TickableBlockEntity {

  private boolean running;
  private int frameWidth, frameHeight, frameDepth;
  private int maxDepth;
  private int multiblockDepth;
  private BlockPos currentPos;
  private BlockState currentBlock;
  private List<BlockPos> framePositions;
  private BlockPos frameCorner1, frameCorner2;

  private final List<BlockPos> multiblockParts;
  private Set<BlockEntity> modules;

  public QuarryControllerBlockEntity(BlockPos pos, BlockState state) {
    super(FWQBlockEntities.QUARRY_CONTROLLER.get(), pos, state);
    running = false;

    frameWidth = 10;
    frameHeight = 4;
    frameDepth = 10;
    maxDepth = -64;
    multiblockDepth = 1;
    currentPos = null;
    currentBlock = null;
    framePositions = new ArrayList<>();

    frameCorner1 = null;
    frameCorner2 = null;
    multiblockParts = new ArrayList<>();
    modules = new HashSet<>();
  }

  private void recalculateFramePositions() {
    framePositions.clear();
    Vector2i corner1, corner2;
    switch (getBlockState().getValue(QuarryControllerBlock.FACING)) {
      case NORTH -> {
        corner1 = new Vector2i(-frameWidth / 2, multiblockDepth);
        corner2 = new Vector2i(corner1.x + frameWidth, corner1.y + frameDepth);
      }
      case SOUTH -> {
        corner1 = new Vector2i(-frameWidth / 2, -multiblockDepth - frameDepth);
        corner2 = new Vector2i(corner1.x + frameWidth, corner1.y + frameDepth);
      }
      case EAST -> {
        corner1 = new Vector2i(-multiblockDepth - frameWidth, -frameWidth / 2);
        corner2 = new Vector2i(corner1.x + frameDepth, corner1.y + frameWidth);
      }
      case WEST -> {
        corner1 = new Vector2i(multiblockDepth, -frameWidth / 2);
        corner2 = new Vector2i(corner1.x + frameDepth, corner1.y + frameWidth);
      }
      default -> {
        corner1 = new Vector2i();
        corner2 = new Vector2i();
      }
    }
    frameCorner1 = worldPosition.offset(corner1.x, -1, corner1.y);
    frameCorner2 = worldPosition.offset(corner2.x, frameHeight - 1, corner2.y);
    QuarryUtils.calculateFramePositions(frameCorner1, frameCorner2, framePositions);
    // sort in order from bottom-up, then by how close it is to the controller
    framePositions.sort(Comparator.comparing(Vec3i::getY).thenComparing(pos -> pos.distSqr(worldPosition)));
  }

  private void reformMultiblock() {

  }

  public boolean isRunning() {
    return running;
  }

  public int getFrameWidth() {
    return frameWidth;
  }

  public int getFrameDepth() {
    return frameDepth;
  }

  public int getMaxDepth() {
    return maxDepth;
  }

  public int getMultiblockDepth() {
    return multiblockDepth;
  }

  public BlockPos getCurrentPos() {
    return currentPos;
  }

  public BlockState getCurrentBlock() {
    return currentBlock;
  }

  public Stream<BlockPos> getFramePositions() {
    return framePositions.stream();
  }

  public boolean hasModule(BlockEntity blockEntity) {
    return modules.contains(blockEntity);
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public boolean toggleRunning() {
    running = !running;
    return running;
  }

  @Override
  public void tick() {
    if (!running) {
      return;
    }
  }

}
