package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.util.FWQLang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FWQCreativeTab {

  private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), FunWithQuarries.MOD_ID);

  public static void register(IEventBus bus) {
    TABS.register(bus);
  }

  public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
      .title(FWQLang.CREATIVE_TAB_MAIN)
      .icon(() -> new ItemStack(FWQItems.QUARRY_CONTROLLER.get()))
      .displayItems((params, output) -> FWQItems.registeredItems().forEach(regObj -> output.accept(regObj.get())))
      .build()
  );

}
