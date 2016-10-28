package com.tehelee.beacons;

import org.bukkit.ChatColor;

public enum BeaconEffect
{
	UNASSIGNED,
	SPEED,
	SLOWNESS,
	HASTE,
	MINING_FATIGUE,
	STRENGTH,
	INSTANT_HEALTH,
	INSTANT_DAMAGE,
	JUMP_BOOST,
	NAUSEA,
	REGENERATION,
	RESISTANCE,
	FIRE_RESISTANCE,
	WATER_BREATHING,
	INVISIBILITY,
	BLINDNESS,
	NIGHT_VISION,
	HUNGER,
	WEAKNESS,
	POISON,
	WITHER,
	HEALTH_BOOST,
	ABSORPTION,
	SATURATION,
	GLOWING,
	LEVITATION,
	LUCK,
	BAD_LUCK;
	
	public static BeaconEffect fromInteger(int i)
	{
		switch(i)
		{
			case 0: return UNASSIGNED;
			case 1: return SPEED;
			case 2: return SLOWNESS;
			case 3: return HASTE;
			case 4: return MINING_FATIGUE;
			case 5: return STRENGTH;
			case 6: return INSTANT_HEALTH;
			case 7: return INSTANT_DAMAGE;
			case 8: return JUMP_BOOST;
			case 9: return NAUSEA;
			case 10: return REGENERATION;
			case 11: return RESISTANCE;
			case 12: return FIRE_RESISTANCE;
			case 13: return WATER_BREATHING;
			case 14: return INVISIBILITY;
			case 15: return BLINDNESS;
			case 16: return NIGHT_VISION;
			case 17: return HUNGER;
			case 18: return WEAKNESS;
			case 19: return POISON;
			case 20: return WITHER;
			case 21: return HEALTH_BOOST;
			case 22: return ABSORPTION;
			case 23: return SATURATION;
			case 24: return GLOWING;
			case 25: return LEVITATION;
			case 26: return LUCK;
			case 27: return BAD_LUCK;
			default: return UNASSIGNED;
		}
	}
	
	public int getAmplification()
	{
		return BeaconEffect.getAmplification(this);
	}
	
