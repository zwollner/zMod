/**
 * 
 */
package com.zmanww.bukkit.zmod;

import java.util.logging.Level;

import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @author Zeb
 * 
 */
public class ZServerListener extends ServerListener {
    private final Zmod plugin;

    public ZServerListener(Zmod plugin) {
        this.plugin = plugin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bukkit.event.server.ServerListener#onServerCommand(org.bukkit.event
     * .server.ServerCommandEvent)
     */
    @Override
    public void onServerCommand(ServerCommandEvent event) {
        Zmod.logger.log(Level.WARNING, this, event.toString());
        super.onServerCommand(event);
    }
}
