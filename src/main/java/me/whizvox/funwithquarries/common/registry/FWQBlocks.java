package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.block.DroneStationBlock;
import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.block.QuarryFrameBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FWQBlocks {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FunWithQuarries.MOD_ID);

  public static final TagKey<Block>
      TAG_QUARRY_COMPONENT = BlockTags.create(new ResourceLocation(FunWithQuarries.MOD_ID, "quarry_component")),
      TAG_MINEABLE = BlockTags.create(new ResourceLocation(FunWithQuarries.MOD_ID, "mineable"));
  public static final BlockBehaviour.Properties MACHINE_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1.5F, 6.0F);

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
  }

  public static final RegistryObject<QuarryControllerBlock> QUARRY_CONTROLLER = BLOCKS.register("quarry_controller", QuarryControllerBlock::new);
  public static final RegistryObject<DroneStationBlock> DRONE_STATION = BLOCKS.register("drone_station", DroneStationBlock::new);
  public static final RegistryObject<QuarryFrameBlock> QUARRY_FRAME = BLOCKS.register("quarry_frame", QuarryFrameBlock::new);
  public static final RegistryObject<Block> MACHINE_BLOCK = BLOCKS.register("machine_block", () -> new Block(MACHINE_PROPERTIES));

}
