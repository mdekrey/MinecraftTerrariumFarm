package net.dekreyconsulting.terrariumfarm.common;

import java.util.Random;
import net.minecraft.world.World;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;

public class TerrariumBaseBlock extends net.minecraft.block.BlockContainer implements ITileEntityProvider {
	
    private IIcon texture;
	
	public TerrariumBaseBlock() {
        super(Material.iron);
		setBlockName(TerrariumFarm.MODID + "_" + "terrariumBaseBlock");
		setHardness(5.0F);
        setResistance(10.0f);
		setStepSound(Block.soundTypeMetal);
        this.setTickRandomly(false);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block baseBlock = world.getBlock(x, y, z);
		return world.isAirBlock(x, y+1, z) && (baseBlock == Blocks.iron_block);
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockType)
    {
		if (world.getBlock(x, y+1, z) != TerrariumBlocks.top) {
			world.setBlock(x, y, z, Blocks.iron_block, 0, 3);
		}
    }
	
    public Item getItemDropped(int metadata, Random random, int p_149650_3_)
    {
        return Blocks.iron_block.getItemDropped(0, random, p_149650_3_);
    }
	
	public void updateTick(World world, int x, int y, int z, Random random)
    {
	}
	
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? texture : Blocks.iron_block.getBlockTextureFromSide(side);
    }
	
	
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.texture = iconRegister.registerIcon("farmland_wet");
    }
    
    @Override
    public boolean hasTileEntity() {
        return true;
    }
    
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityTerrarium();
    }
    
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
    }

    public void breakBlock(World world, int x, int y, int z, Block blockType, int p_149749_6_)
    {
        TerrariumBlocks.top.breakBlock(world, x, y+1, z, TerrariumBlocks.top, p_149749_6_);
        super.breakBlock(world, x, y, z, blockType, p_149749_6_);
        world.removeTileEntity(x, y, z);
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(world, x, y, z, p_149696_5_, p_149696_6_);
        TileEntity var7 = world.getTileEntity(x, y, z);
        return var7 != null ? var7.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityTerrarium terrarium = (TileEntityTerrarium)world.getTileEntity(x, y, z);

            if (terrarium != null)
            {
                player.openGui(TerrariumFarm.instance, 0, world, x, y, z);
            }

            return true;
        }
    }
}
