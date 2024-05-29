package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FWQEntities {

  private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FunWithQuarries.MOD_ID);

  private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> supplier) {
    return ENTITIES.register(name, () -> supplier.get().build(name));
  }

  public static void register(IEventBus bus) {
    ENTITIES.register(bus);
  }

  public static final RegistryObject<EntityType<Drone>> DRONE = register("drone", () -> EntityType.Builder.of(Drone::new, MobCategory.MISC).fireImmune().sized(0.8f, 0.8f));

}
