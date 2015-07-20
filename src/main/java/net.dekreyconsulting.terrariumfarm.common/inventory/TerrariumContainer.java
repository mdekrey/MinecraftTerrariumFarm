package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.inventory.ICrafting;

public class TerrariumContainer extends Container
{
    private TileEntityTerrarium terrarium;

    public TerrariumContainer(InventoryPlayer inventoryPlayer, TileEntityTerrarium terrarium)
    {
        this.terrarium = terrarium;
        this.addSlotToContainer(new Slot(terrarium, 0, 56, 17));
        this.addSlotToContainer(new Slot(terrarium, 1, 56, 53));
        this.addSlotToContainer(new Slot(terrarium, 2, 116, 35));
        int row;

        // add player inventory to gui
        for (row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        // add player quickbar to gui
        for (row = 0; row < 9; ++row)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, row, 8 + row * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        // I don't know what crafters are
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        // I don't know what crafters are
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        return this.terrarium.isUseableByPlayer(player);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        ItemStack var3 = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemsToTransfer = slot.getStack();
            
//            var3 = itemsToTransfer.copy();
// 
//             if (slotIndex == 2)
//             {
//                 if (!this.mergeItemStack(itemsToTransfer, 3, 39, true))
//                 {
//                     return null;
//                 }
// 
//                 slot.onSlotChange(itemsToTransfer, var3);
//             }
//             else if (slotIndex != 1 && slotIndex != 0)
//             {
//                 if (FurnaceRecipes.smelting().func_151395_a(itemsToTransfer) != null)
//                 {
//                     if (!this.mergeItemStack(itemsToTransfer, 0, 1, false))
//                     {
//                         return null;
//                     }
//                 }
//                 else if (TileEntityFurnace.func_145954_b(itemsToTransfer))
//                 {
//                     if (!this.mergeItemStack(itemsToTransfer, 1, 2, false))
//                     {
//                         return null;
//                     }
//                 }
//                 else if (slotIndex >= 3 && slotIndex < 30)
//                 {
//                     if (!this.mergeItemStack(itemsToTransfer, 30, 39, false))
//                     {
//                         return null;
//                     }
//                 }
//                 else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(itemsToTransfer, 3, 30, false))
//                 {
//                     return null;
//                 }
//             }
//             else if (!this.mergeItemStack(itemsToTransfer, 3, 39, false))
//             {
//                 return null;
//             }
// 
//             if (itemsToTransfer.stackSize == 0)
//             {
//                 slot.putStack((ItemStack)null);
//             }
//             else
//             {
//                 slot.onSlotChanged();
//             }
// 
//             if (itemsToTransfer.stackSize == var3.stackSize)
//             {
//                 return null;
//             }
// 
//             slot.onPickupFromSlot(player, itemsToTransfer);
        }

        return var3;
    }
}
