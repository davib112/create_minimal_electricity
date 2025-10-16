package com.davib112.minimal_electricity;
import com.davib112.minimal_electricity.Config.MEStress;
import com.davib112.minimal_electricity.blocks.electric_generator.electric_generator;
import com.davib112.minimal_electricity.blocks.electric_generator.electric_generator_Generator;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor_BlockEntity;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor_Generator;
import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.simibubi.create.foundation.data.SharedProperties;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {

    public static final BlockEntry<electric_motor> ELECTRIC_MOTOR =
            CreateMinimalElectricity.REGISTRATE.block("electric_motor", electric_motor::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    //.tag(AllBlockTags.SAFE_NBT.tag)
                    .transform(pickaxeOnly())
                    .blockstate(new electric_motor_Generator()::generate)
                    //.transform(MEStress.setCapacity(12.0))   //I have no idea what this does in the create mod
                    .onRegister(BlockStressValues.setGeneratorSpeed(electric_motor_BlockEntity.MAX_SPEED, true))
                    .item()
                    .properties(p -> p.rarity(Rarity.COMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<electric_generator> ELECTRIC_GENERATOR =
            CreateMinimalElectricity.REGISTRATE.block("electric_generator", electric_generator::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new electric_generator_Generator()::generate)
                    //.transform(MEStress.setImpact(12))
                    //.onRegister(BlockStressValues.setGeneratorSpeed(electric_generator_BlockEntity.MAX_SPEED, true))
                    .item()
                    .properties(p -> p.rarity(Rarity.COMMON))
                    .transform(customItemModel())
                    .register();


    public static void register() {
        // This method is intentionally left blank. Its purpose is to ensure the class is loaded and static initializers are run.
    }
}

