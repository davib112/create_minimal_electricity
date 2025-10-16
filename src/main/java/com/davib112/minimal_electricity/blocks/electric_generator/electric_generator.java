package com.davib112.minimal_electricity.blocks.electric_generator;

import com.davib112.minimal_electricity.Config.Config;
import com.davib112.minimal_electricity.ModBlockEntityTypes;
import com.davib112.minimal_electricity.ModShapes;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor_BlockEntity;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class electric_generator extends DirectionalKineticBlock implements IBE<electric_generator_BlockEntity> {
    public electric_generator(Properties properties) {super(properties);}

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ModShapes.ELECTRIC_GENERATOR.get(state.getValue(FACING));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        //Prefererd facing from standard create method
        Direction preferred = getPreferredFacing(context);


        if(preferred == null)
            preferred = context.getNearestLookingDirection().getOpposite();

        //CreateElectricMotor.LOGGER.info("preferred: " + preferred + " clickedFace: " + context.getClickedFace() + " isShifting: " + context.getPlayer().isShiftKeyDown());

        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()))
        {
            return defaultBlockState().setValue(FACING, preferred.getOpposite());
        }
        //return super.getStateForPlacement(context);

        return defaultBlockState().setValue(FACING, preferred);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING) || face == state.getValue(FACING).getOpposite();
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }
    @Override
    public Class<electric_generator_BlockEntity> getBlockEntityClass() {
        return electric_generator_BlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends electric_generator_BlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.ELECTRIC_GENERATOR.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Component.empty());

        Component stressCapacityTitle = Component.translatable("create.tooltip.stressImpact").withStyle(ChatFormatting.GRAY);
        tooltip.add(stressCapacityTitle);

        //String genString = "Generates: " + (int)Config.server().RPM_TO_FE_GENERATOR() + " at the cost of " + Config.server().RMP_TO_SU_GENERATOR.get() + " SU x RPM";
        String genString =
                Config.server().RMP_TO_SU_GENERATOR.get() +
                "su * RPM generating: " +
                        (int)Config.server().RPM_TO_FE_GENERATOR() +
                        "fe/t * RPM";

        Component stressCapacity = Component.literal(genString).withStyle(ChatFormatting.GOLD);
        tooltip.add(stressCapacity);
    }
}