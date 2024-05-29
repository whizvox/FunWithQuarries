package me.whizvox.funwithquarries.common.entity;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.entity.ai.FlyToMoveTargetGoal;
import me.whizvox.funwithquarries.common.entity.ai.control.DroneFlyingMoveControl;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

public class Drone extends Mob {

  private static final EntityDataAccessor<BlockPos> DATA_MOVE_POS = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);
  private static final EntityDataAccessor<Boolean> DATA_SHOULD_MOVE = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Byte> DATA_TARGET_TYPE = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BYTE);
  private static final EntityDataAccessor<BlockPos> DATA_TARGET_POS = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);

  private FakePlayer fakePlayer;
  private float targetProgress;
  private int breakProgress;

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
    breakProgress = 0;
  }

  @Nullable
  private BlockPos findObstaclePosition() {
    Vec3 movePos = getMovePosition().getCenter();
    AABB bb = getBoundingBox();
    Vec3 bbCenter = bb.getCenter();
    Vec3 direction = movePos.subtract(bbCenter).normalize().scale(1.5);
    Vec3[] points = new Vec3[] {
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

  public void setBreakTarget(BlockPos pos) {
    setTarget(TargetType.BREAK, pos);
    targetProgress = 0;
    breakProgress = -1;
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
          setBreakTarget(obstaclePos);
        }
      } else if (type == TargetType.BREAK) {
        BlockState obstacleState = level.getBlockState(targetPos);
        targetProgress += obstacleState.getDestroyProgress(fakePlayer, level, targetPos) * 3;
        breakProgress = (int) (targetProgress * 10);
        level.destroyBlockProgress(fakePlayer.getId(), targetPos, breakProgress);
        if (targetProgress >= 1.0f) {
          level.destroyBlock(targetPos, false);
          BlockPos obstaclePos = findObstaclePosition();
          if (obstaclePos != null) {
            setBreakTarget(obstaclePos);
          } else {
            removeTarget();
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