	public static int getAmplification(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:		return 0;
			case SPEED:				return 0;
			case SLOWNESS:			return 2;
			case HASTE:				return 0;
			case MINING_FATIGUE:	return 2;
			case STRENGTH:			return 0;
			case INSTANT_HEALTH:	return 1;
			case INSTANT_DAMAGE:	return 1;
			case JUMP_BOOST:		return 0;
			case NAUSEA:			return -1;
			case REGENERATION:		return -2;
			case RESISTANCE:		return 0;
			case FIRE_RESISTANCE:	return -1;
			case WATER_BREATHING:	return -1;
			case INVISIBILITY:		return -1;
			case BLINDNESS:			return -1;
			case NIGHT_VISION:		return -1;
			case HUNGER:			return 2;
			case WEAKNESS:			return 2;
			case POISON:			return 2;
			case WITHER:			return 2;
			case HEALTH_BOOST:		return 1;
			case ABSORPTION:		return 1;
			case SATURATION:		return 2;
			case GLOWING:			return -1;
			case LEVITATION:		return 1;
			case LUCK:				return 1;
			case BAD_LUCK:			return 1;
			default:				return 0;
		}
	}
	
	public int toInteger()
	{
		return BeaconEffect.toInteger(this);
	}
	
	public static int toInteger(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:		return 0;
			case SPEED:				return 1;
			case SLOWNESS:			return 2;
			case HASTE:				return 3;
			case MINING_FATIGUE:	return 4;
			case STRENGTH:			return 5;
			case INSTANT_HEALTH:	return 6;
			case INSTANT_DAMAGE:	return 7;
			case JUMP_BOOST:		return 8;
			case NAUSEA:			return 9;
			case REGENERATION:		return 10;
			case RESISTANCE:		return 11;
			case FIRE_RESISTANCE:	return 12;
			case WATER_BREATHING:	return 13;
			case INVISIBILITY:		return 14;
			case BLINDNESS:			return 15;
			case NIGHT_VISION:		return 16;
			case HUNGER:			return 17;
			case WEAKNESS:			return 18;
			case POISON:			return 19;
			case WITHER:			return 20;
			case HEALTH_BOOST:		return 21;
			case ABSORPTION:		return 22;
			case SATURATION:		return 23;
			case GLOWING:			return 24;
			case LEVITATION:		return 25;
			case LUCK:				return 26;
			case BAD_LUCK:			return 27;
			default:				return 0;
		}
	}
	
	public boolean isCustomEffect()
	{
		return BeaconEffect.isCustomEffect(this);
	}
	
	public static boolean isCustomEffect(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:	return false;
			case SPEED:			return false;
			case HASTE:			return false;
			case RESISTANCE:	return false;
			case JUMP_BOOST:	return false;
			case STRENGTH:		return false;
			case REGENERATION:	return false;
			default:			return true;
		}
	}
	
	public static BeaconEffect parseEffect(String value)
	{
		String input = ChatColor.stripColor(value.toUpperCase());
    	
		BeaconEffect[] effects = BeaconEffect.values();
    	
    	for (int i = 1; i < effects.length; i++)
    	{
    		if (input.equals(effects[i].toString())) return effects[i];
    		if (input.equals(effects[i].getName().toUpperCase())) return effects[i];
    	}
    	
		return BeaconEffect.UNASSIGNED;
	}
	
	public boolean getDefaultEnabled()
	{
		return BeaconEffect.getDefaultEnabled(this);
	}
	
	public static boolean getDefaultEnabled(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:		return false;
			case SPEED:				return false;
			case SLOWNESS:			return true;
			case HASTE:				return false;
			case MINING_FATIGUE:	return true;
			case STRENGTH:			return false;
			case INSTANT_HEALTH:	return false;
			case INSTANT_DAMAGE:	return false;
			case JUMP_BOOST:		return false;
			case NAUSEA:			return true;
			case REGENERATION:		return true;
			case RESISTANCE:		return false;
			case FIRE_RESISTANCE:	return true;
			case WATER_BREATHING:	return true;
			case INVISIBILITY:		return true;
			case BLINDNESS:			return true;
			case NIGHT_VISION:		return true;
			case HUNGER:			return true;
			case WEAKNESS:			return true;
			case POISON:			return true;
			case WITHER:			return true;
			case HEALTH_BOOST:		return true;
			case ABSORPTION:		return true;
			case SATURATION:		return true;
			case GLOWING:			return false;
			case LEVITATION:		return true;
			case LUCK:				return true;
			case BAD_LUCK:			return false;
			default:				return false;
		}
	}
	
	public int getDefaultCost()
	{
		return getDefaultCost(this);
	}
	
	public static int getDefaultCost(BeaconEffect e)
	{
		return (getDefaultEnabled(e) ? 128 : 0);
	}
	
	public boolean isEnabled()
	{
		return BeaconEffect.isEnabled(this);
	}
	
	public static boolean isEnabled(BeaconEffect e)
	{
		return Main.config.getBoolean("e_"+e.toString(), false);
	}
	
	public int getCost()
	{
		return BeaconEffect.getCost(this);
	}
	
	public static int getCost(BeaconEffect e)
	{
		return Main.config.getInt("c_"+e.toString(), -1);
	}
	
	public void configEnable(boolean enabled)
	{
		configEnable(this, enabled);
	}
	
	public static void configEnable(BeaconEffect e, boolean enabled)
	{
		Main.config.set("e_"+e.toString(), enabled);
		
		Main.writeConfig();
	}
	
	public void configCost(int cost)
	{
		configCost(this, cost);
	}
	
	public static void configCost(BeaconEffect e, int cost)
	{
		boolean disable = (cost <= 0);
		
		if (e.isEnabled() != disable)
		{
			e.configEnable(!disable);
		}
		
		Main.config.set("c_"+e.toString(), cost);
		
		Main.writeConfig();
	}
	
	public static void writeConfigDefaults()
	{
		BeaconEffect[] e = BeaconEffect.values();
		
		for (int i = 1; i < e.length; i++)
		{
			Main.config.addDefault("e_"+e[i].toString(), e[i].getDefaultEnabled());
			Main.config.addDefault("c_"+e[i].toString(), e[i].getDefaultCost());
		}
	}
	
	public static String getPrintout(boolean vertical)
	{
		BeaconEffect[] e = BeaconEffect.values();
		
		String result = "";
		
		for (int i = 1; i < e.length; i++)
		{
			result = result + e[i].toString();
			
			if (i != e.length-1)
			{
				result = result + (vertical ? "\n" : ", ");
			}
		}
		
		return result;
	}
	
	public String getName()
	{
		return BeaconEffect.getName(this);
	}
	
	public static String getName(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:		return "";
			case SPEED:				return "Speed";
			case SLOWNESS:			return "Slowness";
			case HASTE:				return "Haste";
			case MINING_FATIGUE:	return "Mining Fatigue";
			case STRENGTH:			return "Strength";
			case INSTANT_HEALTH:	return "Instant Health";
			case INSTANT_DAMAGE:	return "Instant Damage";
			case JUMP_BOOST:		return "Jump Boost";
			case NAUSEA:			return "Nausea";
			case REGENERATION:		return "Regeneration";
			case RESISTANCE:		return "Resistance";
			case FIRE_RESISTANCE:	return "Fire Resistance";
			case WATER_BREATHING:	return "Water Breathing";
			case INVISIBILITY:		return "Invisibility";
			case BLINDNESS:			return "Blindness";
			case NIGHT_VISION:		return "Night Vision";
			case HUNGER:			return "Hunger";
			case WEAKNESS:			return "Weakness";
			case POISON:			return "Poison";
			case WITHER:			return "Wither";
			case HEALTH_BOOST:		return "Health Boost";
			case ABSORPTION:		return "Absorption";
			case SATURATION:		return "Magic Food";
			case GLOWING:			return "X-Ray";
			case LEVITATION:		return "Levitation";
			case LUCK:				return "Luck";
			case BAD_LUCK:			return "Bad Luck";
			default:				return "";
		}
	}
	
	public String getDescription()
	{
		return BeaconEffect.getDescription(this);
	}
	
	public static String getDescription(BeaconEffect e)
	{
		switch(e)
		{
			case UNASSIGNED:		return "No Effect";
			case SPEED:				return "Increases walking speed";
			case SLOWNESS:			return "Decreases walking speed";
			case HASTE:				return "Blocks break faster, attack speed increases";
			case MINING_FATIGUE:	return "Blocks break slower, attack speed decreases";
			case STRENGTH:			return "Increases damage dealth with melee attacks";
			case INSTANT_HEALTH:	return "Heals all non-hostile mobs and players but damages undead";
			case INSTANT_DAMAGE:	return "Damages everything";
			case JUMP_BOOST:		return "Allows entities to jump higher and reduces fall damage";
			case NAUSEA:			return "Wobbles and warps the screen (ex: nether portal)";
			case REGENERATION:		return "Regerates health over time";
			case RESISTANCE:		return "Reduces all incoming damage";
			case FIRE_RESISTANCE:	return "Immunity to fire, lava and direct impact of blaze fireballs";
			case WATER_BREATHING:	return "Infinite oxygen and slightly increases underwater vision";
			case INVISIBILITY:		return "Invisibilty on mobs and players";
			case BLINDNESS:			return "Creates thick black fog, prevents sprinting and critical hits";
			case NIGHT_VISION:		return "Increases brightness to full";
			case HUNGER:			return "Consumes food over time";
			case WEAKNESS:			return "Damage decreased from all melee attacks, zombie villagers can be cured with this";
			case POISON:			return "Does damage over time, but won't kill players";
			case WITHER:			return "Does damage over time, and kills players";
			case HEALTH_BOOST:		return "Temporarily buffs health while under this effect";
			case ABSORPTION:		return "Adds a temporary health buff";
			case SATURATION:		return "Replenishes food over time";
			case GLOWING:			return "Causes players to glow with an x-ray outline";
			case LEVITATION:		return "Causes players to float involuntarily upwards";
			case LUCK:				return "Increases chances of getting high-quality loot";
			case BAD_LUCK:			return "Reduces chances of getting high-quality loot";
			default:				return "No Effect";
		}
	}
}
