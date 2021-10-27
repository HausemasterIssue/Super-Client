package mod.imphack.module.modules.render;

<<<<<<< HEAD
import java.util.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.IntSetting;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;
import java.awt.Font;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventRender;
import mod.imphack.event.events.ImpHackEventRenderEntityName;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.util.ItemUtil;
import mod.imphack.util.font.FontUtils;
import mod.imphack.util.render.ColorUtil;
import mod.imphack.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;

public class Nametags extends Module {
	FloatSetting scaleSetting = new FloatSetting("Scale", this, 10.0f);
	BooleanSetting renderSelf = new BooleanSetting("self", this, true);
	IntSetting range = new IntSetting("Range", this, 150);
	BooleanSetting items = new BooleanSetting("items", this, true);
	BooleanSetting durability = new BooleanSetting("durability", this, true);
	BooleanSetting protType = new BooleanSetting("protType", this, true);
	BooleanSetting health = new BooleanSetting("health", this, true);
	BooleanSetting ping = new BooleanSetting("ping", this, true);
	public Nametags() {
		super("Nametags", "Adds More Features To Nametags", Category.RENDER);

		addSetting(renderSelf);
		addSetting(scaleSetting);
		addSetting(range);
		addSetting(items);
		addSetting(durability);
		addSetting(protType);
		addSetting(health);
		addSetting(ping);

	}

	public static FontUtils font1 = new FontUtils("Confortaa", Font.PLAIN, 15);
	
	@Override
	public void render(ImpHackEventRender  event) {
		if (mc.player == null || mc.world == null) return;

		mc.world.playerEntities.stream().filter(this::shouldRender).forEach(entityPlayer -> {
			Vec3d vec3d = findEntityVec3d(entityPlayer);
			renderNameTags(entityPlayer, vec3d.x, vec3d.y, vec3d.z);
		});
	}

	private void renderNameTags(EntityPlayer entityPlayer, double posX, double posY, double posZ) {
		double adjustedY = posY + (entityPlayer.isSneaking() ? 1.9 : 2.1);

		String[] name = new String[1];
		name[0] = buildEntityNameString(entityPlayer);

		RenderUtil.drawNametag(posX, adjustedY, posZ, name, new ColorUtil(255, 255, 255, 255), 2);
		renderItemsPos(entityPlayer, 0, 0);
		GlStateManager.popMatrix();
	}

	// utils
	private boolean shouldRender(EntityPlayer entityPlayer) {
		if (entityPlayer == mc.player && !renderSelf.isEnabled()) return false;

		if (entityPlayer.isDead || entityPlayer.getHealth() <= 0) return false;

		return !(entityPlayer.getDistance(mc.player) > range.getValue());
	}

	private Vec3d findEntityVec3d(EntityPlayer entityPlayer) {
		double posX = balancePosition(entityPlayer.posX, entityPlayer.lastTickPosX);
		double posY = balancePosition(entityPlayer.posY, entityPlayer.lastTickPosY);
		double posZ = balancePosition(entityPlayer.posZ, entityPlayer.lastTickPosZ);

		return new Vec3d(posX, posY, posZ);
	}

	private double balancePosition(double newPosition, double oldPosition) {
		return oldPosition + (newPosition - oldPosition) * mc.getRenderPartialTicks();
	}

	private TextFormatting healthColor(int health) {
		if (health <= 0) {
			return TextFormatting.DARK_RED;
		}else if (health <= 5) {
			return TextFormatting.RED;
		}else if (health <= 10) {
			return TextFormatting.GOLD;
		}else if (health <= 15) {
			return TextFormatting.YELLOW;
		}else if (health <= 20) {
			return TextFormatting.DARK_GREEN;
		}
		return TextFormatting.GREEN;
	}

