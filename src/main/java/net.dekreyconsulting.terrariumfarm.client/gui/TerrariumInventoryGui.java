package net.dekreyconsulting.terrariumfarm.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.dekreyconsulting.terrariumfarm.common.TerrariumFarm;
import net.dekreyconsulting.terrariumfarm.common.TerrariumContainer;
import net.dekreyconsulting.terrariumfarm.common.TileEntityTerrarium;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TerrariumInventoryGui extends GuiContainer
{
    private static final ResourceLocation texture = new ResourceLocation(TerrariumFarm.MODID + ":textures/terrarium/gui.png");
    private TileEntityTerrarium tileEntity;
    
    public TerrariumInventoryGui(InventoryPlayer player, TileEntityTerrarium tileEntity)
    {
        super(new TerrariumContainer(player, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String var3 = this.tileEntity.hasCustomInventoryName() ? this.tileEntity.getInventoryName() : I18n.format(this.tileEntity.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);

        // progress bars
        /*if (this.tileEntity.func_145950_i())
        {
            int var6 = this.tileEntity.func_145955_e(13);
            this.drawTexturedModalRect(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 1);
            var6 = this.tileEntity.func_145953_d(24);
            this.drawTexturedModalRect(var4 + 79, var5 + 34, 176, 14, var6 + 1, 16);
        }*/
    }
}
