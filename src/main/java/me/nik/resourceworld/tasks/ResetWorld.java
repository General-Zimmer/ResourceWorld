package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.discord.Discord;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGenerator;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetWorld extends BukkitRunnable {

    private final ResourceWorld plugin;
    private final ResetTeleport resetTeleport;
    private final WorldGenerator worldGenerator;
    private final WorldCommands worldCommands;

    private final Teleport teleport;

    public ResetWorld(ResourceWorld plugin) {
        this.plugin = plugin;
        this.resetTeleport = new ResetTeleport();
        this.worldGenerator = new WorldGenerator();
        this.worldCommands = new WorldCommands();
        this.teleport = new Teleport();
    }

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        teleport.setResettingWorld(true);
        if (Config.Setting.WORLD_STORE_TIME.getBoolean()) {
            plugin.getData().set("world.millis", System.currentTimeMillis());
        }
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_WORLD.getMessage());
        resetTeleport.resetTP();
        World world = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        try {
            WorldUtils.deleteDirectory(world.getWorldFolder());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        worldGenerator.createWorld();
        worldCommands.worldRunCommands();
        plugin.getServer().broadcastMessage(MsgType.WORLD_HAS_BEEN_RESET.getMessage());
        teleport.setResettingWorld(false);
        plugin.getData().set("world.papi", System.currentTimeMillis());
        plugin.saveData();
        plugin.reloadData();
        if (Config.Setting.SETTINGS_DISCORD_WORLD.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource World has been Reset!", Color.GREEN);
            discord.sendNotification();
        }
    }
}