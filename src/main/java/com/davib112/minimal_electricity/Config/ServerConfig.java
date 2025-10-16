package com.davib112.minimal_electricity.Config;

import net.createmod.catnip.config.ConfigBase;

public class ServerConfig extends ConfigBase
{
    //"Scene has to be restarted to apply changes"
    public final ConfigGroup motor = group(0, "Electric Motor", "Changes to these settings require a restart of the game to take effect.");
    public final ConfigFloat SU_TO_FE_MOTOR = f(32, 1, 512, "FE per SU", "⚠RESTART WORLD TO TAKE EFFECT⚠\nFE Price is calculated as SU/this, so higher values make the motor cheaper to run. \nRecommend powers of 2 (1, 2, 4, 8, etc)\n Create Crafts & Additions has the value of 34.133 with some caveats.");
    public final ConfigInt RMP_TO_SU_MOTOR = i(64, 1, 1024, "RMP to SU", "⚠RESTART WORLD TO TAKE EFFECT⚠\nHow many SU is generated per RMP");
    public float RPM_TO_FE_MOTOR() {
        return RMP_TO_SU_MOTOR.get().floatValue() / SU_TO_FE_MOTOR.get().floatValue();
    }


    public final ConfigGroup generator = group(0, "Electric Generator", "Changes to these settings require a restart of the game to take effect.");
    public final ConfigFloat SU_TO_FE_GENERATOR = f(32, 1, 512, "FE per SU", "⚠RESTART WORLD TO TAKE EFFECT⚠\nFE Gain is calculated as SU/this, so higher values gives more FE. \nRecommend powers of 2 (1, 2, 4, 8, etc)\n Create Crafts & Additions has the value of 34.133    1.4 with some caveats.");
    public final ConfigInt RMP_TO_SU_GENERATOR = i(64, 1, 1024, "RMP to SU", "⚠RESTART WORLD TO TAKE EFFECT⚠\nHow many SU it drains per RMP");
    //Create Aditions RPM to FE = 1.4
    //Create Crafts RPM to SU = 64?
    //That means RPM to FE =
    public float RPM_TO_FE_GENERATOR() {
        return RMP_TO_SU_GENERATOR.get().floatValue() / SU_TO_FE_GENERATOR.get().floatValue();
    }

    //public final MEStress stressValues = nested(MEStress::new, "Stress Values");


    @Override
    public String getName() {
        return "electric_motor";
    }
}
