package mod.supergamer5465.sc.util;

import java.awt.Color;
import java.util.Arrays;

import javax.vecmath.Vector3d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;

public class RenderUtil extends Tessellator {
	public static RenderUtil INSTANCE = new RenderUtil();

	public RenderUtil() {
		super(0x200000);
	}

	public static void prepare(String mode_requested) {
		int mode = 0;

		if (mode_requested.equalsIgnoreCase("quads")) {
			mode = GL11.GL_QUADS;
		} else if (mode_requested.equalsIgnoreCase("lines")) {
			mode = GL11.GL_LINES;
		}

		prepare_gl();
		begin(mode);
	}

	public static void prepare_gl() {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(1.5F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1);
	}

	public static void begin(int mode) {
		INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
	}

	public static void release() {
		render();
		release_gl();
	}

	public static void render() {
		INSTANCE.draw();
	}

	public static void release_gl() {
		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableDepth();
	}

	public static void draw_cube(BlockPos blockPos, int argb, String sides) {
		final int a = (argb >>> 24) & 0xFF;
		final int r = (argb >>> 16) & 0xFF;
		final int g = (argb >>> 8) & 0xFF;
		final int b = argb & 0xFF;
		draw_cube(blockPos, r, g, b, a, sides);
	}

	public static void draw_cube(float x, float y, float z, int argb, String sides) {
		final int a = (argb >>> 24) & 0xFF;
		final int r = (argb >>> 16) & 0xFF;
		final int g = (argb >>> 8) & 0xFF;
		final int b = argb & 0xFF;
		draw_cube(INSTANCE.getBuffer(), x, y, z, 1, 1, 1, r, g, b, a, sides);
	}

	public static void draw_cube(BlockPos blockPos, int r, int g, int b, int a, String sides) {
		draw_cube(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, r, g, b, a, sides);
	}

	public static void draw_cube_line(BlockPos blockPos, int argb, String sides) {
		final int a = (argb >>> 24) & 0xFF;
		final int r = (argb >>> 16) & 0xFF;
		final int g = (argb >>> 8) & 0xFF;
		final int b = argb & 0xFF;
		draw_cube_line(blockPos, r, g, b, a, sides);
	}

	public static void draw_cube_line(float x, float y, float z, int argb, String sides) {
		final int a = (argb >>> 24) & 0xFF;
		final int r = (argb >>> 16) & 0xFF;
		final int g = (argb >>> 8) & 0xFF;
		final int b = argb & 0xFF;
		draw_cube_line(INSTANCE.getBuffer(), x, y, z, 1, 1, 1, r, g, b, a, sides);
	}

	public static void draw_cube_line(BlockPos blockPos, int r, int g, int b, int a, String sides) {
		draw_cube_line(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, r, g, b, a,
				sides);
	}

	public static BufferBuilder get_buffer_build() {
		return INSTANCE.getBuffer();
	}

