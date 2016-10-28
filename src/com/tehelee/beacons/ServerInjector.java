package com.tehelee.beacons;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.ItemBlock;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.minecraft.server.v1_10_R1.TileEntity;

public class ServerInjector
{	
	public static void inject() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		if (ReflectionUtils.hasBeenRan()) return;
		
		registerTileEntity(TileEntityBeacon.class, "Beacon");
		registerBlock(138, "beacon", new BlockBeacon());
		fixBlocksRefs();
		Bukkit.resetRecipes();
	}

	@SuppressWarnings("unchecked")
	private static void registerTileEntity(Class<? extends TileEntity> entityClass, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		((Map<String, Class<? extends TileEntity>>) ReflectionUtils.setAccessible(TileEntity.class.getDeclaredField("f")).get(null)).put(name, entityClass);
		((Map<Class<? extends TileEntity>, String>) ReflectionUtils.setAccessible(TileEntity.class.getDeclaredField("g")).get(null)).put(entityClass, name);
	}

	@SuppressWarnings("unchecked")
	private static void registerBlock(int id, String name, Block block) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		MinecraftKey stringkey = new MinecraftKey(name);
		block.c(name);
		Block.REGISTRY.a(id, stringkey, block);
		Iterator<IBlockData> blockdataiterator = block.t().a().iterator();
		while (blockdataiterator.hasNext()) {
			IBlockData blockdata = blockdataiterator.next();
			final int stateId = (id << 4) | block.toLegacyData(blockdata);
			Block.REGISTRY_ID.a(blockdata, stateId);
		}
		
		ItemBlock itemBlock = new ItemBlock(block);
		
		Item existing = Item.REGISTRY.get(stringkey);
		
		if (existing != null)
		{
			int oldId = Item.REGISTRY.a(existing);
			MinecraftKey oldKey = Item.REGISTRY.b(existing);
			
			System.out.println("["+oldId+"] "+oldKey.toString() +" - " +existing.getName());
			
			Item.REGISTRY.a(oldId, oldKey, itemBlock);
		}
		else
		{
			Item.REGISTRY.a(id, stringkey, itemBlock);
		}
		
		 // WHY DO YOU CRASH ON RELOAD
		
		((Map<Block, Item>) ReflectionUtils.setAccessible(Item.class.getDeclaredField("a")).get(null)).put(block, itemBlock);
	}

	private static void fixBlocksRefs() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		for (Field field : Blocks.class.getDeclaredFields())
		{
			field.setAccessible(true);
			if (Block.class.isAssignableFrom(field.getType()))
			{
				Block block = (Block) field.get(null);
				Block newblock = Block.getById(Block.getId(block));
				if (block != newblock) {
					ReflectionUtils.setStaticFinalField(field, newblock);
				}
			}
		}
	}
}