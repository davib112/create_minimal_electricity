package com.davib112.minimal_electricity.blocks.electric_motor;


import com.davib112.minimal_electricity.Config.Config;
import com.davib112.minimal_electricity.ModBlockEntityTypes;
import com.davib112.minimal_electricity.ModShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static java.lang.Math.abs;

public class electric_motor extends DirectionalKineticBlock implements IBE<electric_motor_BlockEntity>
{
    public static final DirectionProperty ATTACHFACING = DirectionProperty.create("attach_facing", Direction.values());

    public electric_motor(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(ATTACHFACING, Direction.DOWN)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ModShapes.ELECTRIC_MOTOR.get(state.getValue(FACING));
    }

    @Override
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
            return defaultBlockState().setValue(FACING, preferred.getOpposite()).setValue(ATTACHFACING, context.getClickedFace().getOpposite());
        }

        return defaultBlockState().setValue(FACING, preferred).setValue(ATTACHFACING, context.getClickedFace().getOpposite());
    }

    // IRotate:

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public boolean hideStressImpact() {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<electric_motor_BlockEntity> getBlockEntityClass() {
        return electric_motor_BlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends electric_motor_BlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.ELECTRIC_MOTOR.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHFACING);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Component.empty());

        Component stressCapacityTitle = Component.translatable("create.tooltip.capacityProvided").withStyle(ChatFormatting.GRAY);
        tooltip.add(stressCapacityTitle);

        String rpmToSu = Config.server().RMP_TO_SU_MOTOR.get().toString() +
                "su * RPM at the cost of: " +
                (int)Config.server().RPM_TO_FE_MOTOR() +
                "fe/t * RPM";

        Component stressCapacity = Component.literal(rpmToSu).withStyle(ChatFormatting.GOLD);
        tooltip.add(stressCapacity);
    }
}
