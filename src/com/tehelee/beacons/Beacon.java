package com.tehelee.beacons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.ChatColor;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.TileEntity;

public class Beacon
{
	
	public static Beacon GetFromBlock(Block block)
	{
		if (block != null)
		{
			CraftWorld cworld = (CraftWorld)block.getWorld();
			
			TileEntity te = cworld.getTileEntityAt(block.getX(), block.getY(), block.getZ());
			
			if ((te != null) && (te.c().getString("id") == "Beacon"))
			{
				return new Beacon(te);
			}
		}
		
		return null;
	}
	
	public static Beacon SpawnBeaconEntity(Location location)
	{
		Block block = location.getBlock();
		
		block.setType(Material.BEACON);
		
		return GetFromBlock(block);
	}
	
	private static ItemStack addGlow(ItemStack item){
	   net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	   NBTTagCompound tag = null;
	   if (!nmsStack.hasTag()) {
		  tag = new NBTTagCompound();
		  nmsStack.setTag(tag);
	   }
	   if (tag == null) tag = nmsStack.getTag();
	   NBTTagList ench = new NBTTagList();
	   tag.set("ench", ench);
	   nmsStack.setTag(tag);
	   return CraftItemStack.asCraftMirror(nmsStack);
    }
	
	public static ItemStack CreateBeaconItem(BeaconEffect primary, BeaconEffect secondary)
	{
		ItemStack item = new ItemStack(Material.BEACON, 1);
		
		if ((item != null) && (primary.isCustomEffect() || secondary.isCustomEffect()))
		{	
			ItemMeta meta = item.getItemMeta();
			
			if (meta == null)
			{
				meta = Bukkit.getItemFactory().getItemMeta(Material.BEACON);
			}
			
			List<String> lore = new ArrayList<String>();
			
			if (primary == secondary)
			{
				lore.add(ChatColor.LIGHT_PURPLE + primary.getName() + ChatColor.GRAY + ": " + primary.getDescription());
				
				meta.setDisplayName(primary.getName() + " II Beacon");
			}
			else
			{
				lore.add(ChatColor.LIGHT_PURPLE + primary.getName() + ChatColor.GRAY + ": " + primary.getDescription());
				
				if (secondary != BeaconEffect.UNASSIGNED)
				{
					lore.add(ChatColor.LIGHT_PURPLE + secondary.getName() + ChatColor.GRAY + ": " + secondary.getDescription());
					
					meta.setDisplayName(primary.getName() + " & " + secondary.getName() + " Beacon");
				}
				else
				{
					meta.setDisplayName(primary.getName() + " Beacon");
				}
			}
			
			meta.setLore(lore);
			
			item.setItemMeta(meta);
			
			item = addGlow(item);
			
			return item;
		}
		
		return item;
	}
	
	private TileEntity entity; 
	
	private NBTTagCompound ntc;
	
	private int levels;
	private BeaconEffect primary;
	private BeaconEffect secondary;
	
	private int override;
	
	private Beacon(TileEntity tileEntity)
	{
		entity = tileEntity;
		
		ntc = entity.c();

		levels = ntc.getInt("Levels");
		primary = BeaconEffect.fromInteger(ntc.getInt("Primary"));
		secondary = BeaconEffect.fromInteger(ntc.getInt("Secondary"));
		override = ntc.getInt("Override");
	}
	
	public int getLevels()
	{
		return levels;
	}
	
	public BeaconEffect getPrimary()
	{
		return primary;
	}
	
	public BeaconEffect getSecondary()
	{
		return secondary;
	}
	
	public ItemStack createItem()
	{
		return Beacon.CreateBeaconItem(primary, secondary);
	}
	
	public void setLevels(int levels)
	{
		this.levels = levels;
		ntc.setInt("Levels", this.levels);
		override = 1;
		ntc.setInt("Override", override);
		save();
	}
	
	public void setPrimary(BeaconEffect primary)
	{
		this.primary = primary;
		ntc.setInt("Primary", BeaconEffect.toInteger(this.primary));
		save();
	}
	
	public void setSecondary(BeaconEffect secondary)
	{
		this.secondary = secondary;
		ntc.setInt("Secondary", BeaconEffect.toInteger(secondary));
		save();
	}
	
	public boolean isPowered()
	{
		return (levels != -1);
	}
	
	public boolean isBlocked()
	{
		return (levels == 0);
	}
	
	public boolean isActive()
	{
		return ((primary != BeaconEffect.UNASSIGNED) || (secondary != BeaconEffect.UNASSIGNED));
	}
	
	public boolean isCustom()
	{
		return (primary.isCustomEffect() || secondary.isCustomEffect());
	}
	
	private void save()
	{
		entity.a(ntc);
		TileEntity.a(entity.getWorld(), ntc);
	}
	
	@Override
	public String toString()
	{
		return "Beacon NBT:\n    Primary ID: " + primary + "\n    Secondary ID: " + secondary + "\n    Levels: " + levels;
	}
}
