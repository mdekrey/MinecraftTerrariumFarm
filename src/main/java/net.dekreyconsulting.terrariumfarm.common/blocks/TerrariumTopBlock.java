package net.dekreyconsulting.terrariumfarm.common;

import net.dekreyconsulting.terrariumfarm.client.TerrariumFarmClientProxy;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.Facing;
import net.minecraft.entity.player.EntityPlayer;

public class TerrariumTopBlock extends Block {
	
	public TerrariumTopBlock() {
		super(Material.glass);
		setBlockName(TerrariumFarm.MODID + "_" + "terrariumTopBlock");
		setHardness(0.6F);
		setStepSound(Block.soundTypeStone);
		setBlockTextureName("glass");
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockType)
    {
		// TODO - drop inventory
		if (world.getBlock(x, y-1, z) != TerrariumBlocks.base) {
			this.dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
		}
    }
	
    public Item getItemDropped(int metadata, Random random, int p_149650_3_)
    {
		// TODO - drop inventory
        return TerrariumItems.terrarium;
    }
	
    public int getRenderBlockPass()
    {
        return 0;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    protected boolean canSilkHarvest()
    {
        return false;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public int getRenderType()
    {
        return TerrariumFarmClientProxy.terrariumRenderType;
    }
    
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        if (this == access.getBlock(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]))
        {
            return true;
        }

        return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return TerrariumBlocks.base.onBlockActivated(world, x, y-1, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }
}
