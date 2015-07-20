package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityTerrarium extends TileEntity implements ISidedInventory
{
    private static final int[] SeedInventorySlots = new int[] {0};
    private static final int[] GroundInventorySlots = new int[] {1};
    private static final int[] ProduceInventorySlots = new int[] {2,3,4,5};
    private static final int[] ProduceAndSeedInventorySlots = new int[] {0,2,3,4,5};
    private ItemStack[] inventory = new ItemStack[9]; // TODO - set this back to 6
    private String customName;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot)
    {
        return this.inventory[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int numberToRemove)
    {
        if (this.inventory[slot] != null)
        {
            ItemStack var3;

            if (this.inventory[slot].stackSize <= numberToRemove)
            {
                var3 = this.inventory[slot];
                this.inventory[slot] = null;
                return var3;
            }
            else
            {
                var3 = this.inventory[slot].splitStack(numberToRemove);

                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        // copied from the furnace decompiled code - not sure why I would null the value out, it's going to get reset
        if (this.inventory[slot] != null)
        {
            ItemStack var2 = this.inventory[slot];
            this.inventory[slot] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack contents)
    {
        this.inventory[slot] = contents;

        if (contents != null && contents.stackSize > this.getInventoryStackLimit())
        {
            contents.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.customName : TerrariumFarm.MODID + "_" + "terrarium.container";
    }

    /**
     * Returns if the inventory name is localized
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    // TODO - this was @Override before - should be related to how we rename on Anvil
    public void setCustomName(String newName)
    {
        this.customName = newName;
    }

    public void readFromNBT(NBTTagCompound data)
    {
        super.readFromNBT(data);
        NBTTagList var2 = data.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (data.hasKey("CustomName"))
        {
            this.customName = data.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound data)
    {
        super.writeToNBT(data);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        data.setTag("Items", var2);

        if (this.hasCustomInventoryName())
        {
            data.setString("CustomName", this.customName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        // Choosing 80 because that's the number of dirt plots in a 9x9 farm
        return 80;
    }

    public void updateEntity()
    {
        // boolean var1 = this.burndownCounter > 0;
        // boolean var2 = false;

//         if (this.burndownCounter > 0)
//         {
//             --this.burndownCounter;
//         }
// 
//         if (!this.worldObj.isRemote)
//         {
//             if (this.burndownCounter != 0 || this.inventory[1] != null && this.inventory[0] != null)
//             {
//                 if (this.burndownCounter == 0 && this.hasSomethingToSmelt())
//                 {
//                     this.maxBurnTime = this.burndownCounter = getBurnTime(this.inventory[1]);
// 
//                     if (this.burndownCounter > 0)
//                     {
//                         var2 = true;
// 
//                         if (this.inventory[1] != null)
//                         {
//                             --this.inventory[1].stackSize;
// 
//                             if (this.inventory[1].stackSize == 0)
//                             {
//                                 Item var3 = this.inventory[1].getItem().getContainerItem();
//                                 this.inventory[1] = var3 != null ? new ItemStack(var3) : null;
//                             }
//                         }
//                     }
//                 }
// 
//                 if (this.func_145950_i() && this.hasSomethingToSmelt())
//                 {
//                     ++this.cookingCounter;
// 
//                     if (this.cookingCounter == 200)
//                     {
//                         this.cookingCounter = 0;
//                         this.completeSmelting();
//                         var2 = true;
//                     }
//                 }
//                 else
//                 {
//                     this.cookingCounter = 0;
//                 }
//             }
// 
//             if (var1 != this.burndownCounter > 0)
//             {
//                 var2 = true;
//                 BlockFurnace.func_149931_a(this.burndownCounter > 0, this.worldObj, this.field_145851_c, this.field_145848_d, this.field_145849_e);
//             }
//         }
// 
//         if (var2)
//         {
//             this.onInventoryChanged();
//         }
    }

    // private boolean hasSomethingToSmelt()
    // {
    //     if (this.inventory[0] == null)
    //     {
    //         return false;
    //     }
    //     else
    //     {
    //         ItemStack var1 = FurnaceRecipes.smelting().func_151395_a(this.inventory[0]);
    //         return var1 == null ? false : (this.inventory[2] == null ? true : (!this.inventory[2].isItemEqual(var1) ? false : (this.inventory[2].stackSize < this.getInventoryStackLimit() && this.inventory[2].stackSize < this.inventory[2].getMaxStackSize() ? true : this.inventory[2].stackSize < var1.getMaxStackSize())));
    //     }
    // }

//     public void completeSmelting()
//     {
//         if (this.hasSomethingToSmelt())
//         {
//             ItemStack var1 = FurnaceRecipes.smelting().func_151395_a(this.inventory[0]);
// 
//             if (this.inventory[2] == null)
//             {
//                 this.inventory[2] = var1.copy();
//             }
//             else if (this.inventory[2].getItem() == var1.getItem())
//             {
//                 ++this.inventory[2].stackSize;
//             }
// 
//             --this.inventory[0].stackSize;
// 
//             if (this.inventory[0].stackSize <= 0)
//             {
//                 this.inventory[0] = null;
//             }
//         }
//     }

//     public static int getBurnTime(ItemStack p_145952_0_)
//     {
//         if (p_145952_0_ == null)
//         {
//             return 0;
//         }
//         else
//         {
//             Item var1 = p_145952_0_.getItem();
// 
//             if (var1 instanceof ItemBlock && Block.getBlockFromItem(var1) != Blocks.air)
//             {
//                 Block var2 = Block.getBlockFromItem(var1);
// 
//                 if (var2 == Blocks.wooden_slab)
//                 {
//                     return 150;
//                 }
// 
//                 if (var2.getMaterial() == Material.wood)
//                 {
//                     return 300;
//                 }
// 
//                 if (var2 == Blocks.coal_block)
//                 {
//                     return 16000;
//                 }
//             }
// 
//             return var1 instanceof ItemTool && ((ItemTool)var1).getToolMaterialName().equals("WOOD") ? 200 : (var1 instanceof ItemSword && ((ItemSword)var1).func_150932_j().equals("WOOD") ? 200 : (var1 instanceof ItemHoe && ((ItemHoe)var1).getMaterialName().equals("WOOD") ? 200 : (var1 == Items.stick ? 100 : (var1 == Items.coal ? 1600 : (var1 == Items.lava_bucket ? 20000 : (var1 == Item.getItemFromBlock(Blocks.sapling) ? 100 : (var1 == Items.blaze_rod ? 2400 : 0)))))));
//         }
//     }

    // public static boolean func_145954_b(ItemStack p_145954_0_)
    // {
    //     return getBurnTime(p_145954_0_) > 0;
    // }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack items)
    {
        // 0 - IPlantable only
        // 1 - dirt only
        // Others, none
        return slot == 2 ? false : true;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == 0)
            return SeedInventorySlots;
        else if (side == 1)
            return GroundInventorySlots;
        else
            return ProduceAndSeedInventorySlots;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return java.util.Arrays.asList(getAccessibleSlotsFromSide(side)).contains(slot);
    }
    
    public void displayGuiTo(World world, EntityPlayer player) {
        TerrariumFarm.proxy.displayTerrariumGui(world, this, player);
    }
}
