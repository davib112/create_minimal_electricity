package com.davib112.minimal_electricity.Config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class CommonConfig extends ConfigBase
{
    @Override
    public String getName() {
        return "electric_motor";
    }

    private static <T> T register(Function<ForgeConfigSpec.Builder, Pair<T, ForgeConfigSpec>> constructor, ModConfig.Type type) {
        Pair<T, ForgeConfigSpec> specPair = (Pair<T, ForgeConfigSpec>) new ForgeConfigSpec.Builder().configure(constructor);
        //ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair.getLeft();
    }
}