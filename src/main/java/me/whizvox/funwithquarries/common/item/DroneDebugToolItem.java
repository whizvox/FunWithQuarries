package me.whizvox.funwithquarries.common.item;

import me.whizvox.funwithquarries.common.entity.Drone;
import me.whizvox.funwithquarries.common.registry.FWQEntities;
import me.whizvox.funwithquarries.common.util.FWQStrings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DroneDebugToolItem extends Item {

  public DroneDebugToolItem() {
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    Player player = context.getPlayer();
    if (!level.isClientSide) {
      ItemStack stack = context.getItemInHand();
      if (stack.is(this)) {
        if (player != null && player.isShiftKeyDown()) {
          Mode newMode = cycleMode(stack);
          player.displayClientMessage(Component.translatable(FWQStrings.MSG_DRONE_DEBUG_CURRENT_MODE, Component.translatable(FWQStrings.droneDebugToolMode(newMode)).withStyle(ChatFormatting.AQUA)), true);
        } else {
          Mode mode = getMode(stack);
          BlockPos clickPos = context.getClickedPos();
          if (mode == Mode.SPAWN) {
            if (hasDrone(stack)) {
              Drone prevDrone = getDrone(stack, (ServerLevel) level);
              if (prevDrone != null) {
                prevDrone.kill();
              }
            }
            Drone drone = FWQEntities.DRONE.get().create(level);
            drone.setPos(clickPos.relative(context.getClickedFace()).getCenter());
            level.addFreshEntity(drone);
            setDrone(stack, drone);
          } else {
            Drone drone = getDrone(stack, (ServerLevel) level);
            if (drone != null) {
              if (mode == Mode.SET_MOVE_TARGET) {
                drone.setMovePosition(clickPos.relative(context.getClickedFace()));
              } else if (mode == Mode.SET_BREAK_TARGET) {
                drone.setTarget(Drone.TargetType.BREAK, clickPos);
              } else if (mode == Mode.SET_PLACE_TARGET) {
                drone.setTarget(Drone.TargetType.PLACE, clickPos);
              } else if (mode == Mode.REMOVE_TARGET) {
                drone.removeTarget();
              } else if (mode == Mode.SET_CORNER_1) {
                setCorner1(stack, clickPos.relative(context.getClickedFace()));
              } else if (mode == Mode.SET_CORNER_2) {
                BlockPos corner1 = getCorner1(stack);
                if (corner1 == null) {
                  if (player != null) {
                    player.displayClientMessage(Component.translatable(FWQStrings.MSG_DRONE_DEBUG_CORNER_NOT_SET), true);
                  }
                } else {
                  BlockPos corner2 = clickPos.relative(context.getClickedFace());
                  int area = Math.abs((corner2.getX() - corner1.getX()) * (corner2.getY() - corner1.getY()) * (corner2.getZ() - corner1.getZ()));
                  if (area < 27 || area > 16384) { // 32 x 32 x 16 or 64 x 64 x 4
                    if (player != null) {
                      player.displayClientMessage(Component.translatable(FWQStrings.MSG_DRONE_DEBUG_INVALID_AREA, area), true);
                    }
                  } else {
                    drone.setFrameBounds(corner1, corner2);
                  }
                }
              }
            }
          }
        }
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  public static final String
      TAG_DRONE_ID = "DroneId",
      TAG_MODE = "DroneMode",
      TAG_CORNER_1 = "Corner1";

  public boolean hasDrone(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(TAG_DRONE_ID);
  }

  @Nullable
  public static Drone getDrone(ItemStack stack, ServerLevel level) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains(TAG_DRONE_ID)) {
      UUID droneId = tag.getUUID(TAG_DRONE_ID);
      Entity entity = level.getEntities().get(droneId);
      if (entity instanceof Drone drone) {
        return drone;
      }
    }
    return null;
  }

  public static Mode getMode(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains(TAG_MODE)) {
      byte byteValue = tag.getByte(TAG_MODE);
      return Mode.from(byteValue);
    }
    setMode(stack, Mode.SPAWN);
    return Mode.SPAWN;
  }

  public static void setDrone(ItemStack stack, Drone drone) {
    stack.getOrCreateTag().putUUID(TAG_DRONE_ID, drone.getUUID());
  }

  public static void setMode(ItemStack stack, Mode mode) {
    stack.getOrCreateTag().putByte(TAG_MODE, mode.byteValue);
  }

  public static Mode cycleMode(ItemStack stack) {
    Mode mode = getMode(stack);
    if (mode == null) {
      setMode(stack, Mode.SET_MOVE_TARGET);
      return Mode.SET_MOVE_TARGET;
    }
    int nextOrdinal = mode.ordinal() + 1;
    if (nextOrdinal >= Mode.values().length) {
      nextOrdinal = 0;
    }
    Mode nextMode = Mode.values()[nextOrdinal];
    setMode(stack, nextMode);
    return nextMode;
  }

  @Nullable
  public static BlockPos getCorner1(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains(TAG_CORNER_1)) {
      int[] corner1Arr = tag.getIntArray(TAG_CORNER_1);
      return new BlockPos(corner1Arr[0], corner1Arr[1], corner1Arr[2]);
    }
    return null;
  }

  public static void setCorner1(ItemStack stack, BlockPos corner1) {
    stack.getOrCreateTag().putIntArray(TAG_CORNER_1, new int[] { corner1.getX(), corner1.getY(), corner1.getZ() });
  }

  public enum Mode {
    SPAWN,
    SET_MOVE_TARGET,
    SET_BREAK_TARGET,
    SET_PLACE_TARGET,
    REMOVE_TARGET,
    SET_CORNER_1,
    SET_CORNER_2;

    public final byte byteValue;

    Mode() {
      byteValue = (byte) ordinal();
    }

    public static Mode from(byte byteValue) {
      if (byteValue >= 0 && byteValue < values().length) {
        return values()[byteValue];
      }
      return SPAWN;
    }
  }

}
