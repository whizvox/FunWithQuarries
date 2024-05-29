package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import me.whizvox.funwithquarries.common.util.FWQStrings;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
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
    add(FWQStrings.MAIN_CREATIVE_TAB, "Fun with Quarries");
  }

}
