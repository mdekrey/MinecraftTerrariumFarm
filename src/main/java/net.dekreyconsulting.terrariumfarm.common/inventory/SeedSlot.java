package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;

class SeedSlot extends Slot {
	GroundSlot groundSlot;
	
	public SeedSlot(IInventory inventory, int slotIndex, int x, int y, GroundSlot groundSlot) {
		super(inventory, slotIndex, x, y);
		this.groundSlot = groundSlot;
	}
	
    public boolean isItemValid(net.minecraft.item.ItemStack itemStack)
    {
		if (!groundSlot.getHasStack())
			return false;
        if (itemStack == null)
            return true;
        return itemStack.getItem() instanceof net.minecraft.item.ItemSeeds;
    }
	
    public int getSlotStackLimit()
    {
		if (!groundSlot.getHasStack())
			return 0;
        return groundSlot.getStack().stackSize;
    }
	
}