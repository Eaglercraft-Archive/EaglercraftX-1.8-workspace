package net.minecraft.world;

import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

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
public class Teleporter {
	private final WorldServer worldServerInstance;
	private final EaglercraftRandom random;
	/**+
	 * Stores successful portal placement locations for rapid
	 * lookup.
	 */
	private final LongObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new LongObjectHashMap<>();
	/**+
	 * A list of valid keys for the destinationCoordainteCache.
	 * These are based on the X & Z of the players initial location.
	 */
	private final LongArrayList destinationCoordinateKeys = new LongArrayList();

	public Teleporter(WorldServer worldIn) {
		this.worldServerInstance = worldIn;
		this.random = new EaglercraftRandom(worldIn.getSeed(), !worldIn.getWorldInfo().isOldEaglercraftRandom());
	}

	public void placeInPortal(Entity entityIn, float rotationYaw) {
		if (this.worldServerInstance.provider.getDimensionId() != 1) {
			if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
				this.makePortal(entityIn);
				this.placeInExistingPortal(entityIn, rotationYaw);
			}
		} else {
			int i = MathHelper.floor_double(entityIn.posX);
			int j = MathHelper.floor_double(entityIn.posY) - 1;
			int k = MathHelper.floor_double(entityIn.posZ);
			byte b0 = 1;
			byte b1 = 0;

			for (int l = -2; l <= 2; ++l) {
				for (int i1 = -2; i1 <= 2; ++i1) {
					for (int j1 = -1; j1 < 3; ++j1) {
						int k1 = i + i1 * b0 + l * b1;
						int l1 = j + j1;
						int i2 = k + i1 * b1 - l * b0;
						boolean flag = j1 < 0;
						this.worldServerInstance.setBlockState(new BlockPos(k1, l1, i2),
								flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
					}
				}
			}

			entityIn.setLocationAndAngles((double) i, (double) j, (double) k, entityIn.rotationYaw, 0.0F);
			entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
		}
	}

	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		boolean flag = true;
		double d0 = -1.0D;
		int i = MathHelper.floor_double(entityIn.posX);
		int j = MathHelper.floor_double(entityIn.posZ);
		boolean flag1 = true;
		Object object = BlockPos.ORIGIN;
		long k = ChunkCoordIntPair.chunkXZ2Int(i, j);
		if (this.destinationCoordinateCache.containsKey(k)) {
			Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(k);
			d0 = 0.0D;
			object = teleporter$portalposition;
			teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
			flag1 = false;
		} else {
			BlockPos blockpos2 = new BlockPos(entityIn);

			for (int l = -128; l <= 128; ++l) {
				BlockPos blockpos1;
				for (int i1 = -128; i1 <= 128; ++i1) {
					for (BlockPos blockpos = blockpos2.add(l,
							this.worldServerInstance.getActualHeight() - 1 - blockpos2.getY(), i1); blockpos
									.getY() >= 0; blockpos = blockpos1) {
						blockpos1 = blockpos.down();
						if (this.worldServerInstance.getBlockState(blockpos).getBlock() == Blocks.portal) {
							while (this.worldServerInstance.getBlockState(blockpos1 = blockpos.down())
									.getBlock() == Blocks.portal) {
								blockpos = blockpos1;
							}

							double d1 = blockpos.distanceSq(blockpos2);
							if (d0 < 0.0D || d1 < d0) {
								d0 = d1;
								object = blockpos;
							}
						}
					}
				}
			}
		}

