package com.tehelee.beacons;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class BeaconListener implements Listener
{
	@EventHandler
	public void onBeaconClick(PlayerInteractEvent e)
	{
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		Player player = e.getPlayer();
		
		Block block = e.getClickedBlock();
		Material mat = block.getType();
		
		if (mat == Material.BEACON)
		{
			if (player.hasPermission("permissions.beacons.debug"))
			{
				Beacon beacon = Beacon.GetFromBlock(block);
				
				if (beacon != null)
				{
					if (beacon.getPrimary().isCustomEffect() || beacon.getSecondary().isCustomEffect())
					{
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBeaconBreak(BlockBreakEvent e)
	{
		Block block = e.getBlock();
		Material mat = block.getType();
		
		if (mat == Material.BEACON)
		{
			Beacon beacon = Beacon.GetFromBlock(block);
			
			ItemStack item;
			
			if ((beacon != null) && beacon.isCustom() && Main.config.getBoolean("keepSpecialEffectsOnBreak", true))
			{
				item = beacon.createItem();
			}
			else
			{
				item = new ItemStack(Material.BEACON, 1);
			}
			
			block.setType(Material.AIR);
			
			block.getWorld().dropItemNaturally(block.getLocation(), item);
		}
	}
	
	@EventHandler
	public void onBeaconPlaced(BlockPlaceEvent e)
	{	
		ItemStack item = e.getItemInHand();
		
		if ((item != null) && (item.getType() == Material.BEACON))
		{
			Block block = e.getBlock();
			
			Beacon beacon = Beacon.GetFromBlock(block);
			
			if ((beacon != null) && (item.getItemMeta().hasDisplayName()))
			{		
				String name = item.getItemMeta().getDisplayName();
				
				String[] split = name.split(" ");
				
				if (split[split.length-1].equals("Beacon"))
				{
					BeaconEffect e0 = BeaconEffect.UNASSIGNED;
					BeaconEffect e1 = BeaconEffect.UNASSIGNED;
					boolean doubleEffect = false;
					
					String parse0 = "", parse1 = "";
					int j = 0;
					for (int i = 0; i < split.length; i++, j++)
					{
						if (split[i].equals("II"))
						{
							doubleEffect = true;
							break;
						}
						if (split[i].equals("&") || split[i].equals("Beacon")) break;
						
						parse0 += split[i] + " ";
					}
					
					parse0 = parse0.trim();
					
					if (!doubleEffect)
					{
						for (int i = j+1; i < split.length; i++)
						{
							if (split[i].equals("&") || split[i].equals("Beacon")) break;
							
							parse1 += split[i] + " ";
						}
						
						parse1 = parse1.trim();
					}
					
					e0 = BeaconEffect.parseEffect(parse0);
					e1 = BeaconEffect.parseEffect(parse1);
					
					if (e0 != BeaconEffect.UNASSIGNED)
					{
						if (doubleEffect)
						{
							beacon.setPrimary(e0);
							beacon.setSecondary(e0);
						}
						else
						{
							beacon.setPrimary(e0);
							
							if (e1 != BeaconEffect.UNASSIGNED)
							{
								beacon.setSecondary(e1);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBeaconPickup(PlayerPickupItemEvent e)
	{
		ItemStack item = e.getItem().getItemStack();
		if (item.getType() == Material.BEACON)
		{
			translateBeaconItem(item);
		}
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e)
	{
		if (e.getInventory().getType() == InventoryType.MERCHANT)
		{
			ItemStack[] items = e.getPlayer().getInventory().getStorageContents();
			for (ItemStack item : items)
			{
				if ((item != null) && (item.getType() == Material.BEACON))
				{
					translateBeaconItem(item);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void translateBeaconItem(ItemStack item)
	{	
		Map<Enchantment, Integer> enchantMap = item.getEnchantments();
		if ((enchantMap != null) && (enchantMap.size() > 0))
		{
			BeaconEffect e0 = BeaconEffect.UNASSIGNED;
			BeaconEffect e1 = BeaconEffect.UNASSIGNED;
			
			int l0 = 0;
			
			Enchantment[] enchants = enchantMap.keySet().toArray(new Enchantment[enchantMap.size()]); 
			
			if (enchants.length == 1)
			{
				e0 = BeaconEffect.fromInteger(enchants[0].getId());
				l0 = enchantMap.get(enchants[0]);
			}
			else
			{
				e0 = BeaconEffect.fromInteger(enchants[0].getId());
				l0 = enchantMap.get(enchants[0]);
				e1 = BeaconEffect.fromInteger(enchants[1].getId());
			}
			
			if (l0 == 1) e1 = e0;
			
			ItemStack beaconItem = Beacon.CreateBeaconItem(e0, e1);
			
			item.setItemMeta(beaconItem.getItemMeta());
		}
	}
}
