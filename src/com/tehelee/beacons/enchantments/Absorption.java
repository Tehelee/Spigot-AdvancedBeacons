package com.tehelee.beacons.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Absorption extends Enchantment
{	
	public Absorption()
	{
		super(522);
	}

	@Override
	public boolean canEnchantItem(ItemStack arg0)
	{
		return (arg0.getType() == Material.BEACON);
	}

	@Override
	public boolean conflictsWith(Enchantment arg0)
	{
		switch(arg0.hashCode())
		{
			default:
				return false;
		}
	}

	@Override
	public EnchantmentTarget getItemTarget()
	{
		return null;
	}

	@Override
	public int getMaxLevel()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "Absorption";
	}

	@Override
	public int getStartLevel()
	{
		return 0;
	}
	
}
