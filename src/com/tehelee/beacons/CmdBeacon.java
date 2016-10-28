package com.tehelee.beacons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

public class CmdBeacon implements CommandExecutor
{
	private enum ValidCommand
	{
		spawn,
		merchant,
		info,
		effects,
		effect,
		give,
		giveto,
		set,
		config,
		reload;
		
		public String toString()
		{
			switch(this)
			{
				case spawn:		return "spawn";
				case merchant:	return "merchant";
				case info:		return "info";
				case effects:	return "effects";
				case effect:	return "effect";
				case give:		return "give";
				case giveto:	return "giveto";
				case set:		return "set";
				case config:	return "config";
				case reload:	return "reload";
			}
			return null;
		}

		public static ValidCommand getValue(String value)
		{
			String input = value.toLowerCase();
			
			ValidCommand[] commands = ValidCommand.values();
			
			for (int i = 0; i < commands.length; i++)
			{
				if (input.equals(commands[i].toString())) return commands[i];
			}
			
			return null;
		}
	};
	
	////	////	////	////	////
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{	
		if ((args.length < 1) || isHelpText(args[0]))
		{
			DisplayAvailableCommands(sender);
		}
		else
		{
			ValidCommand command = ValidCommand.getValue(args[0]);
			if (command == null)
			{
				DisplayAvailableCommands(sender);
			}
			else
			{
				boolean isPlayer = (sender instanceof Player); 
				
				switch(command)
				{
					case spawn:
						return spawn(sender, args);
						
					case merchant:
						if (!isPlayer) return false;
						return merchant((Player)sender, args);
						
					case info:
						if (!isPlayer) return false;
						return info((Player)sender);
						
					case effects:
						return effects(sender);
						
					case effect:
						return effect(sender, args);
						
					case give:
						if (!isPlayer) return false;
						return give((Player)sender, args);
						
					case giveto:
						return giveto(sender, args);
						
					case set:
						if (!isPlayer) return false;
						return set((Player)sender, args);
						
					case config:
						return config(sender, args);
					
					case reload:
						return reload(sender);
				}
			}
		}
		
		return true;
	}

	////	////	////	////	////
	
	private boolean spawn(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission("permissions.beacons.spawn")) return false;
		
		int level = 0;
		
		BeaconEffect primary = BeaconEffect.UNASSIGNED;
		BeaconEffect secondary = BeaconEffect.UNASSIGNED;
		
