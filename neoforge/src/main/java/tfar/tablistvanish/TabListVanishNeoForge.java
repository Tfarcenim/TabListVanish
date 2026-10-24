package tfar.tablistvanish;


import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.player.PermissionsChangedEvent;

@Mod(TabListVanish.MOD_ID)
public class TabListVanishNeoForge {

    public TabListVanishNeoForge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        TabListVanish.init();
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW,this::onPermissionUpdate);
    }

    void onPermissionUpdate(PermissionsChangedEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            int oldLevel = event.getOldLevel();
            int newLevel = event.getNewLevel();
            boolean changedVisibility = oldLevel< Commands.LEVEL_MODERATORS ^ newLevel < Commands.LEVEL_MODERATORS;

            if (changedVisibility) {
                TabListVanish.sendPacketsOnOP(serverPlayer,newLevel >= Commands.LEVEL_MODERATORS);
            }
        }
    }
}