	// render text
	private String buildEntityNameString(EntityPlayer entityPlayer) {
		String name = entityPlayer.getName();
		if (ping.isEnabled()) {
			int value = 0;

			if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()) != null) {
				value = mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime();
			}
			name = name + " " + value + "ms";
		}
		if (health.isEnabled()) {
			int health = (int) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
			TextFormatting textFormatting = healthColor(health);

			name = name + " " + textFormatting + health;
		}

		return name;
	}


	// render items
	private void renderItem(ItemStack itemStack, int posX, int posY, int posY2) {
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		GlStateManager.enableDepth();
		GlStateManager.disableAlpha();

		final int posY3 = (posY2 > 4) ? ((posY2 - 4) * 8 / 2) : 0;

		mc.getRenderItem().zLevel = -150.0f;
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY + posY3);
		mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, posX, posY + posY3);
		RenderHelper.disableStandardItemLighting();
		mc.getRenderItem().zLevel = 0.0f;
		RenderUtil.prepare();
		GlStateManager.pushMatrix();
		GlStateManager.scale(.5, .5, .5);
		renderEnchants(itemStack, posX, posY - 24);
		GlStateManager.popMatrix();
	}

	private void renderItemDurability(ItemStack itemStack, int posX, int posY) {
		float damagePercent = (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage();

		float green = damagePercent;
		if (green > 1) green = 1;
		else if (green < 0) green = 0;

		GlStateManager.enableTexture2D();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 0.5);
		mc.fontRenderer.drawStringWithShadow((int) (damagePercent * 100) + "%", posX * 2, posY, 0xff00ff00);
		GlStateManager.popMatrix();
		GlStateManager.disableTexture2D();
	}

	// render item positions
	private void renderItemsPos(EntityPlayer entityPlayer, int posX, int posY) {
		ItemStack mainHandItem = entityPlayer.getHeldItemMainhand();
		ItemStack offHandItem = entityPlayer.getHeldItemOffhand();

		int armorCount = 3;
		for (int i = 0; i <= 3; i++) {
			ItemStack itemStack = entityPlayer.inventory.armorInventory.get(armorCount);

			if (!itemStack.isEmpty()) {
				posX -= 8;

				int size = EnchantmentHelper.getEnchantments(itemStack).size();

				if (items.isEnabled() && size > posY) {
					posY = size;
				}
			}
			armorCount --;
		}

		if (!mainHandItem.isEmpty() && (items.isEnabled() || durability.isEnabled() && offHandItem.isItemStackDamageable())) {
			posX -= 8;

			int enchantSize = EnchantmentHelper.getEnchantments(offHandItem).size();
			if (items.isEnabled() && enchantSize > posY) {
				posY = enchantSize;
			}
		}

		if (!mainHandItem.isEmpty()) {
			int enchantSize = EnchantmentHelper.getEnchantments(mainHandItem).size();
			if (items.isEnabled() && enchantSize > posY) {
				posY = enchantSize;
			}
			int armorY = findArmorY(posY);
			if (items.isEnabled() || (durability.isEnabled() && mainHandItem.isItemStackDamageable())) {
				posX -= 8;
			}
			if (items.isEnabled()) {
				renderItem(mainHandItem, posX, armorY, posY);
				armorY -= 32;
			}
			if (durability.isEnabled() && mainHandItem.isItemStackDamageable()) {
				renderItemDurability(mainHandItem, posX, armorY);
			}
			armorY -= (mc.fontRenderer.FONT_HEIGHT);
			if (items.isEnabled() || (durability.isEnabled() && mainHandItem.isItemStackDamageable())) {
				posX += 16;
			}
		}

		int armorCount2 = 3;
		for (int i = 0; i <= 3; i++) {
			ItemStack itemStack = entityPlayer.inventory.armorInventory.get(armorCount2);

			if (!itemStack.isEmpty()) {
				int armorY = findArmorY(posY);
				if (items.isEnabled()) {
					renderItem(itemStack, posX, armorY, posY);
					armorY -= 32;
				}
				if (durability.isEnabled() && itemStack.isItemStackDamageable()) {
					renderItemDurability(itemStack, posX, armorY);
				}
				posX += 16;
			}
			armorCount2--;
		}

		if (!offHandItem.isEmpty()) {
			int armorY = findArmorY(posY);
			if (items.isEnabled()) {
				renderItem(offHandItem, posX, armorY, posY);
				armorY -= 32;
			}
			if (durability.isEnabled() && offHandItem.isItemStackDamageable()) {
				renderItemDurability(offHandItem, posX, armorY);
			}
		}
	}
	private int findArmorY(int posY) {
		int posY2 = durability.isEnabled() ? -26 : -27;
		if (posY > 4) {
			posY2 -= (posY - 4) * 8;
		}

		return posY2;
	}

	// enchantment

	private void renderEnchants(ItemStack itemStack, int posX, int posY) {
		GlStateManager.enableTexture2D();

		for (Enchantment enchantment : EnchantmentHelper.getEnchantments(itemStack).keySet()) {
			 int level = EnchantmentHelper.getEnchantmentLevel(enchantment, itemStack);

			
			if (enchantment == null) {
				continue;
			}

			if(protType.isEnabled()) {
				if(enchantment.equals(Enchantments.BLAST_PROTECTION))
					mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + findStringForEnchants(enchantment, level), posX * 2 + 13, posY + 0, 0xffffffff);
			}
			if(enchantment.equals(Enchantments.PROTECTION))
				
				font1.drawString(findStringForEnchants(enchantment, level), posX * 2 + 13, posY + 0, 0xffffffff);

		if(enchantment.equals(Enchantments.MENDING))
			mc.fontRenderer.drawString(ChatFormatting.WHITE + findStringForEnchants(enchantment, level), posX * 2 + 13, posY + 5, 0xffffffff);
		}

		GlStateManager.disableTexture2D();
	}

	private String findStringForEnchants(Enchantment enchantment, int level) {
		ResourceLocation resourceLocation = Enchantment.REGISTRY.getNameForObject(enchantment);

		String string = resourceLocation == null ? enchantment.getName() : resourceLocation.toString();

		int charCount = (level > 1) ? 12 : 13;

		if (string.length() > charCount) {
			string = string.substring(10, charCount);
		}

		return string.substring(0, 1).toUpperCase() + string.substring(1) + ((level > 1) ? level : "");
	}
	@EventHandler
	private Listener<ImpHackEventRenderEntityName> player_nametag = new Listener<>(event -> {
		event.cancel();
	});

}
=======
import me.zero.alpine.event.type.Cancellable;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.imphack.event.events.ImpHackEventRender;
import mod.imphack.event.events.ImpHackEventRenderEntityName;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.util.ItemUtil;
import mod.imphack.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class Nametags extends Module {
	final FloatSetting scaleSetting = new FloatSetting("Scale", this, 10.0f);

	public Nametags() {
		super("Nametags", "Adds More Features To Nametags", Category.RENDER);

		addSetting(scaleSetting);
	}

	@Override
	public void render(ImpHackEventRender event) {
		for (final EntityPlayer player : mc.world.playerEntities) {
			if (player != null && !player.equals(mc.player) && player.isEntityAlive() && (!player.isInvisible())) {
				final double x = this.interpolate(player.lastTickPosX, player.posX, event.get_partial_ticks())
						- mc.getRenderManager().renderPosX;

				final double y = this.interpolate(player.lastTickPosY, player.posY, event.get_partial_ticks())
						- mc.getRenderManager().renderPosY;
				final double z = this.interpolate(player.lastTickPosZ, player.posZ, event.get_partial_ticks())
						- mc.getRenderManager().renderPosZ;
				this.renderNameTag(player, x, y, z, event.get_partial_ticks());
			}
		}
	}

	private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z,
			final float delta) {
		double tempY = y;
		tempY += (player.isSneaking() ? 0.5 : 0.7);
		final Entity camera = mc.getRenderViewEntity();
		assert camera != null;
		final double originalPositionX = camera.posX;
		final double originalPositionY = camera.posY;
		final double originalPositionZ = camera.posZ;
		camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
		camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
		camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
		final String displayTag = this.getDisplayTag(player);
		final double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX,
				y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
		final int width = mc.fontRenderer.getStringWidth(displayTag) / 2;
		double scale = (0.0018 + scaleSetting.value * (distance * 0.3)) / 1000.0;
		if (distance <= 8.0) {
			scale = 0.0245;
		}
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enablePolygonOffset();
		GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
		GlStateManager.disableLighting();
		GlStateManager.translate((float) x, (float) tempY + 1.4f, (float) z);
		GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f,
				0.0f, 0.0f);
		GlStateManager.scale(-scale, -scale, scale);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.enableBlend();

		int red = 255;
		int green = 255;
		int blue = 255;
		float a = 0.0f;

		RenderUtil.drawRect((float) (-width - 2) - 1, (float) (-(mc.fontRenderer.FONT_HEIGHT + 1)) - 1, width + 3f,
				2.5f, red, green, blue, a);

		GlStateManager.disableBlend();
		final ItemStack renderMainHand = player.getHeldItemMainhand().copy();

		if (renderMainHand.hasEffect()
				&& (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
			renderMainHand.stackSize = 1;
		}
		if (!renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
			final String stackName = renderMainHand.getDisplayName();
			final int stackNameWidth = mc.fontRenderer.getStringWidth(stackName) / 2;
			GL11.glPushMatrix();
			GL11.glScalef(0.75f, 0.75f, 0.0f);
			mc.fontRenderer.drawStringWithShadow(stackName, (float) (-stackNameWidth),
					-(this.getBiggestArmorTag(player) + 32.0f), -1);
			GL11.glScalef(1.5f, 1.5f, 1.0f);
			GL11.glPopMatrix();
		}

		GlStateManager.pushMatrix();
		int xOffset = -8;
		for (final ItemStack stack : player.inventory.armorInventory) {
			if (stack != null) {
				xOffset -= 8;
			}
		}
		xOffset -= 8;
		final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
		if (renderOffhand.hasEffect()
				&& (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
			renderOffhand.stackSize = 1;
		}
		this.renderItemStack(renderOffhand, xOffset);
		xOffset += 16;

		for (int i = player.inventory.armorInventory.size(); i > 0; i--) {
			final ItemStack stack2 = player.inventory.armorInventory.get(i - 1);
			final ItemStack armourStack = stack2.copy();
			if (armourStack.hasEffect()
					&& (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
				armourStack.stackSize = 1;
			}
			this.renderItemStack(armourStack, xOffset);
			xOffset += 16;
		}
		this.renderItemStack(renderMainHand, xOffset);
		GlStateManager.popMatrix();
		mc.fontRenderer.drawStringWithShadow(displayTag, (float) (-width), (float) (-(mc.fontRenderer.FONT_HEIGHT - 1)),
				this.getDisplayColour(player));
		camera.posX = originalPositionX;
		camera.posY = originalPositionY;
		camera.posZ = originalPositionZ;
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.disablePolygonOffset();
		GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
		GlStateManager.popMatrix();
	}

	private void renderItemStack(final ItemStack stack, final int x) {
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableAlpha();
		GlStateManager.enableDepth();
		GlStateManager.disableCull();
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -29);
		mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, -29);
		mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableCull();
		GlStateManager.enableAlpha();
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		GlStateManager.disableDepth();
		this.renderEnchantmentText(stack, x);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.popMatrix();
	}

	private void renderEnchantmentText(final ItemStack stack, final int x) {
		int enchantmentY = -37;
		final NBTTagList enchants = stack.getEnchantmentTagList();
		if (enchants.tagCount() > 2) {
			mc.fontRenderer.drawStringWithShadow("max", (float) (x * 2), (float) enchantmentY, -3977919);
			enchantmentY -= 8;
		} else {
			for (int index = 0; index < enchants.tagCount(); ++index) {
				final short id = enchants.getCompoundTagAt(index).getShort("id");
				final short level = enchants.getCompoundTagAt(index).getShort("lvl");
				final Enchantment enc = Enchantment.getEnchantmentByID(id);
				if (enc != null) {
					String encName = enc.isCurse()
							? (TextFormatting.RED
									+ enc.getTranslatedName(level).substring(11).substring(0, 1).toLowerCase())
							: enc.getTranslatedName(level).substring(0, 1).toLowerCase();
					encName += level;
					mc.fontRenderer.drawStringWithShadow(encName, (float) (x * 2), (float) enchantmentY, -1);
					enchantmentY -= 8;
				}
			}
		}
		if (ItemUtil.hasDurability(stack)) {
			final int percent = ItemUtil.getRoundedDamage(stack);
			String color;
			if (percent >= 60) {
				color = section_sign() + "a";
			} else if (percent >= 25) {
				color = section_sign() + "e";
			} else {
				color = section_sign() + "c";
			}
			mc.fontRenderer.drawStringWithShadow(color + percent + "%", (float) (x * 2),
					(enchantmentY < -62) ? (float) enchantmentY : -62, -1);
		}
	}

	private float getBiggestArmorTag(final EntityPlayer player) {
		float enchantmentY = 0.0f;
		boolean arm = false;
		for (final ItemStack stack : player.inventory.armorInventory) {
			float encY = 0.0f;
			if (stack != null) {
				final NBTTagList enchants = stack.getEnchantmentTagList();
				for (int index = 0; index < enchants.tagCount(); ++index) {
					final short id = enchants.getCompoundTagAt(index).getShort("id");
					final Enchantment enc = Enchantment.getEnchantmentByID(id);
					if (enc != null) {
						encY += 8.0f;
						arm = true;
					}
				}
			}
			if (encY > enchantmentY) {
				enchantmentY = encY;
			}
		}
		final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
		if (renderMainHand.hasEffect()) {
			float encY2 = 0.0f;
			final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
			for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
				final short id2 = enchants2.getCompoundTagAt(index2).getShort("id");
				final Enchantment enc2 = Enchantment.getEnchantmentByID(id2);
				if (enc2 != null) {
					encY2 += 8.0f;
					arm = true;
				}
			}
			if (encY2 > enchantmentY) {
				enchantmentY = encY2;
			}
		}
		final ItemStack renderOffHand = player.getHeldItemOffhand().copy();
		if (renderOffHand.hasEffect()) {
			float encY = 0.0f;
			final NBTTagList enchants = renderOffHand.getEnchantmentTagList();
			for (int index = 0; index < enchants.tagCount(); ++index) {
				final short id = enchants.getCompoundTagAt(index).getShort("id");
				final Enchantment enc = Enchantment.getEnchantmentByID(id);
				if (enc != null) {
					encY += 8.0f;
					arm = true;
				}
			}
			if (encY > enchantmentY) {
				enchantmentY = encY;
			}
		}
		return (arm ? 0 : 20) + enchantmentY;
	}

	private String getDisplayTag(final EntityPlayer player) {
		String name = player.getDisplayNameString();
		final float health = player.getHealth() + player.getAbsorptionAmount();
		if (health <= 0) {
			return name + " - DEAD";
		}
		String color;
		if (health > 25.0f) {
			color = section_sign() + "5";
		} else if (health > 20.0f) {
			color = section_sign() + "a";
		} else if (health > 15.0f) {
			color = section_sign() + "2";
		} else if (health > 10.0f) {
			color = section_sign() + "6";
		} else if (health > 5.0f) {
			color = section_sign() + "c";
		} else {
			color = section_sign() + "4";
		}
		String pingStr = "";
		try {
			final int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID())
					.getResponseTime();
			if (responseTime > 150) {
				pingStr += section_sign() + "4";
			} else if (responseTime > 100) {
				pingStr += section_sign() + "6";
			} else {
				pingStr += section_sign() + "2";
			}
			pingStr += responseTime + "ms ";
		} catch (Exception ignore) {
		}
		String popStr = " ";
		if (Math.floor(health) == health) {
			name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int) Math.floor(health)) : "dead");
		} else {
			name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int) health) : "dead");
		}
		return pingStr + section_sign() + "r" + name + section_sign() + "r" + popStr;
	}

	private int getDisplayColour(final EntityPlayer player) {
		return -5592406;
	}

	private double interpolate(final double previous, final double current, final float delta) {
		return previous + (current - previous) * delta;
	}

	public String section_sign() {
		return "\u00A7";
	}

	@EventHandler
	private final Listener<ImpHackEventRenderEntityName> player_nametag = new Listener<>(Cancellable::cancel);

}
>>>>>>> branch 'master' of https://github.com/Supergamer5465/ImpHack-Revised.git