		if ((args.length == 4) && (sender instanceof Player))
		{
			Block target = getPlayerBlockTarget((Player)sender, true);
			
			if (target != null)
			{
				if (!isInteger(args[1]))
				{
					Main.message(sender, HelpText.cmdSpawn);
				}
				else
				{
					level = safeParseInt(args[1]);
					primary = BeaconEffect.parseEffect(args[2]);
					secondary = BeaconEffect.parseEffect(args[3]);
					
					Beacon beacon = Beacon.SpawnBeaconEntity(target.getLocation());
					
					if (beacon != null)
					{
						beacon.setLevels(level);
	
						beacon.setPrimary(primary);
						beacon.setSecondary(secondary);
						
						Main.message(sender, beacon.toString());
					}
					else
					{
						Main.message(sender, HelpText.spawnFailed, true);
					}
				}
			}
			else
			{
				Main.message(sender, "You must be looking at a solid block, or use coordinates.", true);
			}
		}
		else if ((args.length == 7) && (sender instanceof Player))
		{	
			int x, y, z;
			
			Player player = (Player)sender;
			
			Location loc = player.getLocation();
			
			if (args[1].startsWith("~"))
			{
				x = loc.getBlockX();
				
				if (args[1].length() > 2)
				{
					if (args[1].startsWith("~+"))
					{
						String offset = args[1].substring(2, args[1].length());
						x += safeParseInt(offset);
					}
					else if (args[1].startsWith("~-"))
					{
						String offset = args[1].substring(2, args[1].length());
						x -= safeParseInt(offset);
					}
				}
			}
			else if (isInteger(args[1]))
			{
				x = safeParseInt(args[1]);
			}
			else
			{
				Main.message(sender, HelpText.cmdSpawnAt);
				return true;
			}
			
			if (args[2].startsWith("~"))
			{
				y = loc.getBlockY();
				
				if (args[2].length() > 2)
				{
					if (args[2].startsWith("~+"))
					{
						String offset = args[2].substring(2, args[2].length());
						y += safeParseInt(offset);
					}
					else if (args[3].startsWith("~-"))
					{
						String offset = args[2].substring(2, args[2].length());
						y -= safeParseInt(offset);
					}
				}
			}
			else if (isInteger(args[2]))
			{
				y = safeParseInt(args[2]);
			}
			else
			{
				Main.message(sender, HelpText.cmdSpawnAt);
				return true;
			}
			
			if (args[3].startsWith("~"))
			{
				z = loc.getBlockZ();
				
				if (args[3].length() > 2)
				{
					if (args[3].startsWith("~+"))
					{
						String offset = args[3].substring(2, args[3].length());
						z += safeParseInt(offset);
					}
					else if (args[3].startsWith("~-"))
					{
						String offset = args[3].substring(2, args[3].length());
						z -= safeParseInt(offset);
					}
				}
			}
			else if (isInteger(args[3]))
			{
				z = safeParseInt(args[3]);
			}
			else
			{
				Main.message(sender, HelpText.cmdSpawnAt);
				return true;
			}
			
			if (isInteger(args[4]))
			{
				level = safeParseInt(args[4]);
			}
			else
			{
				Main.message(sender, HelpText.cmdSpawnAt);
				return true;
			}
			primary = BeaconEffect.parseEffect(args[5]);
			secondary = BeaconEffect.parseEffect(args[6]);
			
			Beacon beacon = Beacon.SpawnBeaconEntity(new Location(player.getWorld(), x, y, z));
			
			if (beacon != null)
			{
				beacon.setLevels(level);

				beacon.setPrimary(primary);
				beacon.setSecondary(secondary);
				
				Main.message(sender, beacon.toString());
			}
			else
			{
				Main.message(sender, HelpText.spawnFailed, true);
			}
		}
		else if (args.length == 8)
		{
			World world = Bukkit.getWorld(args[1]);
			
			if (world == null)
			{
				Main.message(sender, "\""+args[1]+"\" is not a valid world.", true);
			}
			else
			{
				if (isInteger(args[2]) && isInteger(args[3]) && isInteger(args[4]) && isInteger(args[5]))
				{
					int x = safeParseInt(args[2]);
					int y = safeParseInt(args[3]);
					int z = safeParseInt(args[4]);
					level = safeParseInt(args[5]);
					primary = BeaconEffect.parseEffect(args[6]);
					secondary = BeaconEffect.parseEffect(args[7]);
					
					Beacon beacon = Beacon.SpawnBeaconEntity(new Location(world, x, y, z));
					
					if (beacon != null)
					{
						beacon.setLevels(level);
	
						beacon.setPrimary(primary);
						beacon.setSecondary(secondary);
						
						Main.message(sender, beacon.toString());
					}
					else
					{
						Main.message(sender, HelpText.spawnFailed, true);
					}
				}
				else
				{
					Main.message(sender, HelpText.cmdSpawnAtWorld);
				}
			}
		}
		else
		{
			if (sender instanceof Player)
			{
				Main.message(sender, HelpText.cmdSpawnAt);
			}
			else
			{
				Main.message(sender, HelpText.cmdSpawnAtWorld);
			}
		}
		
