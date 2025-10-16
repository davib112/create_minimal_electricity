package com.davib112.minimal_electricity.blocks.electric_generator;

import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class electric_generator_Generator extends SpecialBlockStateGen
{
    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(electric_generator.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(electric_generator.FACING)
                .getAxis()
                .isVertical() ? 0 : horizontalAngle(state.getValue(electric_generator.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {

        return AssetLookup.partialBaseModel(ctx, prov);
    }
}