		if (d0 >= 0.0D) {
			if (flag1) {
				this.destinationCoordinateCache.put(k,
						new Teleporter.PortalPosition((BlockPos) object, this.worldServerInstance.getTotalWorldTime()));
				this.destinationCoordinateKeys.add(Long.valueOf(k));
			}

			double d5 = (double) ((BlockPos) object).getX() + 0.5D;
			double d6 = (double) ((BlockPos) object).getY() + 0.5D;
			double d7 = (double) ((BlockPos) object).getZ() + 0.5D;
			BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal
					.func_181089_f(this.worldServerInstance, (BlockPos) object);
			boolean flag2 = blockpattern$patternhelper.getFinger().rotateY()
					.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
			double d2 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X
					? (double) blockpattern$patternhelper.func_181117_a().getZ()
					: (double) blockpattern$patternhelper.func_181117_a().getX();
			d6 = (double) (blockpattern$patternhelper.func_181117_a().getY() + 1)
					- entityIn.func_181014_aG().yCoord * (double) blockpattern$patternhelper.func_181119_e();
			if (flag2) {
				++d2;
			}

			if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) {
				d7 = d2 + (1.0D - entityIn.func_181014_aG().xCoord)
						* (double) blockpattern$patternhelper.func_181118_d()
						* (double) blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
			} else {
				d5 = d2 + (1.0D - entityIn.func_181014_aG().xCoord)
						* (double) blockpattern$patternhelper.func_181118_d()
						* (double) blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
			}

			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			float f3 = 0.0F;
			if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH()) {
				f = 1.0F;
				f1 = 1.0F;
			} else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH()
					.getOpposite()) {
				f = -1.0F;
				f1 = -1.0F;
			} else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().rotateY()) {
				f2 = 1.0F;
				f3 = -1.0F;
			} else {
				f2 = -1.0F;
				f3 = 1.0F;
			}

			double d3 = entityIn.motionX;
			double d4 = entityIn.motionZ;
			entityIn.motionX = d3 * (double) f + d4 * (double) f3;
			entityIn.motionZ = d3 * (double) f2 + d4 * (double) f1;
			entityIn.rotationYaw = rotationYaw
					- (float) (entityIn.func_181012_aH().getOpposite().getHorizontalIndex() * 90)
					+ (float) (blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90);
			entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
			return true;
		} else {
			return false;
		}
	}

	public boolean makePortal(Entity parEntity) {
		byte b0 = 16;
		double d0 = -1.0D;
		int i = MathHelper.floor_double(parEntity.posX);
		int j = MathHelper.floor_double(parEntity.posY);
		int k = MathHelper.floor_double(parEntity.posZ);
		int l = i;
		int i1 = j;
		int j1 = k;
		int k1 = 0;
		int l1 = this.random.nextInt(4);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int i2 = i - b0; i2 <= i + b0; ++i2) {
			double d1 = (double) i2 + 0.5D - parEntity.posX;

			for (int k2 = k - b0; k2 <= k + b0; ++k2) {
				double d2 = (double) k2 + 0.5D - parEntity.posZ;

				label142: for (int i3 = this.worldServerInstance.getActualHeight() - 1; i3 >= 0; --i3) {
					if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(i2, i3, k2))) {
						while (i3 > 0 && this.worldServerInstance
								.isAirBlock(blockpos$mutableblockpos.func_181079_c(i2, i3 - 1, k2))) {
							--i3;
						}

						for (int j3 = l1; j3 < l1 + 4; ++j3) {
							int k3 = j3 % 2;
							int l3 = 1 - k3;
							if (j3 % 4 >= 2) {
								k3 = -k3;
								l3 = -l3;
							}

							for (int i4 = 0; i4 < 3; ++i4) {
								for (int j4 = 0; j4 < 4; ++j4) {
									for (int k4 = -1; k4 < 4; ++k4) {
										int l4 = i2 + (j4 - 1) * k3 + i4 * l3;
										int i5 = i3 + k4;
										int j5 = k2 + (j4 - 1) * l3 - i4 * k3;
										blockpos$mutableblockpos.func_181079_c(l4, i5, j5);
										if (k4 < 0
												&& !this.worldServerInstance.getBlockState(blockpos$mutableblockpos)
														.getBlock().getMaterial().isSolid()
												|| k4 >= 0 && !this.worldServerInstance
														.isAirBlock(blockpos$mutableblockpos)) {
											continue label142;
										}
									}
								}
							}

							double d5 = (double) i3 + 0.5D - parEntity.posY;
							double d7 = d1 * d1 + d5 * d5 + d2 * d2;
							if (d0 < 0.0D || d7 < d0) {
								d0 = d7;
								l = i2;
								i1 = i3;
								j1 = k2;
								k1 = j3 % 4;
							}
						}
					}
				}
			}
		}

		if (d0 < 0.0D) {
			for (int k5 = i - b0; k5 <= i + b0; ++k5) {
				double d3 = (double) k5 + 0.5D - parEntity.posX;

				for (int i6 = k - b0; i6 <= k + b0; ++i6) {
					double d4 = (double) i6 + 0.5D - parEntity.posZ;

					label562: for (int l6 = this.worldServerInstance.getActualHeight() - 1; l6 >= 0; --l6) {
						if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(k5, l6, i6))) {
							while (l6 > 0 && this.worldServerInstance
									.isAirBlock(blockpos$mutableblockpos.func_181079_c(k5, l6 - 1, i6))) {
								--l6;
							}

							for (int j7 = l1; j7 < l1 + 2; ++j7) {
								int i8 = j7 % 2;
								int i9 = 1 - i8;

								for (int i10 = 0; i10 < 4; ++i10) {
									for (int i11 = -1; i11 < 4; ++i11) {
										int i12 = k5 + (i10 - 1) * i8;
										int l12 = l6 + i11;
										int i13 = i6 + (i10 - 1) * i9;
										blockpos$mutableblockpos.func_181079_c(i12, l12, i13);
										if (i11 < 0
												&& !this.worldServerInstance.getBlockState(blockpos$mutableblockpos)
														.getBlock().getMaterial().isSolid()
												|| i11 >= 0 && !this.worldServerInstance
														.isAirBlock(blockpos$mutableblockpos)) {
											continue label562;
										}
									}
								}

								double d6 = (double) l6 + 0.5D - parEntity.posY;
								double d8 = d3 * d3 + d6 * d6 + d4 * d4;
								if (d0 < 0.0D || d8 < d0) {
									d0 = d8;
									l = k5;
									i1 = l6;
									j1 = i6;
									k1 = j7 % 2;
								}
							}
						}
					}
				}
			}
		}

		int l5 = l;
		int j2 = i1;
		int j6 = j1;
		int k6 = k1 % 2;
		int l2 = 1 - k6;
		if (k1 % 4 >= 2) {
			k6 = -k6;
			l2 = -l2;
		}

		if (d0 < 0.0D) {
			i1 = MathHelper.clamp_int(i1, 70, this.worldServerInstance.getActualHeight() - 10);
			j2 = i1;

			for (int i7 = -1; i7 <= 1; ++i7) {
				for (int k7 = 1; k7 < 3; ++k7) {
					for (int j8 = -1; j8 < 3; ++j8) {
						int j9 = l5 + (k7 - 1) * k6 + i7 * l2;
						int j10 = j2 + j8;
						int j11 = j6 + (k7 - 1) * l2 - i7 * k6;
						boolean flag = j8 < 0;
						this.worldServerInstance.setBlockState(new BlockPos(j9, j10, j11),
								flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
					}
				}
			}
		}

		IBlockState iblockstate = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS,
				k6 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);

		for (int l7 = 0; l7 < 4; ++l7) {
			for (int k8 = 0; k8 < 4; ++k8) {
				for (int k9 = -1; k9 < 4; ++k9) {
					int k10 = l5 + (k8 - 1) * k6;
					int k11 = j2 + k9;
					int j12 = j6 + (k8 - 1) * l2;
					boolean flag1 = k8 == 0 || k8 == 3 || k9 == -1 || k9 == 3;
					this.worldServerInstance.setBlockState(new BlockPos(k10, k11, j12),
							flag1 ? Blocks.obsidian.getDefaultState() : iblockstate, 2);
				}
			}

			for (int l8 = 0; l8 < 4; ++l8) {
				for (int l9 = -1; l9 < 4; ++l9) {
					int l10 = l5 + (l8 - 1) * k6;
					int l11 = j2 + l9;
					int k12 = j6 + (l8 - 1) * l2;
					BlockPos blockpos = new BlockPos(l10, l11, k12);
					this.worldServerInstance.notifyNeighborsOfStateChange(blockpos,
							this.worldServerInstance.getBlockState(blockpos).getBlock());
				}
			}
		}

		return true;
	}

	/**+
	 * called periodically to remove out-of-date portal locations
	 * from the cache list. Argument par1 is a
	 * WorldServer.getTotalWorldTime() value.
	 */
	public void removeStalePortalLocations(long worldTime) {
		if (worldTime % 100L == 0L) {
			long i = worldTime - 300L;

			for (int j = 0; j < destinationCoordinateKeys.size(); ++j) {
				long olong = destinationCoordinateKeys.get(j);
				Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(olong);
				if (teleporter$portalposition == null || teleporter$portalposition.lastUpdateTime < i) {
					destinationCoordinateKeys.removeAt(j--);
					this.destinationCoordinateCache.remove(olong);
				}
			}
		}

	}

	public class PortalPosition extends BlockPos {
		public long lastUpdateTime;

		public PortalPosition(BlockPos pos, long lastUpdate) {
			super(pos.getX(), pos.getY(), pos.getZ());
			this.lastUpdateTime = lastUpdate;
		}
	}
}