package net.xXinailXx.thirteen_flames.utils;

import lombok.Getter;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.IRunnable;
import net.xXinailXx.enderdragonlib.client.particle.IRunnableHandler;
import org.zeith.hammerlib.util.java.tuples.Tuple6;

public enum ParticleMoveRunnableType implements IRunnableHandler {
    SAW_UPGRADE((tickCount, level, posX, posY, posZ, moveX, moveY, moveZ) -> {
        if (tickCount > 0) {
            double mX = moveX;
            double mY = moveY;
            double mZ = moveZ;

            var damp = 1D - tickCount / 10;

            Vec3 pos = new Vec3(posX, posY, posZ);
            Vec3 move = pos.add(pos.x + damp, 0.005, pos.z + damp).normalize();

            return new Tuple6<>(posX, posY, posZ, mX + Math.sin(tickCount) * 0.001, mY + 0.000001F, mZ);
        }

        return new Tuple6<>(posX, posY, posZ, moveX, moveY, moveZ);
    }),
    CUP_FIRE((tickCount, level, posX, posY, posZ, moveX, moveY, moveZ) -> {
        return new Tuple6<>(posX, posY, posZ, 0D, level.getRandom().nextInt(1, 10) * 0.005, 0D);
    });

    @Getter
    private final IRunnable runnable;
    @Getter
    private IRunnable executer;

    ParticleMoveRunnableType(IRunnable runnable) {
        this.runnable = runnable;
    }

    public IRunnable getRunnable() {
        return this.runnable;
    }

    public IRunnableHandler executer(IRunnable executer) {
        this.executer = executer;

        return this;
    }

    public int getOriginal() {
        return this.ordinal();
    }
}
