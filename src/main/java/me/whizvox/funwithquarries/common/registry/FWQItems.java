package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.item.DroneDebugToolItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FWQItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FunWithQuarries.MOD_ID);
  private static final List<RegistryObject<? extends Item>> REGISTERED_ITEMS = new ArrayList<>();

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block) {
    var regObj = ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    REGISTERED_ITEMS.add(regObj);
    return regObj;
  }

  private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier) {
    RegistryObject<T> regObj = ITEMS.register(name, supplier);
    REGISTERED_ITEMS.add(regObj);
    return regObj;
  }

  public static Stream<RegistryObject<? extends Item>> registeredItems() {
    // all registry objects are present in DeferredRegister#getEntries, but the insertion order isn't preserved
    return REGISTERED_ITEMS.stream();
  }

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
  }

  public static final RegistryObject<DroneDebugToolItem> DRONE_DEBUG_TOOL = register("drone_debug_tool", DroneDebugToolItem::new);

  public static final RegistryObject<BlockItem>
      QUARRY_CONTROLLER = registerBlockItem(FWQBlocks.QUARRY_CONTROLLER),
      DRONE_STATION = registerBlockItem(FWQBlocks.DRONE_STATION),
      QUARRY_FRAME = registerBlockItem(FWQBlocks.QUARRY_FRAME),
      MACHINE_BLOCK = registerBlockItem(FWQBlocks.MACHINE_BLOCK);

}
