package me.whizvox.funwithquarries.data;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.data.client.FWQBlockStateProvider;
import me.whizvox.funwithquarries.data.client.FWQLanguageProvider;
import me.whizvox.funwithquarries.data.server.FWQBlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = FunWithQuarries.MOD_ID)
public class FWQDataGenerator {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    PackOutput output = gen.getPackOutput();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    var lookupProvider = event.getLookupProvider();
    boolean includeClient = event.includeClient();
    boolean includeServer = event.includeServer();

    gen.addProvider(includeClient, new FWQLanguageProvider(output));
    gen.addProvider(includeClient, new FWQBlockStateProvider(output, fileHelper));
    gen.addProvider(includeServer, new FWQBlockTagsProvider(output, lookupProvider, fileHelper));
  }

}
