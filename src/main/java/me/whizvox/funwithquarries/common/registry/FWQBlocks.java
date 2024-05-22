package me.whizvox.funwithquarries.common.registry;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.block.QuarryFrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FWQBlocks {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FunWithQuarries.MOD_ID);

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
  }

  public static final RegistryObject<QuarryControllerBlock> QUARRY_CONTROLLER = BLOCKS.register("quarry_controller", QuarryControllerBlock::new);
  public static final RegistryObject<QuarryFrameBlock> QUARRY_FRAME = BLOCKS.register("quarry_frame", QuarryFrameBlock::new);
  public static final RegistryObject<Block> MACHINE_BLOCK = BLOCKS.register("machine_block", () -> new Block(BlockBehaviour.Properties.copy(QUARRY_CONTROLLER.get())));

}
