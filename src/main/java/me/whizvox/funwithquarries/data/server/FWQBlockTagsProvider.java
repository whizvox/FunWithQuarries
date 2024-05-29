package me.whizvox.funwithquarries.data.server;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FWQBlockTagsProvider extends BlockTagsProvider {

  public FWQBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, FunWithQuarries.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(FWQBlocks.QUARRY_CONTROLLER.get())
        .add(FWQBlocks.DRONE_STATION.get())
        .add(FWQBlocks.MACHINE_BLOCK.get());
    tag(BlockTags.MINEABLE_WITH_AXE)
        .add(FWQBlocks.QUARRY_FRAME.get());
    //noinspection unchecked
    tag(FWQBlocks.TAG_MINEABLE)
        .addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);
  }

}
