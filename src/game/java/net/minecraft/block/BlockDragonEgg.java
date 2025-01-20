package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
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
public class BlockDragonEgg extends Block {
	public BlockDragonEgg() {
		super(Material.dragonEgg, MapColor.blackColor);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState var3) {
		world.scheduleUpdate(blockpos, this, this.tickRate(world));
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState var3, Block var4) {
		world.scheduleUpdate(blockpos, this, this.tickRate(world));
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		this.checkFall(world, blockpos);
	}

	private void checkFall(World worldIn, BlockPos pos) {
		if (BlockFalling.canFallInto(worldIn, pos.down()) && pos.getY() >= 0) {
			byte b0 = 32;
			if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-b0, -b0, -b0), pos.add(b0, b0, b0))) {
				worldIn.spawnEntityInWorld(new EntityFallingBlock(worldIn, (double) ((float) pos.getX() + 0.5F),
						(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), this.getDefaultState()));
			} else {
				worldIn.setBlockToAir(pos);

				BlockPos blockpos;
				for (blockpos = pos; BlockFalling.canFallInto(worldIn, blockpos)
						&& blockpos.getY() > 0; blockpos = blockpos.down()) {
					;
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos, this.getDefaultState(), 2);
				}
			}

		}
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer var4,
			EnumFacing var5, float var6, float var7, float var8) {
		this.teleport(world, blockpos);
		return true;
	}

	public void onBlockClicked(World world, BlockPos blockpos, EntityPlayer var3) {
		this.teleport(world, blockpos);
	}

	private void teleport(World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (iblockstate.getBlock() == this) {
			for (int i = 0; i < 1000; ++i) {
				BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16),
						worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8),
						worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
				if (worldIn.getBlockState(blockpos).getBlock().blockMaterial == Material.air) {
					if (worldIn.isRemote) {
						for (int j = 0; j < 128; ++j) {
							double d0 = worldIn.rand.nextDouble();
							float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
							float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
							float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
							double d1 = (double) blockpos.getX() + (double) (pos.getX() - blockpos.getX()) * d0
									+ (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
							double d2 = (double) blockpos.getY() + (double) (pos.getY() - blockpos.getY()) * d0
									+ worldIn.rand.nextDouble() * 1.0D - 0.5D;
							double d3 = (double) blockpos.getZ() + (double) (pos.getZ() - blockpos.getZ()) * d0
									+ (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
							worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1,
									(double) f2, new int[0]);
						}
					} else {
						worldIn.setBlockState(blockpos, iblockstate, 2);
						worldIn.setBlockToAir(pos);
					}

					return;
				}
			}

		}
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 5;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
		return true;
	}

	public Item getItem(World var1, BlockPos var2) {
		return null;
	}
}