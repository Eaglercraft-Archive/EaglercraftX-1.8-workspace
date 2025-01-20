package net.minecraft.world;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

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
public class Explosion {
	private final boolean isFlaming;
	private final boolean isSmoking;
	private final EaglercraftRandom explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3> playerKnockbackMap;

	public Explosion(World worldIn, Entity parEntity, double parDouble1, double parDouble2, double parDouble3,
			float parFloat1, List<BlockPos> parList) {
		this(worldIn, parEntity, parDouble1, parDouble2, parDouble3, parFloat1, false, true, parList);
	}

	public Explosion(World worldIn, Entity parEntity, double parDouble1, double parDouble2, double parDouble3,
			float parFloat1, boolean parFlag, boolean parFlag2, List<BlockPos> parList) {
		this(worldIn, parEntity, parDouble1, parDouble2, parDouble3, parFloat1, parFlag, parFlag2);
		this.affectedBlockPositions.addAll(parList);
	}

	public Explosion(World worldIn, Entity parEntity, double parDouble1, double parDouble2, double parDouble3,
			float size, boolean parFlag, boolean parFlag2) {
		this.explosionRNG = new EaglercraftRandom();
		this.affectedBlockPositions = Lists.newArrayList();
		this.playerKnockbackMap = Maps.newHashMap();
		this.worldObj = worldIn;
		this.exploder = parEntity;
		this.explosionSize = size;
		this.explosionX = parDouble1;
		this.explosionY = parDouble2;
		this.explosionZ = parDouble3;
		this.isFlaming = parFlag;
		this.isSmoking = parFlag2;
	}

	/**+
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA() {
		HashSet hashset = Sets.newHashSet();
		boolean flag = true;

		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < 16; ++k) {
					if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
						double d0 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
						double d1 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
						double d2 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						double d4 = this.explosionX;
						double d6 = this.explosionY;
						double d8 = this.explosionZ;

						for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
							if (iblockstate.getBlock().getMaterial() != Material.air) {
								float f2 = this.exploder != null
										? this.exploder.getExplosionResistance(this, this.worldObj, blockpos,
												iblockstate)
										: iblockstate.getBlock().getExplosionResistance((Entity) null);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if (f > 0.0F && (this.exploder == null
									|| this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
								hashset.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(hashset);
		float f3 = this.explosionSize * 2.0F;
		int j1 = MathHelper.floor_double(this.explosionX - (double) f3 - 1.0D);
		int k1 = MathHelper.floor_double(this.explosionX + (double) f3 + 1.0D);
		int l1 = MathHelper.floor_double(this.explosionY - (double) f3 - 1.0D);
		int l = MathHelper.floor_double(this.explosionY + (double) f3 + 1.0D);
		int i2 = MathHelper.floor_double(this.explosionZ - (double) f3 - 1.0D);
		int i1 = MathHelper.floor_double(this.explosionZ + (double) f3 + 1.0D);
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder,
				new AxisAlignedBB((double) j1, (double) l1, (double) i2, (double) k1, (double) l, (double) i1));
		Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

		for (int j2 = 0; j2 < list.size(); ++j2) {
			Entity entity = (Entity) list.get(j2);
			if (!entity.isImmuneToExplosions()) {
				double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) f3;
				if (d12 <= 1.0D) {
					double d5 = entity.posX - this.explosionX;
					double d7 = entity.posY + (double) entity.getEyeHeight() - this.explosionY;
					double d9 = entity.posZ - this.explosionZ;
					double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);
					if (d13 != 0.0D) {
						d5 = d5 / d13;
						d7 = d7 / d13;
						d9 = d9 / d13;
						double d14 = (double) this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
						double d10 = (1.0D - d12) * d14;
						entity.attackEntityFrom(DamageSource.setExplosionSource(this),
								(float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) f3 + 1.0D)));
						double d11 = EnchantmentProtection.func_92092_a(entity, d10);
						entity.motionX += d5 * d11;
						entity.motionY += d7 * d11;
						entity.motionZ += d9 * d11;
						if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.disableDamage) {
							this.playerKnockbackMap.put((EntityPlayer) entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
						}
					}
				}
			}
		}

	}

	/**+
	 * Does the second part of the explosion (sound, particles, drop
	 * spawn)
	 */
	public void doExplosionB(boolean spawnParticles) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F,
				(1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		if (this.explosionSize >= 2.0F && this.isSmoking) {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY,
					this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		} else {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY,
					this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		}

		if (this.isSmoking) {
			for (int i = 0, l = this.affectedBlockPositions.size(); i < l; ++i) {
				BlockPos blockpos = this.affectedBlockPositions.get(i);
				Block block = this.worldObj.getBlockState(blockpos).getBlock();
				if (spawnParticles) {
					double d0 = (double) ((float) blockpos.getX() + this.worldObj.rand.nextFloat());
					double d1 = (double) ((float) blockpos.getY() + this.worldObj.rand.nextFloat());
					double d2 = (double) ((float) blockpos.getZ() + this.worldObj.rand.nextFloat());
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 = d3 / d6;
					d4 = d4 / d6;
					d5 = d5 / d6;
					double d7 = 0.5D / (d6 / (double) this.explosionSize + 0.1D);
					d7 = d7 * (double) (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					d3 = d3 * d7;
					d4 = d4 * d7;
					d5 = d5 * d7;
					this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
							(d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D,
							(d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5, new int[0]);
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				}

				if (block.getMaterial() != Material.air) {
					if (block.canDropFromExplosion(this)) {
						block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos),
								1.0F / this.explosionSize, 0);
					}

					this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState(), 3);
					block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
				}
			}
		}

		if (this.isFlaming) {
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0, l = this.affectedBlockPositions.size(); i < l; ++i) {
				BlockPos blockpos1 = this.affectedBlockPositions.get(i);
				if (this.worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && this.worldObj
						.getBlockState(blockpos1.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock().isFullBlock()
						&& this.explosionRNG.nextInt(3) == 0) {
					this.worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
				}
			}
		}

	}

	public Map<EntityPlayer, Vec3> getPlayerKnockbackMap() {
		return this.playerKnockbackMap;
	}

	/**+
	 * Returns either the entity that placed the explosive block,
	 * the entity that caused the explosion or null.
	 */
	public EntityLivingBase getExplosivePlacedBy() {
		return this.exploder == null ? null
				: (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy()
						: (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
	}

	public void func_180342_d() {
		this.affectedBlockPositions.clear();
	}

	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}
}