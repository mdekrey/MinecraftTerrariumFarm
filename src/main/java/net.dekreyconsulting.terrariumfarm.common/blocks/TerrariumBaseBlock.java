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

public class TerrariumBaseBlock extends net.minecraft.block.BlockFarmland implements ITileEntityProvider {
	
    private IIcon texture;
	
	public TerrariumBaseBlock() {
        super();
		setBlockName(TerrariumFarm.MODID + "_" + "terrariumBaseBlock");
		setHardness(1.6F);
		setStepSound(Block.soundTypeGravel);
        this.setTickRandomly(false);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block baseBlock = world.getBlock(x, y, z);
		return world.isAirBlock(x, y+1, z) && (baseBlock == Blocks.dirt || baseBlock == Blocks.farmland || baseBlock == Blocks.grass);
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockType)
    {
		if (world.getBlock(x, y+1, z) != TerrariumBlocks.top) {
			world.setBlock(x, y, z, Blocks.dirt, 0, 3);
		}
    }
	
    public Item getItemDropped(int metadata, Random random, int p_149650_3_)
    {
        return Blocks.dirt.getItemDropped(0, random, p_149650_3_);
    }
	
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		// TODO
	}
	
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? texture : Blocks.dirt.getBlockTextureFromSide(side);
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
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.sendChatMessage("Add a terrarium");
        return new TileEntityTerrarium();
    }
    
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
    }

    public void breakBlock(World world, int x, int y, int z, Block blockType, int p_149749_6_)
    {
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
            
            if (terrarium == null) {
                terrarium = (TileEntityTerrarium)createNewTileEntity(world, 0);
                world.setTileEntity(x, y, z, terrarium);
            }

            if (terrarium != null)
            {
                terrarium.displayGuiTo(world, player);
            }

            return true;
        }
    }
}
