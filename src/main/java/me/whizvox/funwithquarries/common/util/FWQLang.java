package me.whizvox.funwithquarries.common.util;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.item.DroneDebugToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Arrays;

public class FWQLang {

  public static class Keys {

    private static String build(String category, String path) {
      return category + "." + FunWithQuarries.MOD_ID + "." + path;
    }

    private static final String[] DRONE_DEBUG_TOOL_MODES;

    static {
      DRONE_DEBUG_TOOL_MODES = Arrays.stream(DroneDebugToolItem.Mode.values()).map(mode -> build("tooltip", "drone_debug_tool.mode." + mode.name().toLowerCase())).toArray(String[]::new);
    }

    public static final String
        CREATIVE_TAB_MAIN = build("itemGroup", "main"),
        CONTAINER_QUARRY_CONTROLLER = build("container", "quarry_controller"),
        GUI_CONTROLLER_ON = build("gui", "quarry_controller.turn_on"),
        GUI_CONTROLLER_OFF = build("gui", "quarry_controller.turn_off"),
        MSG_DRONE_DEBUG_CURRENT_MODE = build("message", "drone_debug_tool.current_mode"),
        MSG_DRONE_DEBUG_CORNER_NOT_SET = build("message", "drone_debug_tool.corner_not_set"),
        MSG_DRONE_DEBUG_INVALID_AREA = build("message", "drone_debug_tool.area_too_large");

    public static String droneDebugToolMode(DroneDebugToolItem.Mode mode) {
      return DRONE_DEBUG_TOOL_MODES[Mth.clamp(mode.ordinal(), 0, DRONE_DEBUG_TOOL_MODES.length - 1)];
    }

  }

  private static Component create(String key) {
    return Component.translatable(key);
  }

  public static final Component
      CREATIVE_TAB_MAIN = create(Keys.CREATIVE_TAB_MAIN),
      CONTAINER_QUARRY_CONTROLLER = create(Keys.CONTAINER_QUARRY_CONTROLLER),
      GUI_CONTROLLER_ON = create(Keys.GUI_CONTROLLER_ON),
      GUI_CONTROLLER_OFF = create(Keys.GUI_CONTROLLER_OFF),
      MSG_DRONE_DEBUG_CORNER_NOT_SET = create(Keys.MSG_DRONE_DEBUG_CORNER_NOT_SET);

  public static Component messageDroneDebugCurrentMode(DroneDebugToolItem.Mode mode) {
    return Component.translatable(Keys.MSG_DRONE_DEBUG_CURRENT_MODE, Component.translatable(Keys.droneDebugToolMode(mode)).withStyle(ChatFormatting.AQUA));
  }

  public static Component messageDroneDebugInvalidArea(int area) {
    return Component.translatable(Keys.MSG_DRONE_DEBUG_INVALID_AREA, Component.literal(area + "").withStyle(ChatFormatting.AQUA));
  }

}
