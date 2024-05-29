package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.block.entity.DroneStationBlockEntity;
import me.whizvox.funwithquarries.common.block.entity.QuarryControllerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Supplier;

public class FWQBlockEntities {

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FunWithQuarries.MOD_ID);

  @SafeVarargs
  private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... validBlocks) {
    return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new)).build(null));
  }

  public static void register(IEventBus bus) {
    BLOCK_ENTITIES.register(bus);
  }

  public static final RegistryObject<BlockEntityType<QuarryControllerBlockEntity>> QUARRY_CONTROLLER = register("quarry_controller", QuarryControllerBlockEntity::new, FWQBlocks.QUARRY_CONTROLLER);
  public static final RegistryObject<BlockEntityType<DroneStationBlockEntity>> DRONE_STATION = register("drone_station", DroneStationBlockEntity::new, FWQBlocks.DRONE_STATION);
}
