/**
 * 
 */
package com.zmanww.bukkit.zmod;

import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import com.zmanww.util.EasyRandom;

/**
 * @author Zeb
 * 
 */
public class ZBlockListener extends BlockListener {
    private final Zmod plugin;

    public ZBlockListener(final Zmod plugin) {
	this.plugin = plugin;
    }

    @Override
    public void onLeavesDecay(LeavesDecayEvent event) {
	Block block = event.getBlock();
	World world = block.getWorld();

	EasyRandom rnd = new EasyRandom();
	double stickDropPercent = Double.parseDouble(Zmod.props.getProperty("StickDropPercent"));
	if (rnd.isProbable(stickDropPercent)) {
	    world.dropItem(block.getLocation(), new ItemStack(Material.STICK, 1));
	}
	double appleDropPercent = Double.parseDouble(Zmod.props.getProperty("AppleDropPercent"));
	if (rnd.isProbable(appleDropPercent)) {
	    world.dropItem(block.getLocation(), new ItemStack(Material.APPLE, 1));
	}

	super.onLeavesDecay(event);
    }


    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
	if (event.getBlock().getType() == Material.AIR
		&& event.getBlock().getFace(BlockFace.DOWN, 1).getType() == Material.FENCE
		&& event.getMaterial() == Material.FENCE) {
	    event.setBuildable(true);
	}
	super.onBlockCanBuild(event);
    }


    @Override
    public void onBlockBreak(BlockBreakEvent event) {
	Block block = event.getBlock();
	World world = block.getWorld();
	EasyRandom rnd = new EasyRandom();

	if (block.getType() == Material.LEAVES) {
	    double stickDropPercent = Double.parseDouble(Zmod.props.getProperty("StickDropPercent"));
	    if (rnd.isProbable(stickDropPercent)) {
		world.dropItem(block.getLocation(), new ItemStack(Material.STICK, 1));
	    }
	}
	super.onBlockBreak(event);
    }

}
