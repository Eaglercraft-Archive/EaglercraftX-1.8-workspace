package net.minecraft.client.renderer;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

import java.util.Arrays;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;

import java.util.concurrent.Callable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.EffectPipelineFXAA;
import net.lax1dude.eaglercraft.v1_8.opengl.GameOverlayFramebuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BetterFrustum;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DebugFramebufferView;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredConfig;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShaderConfig;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EmissiveItems;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceTagRenderer;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal.ChunkCullAdapter;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.Config;

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
public class EntityRenderer implements IResourceManagerReloadListener {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
	private static final ResourceLocation locationRainDeferredPng = new ResourceLocation(
			"eagler:glsl/deferred/rain.png");
	private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
	public static boolean anaglyphEnable;
	public static int anaglyphField;
	private Minecraft mc;
	private final IResourceManager resourceManager;
	private EaglercraftRandom random = new EaglercraftRandom();
	private float farPlaneDistance;
	public final ItemRenderer itemRenderer;
	private final MapItemRenderer theMapItemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();
	private float thirdPersonDistance = 4.0F;
	/**+
	 * Third person distance temp
	 */
	private float thirdPersonDistanceTemp = 4.0F;
	private float smoothCamYaw;
	private float smoothCamPitch;
	private float smoothCamFilterX;
	private float smoothCamFilterY;
	private float smoothCamPartialTicks;
	private float fovModifierHand;
	private float fovModifierHandPrev;
	private float bossColorModifier;
	private float bossColorModifierPrev;
	private boolean cloudFog;
	private boolean renderHand = true;
	private boolean drawBlockOutline = true;
	/**+
	 * Previous frame time in milliseconds
	 */
	private long prevFrameTime = Minecraft.getSystemTime();
	private long renderEndNanoTime;
	private final DynamicTexture lightmapTexture;
	private final int[] lightmapColors;
	private final ResourceLocation locationLightMap;
	private boolean lightmapUpdateNeeded;
	private float torchFlickerX;
	private float torchFlickerDX;
	private int rainSoundCounter;
	private float[] rainXCoords = new float[1024];
	private float[] rainYCoords = new float[1024];
	/**+
	 * Fog color buffer
	 */
	private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float fogColor2;
	private float fogColor1;
	private int debugViewDirection = 0;
	private boolean debugView = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw;
	private double cameraPitch;
	private int shaderIndex;
	private boolean useShader;
	private int frameCount;
	private GameOverlayFramebuffer overlayFramebuffer;
	private float eagPartialTicks = 0.0f;
	public float currentProjMatrixFOV = 0.0f;
	private boolean initializedOF = false;

