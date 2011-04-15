package com.zman.bukkit.zmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zman.util.Logger;

public class Zmod extends JavaPlugin {

    protected static final Logger               logger         = new Logger("Minecraft");
    private final ZPlayerListener               playerListener = new ZPlayerListener(this);
    private final ZBlockListener                blockListener  = new ZBlockListener(this);
    private final ZEntityListener               entityListener = new ZEntityListener(this);
    private final HashMap<Player, Boolean>      debugees       = new HashMap<Player, Boolean>();
    protected final HashMap<Player, PlayerData> pData          = new HashMap<Player, PlayerData>();

    public static Properties                    props          = new Properties();

    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a
        // plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check
        // all is well
        System.out.println("Goodbye world!");
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of
        // any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        // PlayerEvents
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
        // pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        // pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
        // pm.registerEvent(Event.Type.INVENTORY_TRANSACTION, playerListener, Priority.Normal, this);

        // Block Events
        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.LEAVES_DECAY, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);

        // Entity Events
        pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Priority.High, this);

        // Load Properties
        loadProperties();

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    private boolean loadProperties() {
        boolean retVal = false;

        try {
            File config = new File(getDataFolder(), "config.properties");
            if (config.exists()) {
                props.load(new FileInputStream(config));
            } else {// Time to create some
                if (getDataFolder().mkdir()) {
                    config = new File(getDataFolder(), "config.properties");
                    props.setProperty("StickDropPercent", "6.25");
                    props.setProperty("AppleDropPercent", "0.5");
                    props.store(new FileOutputStream(config), null);
                } else {// Could not create the directory!!
                    logger.log(Level.SEVERE, this, "Data Folder could not be created!");

                }
            }

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, this, "Properties could not be loaded!");
            e.printStackTrace();
        } catch (IOException e) {
            logger.log(Level.SEVERE, this, "Properties could not be loaded!");
            e.printStackTrace();
        }

        return retVal;
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub

    }

}
