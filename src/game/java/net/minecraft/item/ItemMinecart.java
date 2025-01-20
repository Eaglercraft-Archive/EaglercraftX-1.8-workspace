package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemMinecart extends Item {
	private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem() {
		private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
			World world = source.getWorld();
			double d0 = source.getX() + (double) enumfacing.getFrontOffsetX() * 1.125D;
			double d1 = Math.floor(source.getY()) + (double) enumfacing.getFrontOffsetY();
			double d2 = source.getZ() + (double) enumfacing.getFrontOffsetZ() * 1.125D;
			BlockPos blockpos = source.getBlockPos().offset(enumfacing);
			IBlockState iblockstate = world.getBlockState(blockpos);
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate
					.getBlock() instanceof BlockRailBase
							? (BlockRailBase.EnumRailDirection) iblockstate
									.getValue(((BlockRailBase) iblockstate.getBlock()).getShapeProperty())
							: BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			double d3;
			if (BlockRailBase.isRailBlock(iblockstate)) {
				if (blockrailbase$enumraildirection.isAscending()) {
					d3 = 0.6D;
				} else {
					d3 = 0.1D;
				}
			} else {
				if (iblockstate.getBlock().getMaterial() != Material.air
						|| !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
					return this.behaviourDefaultDispenseItem.dispense(source, stack);
				}

				IBlockState iblockstate1 = world.getBlockState(blockpos.down());
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1
						.getBlock() instanceof BlockRailBase
								? (BlockRailBase.EnumRailDirection) iblockstate1
										.getValue(((BlockRailBase) iblockstate1.getBlock()).getShapeProperty())
								: BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending()) {
					d3 = -0.4D;
				} else {
					d3 = -0.9D;
				}
			}

			EntityMinecart entityminecart = EntityMinecart.func_180458_a(world, d0, d1 + d3, d2,
					((ItemMinecart) stack.getItem()).minecartType);
			if (stack.hasDisplayName()) {
				entityminecart.setCustomNameTag(stack.getDisplayName());
			}

			world.spawnEntityInWorld(entityminecart);
			stack.splitStack(1);
			return stack;
		}

		protected void playDispenseSound(IBlockSource source) {
			source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
		}
	};
	private final EntityMinecart.EnumMinecartType minecartType;

	public ItemMinecart(EntityMinecart.EnumMinecartType type) {
		this.maxStackSize = 1;
		this.minecartType = type;
		this.setCreativeTab(CreativeTabs.tabTransport);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (BlockRailBase.isRailBlock(iblockstate)) {
			if (!worldIn.isRemote) {
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate
						.getBlock() instanceof BlockRailBase
								? (BlockRailBase.EnumRailDirection) iblockstate
										.getValue(((BlockRailBase) iblockstate.getBlock()).getShapeProperty())
								: BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double d0 = 0.0D;
				if (blockrailbase$enumraildirection.isAscending()) {
					d0 = 0.5D;
				}

				EntityMinecart entityminecart = EntityMinecart.func_180458_a(worldIn, (double) pos.getX() + 0.5D,
						(double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D, this.minecartType);
				if (stack.hasDisplayName()) {
					entityminecart.setCustomNameTag(stack.getDisplayName());
				}

				worldIn.spawnEntityInWorld(entityminecart);
			}

			--stack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}