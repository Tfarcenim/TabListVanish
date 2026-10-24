package tfar.tablistvanish;

import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.tablistvanish.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import java.util.List;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TabListVanish {

    public static final String MOD_ID = "tablistvanish";
    public static final String MOD_NAME = "TabListVanish";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
    }

    public static boolean canSeeTabList(ServerPlayer player) {
        return player.hasPermissions(Commands.LEVEL_MODERATORS);
    }

    public static void sendPacketsOnOP(ServerPlayer changingPlayer,boolean isOp) {
        List<ServerPlayer> list = changingPlayer.server.getPlayerList().getPlayers();

        for (ServerPlayer otherPlayer : list) {
                changingPlayer.connection.send(isOp ? ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(otherPlayer)) :
                        new ClientboundPlayerInfoRemovePacket(List.of(otherPlayer.getUUID())));
        }
        Services.PLATFORM.refreshTabListName(changingPlayer);
    }
}
//Tab hide list (In the same mod since it goes hand in hand)
//Server sided mod (if possible) that hides the tab list for all players
//operators can see the tab list, works with story mod as their user will show up as "Lore user (Mc user)"