package com.davib112.minimal_electricity.blocks.electric_generator;

import com.davib112.minimal_electricity.CreateMinimalElectricity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class electric_generator_Renderer extends KineticBlockEntityRenderer<electric_generator_BlockEntity>
{
    public electric_generator_Renderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(electric_generator_BlockEntity be, BlockState state) {
        CreateMinimalElectricity.LOGGER.info("Getting rotated model from electric_generator_Renderer");
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT, state);
    }
}
