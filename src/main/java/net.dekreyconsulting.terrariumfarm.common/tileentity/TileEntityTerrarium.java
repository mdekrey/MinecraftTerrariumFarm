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
    private static final int[] TopInventorySlots = new int[] {0};
    private static final int[] BottomInventorySlots = new int[] {2, 1};
    private static final int[] SideInventorySlots = new int[] {1};
    private ItemStack[] inventory = new ItemStack[3];
    public int burndownCounter;
    public int maxBurnTime;
    public int cookingCounter;
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
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return this.inventory[p_70301_1_];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (this.inventory[p_70298_1_] != null)
        {
            ItemStack var3;

            if (this.inventory[p_70298_1_].stackSize <= p_70298_2_)
            {
                var3 = this.inventory[p_70298_1_];
                this.inventory[p_70298_1_] = null;
                return var3;
            }
            else
            {
                var3 = this.inventory[p_70298_1_].splitStack(p_70298_2_);

                if (this.inventory[p_70298_1_].stackSize == 0)
                {
                    this.inventory[p_70298_1_] = null;
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
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (this.inventory[p_70304_1_] != null)
        {
            ItemStack var2 = this.inventory[p_70304_1_];
            this.inventory[p_70304_1_] = null;
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
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        this.inventory[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        // TODO - name
        return this.hasCustomInventoryName() ? this.customName : "container.furnace";
    }

    /**
     * Returns if the inventory name is localized
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    // TODO - this was @Override before
    public void setCustomName(String p_145951_1_)
    {
        this.customName = p_145951_1_;
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList var2 = p_145839_1_.getTagList("Items", 10);
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

        this.burndownCounter = p_145839_1_.getShort("BurnTime");
        this.cookingCounter = p_145839_1_.getShort("CookTime");
        // this.maxBurnTime = getBurnTime(this.inventory[1]);

        if (p_145839_1_.hasKey("CustomName"))
        {
            this.customName = p_145839_1_.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setShort("BurnTime", (short)this.burndownCounter);
        p_145841_1_.setShort("CookTime", (short)this.cookingCounter);
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

        p_145841_1_.setTag("Items", var2);

        if (this.hasCustomInventoryName())
        {
            p_145841_1_.setString("CustomName", this.customName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int func_145953_d(int p_145953_1_)
    {
        return this.cookingCounter * p_145953_1_ / 200;
    }

    public int func_145955_e(int p_145955_1_)
    {
        if (this.maxBurnTime == 0)
        {
            this.maxBurnTime = 200;
        }

        return this.burndownCounter * p_145955_1_ / this.maxBurnTime;
    }

    public boolean func_145950_i()
    {
        return this.burndownCounter > 0;
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
        // TODO - dirt only in one, IPlantable only in the other
        return slot == 2 ? false : true;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 0 ? TopInventorySlots : (side == 1 ? BottomInventorySlots : SideInventorySlots);
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
        return side != 0 || slot != 1;
    }
    
    public void displayGuiTo(World world, EntityPlayer player) {
        TerrariumFarm.proxy.displayTerrariumGui(world, this, player);
    }
}
