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
    static final int TerrariumSlots = 6;
    // player inventory + quickbar
    static final int PlayerSlotCount = 9 * 3 + 9;

    public TerrariumContainer(InventoryPlayer inventoryPlayer, TileEntityTerrarium terrarium)
    {
        this.terrarium = terrarium;
        terrarium.openInventory();
        GroundSlot groundSlot = new GroundSlot(terrarium, 1, 42, 45);
        this.addSlotToContainer(new SeedSlot(terrarium, 0, 42, 24, groundSlot));
        this.addSlotToContainer(groundSlot);
        this.addSlotToContainer(new Slot(terrarium, 2, 92, 26));
        this.addSlotToContainer(new Slot(terrarium, 3, 110, 26));
        this.addSlotToContainer(new Slot(terrarium, 4, 92, 44));
        this.addSlotToContainer(new Slot(terrarium, 5, 110, 44));

        // add player inventory to gui
        for (int row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        // add player quickbar to gui
        for (int column = 0; column < 9; ++column)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, column, 8 + column * 18, 142));
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
    
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        this.terrarium.closeInventory();
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlotIndex)
    {
        ItemStack sourceItems = null;
        Slot slot = (Slot)this.inventorySlots.get(fromSlotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemsToTransfer = slot.getStack();
            
            sourceItems = itemsToTransfer.copy();

            if (fromSlotIndex < TerrariumSlots) {
                // from terrarium to player
                if (!this.mergeItemStack(itemsToTransfer, TerrariumSlots, this.inventorySlots.size(), true)) {
                    return null;
                }
            } 
            else {
                // from player to terrarium
                if (!this.mergeItemStack(itemsToTransfer, 0, TerrariumSlots, false))
                {
                    return null;
                }
            }

            if (itemsToTransfer.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return sourceItems;
    }
}
