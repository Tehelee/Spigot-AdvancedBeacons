package com.tehelee.beacons;

import javax.annotation.Nullable;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.CreativeModeTab;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EnumDirection;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.EnumRenderType;
import net.minecraft.server.v1_10_R1.HttpUtilities;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.StatisticList;
import net.minecraft.server.v1_10_R1.TileEntity;
import net.minecraft.server.v1_10_R1.World;
import net.minecraft.server.v1_10_R1.WorldServer;

public class BlockBeacon extends net.minecraft.server.v1_10_R1.BlockBeacon {

    public BlockBeacon() {
        super();
        this.c(3.0F);
        this.a(CreativeModeTab.f);
    }

    public TileEntity a(World world, int i) {
        return new TileEntityBeacon();
    }

    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, @Nullable ItemStack itemstack, EnumDirection enumdirection, float f, float f1, float f2) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBeacon) {
                entityhuman.openContainer((TileEntityBeacon) tileentity);
                entityhuman.b(StatisticList.P);
            }

            return true;
        }
    }

    public boolean b(IBlockData iblockdata) {
        return false;
    }

    public boolean c(IBlockData iblockdata) {
        return false;
    }

    public EnumRenderType a(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        super.postPlace(world, blockposition, iblockdata, entityliving, itemstack);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityBeacon) {
        	if (itemstack.hasName())
        	{
        		((TileEntityBeacon) tileentity).a(itemstack.getName());
        	}
        }
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon) tileentity).m();
            world.playBlockAction(blockposition, this, 1, 0);
        }

    }

    public static void c(final World world, final BlockPosition blockposition) {
        HttpUtilities.a.submit(new Runnable() {
            public void run() {
                Chunk chunk = world.getChunkAtWorldCoords(blockposition);

                for (int i = blockposition.getY() - 1; i >= 0; --i) {
                    final BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), i, blockposition.getZ());

                    if (!chunk.c(blockposition1)) {
                        break;
                    }

                    IBlockData iblockdata = world.getType(blockposition1);

                    if (iblockdata.getBlock() == Blocks.BEACON) {
                        ((WorldServer) world).postToMainThread(new Runnable() {
                            public void run() {
                                TileEntity tileentity = world.getTileEntity(blockposition);

                                if (tileentity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon) tileentity).m();
                                    world.playBlockAction(blockposition, Blocks.BEACON, 1, 0);
                                }

                            }
                        });
                    }
                }

            }
        });
    }
}
