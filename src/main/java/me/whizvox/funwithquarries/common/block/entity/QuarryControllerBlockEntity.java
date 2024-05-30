package me.whizvox.funwithquarries.common.block.entity;

import it.unimi.dsi.fastutil.Pair;
import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import me.whizvox.funwithquarries.common.util.QuarryUtils;
import me.whizvox.funwithquarries.common.util.StructureBlockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2i;

import java.util.*;
import java.util.stream.Stream;

public class QuarryControllerBlockEntity extends TickableBlockEntity {

  public static final StructureBlockValidator IS_QUARRY_COMPONENT = (level, pos, state) -> state.is(FWQBlocks.TAG_QUARRY_COMPONENT);

  private boolean running;
  private int frameWidth, frameHeight, frameDepth;
  private int maxDepth;
  private int multiblockDepth;
  private BlockPos currentPos;
  private BlockState currentBlock;
  private List<BlockPos> framePositions;
  private BlockPos frameCorner1, frameCorner2;
  private final List<Pair<BlockPos, StructureBlockValidator>> structureBlocks;
  private BlockPos structureCorner1, structureCorner2;
  private boolean structureFormed;

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
    structureCorner1 = null;
    structureCorner2 = null;
    structureFormed = false;
    structureBlocks = new ArrayList<>();
    modules = new HashSet<>();

    setupStructure();
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

  private void setupStructure() {
    Vector2i corner1 = switch (getBlockState().getValue(QuarryControllerBlock.FACING)) {
      case SOUTH -> new Vector2i(-1, -2);
      case EAST -> new Vector2i(-2, -1);
      case WEST -> new Vector2i(0, -1);
      default -> new Vector2i(-1, 0); // north
    };
    Vector2i corner2 = corner1.add(2, 2, new Vector2i());
    structureCorner1 = worldPosition.offset(corner1.x, -1, corner1.y);
    structureCorner2 = worldPosition.offset(corner2.x, 1, corner2.y);
    structureBlocks.clear();
    for (int x = corner1.x; x <= corner2.x; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = corner1.y; z <= corner2.y; z++) {
          BlockPos pos = worldPosition.offset(x, y, z);
          if (pos != worldPosition) {
            if (x == corner1.x + 1 && y == 0 && z == corner1.y + 1) {
              // center block must be air
              structureBlocks.add(Pair.of(pos, StructureBlockValidator.IS_AIR));
            } else {
              structureBlocks.add(Pair.of(pos, IS_QUARRY_COMPONENT));
            }
          }
        }
      }
    }
  }

  private void attemptAddModule(BlockPos pos, boolean replace) {
    if (replace) {
      modules.removeIf(be -> be.getBlockPos() == pos);
    }
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity != null) {
      modules.add(blockEntity);
      if (blockEntity instanceof DroneStationBlockEntity droneStation) {
        droneStation.setController(this);
      }
    }
  }

  private void attemptReformStructure() {
    modules.clear();
    structureFormed = true;
    for (Pair<BlockPos, StructureBlockValidator> pair : structureBlocks) {
      BlockPos pos = pair.first();
      BlockState state = level.getBlockState(pos);
      if (!pair.second().isValid(level, pos, state)) {
        structureFormed = false;
        break;
      }
      attemptAddModule(pos, false);
    }
    if (structureFormed) {
      level.setBlock(worldPosition, getBlockState().setValue(QuarryControllerBlock.STATE, QuarryControllerBlock.State.STANDBY), Block.UPDATE_ALL);
    } else {
      modules.clear();
    }
  }

  private void checkStructureIntegrity() {
    for (Pair<BlockPos, StructureBlockValidator> pair : structureBlocks) {
      BlockPos pos = pair.first();
      BlockState state = level.getBlockState(pos);
      if (!pair.second().isValid(level, pos, state)) {
        structureFormed = false;
        level.setBlock(worldPosition, getBlockState().setValue(QuarryControllerBlock.STATE, QuarryControllerBlock.State.OFF), Block.UPDATE_ALL);
        break;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("StructureFormed", structureFormed);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    structureFormed = tag.getBoolean("StructureFormed");
    setupStructure();
    if (structureFormed) {
      // a bit misleading, but this will update the modules set
      attemptReformStructure();
    }
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

  public BlockPos getFrameCorner1() {
    return frameCorner1;
  }

  public BlockPos getFrameCorner2() {
    return frameCorner2;
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

  public void updateComponent(BlockPos compPos) {
    if (structureBlocks.stream().anyMatch(pair -> pair.first() == compPos)) {
      attemptAddModule(compPos, true);
    }
  }

  @Override
  public void tick() {
    if (level.isClientSide) {
      return;
    }
    if (level.getGameTime() % 20 == 0) {
      if (structureFormed) {
        checkStructureIntegrity();
      } else {
        attemptReformStructure();
      }
    }
  }

}
