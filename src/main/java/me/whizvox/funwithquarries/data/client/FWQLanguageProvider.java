package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.item.DroneDebugToolItem;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import me.whizvox.funwithquarries.common.util.FWQStrings;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class FWQLanguageProvider extends LanguageProvider {

  public FWQLanguageProvider(PackOutput output) {
    super(output, FunWithQuarries.MOD_ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    addItem(FWQItems.DRONE_DEBUG_TOOL, "Drone Debug Tool");
    addBlock(FWQBlocks.QUARRY_CONTROLLER, "Quarry Controller");
    addBlock(FWQBlocks.DRONE_STATION, "Drone Station");
    addBlock(FWQBlocks.QUARRY_FRAME, "Quarry Frame");
    addBlock(FWQBlocks.MACHINE_BLOCK, "Machine Block");
    add(FWQStrings.CREATIVE_TAB_MAIN, "Fun with Quarries");
    add(FWQStrings.MSG_DRONE_DEBUG_CURRENT_MODE, "Current mode: %s");
    add(FWQStrings.MSG_DRONE_DEBUG_CORNER_NOT_SET, "First corner has not been set");
    add(FWQStrings.MSG_DRONE_DEBUG_INVALID_AREA, "Area is too large or too small: %s");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SPAWN), "Spawn Drone");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SET_MOVE_TARGET), "Set Movement Target");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SET_BREAK_TARGET), "Set Break Target");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SET_PLACE_TARGET), "Set Place Target");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.REMOVE_TARGET), "Remove Target");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SET_CORNER_1), "Set First Corner");
    add(FWQStrings.droneDebugToolMode(DroneDebugToolItem.Mode.SET_CORNER_2), "Set Second Corner");
  }

}
