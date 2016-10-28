package com.tehelee.beacons;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.PacketPlayOutTileEntityData;
import net.minecraft.server.v1_10_R1.PlayerInventory;
import net.minecraft.server.v1_10_R1.Statistic;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

import net.minecraft.server.v1_10_R1.AchievementList;
import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.BlockStainedGlass;
import net.minecraft.server.v1_10_R1.BlockStainedGlassPane;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.Container;
import net.minecraft.server.v1_10_R1.ContainerBeacon;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.EnumColor;
import net.minecraft.server.v1_10_R1.EnumDirection;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.Items;
import net.minecraft.server.v1_10_R1.MobEffect;
import net.minecraft.server.v1_10_R1.MobEffectList;
import net.minecraft.server.v1_10_R1.MobEffects;

public class TileEntityBeacon extends net.minecraft.server.v1_10_R1.TileEntityBeacon
{
	public static final MobEffectList[][] a = new MobEffectList[][] { { MobEffects.FASTER_MOVEMENT, MobEffects.FASTER_DIG}, { MobEffects.RESISTANCE, MobEffects.JUMP}, { MobEffects.INCREASE_DAMAGE}, { MobEffects.REGENERATION}};
	private static final Set<MobEffectList> f = Sets.newHashSet();
	private final List<com.tehelee.beacons.BeaconColorTracker> colorTracker = Lists.newArrayList();
	private boolean active;
	private int level = -1;
	@Nullable
	private MobEffectList primaryEffect;
	@Nullable
	private MobEffectList secondaryEffect;
	private int override;
	private ItemStack inventorySlot;
	private String o;
	private boolean custom;
	private Location location;
	private World.Spigot spigotWorld;

	public TileEntityBeacon() { }

	public void E_() {
		long time = this.world.getTime();
		if (time % 80L == 0L) {
			this.m();
		}
		
		if (time % 10L == 0L) {
			this.playEffect();
		}
		
	}

	public void m()
	{
		if (this.world != null)
		{
			try
			{
				this.F();
			}
			catch (ClassNotFoundException ex) { }
			this.E();
		}

	}

