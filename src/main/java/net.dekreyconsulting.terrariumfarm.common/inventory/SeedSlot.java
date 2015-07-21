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
        if (itemStack == null)
            return true;
        return SeedSlot.isValid(itemStack);
    }
	
    public int getSlotStackLimit()
    {
        // TODO - not actually letting us get above 64
        return 80;
    }
    
    public static boolean isValid(net.minecraft.item.ItemStack itemStack) {
        return itemStack.getItem() instanceof net.minecraft.item.ItemSeeds
            || itemStack.getItem() instanceof net.minecraft.item.ItemSeedFood;
    }
}