package com.zmanww.bukkit.util;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;

/**
 * @author Zeb
 * 
 */
public class LocationUtil {

    /**
     * Evaluates whether or not the passed material is within the given distance
     * of the location passed.
     * 
     * @param loc
     * @param mat
     * @param blocks
     * @return
     */
    public static boolean isWithinBlocksOf(Location loc, Material mat, int blocks) {
	boolean retVal = false;
	Block baseBlock = loc.getBlock();

	for (int dist = 1; dist <= blocks; dist++) {
	    for (BlockFace face : BlockFace.values()) {
		if (baseBlock.getFace(face, dist).getType() == mat) {
		    return true;
		}
	    }
	}

	return retVal;
    }
    
    /**
     * Returns the location of the nearest material passes, within the given block radius.
     * 
     * @param loc
     * @param mat
     * @param blocks
     * @return
     */
    public static Location locateNearest(Location loc, Material mat, int blockRadius) {
        Location retVal = loc;
        Block baseBlock = loc.getBlock();

        // this search is very expensive, the amount of processing it takes
        // grows exponentially the further it has to search.

        try {
            for (int i = 1; i <= blockRadius; i++) {
                // checks the entire 3x3, 5x5, etc grid only at
                // "top and bottom of the imaginary box"
                for (int y = -i; y <= i; y++) {
                    if (y == -i || y == i) {
                        for (int x = -i; x <= i; x++) {
                            for (int z = -i; z <= i; z++) {
                                if (baseBlock.getRelative(x, y, z).getType().getId()==mat.getId()) {
                                    return baseBlock.getRelative(x, y, z).getLocation();
                                }
                            }
                        }
                    }
                    // checks the other "sides" of the imaginary box
                    if (y != -i && y != i) {
                        for (int x = -i; x <= i; x++) {
                            // checks the x sides of the box
                            if (x == -i || x == i) {
                                for (int z = -i; z <= i; z++) {
                                    if (baseBlock.getRelative(x, y, z).getType().getId()==mat.getId()) {
                                        return baseBlock.getRelative(x, y, z).getLocation();
                                    }
                                }
                            }
                            // checks the z sides of the box
                            if (x != -i || x != i) {
                                int z = -i;
                                if (baseBlock.getRelative(x, y, z).getType().getId()==mat.getId()) {
                                    return baseBlock.getRelative(x, y, z).getLocation();
                                }
                                z = i;
                                if (baseBlock.getRelative(x, y, z).getType().getId()==mat.getId()) {
                                    return baseBlock.getRelative(x, y, z).getLocation();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            retVal = loc;
        }

//	try {
//            for (int y = 0; y <= blockRadius; y++) {
//                for (int x = 0; x <= y; x++) {
//                    for (int z = 0; z <= x; z++) {
//                        if (baseBlock.getRelative(x, y, z).getType().equals(mat)) {
//                            return baseBlock.getRelative(x, y, z).getLocation();
//                        }
//                    }
//                }
//                for (int x = -y; x >= -y; x--) {
//                    for (int z = -x; z >= -x; z--) {
//                        if (baseBlock.getRelative(x, -y, z).getType().equals(mat)) {
//                            return baseBlock.getRelative(x, -y, z).getLocation();
//                        }
//                    }
//                }
//            }
//	} catch (Exception e) {
//	    retVal = loc;
//	}

//	for (int i = 1; i <= blockRadius; i++) {
//	    for (int z = i-1; z <= i; z++) {
//		for (int x = i-1; x <= i; x++) {
//		    for (int y = i-1; y <= i; y++) {
//			if (baseBlock.getRelative(x, y, z).getType().equals(mat)) {
//			    return baseBlock.getRelative(x, y, z).getLocation();
//			}
//		    }
//		}
//	    }
//	    for (int z = i+1; z >= 0-i; z--) {
//		for (int x = i+1; x >= 0-i; x--) {
//		    for (int y = i+1; y >= 0-i; y--) {
//			if (baseBlock.getRelative(x, y, z).getType().equals(mat)) {
//			    return baseBlock.getRelative(x, y, z).getLocation();
//			}
//		    }
//		}
//	    }
//	}

//	for (int dist = 1; dist <= blockRadius; dist++) {
//	    for (BlockFace face : BlockFace.values()) {
//		if (baseBlock.getFace(face, dist).getType() == mat) {
//		    return baseBlock.getFace(face, dist).getLocation();
//		}
//	    }
//	}
	return retVal;
    }

    /**
     * Returns the location of the closest entity to the given location, that is
     * not in a cave!
     * 
     * @param loc
     *            Location to get closest to;
     * @param mobName
     *            Name of Entity must match (ignoring case)
     * @return
     */
    public static Location getClosestMob(Location loc, String mobName) {
	return getClosestMob(loc, mobName, false);
    }

    /**
     * Returns the location of the closest entity to the given location,
     * including or excluding caves.
     * 
     * @param loc
     *            Location to get closest to;
     * @param mobName
     *            Name of Entity must match (ignoring case)
     * @param includeCaves
     *            Specifies whether or not to includes mobs in caves.
     * @return
     */
    public static Location getClosestMob(Location loc, String mobName, boolean includeCaves) {
	Location retVal = loc;
	List<LivingEntity> mobs = loc.getWorld().getLivingEntities();

	double closest = 1000.00;
	for (LivingEntity mob : mobs) {
	    if (isEntityOfType(mob, mobName)) {
		double tmp = distanceBetween(loc, mob.getLocation());
		if (tmp < closest && (includeCaves ? true : !isInCave(mob.getLocation()))) {
		    closest = tmp;
		    retVal = mob.getLocation();
		}
	    }
	}
	return retVal;
    }

    public static boolean isInCave(Location loc) {
	boolean retVal = false;

	int yCord = (int) loc.getY();
	int i = 1;
	while (yCord < 100) {// no caves gonna be above this
	    if (loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.DIRT
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.GRASS
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.IRON_BLOCK
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.COBBLESTONE
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.MOSSY_COBBLESTONE
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.SAND
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.GRAVEL
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.STONE
		    || loc.getBlock().getFace(BlockFace.UP, i).getType() == Material.SANDSTONE) {
		retVal = true;
		yCord = 100;// will stop the loop
	    }
	    i++;
	    yCord++;
	}
	return retVal;
    }

    /**
     * Returns the distance between two Locations.
     * 
     * @param loc1
     * @param loc2
     * @return
     */
    public static double distanceBetween(Location loc1, Location loc2) {
	return Math.sqrt(Math.pow((loc2.getX() - loc1.getBlockX()), 2) 
		+ Math.pow((loc2.getY() - loc1.getBlockY()), 2)
		+ Math.pow((loc2.getZ() - loc1.getBlockZ()), 2));

    }

    private static boolean isEntityOfType(Entity e, String mobName) {
	boolean retVal = false;

	if (mobName.equalsIgnoreCase("Chicken")) {
	    if (e instanceof org.bukkit.entity.Chicken) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Cow")) {
	    if (e instanceof org.bukkit.entity.Cow) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Creeper")) {
	    if (e instanceof org.bukkit.entity.Creeper) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Ghast")) {
	    if (e instanceof org.bukkit.entity.Ghast) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Pig")) {
	    if (e instanceof org.bukkit.entity.Pig) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Sheep")) {
	    if (e instanceof org.bukkit.entity.Sheep) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Skeleton")) {
	    if (e instanceof org.bukkit.entity.Skeleton) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Spider")) {
	    if (e instanceof org.bukkit.entity.Spider) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Zombie")) {
	    if (e instanceof org.bukkit.entity.Zombie) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Squid")) {
	    if (e instanceof org.bukkit.entity.Squid) {
		retVal = true;
	    }
	} else if (mobName.equalsIgnoreCase("Slime")) {
	    if (e instanceof org.bukkit.entity.Slime) {
		retVal = true;
	    }
	}

	return retVal;

    }
}
