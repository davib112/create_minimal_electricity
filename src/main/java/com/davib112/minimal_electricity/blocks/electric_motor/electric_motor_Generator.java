package com.davib112.minimal_electricity.blocks.electric_motor;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class electric_motor_Generator extends SpecialBlockStateGen
{
    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(electric_motor.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(electric_motor.FACING)
                .getAxis()
                .isVertical() ? 0 : horizontalAngle(state.getValue(electric_motor.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        Direction facing = state.getValue(electric_motor.FACING);
        Direction attachFacing = state.getValue(electric_motor.ATTACHFACING);

        //Use NoBase model if the motor is facing the same direction as the attachment face or the opposite direction
        if(facing == attachFacing || facing == attachFacing.getOpposite())
        {
            return AssetLookup.partialBaseModel(ctx, prov, "nobase");
        }

        return AssetLookup.partialBaseModel(ctx, prov);
    }
}
