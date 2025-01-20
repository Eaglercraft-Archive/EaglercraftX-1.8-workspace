package net.minecraft.client.renderer.tileentity;

import java.util.Map;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class TileEntityRendererDispatcher {
	private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> mapSpecialRenderers = Maps
			.newHashMap();
	public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
	private FontRenderer fontRenderer;
	public static double staticPlayerX;
	public static double staticPlayerY;
	public static double staticPlayerZ;
	public TextureManager renderEngine;
	public World worldObj;
	public Entity entity;
	public float entityYaw;
	public float entityPitch;
	public double entityX;
	public double entityY;
	public double entityZ;

	private TileEntityRendererDispatcher() {
		this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
		this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
		this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
		this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
		this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
		this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
		this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
		this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
		this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
		this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());

		for (TileEntitySpecialRenderer tileentityspecialrenderer : this.mapSpecialRenderers.values()) {
			tileentityspecialrenderer.setRendererDispatcher(this);
		}

	}

	public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(
			Class<? extends TileEntity> teClass) {
		TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer) this.mapSpecialRenderers
				.get(teClass);
		if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
			tileentityspecialrenderer = this
					.getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());
			this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
		}

		return tileentityspecialrenderer;
	}

	public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn) {
		return tileEntityIn == null ? null : this.getSpecialRendererByClass(tileEntityIn.getClass());
	}

	public void cacheActiveRenderInfo(World worldIn, TextureManager textureManagerIn, FontRenderer fontrendererIn,
			Entity entityIn, float partialTicks) {
		if (this.worldObj != worldIn) {
			this.setWorld(worldIn);
		}

		this.renderEngine = textureManagerIn;
		this.entity = entityIn;
		this.fontRenderer = fontrendererIn;
		this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
		this.entityPitch = entityIn.prevRotationPitch
				+ (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
		this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
	}

	public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage) {
		if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn
				.getMaxRenderDistanceSquared()) {
			int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos blockpos = tileentityIn.getPos();
			this.renderTileEntityAt(tileentityIn, (double) blockpos.getX() - staticPlayerX,
					(double) blockpos.getY() - staticPlayerY, (double) blockpos.getZ() - staticPlayerZ, partialTicks,
					destroyStage);
		}

	}

	/**+
	 * Render this TileEntity at a given set of coordinates
	 */
	public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks) {
		this.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
	}

	/**+
	 * Render this TileEntity at a given set of coordinates
	 */
	public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks,
			int destroyStage) {
		TileEntitySpecialRenderer tileentityspecialrenderer = this.getSpecialRenderer(tileEntityIn);
		if (tileentityspecialrenderer != null) {
			try {
				if (DynamicLightsStateManager.isInDynamicLightsPass()) {
					DynamicLightsStateManager.reportForwardRenderObjectPosition2((float) x, (float) y, (float) z);
				}
				tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
				tileEntityIn.addInfoToCrashReport(crashreportcategory);
				throw new ReportedException(crashreport);
			}
		}

	}

	public void setWorld(World worldIn) {
		this.worldObj = worldIn;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
}