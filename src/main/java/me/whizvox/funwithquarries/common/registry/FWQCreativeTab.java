package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.util.FWQStrings;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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
      .title(Component.translatable(FWQStrings.MAIN_CREATIVE_TAB))
      .icon(() -> new ItemStack(FWQItems.QUARRY_CONTROLLER.get()))
      .displayItems((params, output) -> {
        output.accept(FWQItems.QUARRY_CONTROLLER.get());
        output.accept(FWQItems.QUARRY_FRAME.get());
      })
      .build()
  );

}
