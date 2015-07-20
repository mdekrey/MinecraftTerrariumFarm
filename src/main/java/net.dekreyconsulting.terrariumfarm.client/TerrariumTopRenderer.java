package net.dekreyconsulting.terrariumfarm.client;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class TerrariumTopRenderer implements ISimpleBlockRenderingHandler {
	
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
                    RenderBlocks renderer) {
    }
    
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                    Block block, int modelId, RenderBlocks renderer) {
            
            //which render pass are we doing?
            if (TerrariumFarmClientProxy.renderPass == 0) {
                renderer.renderStandardBlock(Blocks.glass, x, y, z);
                renderer.renderBlockByRenderType(Blocks.wheat, x, y, z);
            }
            else {
            }
            
            return true;
    }
    
    @Override
    public boolean shouldRender3DInInventory(int something) {
            
            return false;
    }
    
    @Override
    public int getRenderId() {
            
            return TerrariumFarmClientProxy.terrariumRenderType;
    }
}
