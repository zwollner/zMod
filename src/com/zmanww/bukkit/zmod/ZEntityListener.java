/**
 * 
 */
package com.zmanww.bukkit.zmod;

import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.zmanww.bukkit.util.LocationUtil;

/**
 * @author Zeb
 * 
 */
public class ZEntityListener extends EntityListener {
    private final Zmod plugin;

    public ZEntityListener(Zmod instance) {
	plugin = instance;
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
//	Player p = null;
//	for (Player player : event.getEntity().getWorld().getPlayers()) {
//	    if (player.getName() != null && player.getName().equalsIgnoreCase("zwollner")) {
//		p = player;
//	    }
//	}
//	if (p != null) {
//	    p.sendMessage(event.getCreatureType() + " EntityId= " + event.getEntity().getEntityId());
//	}

	if (LocationUtil.isWithinBlocksOf(event.getLocation(), Material.WOOD, 1)
		|| LocationUtil.isWithinBlocksOf(event.getLocation(), Material.LEAVES, 1)) {
	    event.setCancelled(true);
	}

	super.onCreatureSpawn(event);
    }


}
