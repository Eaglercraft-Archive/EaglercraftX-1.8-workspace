package net.minecraft.client.particle;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.minecraft.AcceleratedEffectRenderer;
import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;

import java.util.concurrent.Callable;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.google.common.collect.Lists;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.GBufferAcceleratedEffectRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
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
public class EffectRenderer {
	private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
	private static final ResourceLocation particleMaterialsTextures = new ResourceLocation(
			"eagler:glsl/deferred/particles_s.png");
	protected World worldObj;
	private List<EntityFX>[][] fxLayers = new List[4][];
	private List<EntityParticleEmitter> particleEmitters = Lists.newArrayList();
	private TextureManager renderer;
	/**+
	 * RNG.
	 */
	private EaglercraftRandom rand = new EaglercraftRandom();
	private IntObjectMap<IParticleFactory> particleTypes = new IntObjectHashMap<>();

	public static final AcceleratedEffectRenderer vanillaAcceleratedParticleRenderer = new AcceleratedEffectRenderer();
	public IAcceleratedParticleEngine acceleratedParticleRenderer = null;

	public EffectRenderer(World worldIn, TextureManager rendererIn) {
		this.worldObj = worldIn;
		this.renderer = rendererIn;

		for (int i = 0; i < 4; ++i) {
			this.fxLayers[i] = new List[2];

			for (int j = 0; j < 2; ++j) {
				this.fxLayers[i][j] = Lists.newArrayList();
			}
		}

		this.registerVanillaParticles();
		this.acceleratedParticleRenderer = EaglercraftGPU.checkInstancingCapable() ? vanillaAcceleratedParticleRenderer
				: null;
	}

