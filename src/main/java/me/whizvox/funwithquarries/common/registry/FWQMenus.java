package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.menu.QuarryControllerMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FWQMenus {

  private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FunWithQuarries.MOD_ID);

  private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, MenuType.MenuSupplier<T> constructor) {
    return MENUS.register(name, () -> new MenuType<>(constructor, FeatureFlags.DEFAULT_FLAGS));
  }

  public static void register(IEventBus bus) {
    MENUS.register(bus);
  }

  public static final RegistryObject<MenuType<QuarryControllerMenu>> QUARRY_CONTROLLER = register("quarry_controller", QuarryControllerMenu::new);

}