	public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
		this.useShader = false;
		this.frameCount = 0;
		this.mc = mcIn;
		this.resourceManager = resourceManagerIn;
		this.itemRenderer = mcIn.getItemRenderer();
		this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
		this.lightmapTexture = new DynamicTexture(16, 16);
		this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
		this.lightmapColors = this.lightmapTexture.getTextureData();
		this.overlayFramebuffer = new GameOverlayFramebuffer();

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.loadIdentity();
		float f3 = 0.00390625F;
		GlStateManager.scale(f3, f3, f3);
		GlStateManager.translate(8.0F, 8.0F, 8.0F);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f = (float) (j - 16);
				float f1 = (float) (i - 16);
				float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
				this.rainXCoords[i << 5 | j] = -f1 / f2;
				this.rainYCoords[i << 5 | j] = f / f2;
			}
		}

	}

	public boolean isShaderActive() {
		return false;
	}

	public void func_181022_b() {
	}

	public void switchUseShader() {
		this.useShader = !this.useShader;
	}

	/**+
	 * What shader to use when spectating this entity
	 */
	public void loadEntityShader(Entity entityIn) {
	}

	public void activateNextShader() {
	}

	private void loadShader(ResourceLocation resourceLocationIn) {
		this.useShader = false;
	}

	public void onResourceManagerReload(IResourceManager var1) {
	}

	/**+
	 * Updates the entity renderer
	 */
	public void updateRenderer() {
		this.updateFovModifierHand();
		this.updateTorchFlicker();
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistanceTemp = this.thirdPersonDistance;
		if (this.mc.gameSettings.smoothCamera) {
			float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f1 = f * f * f * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
			this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		} else {
			this.smoothCamFilterX = 0.0F;
			this.smoothCamFilterY = 0.0F;
			this.mouseFilterXAxis.reset();
			this.mouseFilterYAxis.reset();
		}

		if (this.mc.getRenderViewEntity() == null) {
			this.mc.setRenderViewEntity(this.mc.thePlayer);
		}

		float f3 = this.mc.theWorld.getLightBrightness(
				DeferredStateManager.isDeferredRenderer() ? new BlockPos(this.mc.getRenderViewEntity()).up()
						: new BlockPos(this.mc.getRenderViewEntity()));
		float f4 = (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
		float f2 = f3 * (1.0F - f4) + f4;
		this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
		this.bossColorModifierPrev = this.bossColorModifier;
		if (BossStatus.hasColorModifier) {
			this.bossColorModifier += 0.05F;
			if (this.bossColorModifier > 1.0F) {
				this.bossColorModifier = 1.0F;
			}

			BossStatus.hasColorModifier = false;
		} else if (this.bossColorModifier > 0.0F) {
			this.bossColorModifier -= 0.0125F;
		}

	}

	public void updateShaderGroupSize(int width, int height) {
	}

	/**+
	 * Finds what block or object the mouse is over at the specified
	 * partial tick time. Args: partialTickTime
	 */
	public void getMouseOver(float partialTicks) {
		Entity entity = this.mc.getRenderViewEntity();
		if (entity != null) {
			if (this.mc.theWorld != null) {
				this.mc.pointedEntity = null;
				double d0 = (double) this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
				double d1 = d0;
				Vec3 vec3 = entity.getPositionEyes(partialTicks);
				boolean flag = false;
				boolean flag1 = true;
				if (this.mc.playerController.extendedReach()) {
					d0 = 6.0D;
					d1 = 6.0D;
				} else {
					if (d0 > 3.0D) {
						flag = true;
					}

					d0 = d0;
				}

				if (this.mc.objectMouseOver != null) {
					d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
				}

				Vec3 vec31 = entity.getLook(partialTicks);
				Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
				this.pointedEntity = null;
				Vec3 vec33 = null;
				float f = 1.0F;
				List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity,
						entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0)
								.expand((double) f, (double) f, (double) f),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
							public boolean apply(Entity entity2) {
								return entity2.canBeCollidedWith();
							}
						}));
				double d2 = d1;

				for (int i = 0; i < list.size(); ++i) {
					Entity entity1 = (Entity) list.get(i);
					float f1 = entity1.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1,
							(double) f1);
					MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
					if (axisalignedbb.isVecInside(vec3)) {
						if (d2 >= 0.0D) {
							this.pointedEntity = entity1;
							vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
							d2 = 0.0D;
						}
					} else if (movingobjectposition != null) {
						double d3 = vec3.distanceTo(movingobjectposition.hitVec);
						if (d3 < d2 || d2 == 0.0D) {
							if (entity1 == entity.ridingEntity) {
								if (d2 == 0.0D) {
									this.pointedEntity = entity1;
									vec33 = movingobjectposition.hitVec;
								}
							} else {
								this.pointedEntity = entity1;
								vec33 = movingobjectposition.hitVec;
								d2 = d3;
							}
						}
					}
				}

				if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
					this.pointedEntity = null;
					this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS,
							vec33, (EnumFacing) null, new BlockPos(vec33));
				}

				if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
					if (this.pointedEntity instanceof EntityLivingBase
							|| this.pointedEntity instanceof EntityItemFrame) {
						this.mc.pointedEntity = this.pointedEntity;
					}
				}
			}
		}
	}

	/**+
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		float f = 1.0F;
		if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.mc.getRenderViewEntity();
			f = abstractclientplayer.getFovModifier();
		}

		this.fovModifierHandPrev = this.fovModifierHand;
		this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;
		if (this.fovModifierHand > 1.5F) {
			this.fovModifierHand = 1.5F;
		}

		if (this.fovModifierHand < 0.1F) {
			this.fovModifierHand = 0.1F;
		}

	}

	/**+
	 * Changes the field of view of the player depending on if they
	 * are underwater or not
	 */
	public float getFOVModifier(float partialTicks, boolean parFlag) {
		if (this.debugView) {
			return 90.0F;
		} else {
			Entity entity = this.mc.getRenderViewEntity();
			float f = 70.0F;
			if (parFlag) {
				f = this.mc.isZoomKey ? this.mc.adjustedZoomValue : this.mc.gameSettings.fovSetting;
				f = f * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
			}

			if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F) {
				float f1 = (float) ((EntityLivingBase) entity).deathTime + partialTicks;
				f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
			}

			Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
			if (block.getMaterial() == Material.water) {
				f = f * 60.0F / 70.0F;
			}

			return f;
		}
	}

	private void hurtCameraEffect(float partialTicks) {
		if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.mc.getRenderViewEntity();
			float f = (float) entitylivingbase.hurtTime - partialTicks;
			if (entitylivingbase.getHealth() <= 0.0F) {
				float f1 = (float) entitylivingbase.deathTime + partialTicks;
				GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
			}

			if (f < 0.0F) {
				return;
			}

			f = f / (float) entitylivingbase.maxHurtTime;
			f = MathHelper.sin(f * f * f * f * 3.1415927F);
			float f2 = entitylivingbase.attackedAtYaw;
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
		}

	}

	/**+
	 * Setups all the GL settings for view bobbing. Args:
	 * partialTickTime
	 */
	private void setupViewBobbing(float partialTicks) {
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
			float f2 = entityplayer.prevCameraYaw
					+ (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
			float f3 = entityplayer.prevCameraPitch
					+ (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
			GlStateManager.translate(MathHelper.sin(f1 * 3.1415927F) * f2 * 0.5F,
					-Math.abs(MathHelper.cos(f1 * 3.1415927F) * f2), 0.0F);
			GlStateManager.rotate(MathHelper.sin(f1 * 3.1415927F) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * 3.1415927F - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
		}
	}

	/**+
	 * sets up player's eye (or camera in third person mode)
	 */
	private void orientCamera(float partialTicks) {
		Entity entity = this.mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
		double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
		double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
			f = (float) ((double) f + 1.0D);
			GlStateManager.translate(0.0F, 0.3F, 0.0F);
			if (!this.mc.gameSettings.debugCamEnable) {
				BlockPos blockpos = new BlockPos(entity);
				IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				if (block == Blocks.bed) {
					int j = ((EnumFacing) iblockstate.getValue(BlockBed.FACING)).getHorizontalIndex();
					GlStateManager.rotate((float) (j * 90), 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.rotate(
						entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F,
						0.0F, -1.0F, 0.0F);
				GlStateManager.rotate(
						entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
						-1.0F, 0.0F, 0.0F);
			}
		} else if (this.mc.gameSettings.thirdPersonView > 0) {
			double d3 = (double) (this.thirdPersonDistanceTemp
					+ (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);
			if (this.mc.gameSettings.debugCamEnable) {
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
			} else {
				float f1 = entity.rotationYaw;
				float f2 = entity.rotationPitch;
				if (this.mc.gameSettings.thirdPersonView == 2) {
					f2 += 180.0F;
				}

				double d4 = (double) (-MathHelper.sin(f1 / 180.0F * 3.1415927F)
						* MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
				double d5 = (double) (MathHelper.cos(f1 / 180.0F * 3.1415927F)
						* MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
				double d6 = (double) (-MathHelper.sin(f2 / 180.0F * 3.1415927F)) * d3;

				for (int i = 0; i < 8; ++i) {
					float f3 = (float) ((i & 1) * 2 - 1);
					float f4 = (float) ((i >> 1 & 1) * 2 - 1);
					float f5 = (float) ((i >> 2 & 1) * 2 - 1);
					f3 = f3 * 0.1F;
					f4 = f4 * 0.1F;
					f5 = f5 * 0.1F;
					MovingObjectPosition movingobjectposition = this.mc.theWorld
							.rayTraceBlocks(new Vec3(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3(
									d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));
					if (movingobjectposition != null) {
						double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));
						if (d7 < d3) {
							d3 = d7;
						}
					}
				}

				if (this.mc.gameSettings.thirdPersonView == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
				GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GlStateManager.translate(0.0F, 0.0F, -0.1F);
		}

		if (!this.mc.gameSettings.debugCamEnable) {
			GlStateManager.rotate(
					entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F,
					0.0F, 0.0F);
			if (entity instanceof EntityAnimal) {
				EntityAnimal entityanimal = (EntityAnimal) entity;
				GlStateManager.rotate(entityanimal.prevRotationYawHead
						+ (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F,
						0.0F, 1.0F, 0.0F);
			} else {
				GlStateManager.rotate(
						entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F,
						0.0F, 1.0F, 0.0F);
			}
		}

		GlStateManager.translate(0.0F, -f, 0.0F);
		d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
		d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
		d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
		this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
	}

	/**+
	 * sets up projection, view effects, camera position/rotation
	 */
	private void setupCameraTransform(float partialTicks, int pass) {
		this.farPlaneDistance = (float) (this.mc.gameSettings.renderDistanceChunks * 16 + 16);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.loadIdentity();
		float f = 0.07F;
		if (this.mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (-(pass * 2 - 1)) * f, 0.0F, 0.0F);
		}

		if (this.cameraZoom != 1.0D) {
			GlStateManager.translate((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
			GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
		}

		float farPlane = this.farPlaneDistance * 2.0f * MathHelper.SQRT_2;
		GlStateManager.gluPerspective(currentProjMatrixFOV = this.getFOVModifier(partialTicks, true),
				(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, farPlane);
		DeferredStateManager.setGBufferNearFarPlanes(0.05f, farPlane);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.loadIdentity();
		if (this.mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(partialTicks);
		if (this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(partialTicks);
		}

		float f1 = this.mc.thePlayer.prevTimeInPortal
				+ (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
		if (f1 > 0.0F) {
			byte b0 = 20;
			if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
				b0 = 7;
			}

			float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
			f2 = f2 * f2;
			GlStateManager.rotate(((float) this.rendererUpdateCount + partialTicks) * (float) b0, 0.0F, 1.0F, 1.0F);
			GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
			GlStateManager.rotate(-((float) this.rendererUpdateCount + partialTicks) * (float) b0, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(partialTicks);
		if (this.debugView) {
			switch (this.debugViewDirection) {
			case 0:
				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 1:
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 2:
				GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				break;
			case 4:
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}

	}

	/**+
	 * Render player hand
	 */
	private void renderHand(float partialTicks, int xOffset) {
		if (!this.debugView) {
			if (DynamicLightsStateManager.isInDynamicLightsPass()) {
				DynamicLightsStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
			}
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			float f = 0.07F;
			if (this.mc.gameSettings.anaglyph) {
				GlStateManager.translate((float) (-(xOffset * 2 - 1)) * f, 0.0F, 0.0F);
			}

			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.loadIdentity();
			if (this.mc.gameSettings.anaglyph) {
				GlStateManager.translate((float) (xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GlStateManager.pushMatrix();
			this.hurtCameraEffect(partialTicks);
			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(partialTicks);
			}

			boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase
					&& ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping();
			if (this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI
					&& !this.mc.playerController.isSpectator()) {
				this.enableLightmap();
				this.itemRenderer.renderItemInFirstPerson(partialTicks);
				this.disableLightmap();
			}

			GlStateManager.popMatrix();
			if (this.mc.gameSettings.thirdPersonView == 0 && !flag) {
				this.itemRenderer.renderOverlays(partialTicks);
				this.hurtCameraEffect(partialTicks);
			}

			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(partialTicks);
			}

		}
	}

	public void disableLightmap() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void enableLightmap() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void disableLightmapStatic() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void enableLightmapStatic() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**+
	 * Recompute a random value that is applied to block color in
	 * updateLightmap()
	 */
	private void updateTorchFlicker() {
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX
				+ (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX * 0.9D);
		this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
		this.lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float partialTicks) {
		if (this.lightmapUpdateNeeded) {
			WorldClient worldclient = this.mc.theWorld;
			if (worldclient != null) {
				float f = worldclient.getSunBrightness(1.0F);
				float f1 = f * 0.95F + 0.05F;

				for (int i = 0; i < 256; ++i) {
					float f2 = worldclient.provider.getLightBrightnessTable()[i / 16] * f1;
					float f3 = worldclient.provider.getLightBrightnessTable()[i % 16]
							* (this.torchFlickerX * 0.1F + 1.5F);
					if (worldclient.getLastLightningBolt() > 0) {
						f2 = worldclient.provider.getLightBrightnessTable()[i / 16];
					}

					float f4 = f2 * (f * 0.65F + 0.35F);
					float f5 = f2 * (f * 0.65F + 0.35F);
					float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
					float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
					float f8 = f4 + f3;
					float f9 = f5 + f6;
					float f10 = f2 + f7;
					f8 = f8 * 0.96F + 0.03F;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;
					if (this.bossColorModifier > 0.0F) {
						float f11 = this.bossColorModifierPrev
								+ (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
						f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
						f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
						f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
					}

					if (worldclient.provider.getDimensionId() == 1) {
						f8 = 0.22F + f3 * 0.75F;
						f9 = 0.28F + f6 * 0.75F;
						f10 = 0.25F + f7 * 0.75F;
					}

					if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
						float f15 = this.getNightVisionBrightness(this.mc.thePlayer, partialTicks);
						float f12 = 1.0F / f8;
						if (f12 > 1.0F / f9) {
							f12 = 1.0F / f9;
						}

						if (f12 > 1.0F / f10) {
							f12 = 1.0F / f10;
						}

						f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
						f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
						f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
					}

					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					float f16 = this.mc.gameSettings.gammaSetting;
					float f17 = 1.0F - f8;
					float f13 = 1.0F - f9;
					float f14 = 1.0F - f10;
					f17 = 1.0F - f17 * f17 * f17 * f17;
					f13 = 1.0F - f13 * f13 * f13 * f13;
					f14 = 1.0F - f14 * f14 * f14 * f14;
					f8 = f8 * (1.0F - f16) + f17 * f16;
					f9 = f9 * (1.0F - f16) + f13 * f16;
					f10 = f10 * (1.0F - f16) + f14 * f16;
					f8 = f8 * 0.96F + 0.03F;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;
					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					if (f8 < 0.0F) {
						f8 = 0.0F;
					}

					if (f9 < 0.0F) {
						f9 = 0.0F;
					}

					if (f10 < 0.0F) {
						f10 = 0.0F;
					}

					short short1 = 255;
					int j = (int) (f8 * 255.0F);
					int k = (int) (f9 * 255.0F);
					int l = (int) (f10 * 255.0F);
					this.lightmapColors[i] = short1 << 24 | j | k << 8 | l << 16;
				}

				this.lightmapTexture.updateDynamicTexture();

				GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				this.mc.getTextureManager().bindTexture(this.locationLightMap);
				if (mc.gameSettings.fancyGraphics || mc.gameSettings.ambientOcclusion > 0
						|| DynamicLightsStateManager.isDynamicLightsRender()) {
					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				} else {
					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				}
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
				GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

				this.lightmapUpdateNeeded = false;
			}
		}
	}

	private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
		int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
		return i > 200 ? 1.0F : 0.7F + MathHelper.sin(((float) i - partialTicks) * 3.1415927F * 0.2F) * 0.3F;
	}

	public void func_181560_a(float parFloat1, long parLong1) {
		if (!initializedOF) {
			Config.frameInitHook();
			initializedOF = true;
		}
		boolean flag = Display.isActive() || mc.gameSettings.touchscreen;
		if (!flag && this.mc.gameSettings.pauseOnLostFocus
				&& (!this.mc.gameSettings.touchscreen || !PointerInputAbstraction.getVCursorButtonDown(1))) {
			if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = Minecraft.getSystemTime();
		}

		if (this.mc.inGameHasFocus && flag) {
			this.mc.mouseHelper.mouseXYChange();
			float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			if (this.mc.gameSettings.keyBindZoomCamera.isKeyDown()) {
				f *= 0.7f;
			}
			float f1 = f * f * f * 8.0F;
			float f2 = (float) this.mc.mouseHelper.deltaX * f1;
			float f3 = (float) this.mc.mouseHelper.deltaY * f1;
			byte b0 = 1;
			if (this.mc.gameSettings.invertMouse) {
				b0 = -1;
			}

			if (this.mc.gameSettings.smoothCamera) {
				this.smoothCamYaw += f2;
				this.smoothCamPitch += f3;
				float f4 = parFloat1 - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = parFloat1;
				f2 = this.smoothCamFilterX * f4;
				f3 = this.smoothCamFilterY * f4;
				this.mc.thePlayer.setAngles(f2, f3 * (float) b0);
			} else {
				this.smoothCamYaw = 0.0F;
				this.smoothCamPitch = 0.0F;
				this.mc.thePlayer.setAngles(f2, f3 * (float) b0);
			}
		}

		if (!this.mc.skipRenderWorld) {
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			final ScaledResolution scaledresolution = mc.scaledResolution;
			int l = scaledresolution.getScaledWidth();
			int i1 = scaledresolution.getScaledHeight();
			final int j1 = PointerInputAbstraction.getVCursorX() * l / this.mc.displayWidth;
			final int k1 = i1 - PointerInputAbstraction.getVCursorY() * i1 / this.mc.displayHeight - 1;
			int l1 = this.mc.gameSettings.limitFramerate;
			if (this.mc.theWorld != null) {
				int i = Math.min(Minecraft.getDebugFPS(), l1);
				i = Math.max(i, 60);
				long j = EagRuntime.nanoTime() - parLong1;
				long k = Math.max((long) (1000000000 / i / 4) - j, 0L);
				this.renderWorld(parFloat1, EagRuntime.nanoTime() + k);
				this.renderEndNanoTime = EagRuntime.nanoTime();
				final boolean b = !this.mc.gameSettings.hideGUI || this.mc.currentScreen != null;
				if (b) {
					GlStateManager.alphaFunc(GL_GREATER, 0.1F);
					long framebufferAge = this.overlayFramebuffer.getAge();
					if (framebufferAge == -1l || framebufferAge > (Minecraft.getDebugFPS() < 25 ? 125l : 75l)) {
						this.overlayFramebuffer.beginRender(mc.displayWidth, mc.displayHeight);
						GlStateManager.colorMask(true, true, true, true);
						GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
						GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
						GlStateManager.enableOverlayFramebufferBlending();
						if (b) {
							this.mc.ingameGUI.renderGameOverlay(parFloat1);
						}
						GlStateManager.disableOverlayFramebufferBlending();
						this.overlayFramebuffer.endRender();
					}
				}
				if (b) {
					this.setupOverlayRendering();
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					if (Minecraft.isFancyGraphicsEnabled()) {
						this.mc.ingameGUI.renderVignette(this.mc.thePlayer.getBrightness(parFloat1), l, i1);
					}
					this.mc.ingameGUI.renderGameOverlayCrosshairs(l, i1);
					GlStateManager.bindTexture(this.overlayFramebuffer.getTexture());
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.enableAlpha();
					GlStateManager.disableDepth();
					GlStateManager.depthMask(false);
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer worldrenderer = tessellator.getWorldRenderer();
					worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
					worldrenderer.pos(0.0D, (double) i1, -90.0D).tex(0.0D, 0.0D).endVertex();
					worldrenderer.pos((double) l, (double) i1, -90.0D).tex(1.0D, 0.0D).endVertex();
					worldrenderer.pos((double) l, 0.0D, -90.0D).tex(1.0D, 1.0D).endVertex();
					worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 1.0D).endVertex();
					tessellator.draw();
					GlStateManager.depthMask(true);
					GlStateManager.enableDepth();
					GlStateManager.disableBlend();
					if (this.mc.gameSettings.hudPlayer) { // give the player model HUD good fps
						this.mc.ingameGUI.drawEaglerPlayerOverlay(l - 3,
								3 + this.mc.ingameGUI.overlayDebug.playerOffset, parFloat1);
					}
				}
			} else {
				GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GlStateManager.matrixMode(GL_PROJECTION);
				GlStateManager.loadIdentity();
				GlStateManager.matrixMode(GL_MODELVIEW);
				GlStateManager.loadIdentity();
				this.setupOverlayRendering();
				this.renderEndNanoTime = EagRuntime.nanoTime();
			}

			this.mc.notifRenderer.renderOverlay(j1, k1);

			if (this.mc.currentScreen != null) {
				GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
				float f = 1.0f;
				final float[] ff = new float[] { 1.0f };

				try {
					f = mc.currentScreen.getEaglerScale();
					int mx, my;
					if (f == 1.0f) {
						mx = j1;
						my = k1;
					} else {
						mx = GuiScreen.applyEaglerScale(f, j1, l);
						my = GuiScreen.applyEaglerScale(f, k1, i1);
						GlStateManager.pushMatrix();
						float fff = (1.0f - f) * 0.5f;
						GlStateManager.translate(fff * l, fff * i1, 0.0f);
						GlStateManager.scale(f, f, f);
					}
					ff[0] = f;
					this.mc.currentScreen.drawScreen(mx, my, parFloat1);
					if (f != 1.0f) {
						GlStateManager.popMatrix();
					}
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
					crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>() {
						public String call() throws Exception {
							return EntityRenderer.this.mc.currentScreen.getClass().getName();
						}
					});
					crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>() {
						public String call() throws Exception {
							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d)",
									new Object[] { Integer.valueOf(j1), Integer.valueOf(k1),
											Integer.valueOf(PointerInputAbstraction.getVCursorX()),
											Integer.valueOf(PointerInputAbstraction.getVCursorY()) });
						}
					});
					crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>() {
						public String call() throws Exception {
							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
									new Object[] { Integer.valueOf(scaledresolution.getScaledWidth()),
											Integer.valueOf(scaledresolution.getScaledHeight()),
											Integer.valueOf(EntityRenderer.this.mc.displayWidth),
											Integer.valueOf(EntityRenderer.this.mc.displayHeight),
											Integer.valueOf(scaledresolution.getScaleFactor()) });
						}
					});
					crashreportcategory.addCrashSectionCallable("Eagler Scale", new Callable<String>() {
						public String call() throws Exception {
							return "" + ff[0];
						}
					});
					throw new ReportedException(crashreport);
				}

				this.mc.voiceOverlay.drawOverlay();
			}
		}
	}

	public void renderStreamIndicator(float partialTicks) {
	}

	private boolean isDrawBlockOutline() {
		if (!this.drawBlockOutline) {
			return false;
		} else {
			Entity entity = this.mc.getRenderViewEntity();
			boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
			if (flag && !((EntityPlayer) entity).capabilities.allowEdit) {
				ItemStack itemstack = ((EntityPlayer) entity).getCurrentEquippedItem();
				if (this.mc.objectMouseOver != null
						&& this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
					Block block = this.mc.theWorld.getBlockState(blockpos).getBlock();
					if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
						flag = block.hasTileEntity() && this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory;
					} else {
						flag = itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
					}
				}
			}

			return flag;
		}
	}

	private void renderWorldDirections(float partialTicks) {
		if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug()
				&& !this.mc.gameSettings.reducedDebugInfo) {
			Entity entity = this.mc.getRenderViewEntity();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			EaglercraftGPU.glLineWidth(1.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.pushMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.loadIdentity();
			this.orientCamera(partialTicks);
			GlStateManager.translate(0.0F, entity.getEyeHeight(), 0.0F);
			RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
			RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
			RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}

	}

	public void renderWorld(float partialTicks, long finishTimeNano) {
		this.updateLightmap(partialTicks);
		if (this.mc.getRenderViewEntity() == null) {
			this.mc.setRenderViewEntity(this.mc.thePlayer);
		}

		this.getMouseOver(partialTicks);

		boolean fxaa = !this.mc.gameSettings.shaders
				&& ((this.mc.gameSettings.fxaa == 0 && this.mc.gameSettings.fancyGraphics)
						|| this.mc.gameSettings.fxaa == 1);
		if (fxaa) {
			EffectPipelineFXAA.begin(this.mc.displayWidth, this.mc.displayHeight);
		}

		VoiceTagRenderer.clearTagsDrawnSet();

		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.5F);
		boolean dlights = DynamicLightsStateManager.isDynamicLightsRender();
		if (dlights) {
			updateDynamicLightListEagler(partialTicks);
		}
		if (this.mc.gameSettings.anaglyph && !this.mc.gameSettings.shaders) {
			if (dlights) {
				GlStateManager.enableExtensionPipeline();
			}
			try {
				anaglyphField = 0;
				GlStateManager.colorMask(false, true, true, false);
				this.renderWorldPass(0, partialTicks, finishTimeNano);
				anaglyphField = 1;
				GlStateManager.colorMask(true, false, false, false);
				this.renderWorldPass(1, partialTicks, finishTimeNano);
				GlStateManager.colorMask(true, true, true, false);
			} finally {
				if (dlights) {
					GlStateManager.disableExtensionPipeline();
				}
			}
		} else {
			if (this.mc.gameSettings.shaders) {
				try {
					this.eaglercraftShaders(partialTicks, finishTimeNano);
				} catch (Throwable t) {
					logger.error("Exception caught running deferred render!");
					logger.error(t);
					EaglerDeferredPipeline.instance.resetContextStateAfterException();
					logger.error("Suspending shaders...");
					EaglerDeferredPipeline.isSuspended = true;

				}
				mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
			} else {
				mc.effectRenderer.acceleratedParticleRenderer = EaglercraftGPU.checkInstancingCapable()
						? EffectRenderer.vanillaAcceleratedParticleRenderer
						: null;
				if (dlights) {
					GlStateManager.enableExtensionPipeline();
				}
				try {
					this.renderWorldPass(2, partialTicks, finishTimeNano);
				} finally {
					if (dlights) {
						GlStateManager.disableExtensionPipeline();
					}
				}
			}
		}

		if (fxaa) {
			EffectPipelineFXAA.end();
		}
	}

	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
		RenderGlobal renderglobal = this.mc.renderGlobal;
		EffectRenderer effectrenderer = this.mc.effectRenderer;
		boolean flag = this.isDrawBlockOutline();
		GlStateManager.enableCull();
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		this.updateFogColor(partialTicks);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.setupCameraTransform(partialTicks, pass);
		boolean isDynamicLights = DynamicLightsStateManager.isDynamicLightsRender();
		if (isDynamicLights) {
			DynamicLightsStateManager.setupInverseViewMatrix();
		}
		ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
		Frustum frustum = new Frustum();
		Entity entity = this.mc.getRenderViewEntity();
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
		TileEntityRendererDispatcher.staticPlayerX = d0; // hack, needed for some eagler stuff
		TileEntityRendererDispatcher.staticPlayerY = d1;
		TileEntityRendererDispatcher.staticPlayerZ = d2;
		frustum.setPosition(d0, d1, d2);
		if (this.mc.gameSettings.renderDistanceChunks >= 4) {
			this.setupFog(-1, partialTicks);
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			float vigg = this.getFOVModifier(partialTicks, true);
			GlStateManager.gluPerspective(vigg, (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance * 4.0F);
			GlStateManager.matrixMode(GL_MODELVIEW);
			renderglobal.renderSky(partialTicks, pass);
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			GlStateManager.gluPerspective(vigg, (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance * MathHelper.SQRT_2);
			GlStateManager.matrixMode(GL_MODELVIEW);
		}

		this.setupFog(0, partialTicks);
		GlStateManager.shadeModel(GL_SMOOTH);
		if (entity.posY + (double) entity.getEyeHeight() < 128.0D) {
			this.renderCloudsCheck(renderglobal, partialTicks, pass);
		}

		this.setupFog(0, partialTicks);
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		renderglobal.setupTerrain(entity, (double) partialTicks, frustum, this.frameCount++,
				this.mc.thePlayer.isSpectator());
		if (pass == 0 || pass == 2) {
			this.mc.renderGlobal.updateChunks(finishTimeNano);
		}

		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, pass, entity);
		GlStateManager.enableAlpha();
		renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, pass, entity);
		this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, pass, entity);
		this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		GlStateManager.shadeModel(GL_FLAT);
		if (!this.debugView) {
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			RenderHelper.enableStandardItemLighting();
			renderglobal.renderEntities(entity, frustum, partialTicks);
			RenderHelper.disableStandardItemLighting();
			this.disableLightmap();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag) {
				EntityPlayer entityplayer = (EntityPlayer) entity;
				GlStateManager.disableAlpha();
				if (isDynamicLights) {
					GlStateManager.disableExtensionPipeline();
				}
				renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
				GlStateManager.enableAlpha();
				if (isDynamicLights) {
					GlStateManager.enableExtensionPipeline();
				}
			}
		}

		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
			EntityPlayer entityplayer1 = (EntityPlayer) entity;
			GlStateManager.disableAlpha();
			if (isDynamicLights) {
				GlStateManager.disableExtensionPipeline();
			}
			renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
			GlStateManager.enableAlpha();
			if (isDynamicLights) {
				GlStateManager.enableExtensionPipeline();
			}
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, 1, 1, 0);
		this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(),
				entity, partialTicks);
		this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
		GlStateManager.disableBlend();
		if (!this.debugView) {
			this.enableLightmap();
			effectrenderer.renderLitParticles(entity, partialTicks);
			RenderHelper.disableStandardItemLighting();
			this.setupFog(0, partialTicks);
			if (isDynamicLights) {
				DynamicLightsStateManager.bindAcceleratedEffectRenderer(effectrenderer);
				DynamicLightsStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
			}
			effectrenderer.renderParticles(entity, partialTicks, 2);
			if (isDynamicLights) {
				effectrenderer.acceleratedParticleRenderer = null;
			}
			this.disableLightmap();
		}

		GlStateManager.depthMask(false);
		GlStateManager.enableCull();
		this.renderRainSnow(partialTicks);
		GlStateManager.depthMask(true);
		renderglobal.renderWorldBorder(entity, partialTicks);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		this.setupFog(0, partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.shadeModel(GL_SMOOTH);
		renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, pass, entity);
		GlStateManager.shadeModel(GL_FLAT);
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();
		if (entity.posY + (double) entity.getEyeHeight() >= 128.0D) {
			this.renderCloudsCheck(renderglobal, partialTicks, pass);
		}

		if (this.renderHand) {
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
			this.renderHand(partialTicks, pass);
			this.renderWorldDirections(partialTicks);
		}

	}

	private void updateDynamicLightListEagler(float partialTicks) {
		DynamicLightsStateManager.clearRenderList();
		Entity entity = this.mc.getRenderViewEntity();
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
		AxisAlignedBB entityAABB = new AxisAlignedBB(d0 - 48.0, d1 - 32.0, d2 - 48.0, d0 + 48.0, d1 + 32.0, d2 + 48.0);
		List<Entity> entities = this.mc.theWorld.getEntitiesWithinAABB(Entity.class, entityAABB);
		for (int i = 0, l = entities.size(); i < l; ++i) {
			entities.get(i).renderDynamicLightsEaglerSimple(partialTicks);
		}
		DynamicLightsStateManager.commitLightSourceBuckets(d0, d1, d2);
	}

	private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass) {
		if (this.mc.gameSettings.func_181147_e() != 0) {
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.pushMatrix();
			this.setupFog(0, partialTicks);
			// renderGlobalIn.renderClouds(partialTicks, pass);
			renderGlobalIn.cloudRenderer.renderClouds(partialTicks, pass);
			GlStateManager.disableFog();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance * MathHelper.SQRT_2);
			GlStateManager.matrixMode(GL_MODELVIEW);
		}

	}

	private void addRainParticles() {
		float f = this.mc.theWorld.getRainStrength(1.0F);
		if (!this.mc.gameSettings.fancyGraphics) {
			f /= 2.0F;
		}

		if (f != 0.0F) {
			this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
			Entity entity = this.mc.getRenderViewEntity();
			WorldClient worldclient = this.mc.theWorld;
			BlockPos blockpos = new BlockPos(entity);
			byte b0 = 10;
			double d0 = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			int i = 0;
			int j = (int) (100.0F * f * f);
			if (this.mc.gameSettings.particleSetting == 1) {
				j >>= 1;
			} else if (this.mc.gameSettings.particleSetting == 2) {
				j = 0;
			}

			for (int k = 0; k < j; ++k) {
				BlockPos blockpos1 = worldclient
						.getPrecipitationHeight(blockpos.add(this.random.nextInt(b0) - this.random.nextInt(b0), 0,
								this.random.nextInt(b0) - this.random.nextInt(b0)));
				BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos1);
				BlockPos blockpos2 = blockpos1.down();
				Block block = worldclient.getBlockState(blockpos2).getBlock();
				if (blockpos1.getY() <= blockpos.getY() + b0 && blockpos1.getY() >= blockpos.getY() - b0
						&& biomegenbase.canSpawnLightningBolt()
						&& biomegenbase.getFloatTemperature(blockpos1) >= 0.15F) {
					double d3 = this.random.nextDouble();
					double d4 = this.random.nextDouble();
					if (block.getMaterial() == Material.lava) {
						this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) blockpos1.getX() + d3,
								(double) ((float) blockpos1.getY() + 0.1F) - block.getBlockBoundsMinY(),
								(double) blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					} else if (block.getMaterial() != Material.air) {
						block.setBlockBoundsBasedOnState(worldclient, blockpos2);
						++i;
						if (this.random.nextInt(i) == 0) {
							d0 = (double) blockpos2.getX() + d3;
							d1 = (double) ((float) blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY() - 1.0D;
							d2 = (double) blockpos2.getZ() + d4;
						}

						if (!DeferredStateManager.isDeferredRenderer()) {
							this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double) blockpos2.getX() + d3,
									(double) ((float) blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY(),
									(double) blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					}
				}
			}

			if (i > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;
				if (d1 > (double) (blockpos.getY() + 1) && worldclient.getPrecipitationHeight(blockpos)
						.getY() > MathHelper.floor_float((float) blockpos.getY())) {
					this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
				} else {
					this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
				}
			}

		}
	}

	/**+
	 * Render rain and snow
	 */
	protected void renderRainSnow(float partialTicks) {
		float f = this.mc.theWorld.getRainStrength(partialTicks);
		if (f > 0.0F) {
			boolean df = DeferredStateManager.isInDeferredPass();
			this.enableLightmap();
			Entity entity = this.mc.getRenderViewEntity();
			WorldClient worldclient = this.mc.theWorld;
			int i = MathHelper.floor_double(entity.posX);
			int j = MathHelper.floor_double(entity.posY);
			int k = MathHelper.floor_double(entity.posZ);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.disableCull();
			if (!df) {
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.alphaFunc(GL_GREATER, 0.1F);
				if (DynamicLightsStateManager.isInDynamicLightsPass()) {
					DynamicLightsStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
				}
			} else {
				GlStateManager.enableAlpha();
				DeferredStateManager.setHDRTranslucentPassBlendFunc();
				DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
				GlStateManager.alphaFunc(GL_GREATER, 0.01F);
				GlStateManager.depthMask(false);
				GlStateManager.enableDepth();
				EaglerDeferredPipeline.instance.setForwardRenderLightFactors(0.65f,
						4.75f - MathHelper.clamp_float(DeferredStateManager.getSunHeight() * 8.0f - 3.0f, 0.0f, 4.0f),
						1.0f, 0.03f);
			}
			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);
			double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
			double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
			double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
			int l = MathHelper.floor_double(d1);
			byte b0 = 5;
			if (df) {
				b0 = 8;
			} else if (this.mc.gameSettings.fancyGraphics) {
				b0 = 10;
			}

			byte b1 = -1;
			float f1 = (float) this.rendererUpdateCount + partialTicks;
			worldrenderer.setTranslation(-d0, -d1, -d2);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos blockpos$mutableblockpos = new BlockPos();

			for (int i1 = k - b0; i1 <= k + b0; ++i1) {
				for (int j1 = i - b0; j1 <= i + b0; ++j1) {
					int k1 = (i1 - k + 16) * 32 + j1 - i + 16;
					double d3 = (double) this.rainXCoords[k1] * 0.5D;
					double d4 = (double) this.rainYCoords[k1] * 0.5D;
					blockpos$mutableblockpos.func_181079_c(j1, 0, i1);
					BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos$mutableblockpos);
					if (biomegenbase.canSpawnLightningBolt() || biomegenbase.getEnableSnow()) {
						int l1 = worldclient.getPrecipitationHeight(blockpos$mutableblockpos).getY();
						int i2 = j - b0;
						int j2 = j + b0;
						if (i2 < l1) {
							i2 = l1;
						}

						if (j2 < l1) {
							j2 = l1;
						}

						int k2 = l1;
						if (l1 < l) {
							k2 = l;
						}

						if (i2 != j2) {
							this.random
									.setSeed((long) (j1 * j1 * 3121 + j1 * 45238971 ^ i1 * i1 * 418711 + i1 * 13761));
							blockpos$mutableblockpos.func_181079_c(j1, i2, i1);
							float f2 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
							if (f2 >= 0.15F) {
								if (b1 != 0) {
									if (b1 >= 0) {
										tessellator.draw();
									}

									b1 = 0;
									this.mc.getTextureManager()
											.bindTexture(df ? locationRainDeferredPng : locationRainPng);
									if (df) {
										DeferredStateManager.setRoughnessConstant(0.5f);
										DeferredStateManager.setMetalnessConstant(0.05f);
										DeferredStateManager.setEmissionConstant(1.0f);
										GlStateManager.color(0.8F, 0.8F, 1.0F, 0.25F);
									}
									worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d5 = ((double) (this.rendererUpdateCount + j1 * j1 * 3121 + j1 * 45238971
										+ i1 * i1 * 418711 + i1 * 13761 & 31) + (double) partialTicks) / 32.0D
										* (3.0D + this.random.nextDouble());
								double d6 = (double) ((float) j1 + 0.5F) - entity.posX;
								double d7 = (double) ((float) i1 + 0.5F) - entity.posZ;
								float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float) b0;
								float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
								blockpos$mutableblockpos.func_181079_c(j1, k2, i1);
								int l2 = worldclient.getCombinedLight(blockpos$mutableblockpos, 0);
								int i3 = l2 >> 16 & '\uffff';
								int j3 = l2 & '\uffff';
								worldrenderer.pos((double) j1 - d3 + 0.5D, (double) i2, (double) i1 - d4 + 0.5D)
										.tex(0.0D, (double) i2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(i3, j3).endVertex();
								worldrenderer.pos((double) j1 + d3 + 0.5D, (double) i2, (double) i1 + d4 + 0.5D)
										.tex(1.0D, (double) i2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(i3, j3).endVertex();
								worldrenderer.pos((double) j1 + d3 + 0.5D, (double) j2, (double) i1 + d4 + 0.5D)
										.tex(1.0D, (double) j2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(i3, j3).endVertex();
								worldrenderer.pos((double) j1 - d3 + 0.5D, (double) j2, (double) i1 - d4 + 0.5D)
										.tex(0.0D, (double) j2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(i3, j3).endVertex();
							} else {
								if (b1 != 1) {
									if (b1 >= 0) {
										tessellator.draw();
									}

									b1 = 1;
									this.mc.getTextureManager().bindTexture(locationSnowPng);
									if (df) {
										DeferredStateManager.setRoughnessConstant(0.7f);
										DeferredStateManager.setMetalnessConstant(0.05f);
										DeferredStateManager.setEmissionConstant(1.0f);
										GlStateManager.color(1.3F, 1.3F, 1.3F, 0.5F);
									}
									worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d8 = (double) (((float) (this.rendererUpdateCount & 511) + partialTicks)
										/ 512.0F);
								double d9 = this.random.nextDouble()
										+ (double) f1 * 0.01D * (double) ((float) this.random.nextGaussian());
								double d10 = this.random.nextDouble()
										+ (double) (f1 * (float) this.random.nextGaussian()) * 0.001D;
								double d11 = (double) ((float) j1 + 0.5F) - entity.posX;
								double d12 = (double) ((float) i1 + 0.5F) - entity.posZ;
								float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float) b0;
								float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
								blockpos$mutableblockpos.func_181079_c(j1, k2, i1);
								int k3 = (worldclient.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
								int l3 = k3 >> 16 & '\uffff';
								int i4 = k3 & '\uffff';
								worldrenderer.pos((double) j1 - d3 + 0.5D, (double) i2, (double) i1 - d4 + 0.5D)
										.tex(0.0D + d9, (double) i2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(l3, i4).endVertex();
								worldrenderer.pos((double) j1 + d3 + 0.5D, (double) i2, (double) i1 + d4 + 0.5D)
										.tex(1.0D + d9, (double) i2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(l3, i4).endVertex();
								worldrenderer.pos((double) j1 + d3 + 0.5D, (double) j2, (double) i1 + d4 + 0.5D)
										.tex(1.0D + d9, (double) j2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(l3, i4).endVertex();
								worldrenderer.pos((double) j1 - d3 + 0.5D, (double) j2, (double) i1 - d4 + 0.5D)
										.tex(0.0D + d9, (double) j2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(l3, i4).endVertex();
							}
						}
					}
				}
			}

			if (b1 >= 0) {
				tessellator.draw();
			}

			worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.enableCull();
			if (!df) {
				GlStateManager.disableBlend();
			} else {
				GlStateManager.disableAlpha();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				DeferredStateManager.setDefaultMaterialConstants();
				GlStateManager.depthMask(true);
				GlStateManager.disableDepth();
				EaglerDeferredPipeline.instance.setForwardRenderLightFactors(1.0f, 1.0f, 1.0f, 1.0f);
			}
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			this.disableLightmap();
		}
	}

	/**+
	 * Setup orthogonal projection for rendering GUI screen overlays
	 */
	public void setupOverlayRendering() {
		ScaledResolution scaledresolution = mc.scaledResolution;
		GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(),
				0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}

	/**+
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float partialTicks) {
		WorldClient worldclient = this.mc.theWorld;
		Entity entity = this.mc.getRenderViewEntity();
		float f = 0.25F + 0.75F * (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
		f = 1.0F - (float) Math.pow((double) f, 0.25D);
		Vec3 vec3 = worldclient.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
		float f1 = (float) vec3.xCoord;
		float f2 = (float) vec3.yCoord;
		float f3 = (float) vec3.zCoord;
		Vec3 vec31 = worldclient.getFogColor(partialTicks);
		this.fogColorRed = (float) vec31.xCoord;
		this.fogColorGreen = (float) vec31.yCoord;
		this.fogColorBlue = (float) vec31.zCoord;
		if (this.mc.gameSettings.renderDistanceChunks >= 4) {
			double d0 = -1.0D;
			Vec3 vec32 = MathHelper.sin(worldclient.getCelestialAngleRadians(partialTicks)) > 0.0F
					? new Vec3(d0, 0.0D, 0.0D)
					: new Vec3(1.0D, 0.0D, 0.0D);
			float f5 = (float) entity.getLook(partialTicks).dotProduct(vec32);
			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			if (f5 > 0.0F) {
				float[] afloat = worldclient.provider
						.calcSunriseSunsetColors(worldclient.getCelestialAngle(partialTicks), partialTicks);
				if (afloat != null) {
					f5 = f5 * afloat[3];
					this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
					this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
					this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
				}
			}
		}

		this.fogColorRed += (f1 - this.fogColorRed) * f;
		this.fogColorGreen += (f2 - this.fogColorGreen) * f;
		this.fogColorBlue += (f3 - this.fogColorBlue) * f;
		float f8 = worldclient.getRainStrength(partialTicks);
		if (f8 > 0.0F) {
			float f4 = 1.0F - f8 * 0.5F;
			float f10 = 1.0F - f8 * 0.4F;
			this.fogColorRed *= f4;
			this.fogColorGreen *= f4;
			this.fogColorBlue *= f10;
		}

		float f9 = worldclient.getThunderStrength(partialTicks);
		if (f9 > 0.0F) {
			float f11 = 1.0F - f9 * 0.5F;
			this.fogColorRed *= f11;
			this.fogColorGreen *= f11;
			this.fogColorBlue *= f11;
		}

		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
		if (this.cloudFog) {
			Vec3 vec33 = worldclient.getCloudColour(partialTicks);
			this.fogColorRed = (float) vec33.xCoord;
			this.fogColorGreen = (float) vec33.yCoord;
			this.fogColorBlue = (float) vec33.zCoord;
		} else if (block.getMaterial() == Material.water) {
			float f12 = (float) EnchantmentHelper.getRespiration(entity) * 0.2F;
			if (entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).isPotionActive(Potion.waterBreathing)) {
				f12 = f12 * 0.3F + 0.6F;
			}

			this.fogColorRed = 0.02F + f12;
			this.fogColorGreen = 0.02F + f12;
			this.fogColorBlue = 0.2F + f12;
		} else if (block.getMaterial() == Material.lava) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
		this.fogColorRed *= f13;
		this.fogColorGreen *= f13;
		this.fogColorBlue *= f13;
		double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks)
				* worldclient.provider.getVoidFogYFactor();
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.blindness)) {
			int i = ((EntityLivingBase) entity).getActivePotionEffect(Potion.blindness).getDuration();
			if (i < 20) {
				d1 *= (double) (1.0F - (float) i / 20.0F);
			} else {
				d1 = 0.0D;
			}
		}

		if (d1 < 1.0D) {
			if (d1 < 0.0D) {
				d1 = 0.0D;
			}

			d1 = d1 * d1;
			this.fogColorRed = (float) ((double) this.fogColorRed * d1);
			this.fogColorGreen = (float) ((double) this.fogColorGreen * d1);
			this.fogColorBlue = (float) ((double) this.fogColorBlue * d1);
		}

		if (this.bossColorModifier > 0.0F) {
			float f14 = this.bossColorModifierPrev
					+ (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
			this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
			this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
			this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
		}

		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.nightVision)) {
			float f15 = this.getNightVisionBrightness((EntityLivingBase) entity, partialTicks);
			float f6 = 1.0F / this.fogColorRed;
			if (f6 > 1.0F / this.fogColorGreen) {
				f6 = 1.0F / this.fogColorGreen;
			}

			if (f6 > 1.0F / this.fogColorBlue) {
				f6 = 1.0F / this.fogColorBlue;
			}

			this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
			this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
			this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
		}

		if (this.mc.gameSettings.anaglyph) {
			float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f16;
			this.fogColorGreen = f17;
			this.fogColorBlue = f7;
		}

		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F);
	}

	/**+
	 * Sets up the fog to be rendered. If the arg passed in is -1
	 * the fog starts at 0 and goes to 80% of far plane distance and
	 * is used for sky rendering.
	 */
	private void setupFog(int partialTicks, float parFloat1) {
		Entity entity = this.mc.getRenderViewEntity();
		boolean flag = false;
		if (entity instanceof EntityPlayer) {
			flag = ((EntityPlayer) entity).capabilities.isCreativeMode;
		}

		EaglercraftGPU.glFog(GL_FOG_COLOR,
				this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		EaglercraftGPU.glNormal3f(0.0F, -1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, parFloat1);
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.blindness)) {
			float f1 = 5.0F;
			int i = ((EntityLivingBase) entity).getActivePotionEffect(Potion.blindness).getDuration();
			if (i < 20) {
				f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) i / 20.0F);
			}

			GlStateManager.setFog(GL_LINEAR);
			if (partialTicks == -1) {
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1 * 0.8F);
			} else {
				GlStateManager.setFogStart(f1 * 0.25F);
				GlStateManager.setFogEnd(f1);
			}
			// EaglercraftGPU.glFogi('\u855a', '\u855b');
		} else if (this.cloudFog) {
			GlStateManager.setFog(GL_EXP);
			GlStateManager.setFogDensity(0.1F);
		} else if (block.getMaterial() == Material.water) {
			GlStateManager.setFog(GL_EXP);
			if (entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).isPotionActive(Potion.waterBreathing)) {
				GlStateManager.setFogDensity(0.01F);
			} else {
				GlStateManager.setFogDensity(0.1F - (float) EnchantmentHelper.getRespiration(entity) * 0.03F);
			}
		} else if (block.getMaterial() == Material.lava) {
			GlStateManager.setFog(GL_EXP);
			GlStateManager.setFogDensity(2.0F);
		} else if (partialTicks != -1 && !this.mc.gameSettings.fog) {
			GlStateManager.setFog(GL_EXP);
			GlStateManager.setFogDensity(0.0F);
		} else {
			GlStateManager.setFogDensity(0.001F);
			float f = this.farPlaneDistance;
			GlStateManager.setFog(GL_LINEAR);
			if (partialTicks == -1) {
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f);
			} else {
				GlStateManager.setFogStart(f * 0.75F);
				GlStateManager.setFogEnd(f);
			}

			if (this.mc.theWorld.provider.doesXZShowFog((int) entity.posX, (int) entity.posZ)) {
				GlStateManager.setFogStart(f * 0.05F);
				GlStateManager.setFogEnd(Math.min(f, 192.0F) * 0.5F);
			}
		}

		GlStateManager.enableColorMaterial();
		GlStateManager.enableFog();
	}

	/**+
	 * Update and return fogColorBuffer with the RGBA values passed
	 * as arguments
	 */
	private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}

	public MapItemRenderer getMapItemRenderer() {
		return this.theMapItemRenderer;
	}

	private static final Vector4f tmpVec4f_1 = new Vector4f();
	private static final Matrix4f tmpMat4f_1 = new Matrix4f();
	private int shadowFrameIndex = 0;

	private double blockWaveOffsetX = 0.0;
	private double blockWaveOffsetY = 0.0;
	private double blockWaveOffsetZ = 0.0;

	private void eaglercraftShaders(float partialTicks, long finishTimeNano) {
		if ((EaglerDeferredPipeline.isSuspended || EaglerDeferredPipeline.instance == null)
				|| (mc.currentScreen != null && mc.currentScreen instanceof GuiShaderConfig)) {
			EaglerDeferredPipeline.renderSuspended();
			return;
		}
		EaglerDeferredPipeline.instance.setPartialTicks(partialTicks);
		eagPartialTicks = partialTicks;
		EaglerDeferredConfig conf = mc.gameSettings.deferredShaderConf;
		boolean flag = isDrawBlockOutline();
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		setupCameraTransform(partialTicks, 2);
		EaglerDeferredPipeline.instance.loadViewMatrix();
		ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
		Frustum frustum = new Frustum();
		Entity entity = mc.getRenderViewEntity();
		if (entity == null) {
			entity = mc.thePlayer;
		}
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
		EaglerDeferredPipeline.instance.setRenderPosGlobal(d0, d1, d2);
		EaglerDeferredPipeline.instance.updateReprojectionCoordinates(d0, d1, d2);
		float eyeHeight = entity.getEyeHeight();
		frustum.setPosition(d0, d1, d2);

//		StringBuilder builder = new StringBuilder();
//		long ll = Double.doubleToLongBits(d0);
//		for(int i = 63; i >= 0; --i) {
//			builder.append((ll >>> i) & 1);
//		}
//		System.out.println(builder.toString());

		float waveTimer = (float) ((EagRuntime.steadyTimeMillis() % 600000l) * 0.001);
		DeferredStateManager.setWaterWindOffset(waveTimer * 0.25f, waveTimer * 0.13f, waveTimer * 1.45f, waveTimer);

		float blockWaveDistX = (float) (d0 - blockWaveOffsetX);
		float blockWaveDistY = (float) (d1 - blockWaveOffsetY);
		float blockWaveDistZ = (float) (d2 - blockWaveOffsetZ);
		if (blockWaveDistX * blockWaveDistX + blockWaveDistY * blockWaveDistY + blockWaveDistZ * blockWaveDistZ > 128.0f
				* 128.0f) {
			blockWaveOffsetX = MathHelper.floor_double(d0);
			blockWaveOffsetY = MathHelper.floor_double(d1);
			blockWaveOffsetZ = MathHelper.floor_double(d2);
			blockWaveDistX = (float) (d0 - blockWaveOffsetX);
			blockWaveDistY = (float) (d1 - blockWaveOffsetY);
			blockWaveDistZ = (float) (d2 - blockWaveOffsetZ);
		}

		boolean wavingBlocks = conf.is_rendering_wavingBlocks;

		DeferredStateManager.setWavingBlockOffset(blockWaveDistX, blockWaveDistY, blockWaveDistZ);
		if (wavingBlocks) {
			DeferredStateManager.setWavingBlockParams(1.0f * waveTimer, 200.0f * waveTimer, 0.0f, 0.0f);
		}

		// if (mc.gameSettings.renderDistanceChunks >= 4) vanilla shows sky not fog

		mc.renderGlobal.setupTerrain(entity, (double) partialTicks, frustum, frameCount++, mc.thePlayer.isSpectator());

		// clear some state:

		GlStateManager.enableCull();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();

		// vanilla solid chunks pass:

		EaglerDeferredPipeline.instance.beginDrawDeferred();
		EaglerDeferredPipeline.instance.beginDrawMainGBuffer();

		EaglerDeferredPipeline.instance.beginDrawMainGBufferTerrain();

		mc.renderGlobal.updateChunks(finishTimeNano);

		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, 2, entity);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.5F);
		if (wavingBlocks)
			DeferredStateManager.enableDrawWavingBlocks();
		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, 2, entity);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, 2, entity);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		if (wavingBlocks)
			DeferredStateManager.disableDrawWavingBlocks();

		// vanilla solid entities:

		EaglerDeferredPipeline.instance.beginDrawMainGBufferEntities();
		if (conf.is_rendering_dynamicLights) {
			DynamicLightManager.setIsRenderingLights(true);
		}

		DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackGBuffer;
		DeferredStateManager.forwardCallbackHandler.reset();

		NameTagRenderer.doRenderNameTags = true;
		NameTagRenderer.nameTagsCount = 0;
		GlStateManager.pushMatrix();
		DeferredStateManager.setDefaultMaterialConstants();
		DeferredStateManager.startUsingEnvMap();
		mc.renderGlobal.renderEntities(entity, frustum, partialTicks);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		EntityFX.interpPosX = d0;
		EntityFX.interpPosY = d1;
		EntityFX.interpPosZ = d2;
		enableLightmap();
		GlStateManager.pushMatrix();
		mc.effectRenderer.renderLitParticles(entity, partialTicks);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		mc.effectRenderer.acceleratedParticleRenderer = EaglerDeferredPipeline.instance.gbufferEffectRenderer;
		mc.effectRenderer.renderParticles(entity, partialTicks, 1);
		mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();
		DeferredStateManager.endUsingEnvMap();
		disableLightmap();
		DynamicLightManager.setIsRenderingLights(false);
		NameTagRenderer.doRenderNameTags = false;

		EaglerDeferredPipeline.instance.endDrawMainGBuffer();

		// calculate sun matrix and angle:

		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		float celestialAngle = mc.theWorld.getCelestialAngle(partialTicks) * 360.0F;
		GlStateManager.rotate(DeferredStateManager.sunAngle, 0.0F, 1.0F, 0.0F);

		if (mc.theWorld.provider.getDimensionId() == 0) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(celestialAngle + 90.0f, 1.0F, 0.0F, 0.0F);
			tmpVec4f_1.set(0.0f, 0.0f, 1.0f);
			GlStateManager.transform(tmpVec4f_1, tmpVec4f_1);
			tmpVec4f_1.normalise();
			DeferredStateManager.setCurrentSunAngle(tmpVec4f_1);
			if (tmpVec4f_1.y > 0.1f) {
				celestialAngle += 180.0f;
			}
			GlStateManager.popMatrix();
		} else {
			tmpVec4f_1.set(0.0f, 1.0f, 0.0f);
			DeferredStateManager.setCurrentSunAngle(tmpVec4f_1);
			celestialAngle = 270.0f;
		}

		if (conf.is_rendering_shadowsSun_clamped > 0) {
			if (conf.is_rendering_shadowsColored) {
				DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackSun;
				DeferredStateManager.forwardCallbackHandler.reset();
			} else {
				DeferredStateManager.forwardCallbackHandler = null;
			}
			EaglerDeferredPipeline.instance.beginDrawMainShadowMap();
			++shadowFrameIndex;
			EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(0);
			GlStateManager.enableCull();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			int shadowMapDist = 16;
			GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);

			setupSunCameraTransform(celestialAngle);

			DeferredStateManager.loadShadowPassViewMatrix();
			DeferredStateManager.loadSunShadowMatrixLOD0();

			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();

			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.loadIdentity();

			final AxisAlignedBB aabb = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD0(), d0, d1 + eyeHeight,
					d2);
			DeferredStateManager.setShadowMapBounds(aabb);

			final BetterFrustum shadowLOD0Frustrum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD0());

			ChunkCullAdapter shadowCullAdapter = (renderChunk) -> {
				if (renderChunk.shadowLOD0FrameIndex != shadowFrameIndex) {
					renderChunk.shadowLOD0FrameIndex = shadowFrameIndex;
					AxisAlignedBB aabb2 = renderChunk.boundingBox;
					if (aabb.intersectsWith(aabb2)) {
						int shadowVisRet = shadowLOD0Frustrum.intersectAab((float) (aabb2.minX - d0),
								(float) (aabb2.minY - d1 - eyeHeight), (float) (aabb2.minZ - d2),
								(float) (aabb2.maxX - d0), (float) (aabb2.maxY - d1 - eyeHeight),
								(float) (aabb2.maxZ - d2));
						renderChunk.shadowLOD0InFrustum = shadowVisRet == BetterFrustum.INSIDE
								? RenderChunk.ShadowFrustumState.INSIDE
								: (shadowVisRet == BetterFrustum.INTERSECT ? RenderChunk.ShadowFrustumState.INTERSECT
										: RenderChunk.ShadowFrustumState.OUTSIDE);
					} else {
						renderChunk.shadowLOD0InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
						return true;
					}
				}
				return renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
			};

			mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb, shadowCullAdapter);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, 0.5F);
			if (wavingBlocks) {
				DeferredStateManager.enableDrawWavingBlocks();
				enableLightmap();
			}
			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb, shadowCullAdapter);
			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb, shadowCullAdapter);
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			if (wavingBlocks) {
				DeferredStateManager.disableDrawWavingBlocks();
				disableLightmap();
			}

			mc.renderGlobal.renderShadowLODEntities(entity, partialTicks, (renderChunk) -> {
				return renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
						&& (renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE
								|| renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE_BB);
			}, (renderChunk, renderManager, renderEntity) -> {
				boolean b;
				if (renderEntity.ignoreFrustumCheck) {
					return false;
				} else if (!renderEntity.isInRangeToRender3d(d0, d1, d2)) {
					return true;
				} else if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
						&& ((b = renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE)
								|| renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE)) {
					return b;
				} else {
					AxisAlignedBB aabbEntity = renderEntity.getEntityBoundingBox();
					if (aabbEntity.func_181656_b() || aabbEntity.getAverageEdgeLength() == 0.0) {
						aabbEntity = new AxisAlignedBB(d0 - 2.0, d1 - 2.0, d2 - 2.0, d0 + 2.0, d1 + 2.0, d2 + 2.0);
					}
					if (shadowLOD0Frustrum.testAab((float) (aabbEntity.minX - d0),
							(float) (aabbEntity.minY - d1 - eyeHeight), (float) (aabbEntity.minZ - d2),
							(float) (aabbEntity.maxX - d0), (float) (aabbEntity.maxY - d1 - eyeHeight),
							(float) (aabbEntity.maxZ - d2))) {
						return !renderManager.shouldRender(renderEntity, frustum, d0, d1, d2);
					} else {
						return true;
					}
				}
			});
			disableLightmap();

			if (conf.is_rendering_shadowsColored) {
				EaglerDeferredPipeline.instance.beginDrawColoredShadows();
				List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackHandler.renderPassList;
				for (int i = 0, l = lst.size(); i < l; ++i) {
					lst.get(i).draw(ShadersRenderPassFuture.PassType.SHADOW);
				}
				DeferredStateManager.forwardCallbackHandler.reset();
				mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.TRANSLUCENT, aabb, shadowCullAdapter);
				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
				if (conf.is_rendering_realisticWater) {
					GlStateManager.disableTexture2D();
					GlStateManager.color(0.173f, 0.239f, 0.957f, 0.25f);
					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.REALISTIC_WATER, aabb,
							shadowCullAdapter);
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					GlStateManager.enableTexture2D();
				}
				EaglerDeferredPipeline.instance.endDrawColoredShadows();
			}
			disableLightmap();

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.disableAlpha();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.popMatrix();

			if (conf.is_rendering_shadowsSun_clamped > 1) {
				EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(1);
				if (conf.is_rendering_shadowsColored) {
					DeferredStateManager.forwardCallbackHandler.reset();
				}

				GlStateManager.enableCull();
				GlStateManager.matrixMode(GL_PROJECTION);
				GlStateManager.pushMatrix();
				GlStateManager.loadIdentity();
				shadowMapDist = 32;
				GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);

				setupSunCameraTransform(celestialAngle);

				DeferredStateManager.loadShadowPassViewMatrix();
				DeferredStateManager.loadSunShadowMatrixLOD1();

				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();

				GlStateManager.matrixMode(GL_MODELVIEW);
				GlStateManager.loadIdentity();

				final AxisAlignedBB aabb2 = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD1(), d0,
						d1 + eyeHeight, d2);
				DeferredStateManager.setShadowMapBounds(aabb2);

				BetterFrustum shadowLOD1Frustrum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD1());

				ChunkCullAdapter shadowCullAdapter2 = (renderChunk) -> {
					if (renderChunk.shadowLOD1FrameIndex != shadowFrameIndex) {
						renderChunk.shadowLOD1FrameIndex = shadowFrameIndex;
						if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
								&& renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
							renderChunk.shadowLOD1InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
							return true;
						} else {
							AxisAlignedBB aabb3 = renderChunk.boundingBox;
							if (aabb2.intersectsWith(aabb3)) {
								int shadowVisRet = shadowLOD1Frustrum.intersectAab((float) (aabb3.minX - d0),
										(float) (aabb3.minY - d1 - eyeHeight), (float) (aabb3.minZ - d2),
										(float) (aabb3.maxX - d0), (float) (aabb3.maxY - d1 - eyeHeight),
										(float) (aabb3.maxZ - d2));
								renderChunk.shadowLOD1InFrustum = shadowVisRet == BetterFrustum.INSIDE
										? RenderChunk.ShadowFrustumState.INSIDE
										: (shadowVisRet == BetterFrustum.INTERSECT
												? RenderChunk.ShadowFrustumState.INTERSECT
												: RenderChunk.ShadowFrustumState.OUTSIDE);
							} else {
								renderChunk.shadowLOD1InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
								return true;
							}
						}
					}
					return renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
				};

				mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb2, shadowCullAdapter2);
				GlStateManager.enableAlpha();
				GlStateManager.alphaFunc(GL_GREATER, 0.5F);
				if (wavingBlocks) {
					DeferredStateManager.enableDrawWavingBlocks();
					enableLightmap();
				}
				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb2, shadowCullAdapter2);
				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb2, shadowCullAdapter2);
				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
				GlStateManager.alphaFunc(GL_GREATER, 0.1F);
				if (wavingBlocks) {
					DeferredStateManager.disableDrawWavingBlocks();
					disableLightmap();
				}

				mc.renderGlobal.renderShadowLODEntities(entity, partialTicks, (renderChunk) -> {
					return renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
							&& (renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE
									|| renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE_BB);
				}, (renderChunk, renderManager, renderEntity) -> {
					boolean b;
					if (renderEntity.ignoreFrustumCheck) {
						return false;
					} else if (!renderEntity.isInRangeToRender3d(d0, d1, d2)) {
						return true;
					} else if (renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
							&& (b = renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE)) {
						return b;
					} else {
						AxisAlignedBB aabbEntity = renderEntity.getEntityBoundingBox();
						if (aabbEntity.func_181656_b() || aabbEntity.getAverageEdgeLength() == 0.0) {
							aabbEntity = new AxisAlignedBB(d0 - 2.0, d1 - 2.0, d2 - 2.0, d0 + 2.0, d1 + 2.0, d2 + 2.0);
						}
						if (shadowLOD1Frustrum.testAab((float) (aabbEntity.minX - d0),
								(float) (aabbEntity.minY - d1 - eyeHeight), (float) (aabbEntity.minZ - d2),
								(float) (aabbEntity.maxX - d0), (float) (aabbEntity.maxY - d1 - eyeHeight),
								(float) (aabbEntity.maxZ - d2))) {
							return !renderManager.shouldRender(renderEntity, frustum, d0, d1, d2);
						} else {
							return true;
						}
					}
				});
				disableLightmap();

				if (conf.is_rendering_shadowsColored) {
					EaglerDeferredPipeline.instance.beginDrawColoredShadows();
					List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackHandler.renderPassList;
					for (int i = 0, l = lst.size(); i < l; ++i) {
						lst.get(i).draw(ShadersRenderPassFuture.PassType.SHADOW);
					}
					DeferredStateManager.forwardCallbackHandler.reset();
					mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.TRANSLUCENT, aabb2, shadowCullAdapter2);
					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
					if (conf.is_rendering_realisticWater) {
						GlStateManager.disableTexture2D();
						GlStateManager.color(0.173f, 0.239f, 0.957f, 0.25f);
						mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.REALISTIC_WATER, aabb2,
								shadowCullAdapter2);
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
						GlStateManager.enableTexture2D();
					}
					EaglerDeferredPipeline.instance.endDrawColoredShadows();
				}
				disableLightmap();

				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.disableAlpha();
				GlStateManager.matrixMode(GL_PROJECTION);
				GlStateManager.popMatrix();

				if (conf.is_rendering_shadowsSun_clamped > 2) {
					EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(2);

					GlStateManager.enableCull();
					GlStateManager.matrixMode(GL_PROJECTION);
					GlStateManager.pushMatrix();
					GlStateManager.loadIdentity();
					shadowMapDist = 1 << (conf.is_rendering_shadowsSun_clamped + 3);
					GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);

					setupSunCameraTransform(celestialAngle);

					DeferredStateManager.loadShadowPassViewMatrix();
					DeferredStateManager.loadSunShadowMatrixLOD2();

					GlStateManager.disableAlpha();
					GlStateManager.disableBlend();

					GlStateManager.matrixMode(GL_MODELVIEW);
					GlStateManager.loadIdentity();

					DeferredStateManager.loadPassViewMatrix();
					DeferredStateManager.loadPassProjectionMatrix();

					AxisAlignedBB aabb3 = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD2(), d0,
							d1 + eyeHeight, d2);
					DeferredStateManager.setShadowMapBounds(aabb3);

					BetterFrustum shadowLOD2Frustum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD2());

					ChunkCullAdapter shadowCullAdapter3 = (renderChunk) -> {
						if (renderChunk.shadowLOD2FrameIndex != shadowFrameIndex) {
							renderChunk.shadowLOD2FrameIndex = shadowFrameIndex;
							if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
									&& renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
								renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
								return true;
							} else if (renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
									&& renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
								renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
								return true;
							} else {
								AxisAlignedBB aabb4 = renderChunk.boundingBox;
								if (aabb3.intersectsWith(aabb4)) {
									int shadowVisRet = shadowLOD2Frustum.intersectAab((float) (aabb4.minX - d0),
											(float) (aabb4.minY - d1 - eyeHeight), (float) (aabb4.minZ - d2),
											(float) (aabb4.maxX - d0), (float) (aabb4.maxY - d1 - eyeHeight),
											(float) (aabb4.maxZ - d2));
									renderChunk.shadowLOD2InFrustum = shadowVisRet == BetterFrustum.INSIDE
											? RenderChunk.ShadowFrustumState.INSIDE
											: (shadowVisRet == BetterFrustum.INTERSECT
													? RenderChunk.ShadowFrustumState.INTERSECT
													: RenderChunk.ShadowFrustumState.OUTSIDE);
								} else {
									renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
									return true;
								}
							}
						}
						return renderChunk.shadowLOD2InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
					};

					mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb3, shadowCullAdapter3);
					GlStateManager.enableAlpha();
					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb3,
							shadowCullAdapter3);
					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb3, shadowCullAdapter3);
					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
					disableLightmap();

					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					GlStateManager.disableAlpha();
					GlStateManager.matrixMode(GL_PROJECTION);
					GlStateManager.popMatrix();
				}
			}

			EaglerDeferredPipeline.instance.endDrawMainShadowMap();
			if (conf.is_rendering_shadowsColored) {
				DeferredStateManager.forwardCallbackHandler.reset();
			}
		}

		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();

		if (conf.is_rendering_dynamicLights && entity != null && mc.gameSettings.thirdPersonView == 0) {
			if (entity instanceof EntityLivingBase) {
				DynamicLightManager.setIsRenderingLights(true);
				ItemStack itemStack = ((EntityLivingBase) entity).getHeldItem();
				if (itemStack != null) {
					float[] emission = EmissiveItems.getItemEmission(itemStack);
					if (emission != null) {
						float yaw = entity.prevRotationYaw
								+ (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
						yaw *= 0.017453293f;
						float s = 0.2f;
						double d02 = d0 + MathHelper.sin(yaw) * s;
						double d22 = d2 + MathHelper.cos(yaw) * s;
						float mag = 0.7f;
						DynamicLightManager.renderDynamicLight("render_view_entity_holding", d02,
								d1 + entity.getEyeHeight(), d22, emission[0] * mag, emission[1] * mag,
								emission[2] * mag, false);
					}
				}
				DynamicLightManager.setIsRenderingLights(false);
			}
		}

		EaglerDeferredPipeline.instance.combineGBuffersAndIlluminate();

		if (conf.is_rendering_useEnvMap) {
			DeferredStateManager.forwardCallbackHandler = null;
			EaglerDeferredPipeline.instance.beginDrawEnvMap();
			GlStateManager.enableCull();

			EaglerDeferredPipeline.instance.beginDrawEnvMapTop(entity.getEyeHeight());
			EaglerDeferredPipeline.instance.beginDrawEnvMapSolid();
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, 1, entity);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, 0.5F);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, 1, entity);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, 1,
					entity);
			DeferredStateManager.setDefaultMaterialConstants();
			mc.renderGlobal.renderParaboloidTileEntities(entity, partialTicks, 1);
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			EaglerDeferredPipeline.instance.beginDrawEnvMapTranslucent();
			if (conf.is_rendering_realisticWater) {
				GlStateManager.disableTexture2D();
				DeferredStateManager.disableMaterialTexture();
				DeferredStateManager.setRoughnessConstant(0.117f);
				DeferredStateManager.setMetalnessConstant(0.067f);
				DeferredStateManager.setEmissionConstant(0.0f);
				GlStateManager.color(0.173f, 0.239f, 0.957f, 0.65f);
				mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks,
						1, entity);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.enableTexture2D();
				DeferredStateManager.enableMaterialTexture();
			}
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, 1,
					entity);
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
			GlStateManager.disableAlpha();

			EaglerDeferredPipeline.instance.beginDrawEnvMapBottom(entity.getEyeHeight());
			EaglerDeferredPipeline.instance.beginDrawEnvMapSolid();
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, -1, entity);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, 0.5F);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, -1, entity);
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, -1,
					entity);
			DeferredStateManager.setDefaultMaterialConstants();
			mc.renderGlobal.renderParaboloidTileEntities(entity, partialTicks, -1);
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			EaglerDeferredPipeline.instance.beginDrawEnvMapTranslucent();
			if (conf.is_rendering_realisticWater) {
				GlStateManager.disableTexture2D();
				DeferredStateManager.disableMaterialTexture();
				DeferredStateManager.setRoughnessConstant(0.117f);
				DeferredStateManager.setMetalnessConstant(0.067f);
				DeferredStateManager.setEmissionConstant(0.0f);
				GlStateManager.color(0.173f, 0.239f, 0.957f, 0.65f);
				mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks,
						-1, entity);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.enableTexture2D();
				DeferredStateManager.enableMaterialTexture();
			}
			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, -1,
					entity);
			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
			GlStateManager.disableAlpha();

			EaglerDeferredPipeline.instance.endDrawEnvMap();
		}

		if (conf.is_rendering_realisticWater) {
			EaglerDeferredPipeline.instance.beginDrawRealisticWaterMask();
			enableLightmap();
			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks, 2, entity);
			disableLightmap();
			EaglerDeferredPipeline.instance.endDrawRealisticWaterMask();
		}

		int dim = mc.theWorld.provider.getDimensionId();
		float ff;
		if (dim == 0) {
			ff = (this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks) * 4.8f - 2.8f;
			if (ff < 0.0f)
				ff = 0.0f;
			if (ff > 1.0f)
				ff = 1.0f;
		} else {
			ff = 1.0f;
		}

		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.blindness)) {
			float f1 = 5.0F;
			int i = ((EntityLivingBase) entity).getActivePotionEffect(Potion.blindness).getDuration();
			if (i < 20) {
				f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) i / 20.0F);
			}
			if (partialTicks == -1) {
				DeferredStateManager.enableFogLinear(0.0f, f1 * 0.8F, false, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
						1.0f);
			} else {
				DeferredStateManager.enableFogLinear(f1 * 0.25F, f1, false, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
						1.0f);
			}
		} else if (block.getMaterial() == Material.water) {
			updateFogColor(partialTicks); // gen vanilla fog color
			ff *= 0.2f;
			ff += 0.8f;
			fogColorRed *= 0.5f;
			fogColorGreen *= 0.5f;
			fogColorBlue *= 0.5f;
			if (entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).isPotionActive(Potion.waterBreathing)) {
				DeferredStateManager.enableFogExp(0.01F, false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f,
						fogColorRed * ff, fogColorGreen * ff, fogColorBlue * ff, 1.0f);
			} else {
				DeferredStateManager.enableFogExp(0.1F - (float) EnchantmentHelper.getRespiration(entity) * 0.03F,
						false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f, fogColorRed * ff, fogColorGreen * ff,
						fogColorBlue * ff, 1.0f);
			}
		} else if (block.getMaterial() == Material.lava) {
			updateFogColor(partialTicks); // gen vanilla fog color
			DeferredStateManager.enableFogExp(2.0F, false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f, fogColorRed,
					fogColorGreen, fogColorBlue, 1.0f);
		} else {
			float ds = 0.0005f;
			if (mc.gameSettings.renderDistanceChunks < 6) {
				ds *= 3.0f - mc.gameSettings.renderDistanceChunks * 0.33f;
			}
			ds *= 1.5f + mc.theWorld.getRainStrength(partialTicks) * 10.0f
					+ mc.theWorld.getThunderStrength(partialTicks) * 5.0f;
			ds *= MathHelper.clamp_float(6.0f - DeferredStateManager.getSunHeight() * 17.0f, 1.0f, 3.0f);
			if (conf.is_rendering_lightShafts) {
				ds *= Math.max(2.5f - Math.abs(DeferredStateManager.getSunHeight()) * 5.0f, 2.5f);
			}
			DeferredStateManager.enableFogExp(ds, true, 1.0f, 1.0f, 1.0f, 1.0f, ff, ff, ff, 1.0f);
		}

		EaglerDeferredPipeline.instance.beginDrawHDRTranslucent();
		DeferredStateManager.setDefaultMaterialConstants();

		if (conf.is_rendering_realisticWater) {
			EaglerDeferredPipeline.instance.beginDrawRealisticWaterSurface();
			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks, 2, entity);
			EaglerDeferredPipeline.instance.endDrawRealisticWaterSurface();
		}

		EaglerDeferredPipeline.instance.applyGBufferFog();

		EaglerDeferredPipeline.instance.beginDrawTranslucentEntities();

		TileEntityRendererDispatcher.staticPlayerX = d0;
		TileEntityRendererDispatcher.staticPlayerY = d1;
		TileEntityRendererDispatcher.staticPlayerZ = d2;
		mc.getRenderManager().setRenderPosition(d0, d1, d2);

		for (int i = 0; i < mc.theWorld.weatherEffects.size(); ++i) {
			Entity entity1 = (Entity) mc.theWorld.weatherEffects.get(i);
			if (entity1.isInRangeToRender3d(d0, d1, d2)) {
				mc.getRenderManager().renderEntitySimple(entity1, partialTicks);
			}
		}
		disableLightmap();

		DeferredStateManager.forwardCallbackGBuffer.sort(0.0f, 0.0f, 0.0f);
		List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackGBuffer.renderPassList;
		for (int i = 0, l = lst.size(); i < l; ++i) {
			lst.get(i).draw(ShadersRenderPassFuture.PassType.MAIN);
		}
		DeferredStateManager.forwardCallbackGBuffer.reset();

		EaglerDeferredPipeline.instance.beginDrawTranslucentBlocks();
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, 2, entity);

		EaglerDeferredPipeline.instance.beginDrawMainGBufferDestroyProgress();

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(0, GL_SRC_ALPHA, 0, 0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		mc.renderGlobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(),
				entity, partialTicks);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();

		EaglerDeferredPipeline.instance.endDrawMainGBufferDestroyProgress();

		if (mc.effectRenderer.hasParticlesInAlphaLayer()) {
			GlStateManager.pushMatrix();
			mc.effectRenderer.acceleratedParticleRenderer = EaglerDeferredPipeline.instance.forwardEffectRenderer;
			DeferredStateManager.setHDRTranslucentPassBlendFunc();
			DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
			GlStateManager.enableBlend();
			GlStateManager.depthMask(false);
			mc.effectRenderer.renderParticles(entity, partialTicks, 0);
			mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
			GlStateManager.enableBlend();
			GlStateManager.depthMask(true);
		}

		if (conf.is_rendering_useEnvMap) {
			EaglerDeferredPipeline.instance.beginDrawGlassHighlights();
			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.GLASS_HIGHLIGHTS, (double) partialTicks, 2, entity);
			EaglerDeferredPipeline.instance.endDrawGlassHighlights();
		}

		EaglerDeferredPipeline.instance.saveReprojData();

		renderRainSnow(partialTicks);

		GlStateManager.disableBlend();

		if (renderHand) {
			EaglerDeferredPipeline.instance.beginDrawHandOverlay();
			DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
			DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackGBuffer;
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.pushMatrix();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.pushMatrix();
			GlStateManager.enableAlpha();
			renderHand(partialTicks, 2);
			DeferredStateManager.forwardCallbackHandler = null;
			GlStateManager.enableBlend();
			DeferredStateManager.setHDRTranslucentPassBlendFunc();
			lst = DeferredStateManager.forwardCallbackGBuffer.renderPassList;
			for (int i = 0, l = lst.size(); i < l; ++i) {
				lst.get(i).draw(ShadersRenderPassFuture.PassType.MAIN);
			}
			DeferredStateManager.forwardCallbackGBuffer.reset();
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
			EaglerDeferredPipeline.instance.endDrawHandOverlay();
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();
		}

		EaglerDeferredPipeline.instance.endDrawHDRTranslucent();

		EaglerDeferredPipeline.instance.endDrawDeferred();

		GlStateManager.setActiveTexture(GL_TEXTURE1);
		this.mc.getTextureManager().bindTexture(this.locationLightMap);

		GlStateManager.setActiveTexture(GL_TEXTURE0);
		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL_MODELVIEW);

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);

		if (!DebugFramebufferView.debugViewShown) {
			GlStateManager.disableAlpha();
			if (isDrawBlockOutline()) {
				mc.renderGlobal.drawSelectionBox(mc.thePlayer, this.mc.objectMouseOver, 0, partialTicks);
			}
			GlStateManager.enableAlpha();
			if (NameTagRenderer.nameTagsCount > 0) {
				enableLightmap();
				Arrays.sort(NameTagRenderer.nameTagsThisFrame, 0, NameTagRenderer.nameTagsCount, (n1, n2) -> {
					return n1.dst2 < n2.dst2 ? 1 : (n1.dst2 > n2.dst2 ? -1 : 0);
				});
				for (int i = 0; i < NameTagRenderer.nameTagsCount; ++i) {
					NameTagRenderer n = NameTagRenderer.nameTagsThisFrame[i];
					int ii = n.entityIn.getBrightnessForRender(partialTicks);
					int j = ii % 65536;
					int k = ii / 65536;
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F,
							(float) k / 1.0F);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					if (n.maxDistance == -69) {
						// calls renderName instead of renderLivingLabel:
						Render.renderNameAdapter(mc.getRenderManager().getEntityRenderObject(n.entityIn), n.entityIn,
								n.x, n.y, n.z);
					} else {
						mc.getRenderManager().getEntityRenderObject(n.entityIn).renderLivingLabel(n.entityIn, n.str,
								n.x, n.y, n.z, n.maxDistance);
					}
				}
				NameTagRenderer.nameTagsCount = 0;
			}
			disableLightmap();
			GlStateManager.disableLighting();
			mc.renderGlobal.renderWorldBorder(entity, partialTicks);
		}
	}

	public boolean renderHeldItemLight(EntityLivingBase entityLiving, float mag) {
		if (DynamicLightManager.isRenderingLights()) {
			ItemStack itemStack = entityLiving.getHeldItem();
			if (itemStack != null) {
				float[] emission = EmissiveItems.getItemEmission(itemStack);
				if (emission != null) {
					double d0 = entityLiving.prevPosX + (entityLiving.posX - entityLiving.prevPosX) * eagPartialTicks;
					double d1 = entityLiving.prevPosY + (entityLiving.posY - entityLiving.prevPosY) * eagPartialTicks;
					double d2 = entityLiving.prevPosZ + (entityLiving.posZ - entityLiving.prevPosZ) * eagPartialTicks;
					float yaw = entityLiving.prevRotationYaw
							+ (entityLiving.rotationYaw - entityLiving.prevRotationYaw) * eagPartialTicks;
					yaw *= 0.017453293f;
					float s = 0.5f;
					d0 -= MathHelper.sin(yaw) * s;
					d2 += MathHelper.cos(yaw) * s;
					mag *= 0.5f;
					DynamicLightManager.renderDynamicLight("entity_" + entityLiving.getEntityId() + "_holding", d0,
							d1 + entityLiving.getEyeHeight() * 0.63f, d2, emission[0] * mag, emission[1] * mag,
							emission[2] * mag, false);
					return true;
				}
			}
		}
		return false;
	}

	public boolean renderItemEntityLight(Entity entity, float mag) {
		if (DynamicLightManager.isRenderingLights()) {
			ItemStack itemStack = null;
			float offsetX = 0.0f;
			float offsetY = 0.0f;
			float offsetZ = 0.0f;
			if (entity instanceof EntityItem) {
				EntityItem ei = (EntityItem) entity;
				itemStack = ei.getEntityItem();
				offsetY = MathHelper.sin(((float) ei.getAge() + eagPartialTicks) / 10.0F + ei.hoverStart) * 0.1F + 0.3F;
			} else if (entity instanceof EntityItemFrame) {
				itemStack = ((EntityItemFrame) entity).getDisplayedItem();
				Vec3i facingVec = ((EntityItemFrame) entity).facingDirection.getDirectionVec();
				offsetX = facingVec.x * 0.1f;
				offsetZ = facingVec.z * 0.1f;
			}
			if (itemStack != null) {
				float[] emission = EmissiveItems.getItemEmission(itemStack);
				if (emission != null) {
					double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * eagPartialTicks;
					double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * eagPartialTicks;
					double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * eagPartialTicks;
					DynamicLightManager.renderDynamicLight("entity_" + entity.getEntityId() + "_item", d0 + offsetX,
							d1 + offsetY, d2 + offsetZ, emission[0] * mag, emission[1] * mag, emission[2] * mag, false);
					return true;
				}
			}
		}
		return false;
	}

	private static final Matrix4f matrixToBounds_tmpMat4f_1 = new Matrix4f();
	private static final Vector4f matrixToBounds_tmpVec4f_1 = new Vector4f();
	private static final Vector4f[] matrixToBounds_tmpVec4f_array = new Vector4f[] { new Vector4f(-1, -1, -1, 1),
			new Vector4f(-1, -1, 1, 1), new Vector4f(-1, 1, -1, 1), new Vector4f(-1, 1, 1, 1),
			new Vector4f(1, -1, -1, 1), new Vector4f(1, -1, 1, 1), new Vector4f(1, 1, -1, 1),
			new Vector4f(1, 1, 1, 1) };

	private static AxisAlignedBB matrixToBounds(Matrix4f matrixIn, double x, double y, double z) {
		Matrix4f.invert(matrixIn, matrixToBounds_tmpMat4f_1);

		float minX = Integer.MAX_VALUE;
		float minY = Integer.MAX_VALUE;
		float minZ = Integer.MAX_VALUE;
		float maxX = Integer.MIN_VALUE;
		float maxY = Integer.MIN_VALUE;
		float maxZ = Integer.MIN_VALUE;
		Vector4f tmpVec = matrixToBounds_tmpVec4f_1;
		float vx, vy, vz;
		for (int i = 0; i < 8; ++i) {
			Matrix4f.transform(matrixToBounds_tmpMat4f_1, matrixToBounds_tmpVec4f_array[i], tmpVec);
			vx = tmpVec.x;
			vy = tmpVec.y;
			vz = tmpVec.z;
			if (vx < minX)
				minX = vx;
			if (vy < minY)
				minY = vy;
			if (vz < minZ)
				minZ = vz;
			if (vx > maxX)
				maxX = vx;
			if (vy > maxY)
				maxY = vy;
			if (vz > maxZ)
				maxZ = vz;
		}

		return new AxisAlignedBB(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
	}

	public static void setupSunCameraTransform(float celestialAngle) {
		GlStateManager.rotate(celestialAngle + 90.0f, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(-DeferredStateManager.sunAngle, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
	}
}