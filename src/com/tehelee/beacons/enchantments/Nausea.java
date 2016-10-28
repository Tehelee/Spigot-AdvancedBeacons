package com.tehelee.beacons.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Nausea extends Enchantment
{
	public Nausea()
	{
		super(509);
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
		return "Nausea";
	}

	@Override
	public int getStartLevel()
	{
		return 0;
	}
	
}
