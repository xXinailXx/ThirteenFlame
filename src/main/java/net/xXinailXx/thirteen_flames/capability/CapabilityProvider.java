package net.xXinailXx.thirteen_flames.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<IData> optionalData = LazyOptional.of(Data::new);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityRegistry.CAPABILITY ? this.optionalData.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.optionalData.orElseThrow(NullPointerException::new).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.optionalData.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
    }

    public static @NotNull IData get(Player player) {
        return player.getCapability(CapabilityRegistry.CAPABILITY).orElse(new Data());
    }
}
