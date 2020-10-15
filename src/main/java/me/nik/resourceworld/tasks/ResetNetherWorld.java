package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.discord.Discord;
import me.nik.resourceworld.utils.ResetTeleport;
import me.nik.resourceworld.utils.WorldCommands;
import me.nik.resourceworld.utils.WorldGeneratorNether;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetNetherWorld extends BukkitRunnable {

    private final ResourceWorld plugin;
    private final ResetTeleport resetTeleport;
    private final WorldGeneratorNether worldGeneratorNether;
    private final WorldCommands worldCommands;
    private final Teleport teleport;

    public ResetNetherWorld(ResourceWorld plugin) {
        this.plugin = plugin;
        this.resetTeleport = new ResetTeleport();
        this.worldGeneratorNether = new WorldGeneratorNether();
        this.worldCommands = new WorldCommands();
        this.teleport = new Teleport();
    }

    @Override
    public void run() {
        if (!WorldUtils.netherExists()) return;
        teleport.setResettingNether(true);
        if (Config.Setting.NETHER_STORE_TIME.getBoolean()) {
            plugin.getData().set("nether.millis", System.currentTimeMillis());
        }
        plugin.getServer().broadcastMessage(MsgType.RESETTING_THE_NETHER.getMessage());
        resetTeleport.resetNetherTP();
        World world = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
        Bukkit.unloadWorld(world, false);
        Bukkit.getWorlds().remove(world);
        try {
            WorldUtils.deleteDirectory(world.getWorldFolder());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        worldGeneratorNether.createWorld();
        worldCommands.netherRunCommands();
        plugin.getServer().broadcastMessage(MsgType.NETHER_HAS_BEEN_RESET.getMessage());
        teleport.setResettingNether(false);
        plugin.getData().set("nether.papi", System.currentTimeMillis());
        plugin.saveData();
        plugin.reloadData();
        if (Config.Setting.SETTINGS_DISCORD_NETHER.getBoolean()) {
            Discord discord = new Discord("Resource World", "The Resource Nether has been Reset!", Color.RED);
            discord.sendNotification();
        }
    }
}