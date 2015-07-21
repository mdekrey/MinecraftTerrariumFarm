package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.world.World;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;

public class TerrariumItem extends Item {
	
    public TerrariumItem()
    {
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setUnlocalizedName(TerrariumFarm.MODID + "_" + "terrarium");
        this.setTextureName("terrariumfarm:terrarium_icon");
    }
	
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!TerrariumBlocks.base.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }
        else {
            --itemStack.stackSize;
            
            world.setBlock(x, y + 1, z, TerrariumBlocks.top, 0, 3);
            world.setBlock(x, y + 0, z, TerrariumBlocks.base, 0, 3);
            if (itemStack.hasDisplayName()) {
                ((TileEntityTerrarium)world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
            }

            
            return true;
        }
    }
}