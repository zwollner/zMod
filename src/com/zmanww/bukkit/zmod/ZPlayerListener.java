/**
 * 
 */
package com.zmanww.bukkit.zmod;

import java.text.DecimalFormat;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.entity.Wolf;

import com.zmanww.bukkit.util.LocationUtil;

/**
 * @author Zeb
 * 
 */
public class ZPlayerListener extends PlayerListener {

    private final Zmod plugin;

    public ZPlayerListener(Zmod instance) {
        plugin = instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bukkit.event.player.PlayerListener#onPlayerChat(org.bukkit.event.
     * player.PlayerChatEvent)
     */
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Zmod.logger.log(Level.WARNING, this, event.getMessage());
        super.onPlayerChat(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bukkit.event.player.PlayerListener#onPlayerJoin(org.bukkit.event.
     * player.PlayerJoinEvent)
     */
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.pData.containsKey(event.getPlayer())) {
            plugin.pData.put(event.getPlayer(), new PlayerData());
        }
        super.onPlayerJoin(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bukkit.event.player.PlayerListener#onPlayerQuit(org.bukkit.event.
     * player.PlayerQuitEvent)
     */
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.pData.containsKey(event.getPlayer())) {
            plugin.pData.remove(event.getPlayer());
        }
        super.onPlayerQuit(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bukkit.event.player.PlayerListener#onPlayerKick(org.bukkit.event.
     * player.PlayerKickEvent)
     */
    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        if (plugin.pData.containsKey(event.getPlayer())) {
            plugin.pData.remove(event.getPlayer());
        }
        super.onPlayerKick(event);
    }

}
