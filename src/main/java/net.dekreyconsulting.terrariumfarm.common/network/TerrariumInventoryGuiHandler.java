package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import net.dekreyconsulting.terrariumfarm.client.TerrariumInventoryGui;

public class TerrariumInventoryGuiHandler implements IGuiHandler {
	//returns an instance of the Container you made earlier
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityTerrarium) {
			return new TerrariumContainer(player.inventory, (TileEntityTerrarium) tileEntity);
		}
		return null;
	}
	
	//returns an instance of the Gui you made earlier
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityTerrarium) {
			return new TerrariumInventoryGui(player.inventory, (TileEntityTerrarium) tileEntity);
		}
		return null;
	}
}