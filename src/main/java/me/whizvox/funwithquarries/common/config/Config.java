package me.whizvox.funwithquarries.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  private static final ForgeConfigSpec CLIENT_SPEC;

  static {
    var pair = BUILDER.configure(ClientConfig::new);
    CLIENT = pair.getKey();
    CLIENT_SPEC = pair.getValue();
  }

  public static final ClientConfig CLIENT;

  public static void register(ModLoadingContext context) {
    context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
  }

}
