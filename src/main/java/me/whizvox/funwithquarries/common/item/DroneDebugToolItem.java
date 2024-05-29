package me.whizvox.funwithquarries.common.item;

import me.whizvox.funwithquarries.common.entity.Drone;
import me.whizvox.funwithquarries.common.registry.FWQEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DroneDebugToolItem extends Item {

  public DroneDebugToolItem() {
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (!level.isClientSide) {
      ItemStack stack = player.getItemInHand(hand);
      if (!stack.isEmpty()) {
        if (hasDrone(stack)) {
          Drone drone = getDrone(stack, (ServerLevel) level);
          if (drone != null) {
            Vec3 pos = player.getEyePosition().add(player.getLookAngle());
            drone.setTarget(Drone.TargetType.MOVE_DESTINATION, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
          } else {
            drone = FWQEntities.DRONE.get().create(level);
            drone.setPos(player.getEyePosition().add(player.getLookAngle()));
            level.addFreshEntity(drone);
            setDrone(stack, drone);
          }
        } else {
          Drone drone = FWQEntities.DRONE.get().create(level);
          drone.setPos(player.getEyePosition().add(player.getLookAngle()));
          level.addFreshEntity(drone);
          setDrone(stack, drone);
        }
      }
    }
    return super.use(level, player, hand);
  }

  public static final String KEY_DRONE_ID = "DroneId";

  public static void setDrone(ItemStack stack, Drone drone) {
    stack.getOrCreateTag().putUUID(KEY_DRONE_ID, drone.getUUID());
  }

  public boolean hasDrone(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(KEY_DRONE_ID);
  }

  @Nullable
  public static Drone getDrone(ItemStack stack, ServerLevel level) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains(KEY_DRONE_ID)) {
      UUID droneId = tag.getUUID(KEY_DRONE_ID);
      Entity entity = level.getEntities().get(droneId);
      if (entity instanceof Drone drone) {
        return drone;
      }
    }
    return null;
  }

}
