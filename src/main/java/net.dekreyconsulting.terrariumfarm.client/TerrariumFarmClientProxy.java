package net.dekreyconsulting.terrariumfarm.client;

import net.dekreyconsulting.terrariumfarm.common.TerrariumFarmCommonProxy;
import net.dekreyconsulting.terrariumfarm.common.TileEntityTerrarium;

import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TerrariumFarmClientProxy extends TerrariumFarmCommonProxy {

    public static int renderPass;
    
    public TerrariumFarmClientProxy() {
        TerrariumFarmClientProxy.setCustomRenderers();
    }
    
    public static void setCustomRenderers()
    {
        net.dekreyconsulting.terrariumfarm.common.TerrariumFarm.terrariumRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new TerrariumTopRenderer());
    }
    
    @Override
	public void displayTerrariumGui(World world, TileEntityTerrarium terrarium, EntityPlayer player) {
		if (!world.isRemote)
        {
            net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
                  new TerrariumInventoryGui(player.inventory, terrarium)); 
        }
	}
}