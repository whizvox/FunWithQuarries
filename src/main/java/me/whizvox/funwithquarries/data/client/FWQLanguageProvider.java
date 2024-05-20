package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
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
    addBlock(FWQBlocks.QUARRY_CONTROLLER, "Quarry Controller");
    addBlock(FWQBlocks.QUARRY_FRAME, "Quarry Frame");
    add(FWQStrings.MAIN_CREATIVE_TAB, "Fun with Quarries");
  }

}
