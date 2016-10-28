package com.tehelee.beacons;

public class HelpText
{
	public static String PluginName = "Advanced Beacons";
	
	public static String logStart = "Initialized";
	public static String logStop = "Shut Down";
	public static String logRestart = "Reloaded Config";
	
	public static String cmdSpawn = "/beacon spawn [level] [primary] [secondary]";
	public static String cmdSpawnAt = "/beacon spawn [x] [y] [z] [level] [primary] [secondary]";
	public static String cmdSpawnAtWorld = "/beacon spawn [world] [x] [y] [z] [level] [primary] [secondary]";
	public static String cmdMerchant = "/beacon merchant [name]";
	public static String cmdInfo = "/beacon info";
	public static String cmdSet = "/beacon set <level | primary | secondary> [level | effect]";
	public static String cmdEffects = "/beacon effects";
	public static String cmdEffect = "/beacon effect [name]";
	public static String cmdGive1 = "/beacon give [effect]";
	public static String cmdGive2 = "/beacon give [effect1] [effect2]";
	public static String cmdGiveTo1 = "/beacon giveto [username] [effect]";
	public static String cmdGiveTo2 = "/beacon giveto [username] [effect1] [effect2]";
	public static String cmdConfig = "/beacon config [effect] [enable | disable | cost]";
	public static String cmdReload = "/beacon reload";
	
	public static String spawnFailed = "Beacon creation failed!";
	
	public static String noTarget = "You must be looking at a beacon to use this command.";
}
