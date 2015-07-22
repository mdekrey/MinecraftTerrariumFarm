package net.dekreyconsulting.terrariumfarm.common;

import net.dekreyconsulting.terrariumfarm.client.TerrariumFarmClientProxy;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class TerrariumTopBlock extends Block {
    
    private IIcon textureTop;
    private IIcon textureSide;
	
	public TerrariumTopBlock() {
		super(Material.glass);
		setBlockName(TerrariumFarm.MODID + "_" + "terrariumTopBlock");
		setHardness(0.6F);
		setStepSound(Block.soundTypeGlass);
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockType)
    {
		if (world.getBlock(x, y-1, z) != TerrariumBlocks.base) {
			this.breakBlock(world, x, y, z, this, 0);
			world.setBlockToAir(x, y, z);
		}
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int p_149650_3_)
    {
        return null;
    }
    
    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }
    
    public void breakBlock(World world, int x, int y, int z, Block block, int fortune)
    {
        java.util.ArrayList<ItemStack> items = getDrops(world, x, y, z, 0, fortune);
        for (int i = 0; i < items.size(); ++i)
        {
            ItemStack var9 = items.get(i);

            if (var9 != null)
            {
                float var10 = world.rand.nextFloat() * 0.8F + 0.1F;
                float var11 = world.rand.nextFloat() * 0.8F + 0.1F;
                net.minecraft.entity.item.EntityItem var14;

                for (float var12 = world.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; world.spawnEntityInWorld(var14))
                {
                    int var13 = world.rand.nextInt(21) + 10;

                    if (var13 > var9.stackSize)
                    {
                        var13 = var9.stackSize;
                    }
                    
                    System.out.println("Dropping " + var13 + " of " + var9.stackSize + " " + var9.getDisplayName());

                    var14 = new net.minecraft.entity.item.EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), var9.splitStack(var13));
                    float var15 = 0.05F;
                    var14.motionX = (double)((float)world.rand.nextGaussian() * var15);
                    var14.motionY = (double)((float)world.rand.nextGaussian() * var15 + 0.2F);
                    var14.motionZ = (double)((float)world.rand.nextGaussian() * var15);
    
                    if (var9.hasTagCompound())
                    {
                        var14.getEntityItem().setTagCompound((net.minecraft.nbt.NBTTagCompound)var9.getTagCompound().copy());
                    }
                }
            }
        }
        world.removeTileEntity(x, y-1, z);

        super.breakBlock(world, x, y, z, block, fortune);
    }

	
    @Override
    public java.util.ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        java.util.ArrayList<ItemStack> items = new java.util.ArrayList<ItemStack>();
        
        net.minecraft.tileentity.TileEntity t = world.getTileEntity(x, y-1, z);
        
        if (t != null && t instanceof TileEntityTerrarium) {
            ItemStack stack = new ItemStack(TerrariumItems.terrarium, 1, 0);
            TileEntityTerrarium tile = (TileEntityTerrarium)t;
            
            for (int var8 = 0; var8 < tile.getSizeInventory(); ++var8)
            {
                ItemStack otherStack = tile.getStackInSlot(var8);
                if (otherStack != null) {
                    items.add((ItemStack)otherStack.copy());
                }
            }
            
            if (tile.hasCustomInventoryName()) {
                stack.setStackDisplayName(tile.getInventoryName());
            }
            items.add(stack);
        }

        return items;
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
    
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? textureTop : textureSide;
    }
	
	
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.textureSide = iconRegister.registerIcon("terrariumfarm:terrarium_side");
        this.textureTop = iconRegister.registerIcon("terrariumfarm:terrarium_top");
    }
    
    @Override
    public int getRenderType()
    {
        return TerrariumFarm.terrariumRenderType;
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
