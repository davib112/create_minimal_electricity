package com.davib112.minimal_electricity.blocks.electric_generator;

import net.minecraftforge.energy.EnergyStorage;

public class GeneratorElectricStorage extends EnergyStorage {
    public GeneratorElectricStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public int AddEnergy(int inputEnergy) {
        int added = Math.min(inputEnergy, capacity - energy);
        energy += added;
        return added;

    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        // Prevent other mods from inserting energy
        return 0;
    }

    @Override
    public boolean canReceive() {
        return false; // other mods cannot insert energy
    }

    public int getMaxOutput(){
        return maxExtract;
    }
}
