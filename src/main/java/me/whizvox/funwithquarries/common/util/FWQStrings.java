package me.whizvox.funwithquarries.common.util;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.item.DroneDebugToolItem;
import net.minecraft.util.Mth;

import java.util.Arrays;

public class FWQStrings {

  private static String build(String category, String path) {
    return category + "." + FunWithQuarries.MOD_ID + "." + path;
  }

  private static final String[] DRONE_DEBUG_TOOL_MODES;

  static {
    DRONE_DEBUG_TOOL_MODES = Arrays.stream(DroneDebugToolItem.Mode.values()).map(mode -> build("tooltip", "drone_debug_tool.mode." + mode.name().toLowerCase())).toArray(String[]::new);
  }

  public static final String
      CREATIVE_TAB_MAIN = build("itemGroup", "main"),
      MSG_DRONE_DEBUG_CURRENT_MODE = build("message", "drone_debug_tool.current_mode"),
      MSG_DRONE_DEBUG_CORNER_NOT_SET = build("message", "drone_debug_tool.corner_not_set"),
      MSG_DRONE_DEBUG_INVALID_AREA = build("message", "drone_debug_tool.area_too_large");

  public static String droneDebugToolMode(DroneDebugToolItem.Mode mode) {
    return DRONE_DEBUG_TOOL_MODES[Mth.clamp(mode.ordinal(), 0, DRONE_DEBUG_TOOL_MODES.length - 1)];
  }

}