		return true;
	}
	
	private boolean merchant(Player player, String[] args)
	{
		if (!player.hasPermission("permissions.beacons.merchant")) return false;
		
		Location location = getPlayerBlockTarget(player, true).getLocation();
		
		location = new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5);
		
		Villager v = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		
		String name = "";
		
		for (int i = 1; i < args.length; i++)
		{
			name += args[i] + ((i == args.length-1) ? "" : " ");
		}
		
		v.setCustomName(name.isEmpty() ? "Beacon Bro" : name);
		v.setCustomNameVisible(true);
		
		v.setProfession(Profession.PRIEST);
		v.setCanPickupItems(false);
		v.setSilent(true);
		v.setInvulnerable(true);
		v.setBreed(false);
		
		v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 100, false, false));
		
		v.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
		v.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
		v.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100000);
		
		BeaconEffect[] effects = BeaconEffect.values();
		
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		
		for (int i = 1; i < effects.length; i++)
		{
			if (effects[i].isEnabled() && effects[i].isCustomEffect())
			{
				MerchantRecipe recipe = new MerchantRecipe(Beacon.CreateBeaconItem(effects[i], BeaconEffect.UNASSIGNED), 0, 10000000, false);
				
				int cost = Math.min(effects[i].getCost(), 128);
				
				if (cost <= 64)
				{
					recipe.addIngredient(new ItemStack(Material.DRAGON_EGG, cost));
				}
				else
				{
					recipe.addIngredient(new ItemStack(Material.DRAGON_EGG, 64));
					recipe.addIngredient(new ItemStack(Material.DRAGON_EGG, cost-64));
				}
				
				recipes.add(recipe);
			}
		}
		
		v.setRecipes(recipes);
		
		if (name.isEmpty())
		{
			Main.message(null, "A beacon merchant has been spawned at ("+location.getX()+", "+location.getY()+", "+location.getZ()+") in "+location.getWorld().getName());
		}
		else
		{
			Main.message(null, "The beacon merchant \""+name+"\" has been spawned at ("+location.getX()+", "+location.getY()+", "+location.getZ()+") in "+location.getWorld().getName());
		}
		
		return true;
	}
	
	private boolean info(Player player)
	{
		if (!player.hasPermission("permissions.beacons.info")) return false;
		
		Beacon beacon = getPlayerBeaconTarget(player);
		
		if (beacon != null)
		{
			Main.message(player, beacon.toString());
		}
		else
		{
			Main.message(player, HelpText.noTarget, true);
		}
		
		return true;
	}
	
	private boolean effects(CommandSender sender)
	{
		if (!sender.hasPermission("permissions.beacons.set")) return false;
		
		Main.message(sender, BeaconEffect.getPrintout(!(sender instanceof Player)));
		
		return true;
	}
	
	private boolean effect(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission("permissions.beacons.set")) return false;
		
		if ((args.length < 2) || isHelpText(args[1]))
		{
			Main.message(sender, HelpText.cmdEffect);
		}
		else
		{
			BeaconEffect effect = BeaconEffect.parseEffect(args[1]);
			
			if (effect == BeaconEffect.UNASSIGNED)
			{
				Main.message(sender, "\""+args[1]+"\" is not a valid effect, for a list of available effects type /beacon effects");
			}
			else
			{
				Main.message(sender, effect.toString() + ": " + BeaconEffect.getDescription(effect));
			}
		}
		
		return true;
	}
	
	private boolean give(Player player, String[] args)
	{
		if (!player.hasPermission("permissions.beacons.give")) return false;
		
		if (!((args.length == 2) || (args.length == 3)) || isHelpText(args[1]))
		{
			Main.message(player, HelpText.cmdGive2);
		}
		else if (args.length == 2)
		{
			BeaconEffect e = BeaconEffect.parseEffect(args[1]);
			
			if (e == BeaconEffect.UNASSIGNED)
			{
				Main.message(player, HelpText.cmdGive1);
			}
			else
			{
				ItemStack item = Beacon.CreateBeaconItem(e, BeaconEffect.UNASSIGNED);
				
				player.getInventory().addItem(item);
				
				Main.message(player, "You've recieved a "+e.getName()+" Beacon", true);
			}
		}
		else if (args.length == 3)
		{
			BeaconEffect e0 = BeaconEffect.parseEffect(args[1]);
			BeaconEffect e1 = BeaconEffect.parseEffect(args[2]);
			
			if ((e0 == BeaconEffect.UNASSIGNED) || (e1 == BeaconEffect.UNASSIGNED))
			{
				Main.message(player, HelpText.cmdGiveTo2);
			}
			else
			{
				if (args[2].equals("2") || args[2].equals("II")) e1 = e0;
				
				ItemStack item = Beacon.CreateBeaconItem(e0, e1);
				
				player.getInventory().addItem(item);
				
				if (e0 == e1)
				{
					Main.message(player, "You've recieved a "+e0.getName()+" II Beacon", true);
				}
				else
				{
					Main.message(player, "You've recieved a "+e0.getName()+" & "+e1.getName()+" Beacon", true);
				}
			}
		}
		
		return true;
	}
	
	private boolean giveto(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission("permissions.beacons.give")) return false;
		
		String playerName = args[1];
		
		Player player = Main.server.getPlayer(playerName);
		
		if (player == null)
		{
			Main.message(sender, "Could not find player: "+playerName, true);
		}
		else if (!((args.length == 3) || (args.length == 4)) || isHelpText(args[1]))
		{
			Main.message(sender, HelpText.cmdGiveTo2);
		}
		else if (args.length == 3)
		{
			BeaconEffect e = BeaconEffect.parseEffect(args[2]);
			
			if (e == BeaconEffect.UNASSIGNED)
			{
				Main.message(sender, HelpText.cmdGiveTo1);
			}
			else
			{
				ItemStack item = Beacon.CreateBeaconItem(e, BeaconEffect.UNASSIGNED);
				
				player.getInventory().addItem(item);
				
				Main.message(sender, "Gave "+playerName+" a "+e.getName()+" Beacon", true);
			}
		}
		else if (args.length == 4)
		{
			BeaconEffect e0 = BeaconEffect.parseEffect(args[2]);
			BeaconEffect e1 = BeaconEffect.parseEffect(args[3]);
			
			if ((e0 == BeaconEffect.UNASSIGNED) || (e1 == BeaconEffect.UNASSIGNED))
			{
				Main.message(sender, HelpText.cmdGiveTo2);
			}
			else
			{
				if (args[2].equals("2") || args[2].equals("II")) e1 = e0;
				
				ItemStack item = Beacon.CreateBeaconItem(e0, e1);
				
				player.getInventory().addItem(item);
				
				if (e0 == e1)
				{
					Main.message(player, "You've recieved a "+e0.getName()+" II Beacon", true);
					Main.message(sender, "Gave "+playerName+" a "+e0.getName()+" II Beacon", true);
				}
				else
				{
					Main.message(player, "You've recieved a "+e0.getName()+" & "+e1.getName()+" Beacon", true);
					Main.message(sender, "Gave "+playerName+" a "+e0.getName()+" & "+e1.getName()+" Beacon", true);
				}
			}
		}
		
		return true;
	}
	
	private boolean set(Player player, String[] args)
	{
		if (!player.hasPermission("permissions.beacons.set")) return false;
		
		Beacon beacon = getPlayerBeaconTarget(player);
		
		if (beacon == null)
		{
			Main.message(player, HelpText.noTarget, true);
		}
		else
		{
			if ((args.length < 3) || isHelpText(args[1]))
			{
				Main.message(player, HelpText.cmdSet);
			}
			else
			{	
				if (args[1].equalsIgnoreCase("level"))
				{
					if (isInteger(args[2]))
					{
						beacon.setLevels(safeParseInt(args[2]));
						Main.message(player, "Changed beacon level to: "+beacon.getLevels(), true);
					}
					else
					{
						Main.message(player, HelpText.cmdSet);
					}
				}
				else if (args[1].equalsIgnoreCase("primary"))
				{
					beacon.setPrimary(BeaconEffect.parseEffect(args[2]));
					
					Main.message(player, "Changed primary beacon effect to: "+beacon.getPrimary().toString(), true);
				}
				else if (args[1].equalsIgnoreCase("secondary"))
				{
					beacon.setSecondary(BeaconEffect.parseEffect(args[2]));
					
					Main.message(player, "Changed secondary beacon effect to: "+beacon.getSecondary().toString(), true);
				}
				else
				{
					Main.message(player, "\""+args[1]+"\" is not valid.", true);
					return false;
				}
				
				Main.writeConfig();
			}
		}
		
		return true;
	}
	
	private boolean config(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission("permissions.beacons.config")) return false;
		
		if (!((args.length == 3) || (args.length == 4)) || isHelpText(args[1]))
		{
			Main.message(sender, HelpText.cmdConfig);
		}
		else
		{
			BeaconEffect effect = BeaconEffect.parseEffect(args[1]);
			
			if (effect == BeaconEffect.UNASSIGNED)
			{
				Main.message(sender, HelpText.cmdConfig);
			}
			else
			{
				int bool = parseBool(args[2]);
				
				if (bool >= 0)
				{
					effect.configEnable(bool == 1);
					
					Main.message(sender, ((bool == 1) ? "Enabled " : "Disabled ")+effect.getName()+" Beacon", true);
				}
				else if ((args.length == 4) && (args[2].equalsIgnoreCase("cost") || args[2].equalsIgnoreCase("price")))
				{
					if (isInteger(args[3]))
					{
						effect.configCost(safeParseInt(args[3]));
						
						Main.message(sender, "Changed "+effect.getName()+"'s cost to "+effect.getCost(), true);
					}
					else
					{
						Main.message(sender, HelpText.cmdConfig);
					}
				}
				else
				{
					Main.message(sender, HelpText.cmdConfig);
				}
			}
		}
		
		return true;
	}
	
	private boolean reload(CommandSender sender)
	{
		if (!sender.hasPermission("permissions.beacons.reload")) return false;
		
		Main.reload();
		
		if (sender instanceof Player)
		{
			Main.message(sender, HelpText.logRestart, true);
		}
		
		return true;
	}
	
	////	////	////	////	////
	
	private void DisplayAvailableCommands(CommandSender sender)
	{
		Main.message(sender, "Alias: /bcn");
		
		boolean spawn = sender.hasPermission("permissions.beacons.spawn");
		boolean info = sender.hasPermission("permissions.beacons.info");
		boolean set = sender.hasPermission("permissions.beacons.set");
		
		if (sender instanceof Player)
		{
			if (spawn)
			{
				Main.message(sender, HelpText.cmdSpawn);
			}
			if (sender.hasPermission("premissions.beacons.merchant"))
			{
				Main.message(sender, HelpText.cmdMerchant);
			}
			if (info)
			{
				Main.message(sender, HelpText.cmdInfo);
			}
			if (set)
			{
				Main.message(sender, HelpText.cmdSet);
			}
		}
		
		if (spawn || info || set)
		{
			Main.message(sender, HelpText.cmdEffects);
			Main.message(sender, HelpText.cmdEffect);
		}

		if (sender.hasPermission("permissions.beacons.config"))
		{
			Main.message(sender, HelpText.cmdConfig);
		}
		
		if (sender.hasPermission("permissions.beacons.reload"))
		{
			Main.message(sender, HelpText.cmdReload);
		}
	}
	
	////	////	////	////	////
	
	private boolean isHelpText(String value)
	{
		return (value.isEmpty() || value.equals("?") || value.equalsIgnoreCase("help"));
	}
	
	private int parseBool(String value)
	{
		if ((null != value) && !value.isEmpty())
		{
			if (isHelpText(value))
				return -2;
			else if (value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("enable") || value.equalsIgnoreCase("enabled"))
				return 1;
			else if (value.equalsIgnoreCase("off") || value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("0") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n") || value.equalsIgnoreCase("disable") || value.equalsIgnoreCase("disabled"))
				return 0;
		}
		
		return -1;
	}
	
	private boolean isInteger(String value)
	{
		if (value.trim().isEmpty()) return false;
		
		boolean result = false;
		
		try
		{
			Integer.parseInt(value);
			result = true;
		}
		catch (NumberFormatException ex) {}
		
		return result;
	}
	
	private int safeParseInt(String value)
	{
		if (value.trim().isEmpty()) return 0;
		
		int result = 0;
		try
		{
			result = Integer.parseInt(value);
		}
		catch (NumberFormatException ex) {}
		
		return result;
	}
	
	////	////	////	////	////
	
	private Beacon getPlayerBeaconTarget(Player player)
	{
		Block target = getPlayerBlockTarget(player, false);
		
		if (target != null)
		{
			Beacon beacon = Beacon.GetFromBlock(target);
			
			if (beacon != null)
			{
				return beacon;
			}
		}
		
		return null;
	}
	
	public Block getPlayerBlockTarget(Player player, boolean getLastAirBlock)
	{
		BlockIterator iter = new BlockIterator(player, 10);
		
		Block lastBlock = null, nextBlock = null;
		
		while (iter.hasNext())
		{
			lastBlock = nextBlock;
			nextBlock = iter.next();
			
			if (nextBlock.getType() != Material.AIR)
			{
				if (getLastAirBlock)
				{
					if (lastBlock == null)
					{
						return player.getLocation().getBlock();
					}
					else
					{
						return lastBlock;
					}
				}
				else
				{
					return nextBlock;
				}
			}
		}
		
		return null;
	}
}