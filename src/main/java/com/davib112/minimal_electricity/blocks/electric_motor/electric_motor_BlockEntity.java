package com.davib112.minimal_electricity.blocks.electric_motor;

import com.davib112.minimal_electricity.Config.Config;
import com.davib112.minimal_electricity.CreateMinimalElectricity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import static java.lang.Math.abs;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;


public class electric_motor_BlockEntity extends GeneratingKineticBlockEntity {

    public static final int MAX_SPEED = 256;
    //RPM values
    public static final int DEFAULT_SPEED = 16;

    //FE values
    private EnergyStorage energyStorage = new EnergyStorage(MAX_SPEED * (int)Config.server().RPM_TO_FE_MOTOR() * 2 , (int)(MAX_SPEED * Config.server().RPM_TO_FE_MOTOR() * 1.5), (int)(MAX_SPEED * Config.server().RPM_TO_FE_MOTOR() * 1.5));


    private final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);

    private int actualSpeed = 0;

    private boolean dirty = true;
    protected ScrollValueBehaviour generatedSpeed;

    public electric_motor_BlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    private void dirtyUpdate()
    {
        updateGeneratedRotation();
        dirty = true;
    }

    //KineticBlockEntity override
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = MAX_SPEED;
        generatedSpeed = new KineticScrollValueBehaviour(CreateLang.translateDirect("kinetics.creative_motor.rotation_speed"),
                this, new MotorValueBox());
        generatedSpeed.between(-max, max);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> dirtyUpdate());
        behaviours.add(generatedSpeed);
    }


    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {

        return convertToDirection(actualSpeed, getBlockState().getValue(electric_motor.FACING));
    }

    @Override
    public float calculateAddedStressCapacity() {
        return Config.server().RMP_TO_SU_MOTOR.get();
    }

    //Energy methods
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && (
                side == null || side == getBlockState().getValue(electric_motor.FACING).getOpposite())) {
            return energyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOptional.invalidate();
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        if (overStressed && !dirty)
        {
            //CreateMinimalElectricity.LOGGER.info("Motor is overstressed, stopping");
            return;
        }
        dirty = false;


        int energyUsed = (int)abs(generatedSpeed.getValue() * Config.server().RPM_TO_FE_MOTOR());


        if (energyStorage.extractEnergy(energyUsed, true) >= energyUsed) {
            energyStorage.extractEnergy(energyUsed, false);
            actualSpeed = generatedSpeed.getValue();
            updateGeneratedRotation();
            //CreateMinimalElectricity.LOGGER.info("Rotation speed: " + actualSpeed + " Energy used: " + energyUsed + " Current: " + energyStorage.getEnergyStored());
        }
        else {
            //CreateMinimalElectricity.LOGGER.info("Using to much energy: " + energyUsed + " Current: " + energyStorage.getEnergyStored());
            actualSpeed = 0;
            updateGeneratedRotation();
        }
    }

    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(electric_motor.FACING);
            return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            Direction facing = state.getValue(electric_motor.FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.of(ms)
                    .rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(electric_motor.FACING);
            if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }

    //Client side
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        //super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if (level == null || !level.isClientSide) {
            return false;
        }

        CreateLang.translate("gui.goggles.kinetic_stats")
                .forGoggles(tooltip);

        int currentRPM = generatedSpeed.getValue();

        addStressImpactStats(tooltip, abs(currentRPM * Config.server().RMP_TO_SU_MOTOR.get()));

        addFEImpactStats(tooltip, (int)abs(currentRPM * Config.server().RPM_TO_FE_MOTOR()));

        return true;
    }

    private void addStressImpactStats(List<Component> tooltip, int su) {

        CreateLang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        CreateLang.number(su)
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(CreateLang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
    }

    private void addFEImpactStats(List<Component> tooltip, int fe) {

        CreateLang.translate("gui.goggles.energy.usage")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        CreateLang.number(fe)
                .translate("gui.goggles.fe")
                .style(ChatFormatting.AQUA)
                .space()
                .add(CreateLang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
    }
}