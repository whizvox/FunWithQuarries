package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.item.DroneDebugToolItem;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import me.whizvox.funwithquarries.common.util.FWQLang;
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
    add(FWQLang.Keys.CREATIVE_TAB_MAIN, "Fun with Quarries");
    add(FWQLang.Keys.CONTAINER_QUARRY_CONTROLLER, "Quarry Controller");
    add(FWQLang.Keys.GUI_CONTROLLER_ON, "Power On");
    add(FWQLang.Keys.GUI_CONTROLLER_OFF, "Power Off");
    add(FWQLang.Keys.MSG_DRONE_DEBUG_CURRENT_MODE, "Current mode: %s");
    add(FWQLang.Keys.MSG_DRONE_DEBUG_CORNER_NOT_SET, "First corner has not been set");
    add(FWQLang.Keys.MSG_DRONE_DEBUG_INVALID_AREA, "Area is too large or too small: %s");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SPAWN), "Spawn Drone");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SET_MOVE_TARGET), "Set Movement Target");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SET_BREAK_TARGET), "Set Break Target");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SET_PLACE_TARGET), "Set Place Target");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.REMOVE_TARGET), "Remove Target");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SET_CORNER_1), "Set First Corner");
    add(FWQLang.Keys.droneDebugToolMode(DroneDebugToolItem.Mode.SET_CORNER_2), "Set Second Corner");
  }

}
