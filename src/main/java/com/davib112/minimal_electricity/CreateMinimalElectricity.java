package com.davib112.minimal_electricity;

import com.davib112.minimal_electricity.Config.Config;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(CreateMinimalElectricity.MOD_ID)
public class CreateMinimalElectricity
{
    public static final String MOD_ID = "create_minimal_electricity";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateMinimalElectricity.MOD_ID);


    static
    {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public CreateMinimalElectricity(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        Config.register(context, context.getContainer());


        REGISTRATE.registerEventListeners(modEventBus);
        ModBlocks.register();
        ModBlockEntityTypes.register();



        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void addCreative(final BuildCreativeModeTabContentsEvent event)
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            //Safety checks
            if(event.getTabKey() == null) return;
            if(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey() == null) return;

            //UGLY check to see if it's the create mods base creative tab
            if(event.getTabKey().compareTo(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) == 0)
            {
                LOGGER.info("Adding client mode tab to: " + event.getTabKey().location());
                event.accept(ModBlocks.ELECTRIC_MOTOR.asItem());
                event.accept(ModBlocks.ELECTRIC_GENERATOR.asItem());
            }
        }
    }

    //May need to be changed for 1.21
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}