	public static void draw_cube(final BufferBuilder buffer, float x, float y, float z, float w, float h, float d,
			int r, int g, int b, int a, String sides) {
		if (((boolean) Arrays.asList(sides.split("-")).contains("down")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("up")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("north")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
		}
	}

	public static void draw_cube_line(final BufferBuilder buffer, float x, float y, float z, float w, float h, float d,
			int r, int g, int b, int a, String sides) {
		if (((boolean) Arrays.asList(sides.split("-")).contains("downwest")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("upwest")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("downeast")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("upeast")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("downnorth")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("upnorth")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("downsouth")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("upsouth")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("nortwest")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("norteast")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("southweast")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
		}

		if (((boolean) Arrays.asList(sides.split("-")).contains("southeast")) || sides.equalsIgnoreCase("all")) {
			buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
			buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
		}
	}

	public static void Line(double sx, double sy, double sz, double ex, double ey, double ez, Vector3d plypos,
			Color color, float width) {
		float r = MathUtil.interpolate(color.getRed(), 0, 255, 0, 1);
		float g = MathUtil.interpolate(color.getGreen(), 0, 255, 0, 1);
		float b = MathUtil.interpolate(color.getBlue(), 0, 255, 0, 1);

		double rx = ex - sx;
		double ry = ey - sy;
		double rz = ez - sz;

		sx = -plypos.getX() + sx;
		sy = -plypos.getY() + sy;
		sz = -plypos.getZ() + sz;

		GL11.glPushMatrix();

		GL11.glTranslated(sx, sy, sz);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glLineWidth(width);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glColor4f(r, g, b, 1f);

		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(rx, ry, rz);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

		GL11.glPopMatrix();
	}

	public static void Prism(double x, double y, double z, double width, double height, double depth, Vector3d plypos,
			Color color) {
		float r = MathUtil.interpolate(color.getRed(), 0, 255, 0, 1);
		float g = MathUtil.interpolate(color.getGreen(), 0, 255, 0, 1);
		float b = MathUtil.interpolate(color.getBlue(), 0, 255, 0, 1);

		double wh = width / 2;
		double hh = height / 2;
		double dh = depth / 2;

		x = -plypos.getX() + x;
		y = -plypos.getY() + y;
		z = -plypos.getZ() + z;

		GL11.glPushMatrix();

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glTranslated(x, y, z);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor4f(r, g, b, 0.25f);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex3d(wh, hh, -dh);
		GL11.glVertex3d(wh, -hh, -dh);
		GL11.glVertex3d(-wh, -hh, -dh);
		GL11.glVertex3d(-wh, hh, -dh);
		GL11.glVertex3d(wh, hh, dh);
		GL11.glVertex3d(wh, -hh, dh);
		GL11.glVertex3d(wh, -hh, -dh);
		GL11.glVertex3d(wh, hh, -dh);
		GL11.glVertex3d(-wh, hh, dh);
		GL11.glVertex3d(-wh, -hh, dh);
		GL11.glVertex3d(wh, -hh, dh);
		GL11.glVertex3d(wh, hh, dh);
		GL11.glVertex3d(-wh, hh, -dh);
		GL11.glVertex3d(-wh, -hh, -dh);
		GL11.glVertex3d(-wh, -hh, dh);
		GL11.glVertex3d(-wh, hh, dh);
		GL11.glVertex3d(wh, -hh, -dh);
		GL11.glVertex3d(wh, -hh, dh);
		GL11.glVertex3d(-wh, -hh, dh);
		GL11.glVertex3d(-wh, -hh, -dh);
		GL11.glVertex3d(-wh, hh, dh);
		GL11.glVertex3d(wh, hh, dh);
		GL11.glVertex3d(wh, hh, -dh);
		GL11.glVertex3d(-wh, hh, -dh);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

		GL11.glPopMatrix();
	}

	public static void OutlinedPrism(double x, double y, double z, double width, double height, double depth,
			Vector3d plypos, Color color, float lwidth) {
		float r = MathUtil.interpolate(color.getRed(), 0, 255, 0, 1);
		float g = MathUtil.interpolate(color.getGreen(), 0, 255, 0, 1);
		float b = MathUtil.interpolate(color.getBlue(), 0, 255, 0, 1);

		double wh = width / 2;
		double hh = height / 2;
		double dh = depth / 2;

		x = -plypos.getX() + x;
		y = -plypos.getY() + y;
		z = -plypos.getZ() + z;

		GL11.glPushMatrix();

		GL11.glTranslated(x, y, z);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glLineWidth(lwidth);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glColor4f(r, g, b, 1f);

		GL11.glBegin(GL11.GL_LINE_STRIP);

		GL11.glVertex3d(-wh, -hh, -dh);
		GL11.glVertex3d(-wh, hh, -dh);
		GL11.glVertex3d(wh, hh, -dh);
		GL11.glVertex3d(wh, -hh, -dh);
		GL11.glVertex3d(wh, -hh, dh);
		GL11.glVertex3d(wh, hh, dh);
		GL11.glVertex3d(-wh, hh, dh);
		GL11.glVertex3d(-wh, -hh, dh);
		GL11.glVertex3d(-wh, -hh, -dh);
		GL11.glVertex3d(wh, -hh, -dh);
		GL11.glVertex3d(wh, hh, -dh);
		GL11.glVertex3d(wh, hh, dh);
		GL11.glVertex3d(wh, -hh, dh);
		GL11.glVertex3d(-wh, -hh, dh);
		GL11.glVertex3d(-wh, hh, dh);
		GL11.glVertex3d(-wh, hh, -dh);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

		GL11.glPopMatrix();
	}

	public static void Text(double x, double y, double z, String text, Vector3d plypos, Color color) {
		float r = MathUtil.interpolate(color.getRed(), 0, 255, 0, 1);
		float g = MathUtil.interpolate(color.getGreen(), 0, 255, 0, 1);
		float b = MathUtil.interpolate(color.getBlue(), 0, 255, 0, 1);

		RenderManager rm = Minecraft.getMinecraft().getRenderManager();

		x = -plypos.getX() + x;
		y = -plypos.getY() + y;
		z = -plypos.getZ() + z;

		GL11.glPushMatrix();

		GL11.glTranslated(x, y, z);

		GL11.glRotatef(-rm.playerViewY, 0f, 1f, 0f);
		GL11.glRotatef(rm.playerViewX, 1f, 0f, 0f);
		GL11.glScalef(-0.05f, -0.05f, 0.05f);

		// GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor4f(r, g, b, 1f);

		Minecraft.getMinecraft().fontRenderer.drawString(text,
				-Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2,
				-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, color.getRGB());

		// GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

		GL11.glPopMatrix();
	}

	public static void drawRect(final float x, final float y, final float w, final float h, final int color) {
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		final float red = (color >> 16 & 0xFF) / 255.0f;
		final float green = (color >> 8 & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(x, h, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(w, h, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(w, y, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRect(final float x, final float y, final float w, final float h, final float r,
			final float g, final float b, final float a) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(x, h, 0.0).color(r / 255, g / 255, b / 255, a).endVertex();
		bufferbuilder.pos(w, h, 0.0).color(r / 255, g / 255, b / 255, a).endVertex();
		bufferbuilder.pos(w, y, 0.0).color(r / 255, g / 255, b / 255, a).endVertex();
		bufferbuilder.pos(x, y, 0.0).color(r / 255, g / 255, b / 255, a).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawLine3D(float x, float y, float z, float x1, float y1, float z1, float thickness, int hex) {
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		float alpha = (hex >> 24 & 0xFF) / 255.0F;

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(thickness);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GlStateManager.disableDepth();
		GL11.glEnable(GL32.GL_DEPTH_CLAMP);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) x, (double) y, (double) z).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos((double) x1, (double) y1, (double) z1).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.enableDepth();
		GL11.glDisable(GL32.GL_DEPTH_CLAMP);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

}