package tfar.tablistvanish.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.tablistvanish.TabListVanish;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
    @Shadow
    @Final
    protected MinecraftServer server;

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void tabListVanish$onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        if ((Object) this instanceof ServerGamePacketListenerImpl listener) {
            ServerPlayer receivingPlayer = listener.player;
            if (packet instanceof ClientboundPlayerInfoUpdatePacket infoPacket) {
                if (!TabListVanish.canSeeTabList(receivingPlayer)) {
                    callbackInfo.cancel();
                }
            }
        }
    }
}
