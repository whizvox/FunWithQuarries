package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.registry.FWQItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class FWQItemModelProvider extends ItemModelProvider {

  public FWQItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, FunWithQuarries.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    getBuilder(ForgeRegistries.ITEMS.getKey(FWQItems.DRONE_DEBUG_TOOL.get()).toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", new ResourceLocation("item/stick"));
  }

}
