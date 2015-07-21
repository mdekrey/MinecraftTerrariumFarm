package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.init.Blocks;

class GroundSlot extends Slot {
	
	public GroundSlot(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
	}
	
    public boolean isItemValid(net.minecraft.item.ItemStack itemStack)
    {
        if (itemStack == null)
            return true;
        return itemStack.getItem() == net.minecraft.item.Item.getItemFromBlock(Blocks.dirt);
    }
	
    public int getSlotStackLimit()
    {
        // TODO - not actually letting us get above 64
        return 80;
    }
    
    public static boolean isValid(net.minecraft.item.ItemStack itemStack) {
        return itemStack.getItem() == net.minecraft.item.Item.getItemFromBlock(Blocks.dirt);        
    }
}