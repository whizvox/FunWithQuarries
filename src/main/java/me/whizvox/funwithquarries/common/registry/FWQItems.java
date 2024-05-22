package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FWQItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FunWithQuarries.MOD_ID);

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block) {
    return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
  }

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
  }

  public static final RegistryObject<BlockItem>
      QUARRY_CONTROLLER = registerBlockItem(FWQBlocks.QUARRY_CONTROLLER),
      QUARRY_FRAME = registerBlockItem(FWQBlocks.QUARRY_FRAME),
      MACHINE_BLOCK = registerBlockItem(FWQBlocks.MACHINE_BLOCK);

}
