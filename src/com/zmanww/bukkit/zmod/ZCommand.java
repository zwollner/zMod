package com.zmanww.bukkit.zmod;

import java.text.DecimalFormat;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zmanww.bukkit.util.LocationUtil;

public class ZCommand implements CommandExecutor {
    
    private final Zmod plugin;

    public ZCommand(Zmod instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean retVal = false;

        if (sender instanceof Player) {
            Player player = (Player)sender;
            PlayerData playerData = new PlayerData();
            if (plugin.pData.containsKey(player)) {
                playerData = plugin.pData.get(player);
            }
            DecimalFormat oneDForm = new DecimalFormat("#.#");

            if (command.getName().equalsIgnoreCase("findmob")) {
                if (args.length > 0) {
                    Location mobLoc = LocationUtil.getClosestMob(player.getLocation(), args[0],
                            playerData.isUGSearchOn());
                    double dist = LocationUtil.distanceBetween(player.getLocation(), mobLoc);
                    if (dist > 2 && dist < 1000) {
                        player.setCompassTarget(mobLoc);
                        player.sendMessage(ChatColor.RED + "Distance to " + args[0] + ": " + ChatColor.WHITE
                                + oneDForm.format(dist) + "blocks");
                        player.sendMessage(ChatColor.GREEN + "Compass set");
                    } else {
                        player.sendMessage(ChatColor.RED + "Sorry, no " + args[0] + " found.");
                    }
                    retVal=true;
                } else {
                    player.sendMessage(ChatColor.RED + "you must specificy a mob to find");
                }
            } else if (command.getName().equalsIgnoreCase("settarget") && args.length > 0 && args[0].compareToIgnoreCase("spawn") == 0) {
                player.setCompassTarget(player.getWorld().getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "Compass set to spawn");
                retVal=true;
            } else if (command.getName().equalsIgnoreCase("getbiome")) {
                player.sendMessage(ChatColor.GREEN + "Your in the "
                        + player.getWorld().getBlockAt(player.getLocation()).getBiome().name() + " biome");
                retVal=true;
            } else if (command.getName().equalsIgnoreCase("ugsearch")) {
                if (args.length > 0 && (args[0].compareToIgnoreCase("ON") == 0 || args[0].compareToIgnoreCase("TRUE") == 0)) {
                    playerData.setUGSearchOn(true);
                } else if (args.length > 0 && (args[0].compareToIgnoreCase("OFF") == 0 || args[0].compareToIgnoreCase("FALSE") == 0)) {
                    playerData.setUGSearchOn(false);
                } else {
                    playerData.setUGSearchOn(playerData.isUGSearchOn() ? false : true);
                }
                player.sendMessage(ChatColor.GREEN + "Underground Search is "
                        + (playerData.isUGSearchOn() ? "ON" : "OFF"));
                retVal=true;
            } else if (command.getName().equalsIgnoreCase("findblock")) {
                if (args.length > 0) {
                    Material mat = Material.matchMaterial(args[0]);
                    if (mat != null) {
                        Location blockLoc = LocationUtil.locateNearest(player.getLocation(), mat, 50);
                        double dist = LocationUtil.distanceBetween(player.getLocation(), blockLoc);
                        if (dist > 1 && dist < 1000) {
                            player.setCompassTarget(blockLoc);
                            player.sendMessage(ChatColor.RED + args[0] + " found at: " + ChatColor.WHITE + "x="
                                    + blockLoc.getBlockX() + ",y=" + blockLoc.getBlockY() + ",z="
                                    + blockLoc.getBlockZ() + ChatColor.GREEN + " Compass set");
                            player.sendMessage(ChatColor.RED
                                    + "Horizontal: "
                                    + ChatColor.WHITE
                                    + oneDForm.format(LocationUtil.distanceBetween(player.getLocation(),
                                            new Location(blockLoc.getWorld(), blockLoc.getX(), player.getLocation()
                                                    .getY(), blockLoc.getZ()))) + " blocks");

                            if (player.getLocation().getY() < blockLoc.getY()) {
                                player.sendMessage(ChatColor.RED + "Up: " + ChatColor.WHITE
                                        + (blockLoc.getBlockY() - player.getLocation().getBlockY()) + " blocks");
                            }

                            if (player.getLocation().getY() > blockLoc.getY()) {
                                player.sendMessage(ChatColor.RED + "Down: " + ChatColor.WHITE
                                        + (player.getLocation().getBlockY() - blockLoc.getBlockY()) + " blocks");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Sorry, no " + args[0] + " found.");
                        }
                        retVal= true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Invalid block name!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "you must specificy a block to find");
                }
            }//End findblock
            playerData = plugin.pData.put(player, playerData);
        }

        return retVal;
    }

}
