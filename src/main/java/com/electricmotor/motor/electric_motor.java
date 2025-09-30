package com.electricmotor.motor;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod("create_electric_motor")
public class electric_motor
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_electric_motor";

    // Create a Deferred Register to hold Blocks which will all be registered under the "create_electric_motor" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    //Create a Deferred Register to hold Items which will all be registered under the "create_electric_motor" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // Create a Deferred Register to hold CreativeModeTabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // Creates a new Block with the id "create_electric_motor:electric_motor", combining the namespace and path
    public static final RegistryObject<Block> ELECTRIC_MOTOR = BLOCKS.register("electric_motor", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND)));

    //With matching item
    //public static  final RegistryObject<Item> MOTOR_BLOCK_ITEM = ITEMS.register("electric_motor", () -> new Item(new Item.Properties().stacksTo(64)));
    //public static final BlockItem MOTOR = new BlockItem(MOTOR_BLOCK.get(), new Item.Properties());
    public static  final RegistryObject<Item> ELECTRIC_MOTOR_ITEM = ITEMS.register("electric_motor", () -> new BlockItem(ELECTRIC_MOTOR.get(), new Item.Properties()));

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> ELECTRIC_TAB = CREATIVE_MODE_TABS.register("electric_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ELECTRIC_MOTOR_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ELECTRIC_MOTOR_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());



    public electric_motor(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("TESTING A FUNCTION IN THE COMMON SETUP");
    }



    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
        //    event.accept(ELECTRIC_MOTOR_ITEM);
    }

}