	private void registerVanillaParticles() {
		this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
		this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
		this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
		this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
		this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
		this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
		this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
		this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
		this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
		this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
		this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
		this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
		this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(),
				new EntitySpellParticleFX.InstantFactory());
		this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
		this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
				new EntitySpellParticleFX.AmbientMobFactory());
		this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
		this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
		this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
		this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(),
				new EntityHeartFX.AngryVillagerFactory());
		this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
				new EntityAuraFX.HappyVillagerFactory());
		this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
		this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
		this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
		this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(),
				new EntityEnchantmentTableParticleFX.EnchantmentTable());
		this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
		this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
		this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
		this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
		this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
		this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
		this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
		this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
		this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
		this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
		this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
		this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
		this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
		this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
		this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
		this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
		this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
	}

	public void registerParticle(int id, IParticleFactory particleFactory) {
		this.particleTypes.put(id, particleFactory);
	}

	public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
		this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
	}

	/**+
	 * Spawns the relevant particle according to the particle id.
	 */
	public EntityFX spawnEffectParticle(int particleId, double parDouble1, double parDouble2, double parDouble3,
			double parDouble4, double parDouble5, double parDouble6, int... parArrayOfInt) {
		IParticleFactory iparticlefactory = this.particleTypes.get(particleId);
		if (iparticlefactory != null) {
			EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, parDouble1, parDouble2,
					parDouble3, parDouble4, parDouble5, parDouble6, parArrayOfInt);
			if (entityfx != null) {
				this.addEffect(entityfx);
				return entityfx;
			}
		}

		return null;
	}

	public void addEffect(EntityFX effect) {
		int i = effect.getFXLayer();
		int j = effect.getAlpha() != 1.0F ? 0 : 1;
		if (this.fxLayers[i][j].size() >= 4000) {
			this.fxLayers[i][j].remove(0);
		}

		this.fxLayers[i][j].add(effect);
	}

	public void updateEffects() {
		for (int i = 0; i < 4; ++i) {
			this.updateEffectLayer(i);
		}

		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0, l = this.particleEmitters.size(); i < l; ++i) {
			EntityParticleEmitter entityparticleemitter = this.particleEmitters.get(i);
			entityparticleemitter.onUpdate();
			if (entityparticleemitter.isDead) {
				arraylist.add(entityparticleemitter);
			}
		}

		this.particleEmitters.removeAll(arraylist);
	}

	private void updateEffectLayer(int parInt1) {
		for (int i = 0; i < 2; ++i) {
			this.updateEffectAlphaLayer(this.fxLayers[parInt1][i]);
		}

	}

	private void updateEffectAlphaLayer(List<EntityFX> parList) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < parList.size(); ++i) {
			EntityFX entityfx = (EntityFX) parList.get(i);
			this.tickParticle(entityfx);
			if (entityfx.isDead) {
				arraylist.add(entityfx);
			}
		}

		parList.removeAll(arraylist);
	}

	private void tickParticle(final EntityFX parEntityFX) {
		try {
			parEntityFX.onUpdate();
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
			final int i = parEntityFX.getFXLayer();
			crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>() {
				public String call() throws Exception {
					return parEntityFX.toString();
				}
			});
			crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>() {
				public String call() throws Exception {
					return i == 0 ? "MISC_TEXTURE"
							: (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i));
				}
			});
			throw new ReportedException(crashreport);
		}
	}

	public boolean hasParticlesInAlphaLayer() {
		for (int i = 0; i < 3; ++i) {
			if (!this.fxLayers[i][0].isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**+
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles(Entity entityIn, float partialTicks, int pass) {
		float f = ActiveRenderInfo.getRotationX();
		float f1 = ActiveRenderInfo.getRotationZ();
		float f2 = ActiveRenderInfo.getRotationYZ();
		float f3 = ActiveRenderInfo.getRotationXY();
		float f4 = ActiveRenderInfo.getRotationXZ();
		EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		if (!DeferredStateManager.isDeferredRenderer()) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		GlStateManager.alphaFunc(GL_GREATER, 0.003921569F);

		for (int i = 0; i < 3; ++i) {
			for (int j = 1; j >= 0; --j) {
				if (pass != 2 && j != pass) {
					continue;
				}
				if (!this.fxLayers[i][j].isEmpty()) {
//					switch (j) {
//					case 0:
//						GlStateManager.depthMask(false);
//						break;
//					case 1:
//						GlStateManager.depthMask(true);
//					}

					float texCoordWidth = 0.001f;
					float texCoordHeight = 0.001f;
					switch (i) {
					case 0:
					default:
						GBufferAcceleratedEffectRenderer.isMaterialNormalTexture = false;
						this.renderer.bindTexture(particleTextures);
						if (DeferredStateManager.isDeferredRenderer()) {
							GlStateManager.setActiveTexture(GL_TEXTURE2);
							this.renderer.bindTexture(particleMaterialsTextures);
							GlStateManager.setActiveTexture(GL_TEXTURE0);
						}
						texCoordWidth = texCoordHeight = 1.0f / 256.0f;
						break;
					case 1:
						GBufferAcceleratedEffectRenderer.isMaterialNormalTexture = true;
						this.renderer.bindTexture(TextureMap.locationBlocksTexture);
						TextureMap blockMap = (TextureMap) this.renderer.getTexture(TextureMap.locationBlocksTexture);
						texCoordWidth = 1.0f / blockMap.getWidth();
						texCoordHeight = 1.0f / blockMap.getHeight();
					}

					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer worldrenderer = tessellator.getWorldRenderer();
					worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

					boolean legacyRenderingHasOccured = false;

					if (acceleratedParticleRenderer != null) {
						acceleratedParticleRenderer.begin(partialTicks);
					}

					for (int k = 0; k < this.fxLayers[i][j].size(); ++k) {
						final EntityFX entityfx = (EntityFX) this.fxLayers[i][j].get(k);

						try {
							if (acceleratedParticleRenderer == null
									|| !entityfx.renderAccelerated(acceleratedParticleRenderer, entityIn, partialTicks,
											f, f4, f1, f2, f3)) {
								entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
								legacyRenderingHasOccured = true;
							}
						} catch (Throwable throwable) {
							CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
							CrashReportCategory crashreportcategory = crashreport
									.makeCategory("Particle being rendered");
							crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>() {
								public String call() throws Exception {
									return entityfx.toString();
								}
							});
							final int l = i;
							crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>() {
								public String call() throws Exception {
									return l == 0 ? "MISC_TEXTURE"
											: (l == 1 ? "TERRAIN_TEXTURE"
													: (l == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + l));
								}
							});
							throw new ReportedException(crashreport);
						}
					}

					if (legacyRenderingHasOccured) {
						tessellator.draw();
					} else {
						worldrenderer.finishDrawing();
					}

					if (acceleratedParticleRenderer != null) {
						acceleratedParticleRenderer.draw(texCoordWidth, texCoordHeight);
					}
				}
			}
		}

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
	}

	public void renderLitParticles(Entity entityIn, float parFloat1) {
		float f = 0.017453292F;
		float f1 = MathHelper.cos(entityIn.rotationYaw * 0.017453292F);
		float f2 = MathHelper.sin(entityIn.rotationYaw * 0.017453292F);
		float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
		float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
		float f5 = MathHelper.cos(entityIn.rotationPitch * 0.017453292F);

		for (int i = 0; i < 2; ++i) {
			List list = this.fxLayers[3][i];
			if (!list.isEmpty()) {
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldrenderer = tessellator.getWorldRenderer();

				for (int j = 0; j < list.size(); ++j) {
					EntityFX entityfx = (EntityFX) list.get(j);
					entityfx.renderParticle(worldrenderer, entityIn, parFloat1, f1, f5, f2, f3, f4);
				}
			}
		}

	}

	public void clearEffects(World worldIn) {
		this.worldObj = worldIn;

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				this.fxLayers[i][j].clear();
			}
		}

		this.particleEmitters.clear();
	}

	public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
		if (state.getBlock().getMaterial() != Material.air) {
			state = state.getBlock().getActualState(state, this.worldObj, pos);
			byte b0 = 4;

			for (int i = 0; i < b0; ++i) {
				for (int j = 0; j < b0; ++j) {
					for (int k = 0; k < b0; ++k) {
						double d0 = (double) pos.getX() + ((double) i + 0.5D) / (double) b0;
						double d1 = (double) pos.getY() + ((double) j + 0.5D) / (double) b0;
						double d2 = (double) pos.getZ() + ((double) k + 0.5D) / (double) b0;
						this.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - (double) pos.getX() - 0.5D,
								d1 - (double) pos.getY() - 0.5D, d2 - (double) pos.getZ() - 0.5D, state))
										.func_174846_a(pos));
					}
				}
			}

		}
	}

	/**+
	 * Adds block hit particles for the specified block
	 */
	public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = this.worldObj.getBlockState(pos);
		Block block = iblockstate.getBlock();
		if (block.getRenderType() != -1) {
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			float f = 0.1F;
			double d0 = (double) i
					+ this.rand.nextDouble()
							* (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double) (f * 2.0F))
					+ (double) f + block.getBlockBoundsMinX();
			double d1 = (double) j
					+ this.rand.nextDouble()
							* (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double) (f * 2.0F))
					+ (double) f + block.getBlockBoundsMinY();
			double d2 = (double) k
					+ this.rand.nextDouble()
							* (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double) (f * 2.0F))
					+ (double) f + block.getBlockBoundsMinZ();
			if (side == EnumFacing.DOWN) {
				d1 = (double) j + block.getBlockBoundsMinY() - (double) f;
			}

			if (side == EnumFacing.UP) {
				d1 = (double) j + block.getBlockBoundsMaxY() + (double) f;
			}

			if (side == EnumFacing.NORTH) {
				d2 = (double) k + block.getBlockBoundsMinZ() - (double) f;
			}

			if (side == EnumFacing.SOUTH) {
				d2 = (double) k + block.getBlockBoundsMaxZ() + (double) f;
			}

			if (side == EnumFacing.WEST) {
				d0 = (double) i + block.getBlockBoundsMinX() - (double) f;
			}

			if (side == EnumFacing.EAST) {
				d0 = (double) i + block.getBlockBoundsMaxX() + (double) f;
			}

			this.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate))
					.func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public void moveToAlphaLayer(EntityFX effect) {
		this.moveToLayer(effect, 1, 0);
	}

	public void moveToNoAlphaLayer(EntityFX effect) {
		this.moveToLayer(effect, 0, 1);
	}

	private void moveToLayer(EntityFX effect, int parInt1, int parInt2) {
		for (int i = 0; i < 4; ++i) {
			if (this.fxLayers[i][parInt1].contains(effect)) {
				this.fxLayers[i][parInt1].remove(effect);
				this.fxLayers[i][parInt2].add(effect);
			}
		}

	}

	public String getStatistics() {
		int i = 0;

		for (int j = 0; j < 4; ++j) {
			for (int k = 0; k < 2; ++k) {
				i += this.fxLayers[j][k].size();
			}
		}

		return "" + i;
	}
}