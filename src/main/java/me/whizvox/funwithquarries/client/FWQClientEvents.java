package me.whizvox.funwithquarries.client;

import me.whizvox.funwithquarries.client.model.DroneModel;
import me.whizvox.funwithquarries.client.model.LaserModel;
import me.whizvox.funwithquarries.client.renderer.DroneRenderer;
import me.whizvox.funwithquarries.client.screen.QuarryControllerScreen;
import me.whizvox.funwithquarries.common.registry.FWQEntities;
import me.whizvox.funwithquarries.common.registry.FWQMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class FWQClientEvents {

  public static void register(IEventBus modBus, IEventBus forgeBus) {
    modBus.addListener(FWQClientEvents::clientSetup);
    modBus.addListener(FWQClientEvents::registerRenderers);
    modBus.addListener(FWQClientEvents::registerLayerDefinitions);
  }

  public static void clientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      MenuScreens.register(FWQMenus.QUARRY_CONTROLLER.get(), QuarryControllerScreen::new);
    });
  }

  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(FWQEntities.DRONE.get(), DroneRenderer::new);
  }

  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(DroneModel.LAYER_LOCATION, DroneModel::createBodyLayer);
    event.registerLayerDefinition(LaserModel.LAYER_LOCATION, LaserModel::createBodyLayer);
  }

}
