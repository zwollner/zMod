/**
 * 
 */
package com.zmanww.mc.zmod;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import com.zmanww.mc.util.LocationUtil;

/**
 * @author Zeb
 * 
 */
public class ZPlayerListener extends PlayerListener {

    private final Zmod plugin;

    public ZPlayerListener(Zmod instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        Player player = event.getPlayer();
        PlayerData playerData = new PlayerData();
        if (plugin.pData.containsKey(player)) {
            playerData = plugin.pData.get(player);
        }
        DecimalFormat oneDForm = new DecimalFormat("#.#");

        if (args.length > 0 && args[0].compareToIgnoreCase("/findmob") == 0) {
            if (args.length > 1) {
                Location mobLoc = LocationUtil.getClosestMob(player.getLocation(), args[1], playerData.isUGSearchOn());
                double dist = LocationUtil.distanceBetween(player.getLocation(), mobLoc);
                if (dist > 2 && dist < 1000) {
                    player.setCompassTarget(mobLoc);
                    player.sendMessage(ChatColor.RED + "Distance to " + args[1] + ": " + ChatColor.WHITE
                            + oneDForm.format(dist) + "blocks");
                    player.sendMessage(ChatColor.GREEN + "Compass set");
                } else {
                    player.sendMessage(ChatColor.RED + "Sorry, no " + args[1] + " found.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "you must specificy a mob to find");
            }
            event.setCancelled(true);
        } else if (args.length > 1 && args[0].compareToIgnoreCase("/settarget") == 0
                && args[1].compareToIgnoreCase("spawn") == 0) {
            event.getPlayer().setCompassTarget(event.getPlayer().getWorld().getSpawnLocation());
            event.getPlayer().sendMessage(ChatColor.GREEN + "Compass set to spawn");
            event.setCancelled(true);
        } else if (args.length > 0 && args[0].compareToIgnoreCase("/getbiome") == 0) {
            player.sendMessage(ChatColor.GREEN + "Your in the "
                    + player.getWorld().getBlockAt(player.getLocation()).getBiome().name() + " biome");
            event.setCancelled(true);
        } else if (args.length > 0 && args[0].compareToIgnoreCase("/ugsearch") == 0) {
            if (args.length > 1 && (args[1].compareToIgnoreCase("ON") == 0 || args[0].compareToIgnoreCase("TRUE") == 0)) {
                playerData.setUGSearchOn(true);
            } else if (args.length > 1
                    && (args[1].compareToIgnoreCase("OFF") == 0 || args[0].compareToIgnoreCase("FALSE") == 0)) {
                playerData.setUGSearchOn(false);
            }
            player.sendMessage(ChatColor.GREEN + "Underground Search is " + (playerData.isUGSearchOn() ? "ON" : "OFF"));
            event.setCancelled(true);
        } else if (args.length > 0 && args[0].compareToIgnoreCase("/findblock") == 0) {
            if (args.length > 1) {
                Material mat = Material.matchMaterial(args[1]);
                if (mat != null) {
                    Location blockLoc = LocationUtil.locateNearest(player.getLocation(), mat, 50);
                    double dist = LocationUtil.distanceBetween(player.getLocation(), blockLoc);
                    if (dist > 1 && dist < 1000) {
                        player.setCompassTarget(blockLoc);
                        player.sendMessage(ChatColor.RED + args[1] + " found at: " + ChatColor.WHITE + "x="
                                + blockLoc.getBlockX() + ",y=" + blockLoc.getBlockY() + ",z=" + blockLoc.getBlockZ()
                                + ChatColor.GREEN + " Compass set");
                        player.sendMessage(ChatColor.RED
                                + "Horizontal: "
                                + ChatColor.WHITE
                                + oneDForm.format(LocationUtil.distanceBetween(player.getLocation(),
                                        new Location(blockLoc.getWorld(), blockLoc.getX(), player.getLocation().getY(),
                                                blockLoc.getZ()))) + " blocks");

                        if (player.getLocation().getY() < blockLoc.getY()) {
                            player.sendMessage(ChatColor.RED + "Up: " + ChatColor.WHITE
                                    + (blockLoc.getBlockY() - player.getLocation().getBlockY()) + " blocks");
                        }

                        if (player.getLocation().getY() > blockLoc.getY()) {
                            player.sendMessage(ChatColor.RED + "Down: " + ChatColor.WHITE
                                    + (player.getLocation().getBlockY() - blockLoc.getBlockY()) + " blocks");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Sorry, no " + args[1] + " found.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid block name!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "you must specificy a block to find");
            }
            event.setCancelled(true);
        }
        playerData = plugin.pData.put(player, playerData);
        super.onPlayerCommandPreprocess(event);
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
