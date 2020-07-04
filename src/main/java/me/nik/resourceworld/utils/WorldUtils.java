package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class WorldUtils {

    private static final FileConfiguration config = ResourceWorld.getInstance().getConfig();

    /**
     * @param directory The directory to delete
     * @return Whether or not it has been deleted - exists (Usually never used)
     */
    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null)
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
        }
        return directory.delete();
    }

    /**
     * @return True if the Resource World exists
     */
    public static boolean worldExists() {
        return Bukkit.getWorld(config.getString("world.settings.world_name")) != null;
    }

    /**
     * @return True if the Nether World exists
     */
    public static boolean netherExists() {
        return Bukkit.getWorld(config.getString("nether_world.settings.world_name")) != null;
    }

    /**
     * @return True if the End World exists
     */
    public static boolean endExists() {
        return Bukkit.getWorld(config.getString("end_world.settings.world_name")) != null;
    }
}