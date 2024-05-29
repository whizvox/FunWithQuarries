package me.whizvox.funwithquarries;

import com.mojang.logging.LogUtils;
import me.whizvox.funwithquarries.client.FWQClientEvents;
import me.whizvox.funwithquarries.common.config.Config;
import me.whizvox.funwithquarries.common.entity.Drone;
import me.whizvox.funwithquarries.common.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(FunWithQuarries.MOD_ID)
public class FunWithQuarries {

  public static final String MOD_ID = "funwithquarries";
  private static final Logger LOGGER = LogUtils.getLogger();

  public FunWithQuarries() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    IEventBus forgeBus = MinecraftForge.EVENT_BUS;

    Config.register(ModLoadingContext.get());
    FWQBlocks.register(modBus);
    FWQItems.register(modBus);
    FWQBlockEntities.register(modBus);
    FWQEntities.register(modBus);
    FWQCreativeTab.register(modBus);
    FWQClientEvents.register(modBus, forgeBus);
    modBus.addListener(this::registerGlobalAttributes);
  }

  private void registerGlobalAttributes(EntityAttributeCreationEvent event) {
    event.put(FWQEntities.DRONE.get(), Drone.createAttributes().build());
  }

}
