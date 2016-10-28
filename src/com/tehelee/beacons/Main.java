package com.tehelee.beacons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.inventory.ItemStack;

public class Main extends JavaPlugin
{
	public static FileConfiguration config;
	public static Server server;
	public static PluginManager pluginManager;
	public static ConsoleCommandSender console;
	public static BukkitScheduler scheduler;
	
	public static Main instance;
	
	public static List<ItemStack> beaconItems = new ArrayList<ItemStack>();
	
	public Main()
	{
		Main.instance = this;
	}
	
	@Override
	public void onLoad() {
		try {
			ServerInjector.inject();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void onEnable()
	{
		Main.instance = this;
		
		initializeConfig();
		
		Main.server = getServer();
		
		Main.pluginManager = server.getPluginManager();
		
		Main.console = Main.server.getConsoleSender();
		
		Main.scheduler = Main.server.getScheduler();
		
		Main.pluginManager.registerEvents(new BeaconListener(), this);
		
		this.getCommand("Beacon").setExecutor(new CmdBeacon());
		
		message(null, HelpText.logStart, true);
	}
	
	@Override
	public void onDisable()
	{	
		message(null, HelpText.logStop, true);
	}
	
	public void initializeConfig()
	{
		Main.config = this.getConfig();
		
		Main.config.addDefault("keepSpecialEffectsOnBreak", true);
		
		BeaconEffect.writeConfigDefaults();
		
		Main.config.options().copyDefaults(true);
		
		writeConfig();
	}
	
	public static void writeConfig()
	{
		if (null != instance)
		{
			instance.saveConfig();
			
			instance.reloadConfig();
			
			Main.config = instance.getConfig();
		}
	}
	
	public static void reload()
	{
		final Plugin plugin = (Plugin)instance;
		
		plugin.reloadConfig();
		
		Main.config = plugin.getConfig();
		
		message(null, HelpText.logRestart, true);
	}
	
	public static void message(Player player, String message)
	{
		message((CommandSender)player, message);
	}
	
	public static void message(CommandSender sender, String message)
	{
		message(sender, message, false);
	}
	
	public static void message(CommandSender sender, String message, boolean prefix)
	{
		String fancyMessage;
		
		ChatColor title = ChatColor.LIGHT_PURPLE; //ChatColor.valueOf(Main.config.getString("titleColor"));
		ChatColor bracket = ChatColor.YELLOW; //ChatColor.valueOf(Main.config.getString("bracketColor"));
		
		if (prefix)
			fancyMessage = bracket + "[" + title + HelpText.PluginName + bracket + "]" + ChatColor.WHITE + ": " + message;
		else
			fancyMessage = ChatColor.WHITE + message;
			
		
		if ((null != sender) && (sender instanceof Player))
		{
			sender.sendMessage(fancyMessage);
		}
		else
		{
			Main.console.sendMessage(fancyMessage);
		}
	}
}
