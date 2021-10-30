package mod.imphack.module.modules.hud;

import mod.imphack.Main;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.util.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;

public class HudArmor extends Gui{

	
 
    private int offHandHeldItemCount;
    private int armourCompress;
    private int armourSpacing;

	private Minecraft mc = Minecraft.getMinecraft();

	
	 private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	 @SubscribeEvent
		public void renderOverlay(RenderGameOverlayEvent event) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				
				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"Armor")).enabled) {

					ScaledResolution resolution = new ScaledResolution(mc);
			    	RenderItem itemRender = mc.getRenderItem();
			    	
			        GlStateManager.enableTexture2D();
			        int i = resolution.getScaledWidth() / 2;
			        int iteration = 0;
			        int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
			        
			        for (ItemStack is : mc.player.inventory.armorInventory) {
			        	
			            iteration++;
			            if (is.isEmpty()) continue;
			            int x = i - 90 + (9 - iteration) * armourSpacing + armourCompress;
			            GlStateManager.enableDepth();
			 
			            itemRender.zLevel = 200F;
			            itemRender.renderItemAndEffectIntoGUI(is, x, y);
			            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
			            itemRender.zLevel = 0F;
			 
			            GlStateManager.enableTexture2D();
			            GlStateManager.disableLighting();
			            GlStateManager.disableDepth();
			 
				        String s = is.getCount() > 50 ? is.getCount() + "" : "";
				        mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffffff);
			 
				        if(mc.player.isCreative()) {
				        	
				        	float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
					        float red = 1 - green;
					        int dmg = 100 - (int) (red * 100);
					        int y1 = resolution.getScaledHeight() - 40 - (mc.player.isInWater() ? 10 : 0);

				                mc.fontRenderer.drawStringWithShadow(dmg  + "%", x + 10 - mc.fontRenderer.getStringWidth(dmg + "" + "%") / 2, y1 - 9, 0xffffffff);		
				                
				                armourCompress = 2;
				                armourSpacing = 20;
				                
				        }else {
			           
				        float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
				        float red = 1 - green;
				        int dmg = 100 - (int) (red * 100);
			                mc.fontRenderer.drawStringWithShadow(dmg  + "%", x + 10 - mc.fontRenderer.getStringWidth(dmg + "" + "%") / 2, y - 8, 0xffffffff);			                
			                armourCompress = 2;
			                armourSpacing = 20;
				        
			            GlStateManager.enableDepth();
			            GlStateManager.disableLighting();
				        }   
			        }
				}
			}
	 }
			       
			    int getItemsOffHand(Item i) {
			        return mc.player.inventory.offHandInventory.stream().
			        		filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
			    
	 }
}