	private void E() {
		if (this.active && this.level > 0 && !this.world.isClientSide && this.primaryEffect != null) {
			double d0 = (double) (this.level * 10 + 10);
			int b0 = 0;

			if (this.level >= 4 && this.primaryEffect == this.secondaryEffect) {
				b0 = 1;
			}

			int i = (9 + this.level * 2) * 20;
			int j = this.position.getX();
			int level = this.position.getY();
			int primaryEffect = this.position.getZ();
			AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) j, (double) level, (double) primaryEffect, (double) (j + 1), (double) (level + 1), (double) (primaryEffect + 1))).g(d0).a(0.0D, (double) this.world.getHeight(), 0.0D);
			List<EntityHuman> list = this.world.a(EntityHuman.class, axisalignedbb);
			Iterator<EntityHuman> iterator = list.iterator();

			EntityHuman entityhuman;
			
			if (this.custom)
			{
				int e0 = BeaconEffect.fromInteger(MobEffectList.getId(this.primaryEffect)).getAmplification();
				int e1 = BeaconEffect.fromInteger(MobEffectList.getId(this.secondaryEffect)).getAmplification();
				if ((e0 > 0) && (this.primaryEffect == this.secondaryEffect) && (e1 > 0))
				{	
					e0 += e1;
				}
				else if (e0 < 0)
				{
					e0 *= -1;
				}
				else if ((BeaconEffect.fromInteger(MobEffectList.getId(this.secondaryEffect)) == BeaconEffect.UNASSIGNED) && this.level >= 4)
				{
					e0 += e0*(int)Math.floor(this.level / 4.0);
				}
				
				if (e1 < 0)
				{
					e1 *= -1;
				}
				
				e0--;
				e1--;
				
				while (iterator.hasNext()) {
					entityhuman = (EntityHuman) iterator.next();
					float health = entityhuman.getHealth();
					entityhuman.addEffect(new MobEffect(this.primaryEffect, i, e0, true, true));
					entityhuman.setHealth(health);
				}
				
				if (this.level >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
					iterator = list.iterator();
	
					while (iterator.hasNext()) {
						entityhuman = (EntityHuman) iterator.next();
						float health = entityhuman.getHealth();
						entityhuman.addEffect(new MobEffect(this.secondaryEffect, i, e1, true, true));
						entityhuman.setHealth(health);
					}
				}
			}
			else
			{
				while (iterator.hasNext()) {
					entityhuman = (EntityHuman) iterator.next();
					entityhuman.addEffect(new MobEffect(this.primaryEffect, i, b0, true, true));
				}
	
				if (this.level >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
					iterator = list.iterator();
	
					while (iterator.hasNext()) {
						entityhuman = (EntityHuman) iterator.next();
						entityhuman.addEffect(new MobEffect(this.secondaryEffect, i, 0, true, true));
					}
				}
			}
		}
	}
	
	private void playEffect()
	{
		if (spigotWorld == null || location == null)
		{
			org.bukkit.World w = (World) this.world.getWorld();
			
			BlockPosition pos = this.position;
			
			location = new Location(w, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
			spigotWorld = w.spigot();
		}
		
		if (this.custom && spigotWorld != null && location != null)
		{
			spigotWorld.playEffect(location, Effect.PORTAL, 0, 0, 0.5F, 1F, 0.5F, 0.05F, 16, 16);
		}
	}
	
	private void checkCustom()
	{
		int primary = MobEffectList.getId(this.primaryEffect);
		int secondary = MobEffectList.getId(this.secondaryEffect);
		
		
		
		this.custom = ((f(primary) == null) && (primary != 0)) || ((f(secondary) == null) && (secondary != 0));
	}

	private void F() throws ClassNotFoundException
	{
		int i = this.level;
		int j = this.position.getX();
		int level = this.position.getY();
		int m = this.position.getZ();

		if (this.override != 1)
		{
			this.level = 0;
		}
		this.colorTracker.clear();
		this.active = true;
		
		com.tehelee.beacons.BeaconColorTracker tileentitybeacon_beaconcolortracker = new com.tehelee.beacons.BeaconColorTracker(EntitySheep.a(EnumColor.WHITE));

		this.colorTracker.add(tileentitybeacon_beaconcolortracker);
		boolean flag = true;
		BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

		int i1;

		for (i1 = level + 1; i1 < 256; ++i1) {
			IBlockData iblockdata = this.world.getType(blockposition_mutableblockposition.c(j, i1, m));
			float[] afloat;

			if (iblockdata.getBlock() == Blocks.STAINED_GLASS) {
				afloat = EntitySheep.a((EnumColor) iblockdata.get(BlockStainedGlass.COLOR));
			} else {
				if (iblockdata.getBlock() != Blocks.STAINED_GLASS_PANE) {
					if (iblockdata.c() >= 15 && iblockdata.getBlock() != Blocks.BEDROCK && this.override != 1) {
						this.active = false;
						this.colorTracker.clear();
						break;
					}

					tileentitybeacon_beaconcolortracker.a();
					continue;
				}

				afloat = EntitySheep.a((EnumColor) iblockdata.get(BlockStainedGlassPane.COLOR));
			}

			if (!flag) {
				afloat = new float[] { (tileentitybeacon_beaconcolortracker.b()[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[2] + afloat[2]) / 2.0F};
			}

			if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.b())) {
				tileentitybeacon_beaconcolortracker.a();
			} else {
				tileentitybeacon_beaconcolortracker = new com.tehelee.beacons.BeaconColorTracker(afloat);
				this.colorTracker.add(tileentitybeacon_beaconcolortracker);
			}

			flag = false;
		}

		if (this.active) {
			int natLvl = 0;
			for (i1 = 1; i1 <= 4; natLvl = i1++) {
				int j1 = level - i1;

				if (j1 < 0) {
					break;
				}

				boolean flag1 = true;

				for (int k1 = j - i1; k1 <= j + i1 && flag1; ++k1) {
					for (int l1 = m - i1; l1 <= m + i1; ++l1) {
						Block block = this.world.getType(new BlockPosition(k1, j1, l1)).getBlock();

						if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
							flag1 = false;
							break;
						}
					}
				}

				if (!flag1) {
					break;
				}
			}
			
			if (this.override != 1) {
				this.level = natLvl;
			}

			if (this.level == 0) {
				this.active = false;
			}
		}

		if (!this.world.isClientSide && this.level >= 4 && i < this.level) {
			Iterator<EntityHuman> iterator = this.world.a(EntityHuman.class, (new AxisAlignedBB((double) j, (double) level, (double) m, (double) j, (double) (level - 4), (double) m)).grow(10.0D, 5.0D, 10.0D)).iterator();

			while (iterator.hasNext()) {
				EntityHuman entityhuman = (EntityHuman) iterator.next();

				entityhuman.b((Statistic) AchievementList.k);
			}
		}

	}

	@Nullable
	public PacketPlayOutTileEntityData getUpdatePacket() {
		return new PacketPlayOutTileEntityData(this.position, 3, this.c());
	}

	public NBTTagCompound c() {
		return this.save(new NBTTagCompound());
	}

	@Nullable
	private static MobEffectList f(int i) {
		MobEffectList mobeffectlist = MobEffectList.fromId(i);

		return TileEntityBeacon.f.contains(mobeffectlist) ? mobeffectlist : null;
	}

	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.primaryEffect = MobEffectList.fromId(nbttagcompound.getInt("Primary"));
		this.secondaryEffect = MobEffectList.fromId(nbttagcompound.getInt("Secondary"));
		this.level = nbttagcompound.getInt("Levels");
		this.override = nbttagcompound.getInt("Override");
		
		checkCustom();
	}

	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.save(nbttagcompound);
		
		nbttagcompound.setInt("Primary", MobEffectList.getId(this.primaryEffect));
		nbttagcompound.setInt("Secondary", MobEffectList.getId(this.secondaryEffect));
		nbttagcompound.setInt("Levels", this.level);
		nbttagcompound.setInt("Override", this.override);
		
		checkCustom();
		
		return nbttagcompound;
	}

	public int getSize() {
		return 1;
	}

	@Nullable
	public ItemStack getItem(int i) {
		return i == 0 ? this.inventorySlot : null;
	}

	@Nullable
	public ItemStack splitStack(int i, int j) {
		if (i == 0 && this.inventorySlot != null) {
			if (j >= this.inventorySlot.count) {
				ItemStack itemstack = this.inventorySlot;

				this.inventorySlot = null;
				return itemstack;
			} else {
				this.inventorySlot.count -= j;
				return new ItemStack(this.inventorySlot.getItem(), j, this.inventorySlot.getData());
			}
		} else {
			return null;
		}
	}

	@Nullable
	public ItemStack splitWithoutUpdate(int i) {
		if (i == 0) {
			ItemStack itemstack = this.inventorySlot;

			this.inventorySlot = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public void setItem(int i, @Nullable ItemStack itemstack) {
		if (i == 0) {
			this.inventorySlot = itemstack;
		}

	}

	public String getName() {
		return this.hasCustomName() ? this.o : "container.beacon";
	}

	public boolean hasCustomName() {
		return this.o != null && !this.o.isEmpty();
	}

	// SetName
	public void a(String s) {
		this.o = s;
	}

	public int getMaxStackSize() {
		return 1;
	}

	public boolean a(EntityHuman entityhuman) {
		return this.world.getTileEntity(this.position) != this ? false : entityhuman.e((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
	}

	public void startOpen(EntityHuman entityhuman) {}

	public void closeContainer(EntityHuman entityhuman) {}

	public boolean b(int i, ItemStack itemstack) {
		return itemstack.getItem() == Items.EMERALD || itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT;
	}

	public String getContainerName() {
		return "minecraft:beacon";
	}

	public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
		return new ContainerBeacon(playerinventory, this);
	}

	public int getProperty(int i) {
		switch (i) {
		case 0:
			return this.level;

		case 1:
			return MobEffectList.getId(this.primaryEffect);

		case 2:
			return MobEffectList.getId(this.secondaryEffect);
			
		case 3:
			return this.override;

		default:
			return 0;
		}
	}

	public void setProperty(int i, int j) {
		switch (i) {
		case 0:
			this.level = j;
			break;

		case 1:
			this.primaryEffect = MobEffectList.fromId(j);
			break;

		case 2:
			this.secondaryEffect = MobEffectList.fromId(j);
			break;
			
		case 3:
			this.override = j;
			break;
		}
		
		checkCustom();
	}

	public int g() {
		return 3;
	}

	public void l() {
		this.inventorySlot = null;
	}

	public boolean c(int i, int j) {
		if (i == 1) {
			this.m();
			return true;
		} else {
			return super.c(i, j);
		}
	}

	public int[] getSlotsForFace(EnumDirection enumdirection) {
		return new int[0];
	}

	public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
		return false;
	}

	public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
		return false;
	}

	static {
		MobEffectList[][] amobeffectlist = TileEntityBeacon.a;
		int i = amobeffectlist.length;

		for (int j = 0; j < i; ++j) {
			MobEffectList[] amobeffectlist1 = amobeffectlist[j];

			Collections.addAll(TileEntityBeacon.f, amobeffectlist1);
		}

	}
}
