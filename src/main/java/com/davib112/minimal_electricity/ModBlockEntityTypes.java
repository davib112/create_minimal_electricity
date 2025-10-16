package com.davib112.minimal_electricity;

import com.davib112.minimal_electricity.blocks.electric_generator.electric_generator_BlockEntity;
import com.davib112.minimal_electricity.blocks.electric_generator.electric_generator_Renderer;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor_BlockEntity;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor_Renderer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class ModBlockEntityTypes {

    //public static final PartialModel GENERATOR_SHAFT = PartialModel.of(CreateMinimalElectricity.asResource("block/electric_generator_shaft"));
    public static final BlockEntityEntry<electric_motor_BlockEntity> ELECTRIC_MOTOR = CreateMinimalElectricity.REGISTRATE
            .blockEntity("electric_motor", electric_motor_BlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
            .validBlocks(ModBlocks.ELECTRIC_MOTOR)
            .renderer(() -> electric_motor_Renderer::new)
            .register();

    public static final BlockEntityEntry<electric_generator_BlockEntity> ELECTRIC_GENERATOR = CreateMinimalElectricity.REGISTRATE
            .blockEntity("electric_generator", electric_generator_BlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(PartialModel.of(CreateMinimalElectricity.asResource("block/electric_generator_shaft"))), false)
            .validBlocks(ModBlocks.ELECTRIC_GENERATOR)
            .renderer(() -> electric_generator_Renderer::new)
            .register();

    public static void register() {
        // This method is intentionally left blank. Its purpose is to ensure the class is loaded and static initializers are run.
    }
}
