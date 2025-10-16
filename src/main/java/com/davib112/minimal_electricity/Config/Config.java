package com.davib112.minimal_electricity.Config;

import com.davib112.minimal_electricity.CreateMinimalElectricity;
import com.simibubi.create.api.stress.BlockStressValues;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;


import net.createmod.catnip.config.ConfigBase;
import org.apache.commons.lang3.tuple.Pair;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs




@Mod.EventBusSubscriber(modid = CreateMinimalElectricity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{

    public static ForgeConfigSpec COMMON_SPEC;
    private static CommonConfig COMMON;

    public static ForgeConfigSpec SERVER_SPEC;
    private static ServerConfig SERVER;


    public static CommonConfig common() {
        return COMMON;
    }

    public static ServerConfig server() {
        return SERVER;
    }


    public static void register(ModLoadingContext context, ModContainer container)
    {
        ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
        COMMON = new CommonConfig();
        COMMON.registerAll(commonBuilder);
        COMMON_SPEC = commonBuilder.build();
        context.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);


        ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
        SERVER = new ServerConfig();
        SERVER.registerAll(serverBuilder);
        SERVER_SPEC = serverBuilder.build();
        context.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);


        /*Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        context.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();
        //SERVER = register(ServerConfig::new, ModConfig.Type.SERVER);

        context.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        MEStress stress = SERVER.stressValues;

        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);*/
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == COMMON_SPEC)
        {
            CreateMinimalElectricity.LOGGER.info("Loaded Electric Motor common config file: {}", event.getConfig().getFileName());
        }
    }
}
