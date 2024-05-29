package me.whizvox.funwithquarries.common.entity;

import me.whizvox.funwithquarries.common.entity.ai.FlyToMoveTargetGoal;
import me.whizvox.funwithquarries.common.entity.ai.control.DroneFlyingMoveControl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class Drone extends Mob {

  private static final EntityDataAccessor<Byte> DATA_TARGET_TYPE = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BYTE);
  private static final EntityDataAccessor<BlockPos> DATA_TARGET_POS = SynchedEntityData.defineId(Drone.class, EntityDataSerializers.BLOCK_POS);

  public Drone(EntityType<? extends Mob> type, Level level) {
    super(type, level);
    moveControl = new DroneFlyingMoveControl(this);
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(1, new FlyToMoveTargetGoal(this));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    entityData.define(DATA_TARGET_TYPE, TargetType.NONE.byteValue);
    entityData.define(DATA_TARGET_POS, BlockPos.ZERO);
    setNoGravity(true);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    entityData.set(DATA_TARGET_TYPE, tag.getByte("TargetType"));
    int targetPosX = tag.getInt("TargetPosX");
    int targetPosY = tag.getInt("TargetPosY");
    int targetPosZ = tag.getInt("TargetPosZ");
    entityData.set(DATA_TARGET_POS, new BlockPos(targetPosX, targetPosY, targetPosZ));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putByte("TargetType", getTargetType().byteValue);
    BlockPos targetPos = getTargetPosition();
    tag.putInt("TargetPosX", targetPos.getX());
    tag.putInt("TargetPosY", targetPos.getY());
    tag.putInt("TargetPosZ", targetPos.getZ());
  }

  public TargetType getTargetType() {
    return TargetType.from(entityData.get(DATA_TARGET_TYPE));
  }

  public BlockPos getTargetPosition() {
    return entityData.get(DATA_TARGET_POS);
  }

  public void removeTarget() {
    entityData.set(DATA_TARGET_TYPE, TargetType.NONE.byteValue);
  }

  public void setTarget(TargetType type, BlockPos pos) {
    entityData.set(DATA_TARGET_TYPE, type.byteValue);
    entityData.set(DATA_TARGET_POS, pos);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 10)
        .add(Attributes.FLYING_SPEED, 0.2)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1);
  }

  public enum TargetType {

    NONE,
    MOVE,
    BREAK,
    PLACE;

    public final byte byteValue;

    TargetType() {
      byteValue = (byte) ordinal();
    }

    public static TargetType from(byte byteValue) {
      return switch (byteValue) {
        case 1 -> MOVE;
        case 2 -> BREAK;
        case 3 -> PLACE;
        default -> NONE;
      };
    }

  }

}
