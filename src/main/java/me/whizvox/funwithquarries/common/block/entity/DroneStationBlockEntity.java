package me.whizvox.funwithquarries.common.block.entity;

import me.whizvox.funwithquarries.common.block.DroneStationBlock;
import me.whizvox.funwithquarries.common.entity.Drone;
import me.whizvox.funwithquarries.common.registry.FWQBlockEntities;
import me.whizvox.funwithquarries.common.registry.FWQEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class DroneStationBlockEntity extends TickableBlockEntity {

  @Nullable
  private QuarryControllerBlockEntity controller;
  @Nullable
  private Drone drone;

  public DroneStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(FWQBlockEntities.DRONE_STATION.get(), pPos, pBlockState);
    controller = null;
    drone = null;
  }

  private void checkForController() {
    if (controller != null && !controller.hasModule(this)) {
      controller = null;
      if (drone != null) {
        drone.remove(Entity.RemovalReason.DISCARDED);
        drone = null;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    if (controller != null) {
      BlockPos pos = controller.getBlockPos();
      tag.putIntArray("ControllerPos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
    }
    if (drone != null) {
      tag.putUUID("DroneId", drone.getUUID());
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    controller = null;
    recallDrone();
    if (tag.contains("ControllerPos")) {
      int[] posArr = tag.getIntArray("ControllerPos");
      BlockPos pos = new BlockPos(posArr[0], posArr[1], posArr[2]);
      if (level.getBlockEntity(pos) instanceof QuarryControllerBlockEntity controller) {
        this.controller = controller;
      }
    }
    if (tag.contains("DroneId")) {
      UUID droneId = tag.getUUID("DroneId");
      if (level instanceof ServerLevel serverLevel && serverLevel.getEntities().get(droneId) instanceof Drone drone) {
        this.drone = drone;
      }
    }
  }

  public boolean hasController() {
    return controller != null;
  }

  public boolean hasDrone() {
    return drone != null;
  }

  public Optional<QuarryControllerBlockEntity> getController() {
    return Optional.ofNullable(controller);
  }

  public Optional<Drone> getDrone() {
    return Optional.ofNullable(drone);
  }

  public void sendOutDrone() {
    if (drone != null || controller == null) {
      return;
    }
    Direction facing = getBlockState().getValue(DroneStationBlock.FACING);
    BlockPos spawnPos = getBlockPos().relative(facing);
    BlockState state = level.getBlockState(spawnPos);
    if (!state.isAir()) {
      level.destroyBlock(spawnPos, true);
    }
    drone = FWQEntities.DRONE.get().create(level);
    drone.setPos(spawnPos.getCenter().add(0, -0.4, 0));
    drone.setFrameBounds(controller.getFrameCorner1(), controller.getFrameCorner2());
  }

  public void recallDrone() {
    if (drone != null) {
      drone.remove(Entity.RemovalReason.DISCARDED);
      drone = null;
    }
  }

  public void setController(@Nullable QuarryControllerBlockEntity controller) {
    this.controller = controller;
    recallDrone();
  }

  public void removeController() {
    setController(null);
  }

  @Override
  public void tick() {
    if (level.getGameTime() % 20 == 0) {
      checkForController();
    }
  }

}
