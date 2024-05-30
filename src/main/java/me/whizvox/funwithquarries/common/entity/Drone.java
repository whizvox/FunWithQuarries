package me.whizvox.funwithquarries.common.entity;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.entity.ai.goal.FlyToMoveTargetGoal;
import me.whizvox.funwithquarries.common.entity.ai.control.DroneFlyingMoveControl;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import me.whizvox.funwithquarries.common.util.QuarryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Drone extends Mob {

  private static final EntityDataAccessor<BlockPos> DATA_MOVE_POS = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);
  private static final EntityDataAccessor<Boolean> DATA_SHOULD_MOVE = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Byte> DATA_TARGET_TYPE = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BYTE);
  private static final EntityDataAccessor<BlockPos> DATA_TARGET_POS = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);
  private static final EntityDataAccessor<BlockPos> DATA_FRAME_CORNER_1 = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);
  private static final EntityDataAccessor<BlockPos> DATA_FRAME_CORNER_2 = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);

  private final FakePlayer fakePlayer;
  private float targetProgress;

  private final LinkedList<BlockPos> remainingTargets;
  private final List<BlockPos> framePositions;

  public Drone(EntityType<? extends Mob> type, Level level) {
    super(type, level);
    moveControl = new DroneFlyingMoveControl(this);
    if (level instanceof ServerLevel serverLevel) {
      fakePlayer = new FakePlayer(serverLevel, FunWithQuarries.FAKE_PLAYER_PROFILE);
      fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FWQItems.UNIVERSAL_DIGGER_NETHERITE.get()));
    } else {
      fakePlayer = null;
    }
    targetProgress = 0;
    remainingTargets = new LinkedList<>();
    framePositions = new ArrayList<>();
  }

  @Nullable
  private BlockPos findObstaclePosition() {
    Vec3 movePos = getMovePosition().getCenter();
    AABB bb = getBoundingBox();
    Vec3 bbCenter = bb.getCenter();
    Vec3 direction = movePos.subtract(bbCenter).normalize().scale(1.5);
    Vec3[] points = new Vec3[]{
        new Vec3(bb.minX, bb.minY, bb.minZ),
        new Vec3(bb.maxX, bb.minY, bb.minZ),
        new Vec3(bb.minX, bb.maxY, bb.minZ),
        new Vec3(bb.maxX, bb.maxY, bb.minZ),
        new Vec3(bb.minX, bb.minY, bb.maxZ),
        new Vec3(bb.maxX, bb.minY, bb.maxZ),
        new Vec3(bb.minX, bb.maxY, bb.maxZ),
        new Vec3(bb.maxX, bb.maxY, bb.maxZ),
        bbCenter
    };
    for (Vec3 point : points) {
      Vec3 point2 = point.add(direction);
      BlockHitResult hit = level().clip(new ClipContext(point, point2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
      if (hit.getType() == HitResult.Type.BLOCK) {
        return hit.getBlockPos();
      }
    }
    return null;
  }

  private boolean workOnDestroyingBlock(ServerLevel level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    // 10x faster than player mining speed
    targetProgress += state.getDestroyProgress(fakePlayer, level, pos) * 30;
    level.destroyBlockProgress(fakePlayer.getId(), pos, (int) (targetProgress * 10));
    if (state.isAir() || targetProgress >= 1.0f) {
      level.destroyBlock(pos, false);
      targetProgress = 0;
      return true;
    }
    return false;
  }

  private boolean workOnPlacingFrame(ServerLevel level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    if (state.is(FWQBlocks.QUARRY_FRAME.get())) {
      level.setBlock(pos, FWQBlocks.QUARRY_FRAME.get().defaultBlockState(), Block.UPDATE_ALL);
      targetProgress = 0;
      return true;
    }
    if (!state.isAir()) {
      setTarget(TargetType.BREAK, pos);
      targetProgress = 0;
      return false;
    }
    // 1 frame every 12 ticks
    targetProgress += 0.25F;
    if (targetProgress >= 1.0F) {
      level.setBlock(pos, FWQBlocks.QUARRY_FRAME.get().defaultBlockState(), Block.UPDATE_ALL);
      level.playSound(null, pos, FWQBlocks.QUARRY_FRAME.get().getSoundType(state, level, pos, null).getPlaceSound(), SoundSource.BLOCKS, 1, 0.8F + random.nextFloat() * 0.4F);
      targetProgress = 0;
      return true;
    }
    return false;
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(1, new FlyToMoveTargetGoal(this));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    entityData.define(DATA_MOVE_POS, BlockPos.ZERO);
    entityData.define(DATA_SHOULD_MOVE, false);
    entityData.define(DATA_TARGET_TYPE, TargetType.NONE.byteValue);
    entityData.define(DATA_TARGET_POS, BlockPos.ZERO);
    entityData.define(DATA_FRAME_CORNER_1, BlockPos.ZERO);
    entityData.define(DATA_FRAME_CORNER_2, BlockPos.ZERO);
    setNoGravity(true);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    int movePosX = tag.getInt("MovePosX");
    int movePosY = tag.getInt("MovePosY");
    int movePosZ = tag.getInt("MovePosZ");
    entityData.set(DATA_MOVE_POS, new BlockPos(movePosX, movePosY, movePosZ));
    entityData.set(DATA_SHOULD_MOVE, tag.getBoolean("ShouldMove"));
    entityData.set(DATA_TARGET_TYPE, tag.getByte("TargetType"));
    int targetPosX = tag.getInt("TargetPosX");
    int targetPosY = tag.getInt("TargetPosY");
    int targetPosZ = tag.getInt("TargetPosZ");
    entityData.set(DATA_TARGET_POS, new BlockPos(targetPosX, targetPosY, targetPosZ));
    int corner1X = tag.getInt("FrameCorner1X");
    int corner1Y = tag.getInt("FrameCorner1Y");
    int corner1Z = tag.getInt("FrameCorner1Z");
    int corner2X = tag.getInt("FrameCorner2X");
    int corner2Y = tag.getInt("FrameCorner2Y");
    int corner2Z = tag.getInt("FrameCorner2Z");
    setFrameBounds(new BlockPos(corner1X, corner1Y, corner1Z), new BlockPos(corner2X, corner2Y, corner2Z));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    BlockPos movePos = getMovePosition();
    tag.putInt("MovePosX", movePos.getX());
    tag.putInt("MovePosY", movePos.getY());
    tag.putInt("MovePosZ", movePos.getZ());
    tag.putBoolean("ShouldMove", shouldMove());
    tag.putByte("TargetType", getTargetType().byteValue);
    BlockPos targetPos = getTargetPosition();
    tag.putInt("TargetPosX", targetPos.getX());
    tag.putInt("TargetPosY", targetPos.getY());
    tag.putInt("TargetPosZ", targetPos.getZ());
    BlockPos corner1 = getFrameCorner1();
    tag.putInt("FrameCorner1X", corner1.getX());
    tag.putInt("FrameCorner1Y", corner1.getY());
    tag.putInt("FrameCorner1Z", corner1.getZ());
    BlockPos corner2 = getFrameCorner2();
    tag.putInt("FrameCorner2X", corner2.getX());
    tag.putInt("FrameCorner2Y", corner2.getY());
    tag.putInt("FrameCorner2Z", corner2.getZ());
  }

  public BlockPos getMovePosition() {
    return entityData.get(DATA_MOVE_POS);
  }

  public boolean shouldMove() {
    return entityData.get(DATA_SHOULD_MOVE);
  }

  public boolean isAtMovePosition() {
    return getBoundingBox().getCenter().distanceToSqr(getMovePosition().getCenter()) < 0.05;
  }

  public TargetType getTargetType() {
    return TargetType.from(entityData.get(DATA_TARGET_TYPE));
  }

  public BlockPos getTargetPosition() {
    return entityData.get(DATA_TARGET_POS);
  }

  public BlockPos getFrameCorner1() {
    return entityData.get(DATA_FRAME_CORNER_1);
  }

  public BlockPos getFrameCorner2() {
    return entityData.get(DATA_FRAME_CORNER_2);
  }

  public void setFrameBounds(BlockPos corner1, BlockPos corner2) {
    BlockPos actualCorner1 = new BlockPos(Math.min(corner1.getX(), corner2.getX()), Math.min(corner1.getY(), corner2.getY()), Math.min(corner1.getZ(), corner2.getZ()));
    int width = Math.abs(corner1.getX() - corner2.getX());
    int height = Math.abs(corner1.getY() - corner2.getY());
    int depth = Math.abs(corner1.getZ() - corner2.getZ());
    BlockPos actualCorner2 = actualCorner1.offset(width, height, depth);
    entityData.set(DATA_FRAME_CORNER_1, actualCorner1);
    entityData.set(DATA_FRAME_CORNER_2, actualCorner2);
    framePositions.clear();
    QuarryUtils.calculateFramePositions(actualCorner1, actualCorner2, framePositions);
    framePositions.sort(Comparator.comparingDouble(pos -> pos.distSqr(actualCorner1)));
    setTarget(TargetType.NONE, BlockPos.ZERO);
    setMovePosition(actualCorner1.offset(Mth.ceil(width / 2.0F), Mth.ceil(height / 2.0F), Mth.ceil(depth / 2.0F)));
  }

  public void setMovePosition(BlockPos pos) {
    entityData.set(DATA_MOVE_POS, pos);
    entityData.set(DATA_SHOULD_MOVE, true);
  }

  public void removeMovePosition() {
    entityData.set(DATA_SHOULD_MOVE, false);
  }

  public void removeTarget() {
    entityData.set(DATA_TARGET_TYPE, TargetType.NONE.byteValue);
  }

  public void setTarget(TargetType type, BlockPos pos) {
    entityData.set(DATA_TARGET_TYPE, type.byteValue);
    entityData.set(DATA_TARGET_POS, pos);
  }

  @Override
  protected void setLevel(Level level) {
    super.setLevel(level);
    if (level instanceof ServerLevel serverLevel) {
      fakePlayer.setServerLevel(serverLevel);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (level().isClientSide) {
      return;
    }
    ServerLevel level = (ServerLevel) level();
    TargetType type = getTargetType();
    BlockPos targetPos = getTargetPosition();
    if (shouldMove()) {
      if (type == TargetType.NONE) {
        BlockPos obstaclePos = findObstaclePosition();
        if (obstaclePos != null) {
          setTarget(TargetType.BREAK, obstaclePos);
          targetProgress = 0;
        }
      } else if (type == TargetType.BREAK) {
        if (workOnDestroyingBlock(level, targetPos)) {
          BlockPos obstaclePos = findObstaclePosition();
          if (obstaclePos != null) {
            setTarget(TargetType.BREAK, obstaclePos);
          } else {
            removeTarget();
          }
        }
      }
    } else {
      BlockPos corner1 = getFrameCorner1();
      BlockPos corner2 = getFrameCorner2();
      if (corner1.equals(corner2)) {
        return;
      }
      if (type == TargetType.NONE) {
        if (remainingTargets.isEmpty()) {
          BlockPos.betweenClosedStream(corner1, corner2)
              .map(BlockPos::new)
              .filter(pos -> {
                BlockState state = level.getBlockState(pos);
                if (framePositions.contains(pos)) {
                  return !state.isAir() && state.canHarvestBlock(level, pos, fakePlayer) && !state.is(FWQBlocks.QUARRY_FRAME.get());
                }
                return !state.isAir() && state.canHarvestBlock(level, pos, fakePlayer);
              })
              .sorted(Comparator.comparing(pos -> pos.getCenter().distanceToSqr(position())))
              .forEach(remainingTargets::add);
          if (remainingTargets.isEmpty()) {
            framePositions.forEach(pos -> {
              BlockState state = level.getBlockState(pos);
              if (!state.is(FWQBlocks.QUARRY_FRAME.get())) {
                remainingTargets.add(pos);
              }
            });
            // TODO Maybe add a cooldown when the drone finishes destroying blocks and building frames
            if (!remainingTargets.isEmpty()) {
              setTarget(TargetType.PLACE, remainingTargets.removeFirst());
            }
          } else {
            setTarget(TargetType.BREAK, remainingTargets.removeFirst());
          }
        } else {
          // shouldn't happen, but not a big deal
          remainingTargets.clear();
        }
      } else if (type == TargetType.BREAK) {
        if (workOnDestroyingBlock(level, targetPos)) {
          if (remainingTargets.isEmpty()) {
            removeTarget();
          } else {
            setTarget(TargetType.BREAK, remainingTargets.removeFirst());
          }
        }
      } else if (type == TargetType.PLACE) {
        if (workOnPlacingFrame(level, targetPos)) {
          if (remainingTargets.isEmpty()) {
            removeTarget();
          } else {
            setTarget(TargetType.PLACE, remainingTargets.removeFirst());
          }
        }
      }
    }
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 10)
        .add(Attributes.FLYING_SPEED, 0.2)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1);
  }

  public enum TargetType {

    NONE,
    BREAK,
    PLACE;

    public final byte byteValue;

    TargetType() {
      byteValue = (byte) ordinal();
    }

    public static TargetType from(byte byteValue) {
      if (byteValue >= 0 && byteValue < values().length) {
        return values()[byteValue];
      }
      return NONE;
    }

  }

}
