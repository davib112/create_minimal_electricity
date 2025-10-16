package com.davib112.minimal_electricity.blocks.electric_generator;

import com.davib112.minimal_electricity.Config.Config;
import com.davib112.minimal_electricity.CreateMinimalElectricity;
import com.davib112.minimal_electricity.blocks.electric_motor.electric_motor;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Math.abs;

public class electric_generator_BlockEntity extends KineticBlockEntity implements ICapabilityProvider {

    public static final int MAX_SPEED = 256;
    private GeneratorElectricStorage energyStorage = new GeneratorElectricStorage(MAX_SPEED * (int) Config.server().RPM_TO_FE_GENERATOR() * 2,(int) (MAX_SPEED * Config.server().RPM_TO_FE_GENERATOR() * 1.5) , (int) (MAX_SPEED * Config.server().RPM_TO_FE_GENERATOR() * 1.5));

    private final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);

    public electric_generator_BlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public void tick() {
        super.tick();
        // Custom behavior for the electric generator can be added here

        if(level == null || level.isClientSide) return;

        if(this.getSpeed() != 0){
            int energyToAdd = (int)abs(this.getSpeed() * Config.server().RPM_TO_FE_GENERATOR());
            int received = energyStorage.AddEnergy(energyToAdd);
            //CreateMinimalElectricity.LOGGER.info("Generating " + received + " FE (Attempted to add " + energyToAdd + " FE) Can extract: " + energyStorage.canExtract() + " | "  + energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored());
        }

        sendOutEnergy();

    }

    private void sendOutEnergy() {
        if (energyStorage.getEnergyStored() <= 0) return;

        // Try all directions *except* the generator's input/output face if you want
        for (Direction dir : Direction.values()) {
            if (dir == getBlockState().getValue(electric_motor.FACING) ||
                    dir == getBlockState().getValue(electric_motor.FACING).getOpposite())
                continue;
            BlockEntity be = level.getBlockEntity(worldPosition.relative(dir));
            if (be == null) continue;

            be.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(handler -> {
                if (handler.canReceive()) {
                    int toSend = Math.min(energyStorage.getEnergyStored(), energyStorage.getMaxOutput());
                    int accepted = handler.receiveEnergy(toSend, false);
                    if (accepted > 0) {
                        energyStorage.extractEnergy(accepted, false);
                        //CreateMinimalElectricity.LOGGER.info("Sent " + accepted + " FE to " + be.getType() + " on side " + dir);
                    }
                }
            });
        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && (
                side == null ||
                (side != getBlockState().getValue(electric_motor.FACING) &&
                side != getBlockState().getValue(electric_motor.FACING).getOpposite())

        )) {
            //CreateMinimalElectricity.LOGGER.info("Providing energy capability on side " + side + " can extract: " + energyStorage.canExtract() + " | "  + energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored());
            return energyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOptional.invalidate();
    }

    @Override
    public float calculateStressApplied() {
        float impact = Config.server().RMP_TO_SU_GENERATOR.get();
        this.lastStressApplied = impact;
        return impact;
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


        addStressImpactStats(tooltip, abs(this.lastStressApplied));

        addFECapacityStats(tooltip, (int)abs(getSpeed() * Config.server().RPM_TO_FE_GENERATOR()));

        return true;
    }

    private void addFECapacityStats(List<Component> tooltip, int fe) {

        CreateLang.translate("gui.goggles.energy.gain")
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
