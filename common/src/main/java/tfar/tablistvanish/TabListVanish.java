package tfar.tablistvanish;

import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.tablistvanish.platform.Services;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        boolean b = player.hasPermissions(Commands.LEVEL_MODERATORS);
        return b;
    }

    @Nullable
    public static Boolean isBecomingOp;

    public static void sendPacketsOnOP(ServerPlayer changingPlayer,boolean isOp) {
        List<ServerPlayer> list = changingPlayer.server.getPlayerList().getPlayers();

        isBecomingOp = isOp;

        for (ServerPlayer otherPlayer : list) {
                changingPlayer.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(otherPlayer)));
        }

        isBecomingOp = null;
        Services.PLATFORM.refreshTabListName(changingPlayer);
    }

    public static void modifyPacket(ServerPlayer receiving,ClientboundPlayerInfoUpdatePacket packet) {

        boolean showTabListInfo;

        showTabListInfo = Objects.requireNonNullElseGet(isBecomingOp, () -> canSeeTabList(receiving));

        List<ClientboundPlayerInfoUpdatePacket.Entry> entries = packet.entries;

        if (!showTabListInfo) {
            List<ClientboundPlayerInfoUpdatePacket.Entry> entriesCopy = new ArrayList<>(entries);
            packet.entries = new ArrayList<>();//the list is immutable

            for (ClientboundPlayerInfoUpdatePacket.Entry entry : entriesCopy) {
                packet.entries.add(new ClientboundPlayerInfoUpdatePacket.Entry(entry.profileId(),
                        entry.profile(),false,
                        entry.latency(),entry.gameMode(),entry.displayName(),entry.chatSession()));
            }
        }
    }
}
//Tab hide list (In the same mod since it goes hand in hand)
//Server sided mod (if possible) that hides the tab list for all players
//operators can see the tab list, works with story mod as their user will show up as "Lore user (Mc user)"