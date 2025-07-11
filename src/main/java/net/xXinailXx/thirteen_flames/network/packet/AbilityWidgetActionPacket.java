package net.xXinailXx.thirteen_flames.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class AbilityWidgetActionPacket implements IPacket {
    private String ability;
    private int action;
    private boolean value;
    private int level;

    public AbilityWidgetActionPacket(String ability, int action, boolean value, int level) {
        this.ability = ability;
        this.action = action;
        this.value = value;
        this.level = level;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.ability);
        buf.writeInt(this.action);
        buf.writeBoolean(this.value);
        buf.writeInt(this.level);
    }

    public void read(FriendlyByteBuf buf) {
        this.ability = buf.readUtf();
        this.action = buf.readInt();
        this.value = buf.readBoolean();
        this.level = buf.readInt();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();

        if (player == null)
            return;

        IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

        switch (this.action) {
            case 0:
                data.setBuyAbility(player, this.ability, this.value);
                break;
            case 1:
                data.setActiveAbility(player, this.ability, this.value);
                break;
            case 2:
                data.setLevelAbility(player, this.ability, this.level);
                break;
            case 3:
                data.addLevelAbility(player, this.ability, this.level);
        }
    }
}
