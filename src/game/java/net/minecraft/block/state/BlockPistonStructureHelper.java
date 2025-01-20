package net.minecraft.block.state;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class BlockPistonStructureHelper {
	private final World world;
	private final BlockPos pistonPos;
	private final BlockPos blockToMove;
	private final EnumFacing moveDirection;
	/**+
	 * This is a List<BlockPos> of all blocks that will be moved by
	 * the piston.
	 */
	private final List<BlockPos> toMove = Lists.newArrayList();
	/**+
	 * This is a List<BlockPos> of blocks that will be destroyed
	 * when a piston attempts to move them.
	 */
	private final List<BlockPos> toDestroy = Lists.newArrayList();

	public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending) {
		this.world = worldIn;
		this.pistonPos = posIn;
		if (extending) {
			this.moveDirection = pistonFacing;
			this.blockToMove = posIn.offset(pistonFacing);
		} else {
			this.moveDirection = pistonFacing.getOpposite();
			this.blockToMove = posIn.offset(pistonFacing, 2);
		}

	}

	public boolean canMove() {
		this.toMove.clear();
		this.toDestroy.clear();
		Block block = this.world.getBlockState(this.blockToMove).getBlock();
		if (!BlockPistonBase.canPush(block, this.world, this.blockToMove, this.moveDirection, false)) {
			if (block.getMobilityFlag() != 1) {
				return false;
			} else {
				this.toDestroy.add(this.blockToMove);
				return true;
			}
		} else if (!this.func_177251_a(this.blockToMove)) {
			return false;
		} else {
			for (int i = 0; i < this.toMove.size(); ++i) {
				BlockPos blockpos = (BlockPos) this.toMove.get(i);
				if (this.world.getBlockState(blockpos).getBlock() == Blocks.slime_block
						&& !this.func_177250_b(blockpos)) {
					return false;
				}
			}

			return true;
		}
	}

	private boolean func_177251_a(BlockPos origin) {
		Block block = this.world.getBlockState(origin).getBlock();
		if (block.getMaterial() == Material.air) {
			return true;
		} else if (!BlockPistonBase.canPush(block, this.world, origin, this.moveDirection, false)) {
			return true;
		} else if (origin.equals(this.pistonPos)) {
			return true;
		} else if (this.toMove.contains(origin)) {
			return true;
		} else {
			int i = 1;
			if (i + this.toMove.size() > 12) {
				return false;
			} else {
				while (block == Blocks.slime_block) {
					BlockPos blockpos = origin.offset(this.moveDirection.getOpposite(), i);
					block = this.world.getBlockState(blockpos).getBlock();
					if (block.getMaterial() == Material.air
							|| !BlockPistonBase.canPush(block, this.world, blockpos, this.moveDirection, false)
							|| blockpos.equals(this.pistonPos)) {
						break;
					}

					++i;
					if (i + this.toMove.size() > 12) {
						return false;
					}
				}

				int i1 = 0;

				for (int j = i - 1; j >= 0; --j) {
					this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
					++i1;
				}

				int j1 = 1;

				while (true) {
					BlockPos blockpos1 = origin.offset(this.moveDirection, j1);
					int k = this.toMove.indexOf(blockpos1);
					if (k > -1) {
						this.func_177255_a(i1, k);

						for (int l = 0; l <= k + i1; ++l) {
							BlockPos blockpos2 = (BlockPos) this.toMove.get(l);
							if (this.world.getBlockState(blockpos2).getBlock() == Blocks.slime_block
									&& !this.func_177250_b(blockpos2)) {
								return false;
							}
						}

						return true;
					}

					block = this.world.getBlockState(blockpos1).getBlock();
					if (block.getMaterial() == Material.air) {
						return true;
					}

					if (!BlockPistonBase.canPush(block, this.world, blockpos1, this.moveDirection, true)
							|| blockpos1.equals(this.pistonPos)) {
						return false;
					}

					if (block.getMobilityFlag() == 1) {
						this.toDestroy.add(blockpos1);
						return true;
					}

					if (this.toMove.size() >= 12) {
						return false;
					}

					this.toMove.add(blockpos1);
					++i1;
					++j1;
				}
			}
		}
	}

	private void func_177255_a(int parInt1, int parInt2) {
		ArrayList arraylist = Lists.newArrayList();
		ArrayList arraylist1 = Lists.newArrayList();
		ArrayList arraylist2 = Lists.newArrayList();
		arraylist.addAll(this.toMove.subList(0, parInt2));
		arraylist1.addAll(this.toMove.subList(this.toMove.size() - parInt1, this.toMove.size()));
		arraylist2.addAll(this.toMove.subList(parInt2, this.toMove.size() - parInt1));
		this.toMove.clear();
		this.toMove.addAll(arraylist);
		this.toMove.addAll(arraylist1);
		this.toMove.addAll(arraylist2);
	}

	private boolean func_177250_b(BlockPos parBlockPos) {
		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			if (enumfacing.getAxis() != this.moveDirection.getAxis()
					&& !this.func_177251_a(parBlockPos.offset(enumfacing))) {
				return false;
			}
		}

		return true;
	}

	/**+
	 * Returns a List<BlockPos> of all the blocks that are being
	 * moved by the piston.
	 */
	public List<BlockPos> getBlocksToMove() {
		return this.toMove;
	}

	/**+
	 * Returns an List<BlockPos> of all the blocks that are being
	 * destroyed by the piston.
	 */
	public List<BlockPos> getBlocksToDestroy() {
		return this.toDestroy;
	}
}