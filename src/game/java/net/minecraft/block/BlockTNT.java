package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
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
public class BlockTNT extends Block {
	public static final PropertyBool EXPLODE = PropertyBool.create("explode");

	public BlockTNT() {
		super(Material.tnt);
		this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.onBlockAdded(world, blockpos, iblockstate);
		if (world.isBlockPowered(blockpos)) {
			this.onBlockDestroyedByPlayer(world, blockpos, iblockstate.withProperty(EXPLODE, Boolean.valueOf(true)));
			world.setBlockToAir(blockpos);
		}

	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (world.isBlockPowered(blockpos)) {
			this.onBlockDestroyedByPlayer(world, blockpos, iblockstate.withProperty(EXPLODE, Boolean.valueOf(true)));
			world.setBlockToAir(blockpos);
		}

	}

	/**+
	 * Called when this Block is destroyed by an Explosion
	 */
	public void onBlockDestroyedByExplosion(World world, BlockPos blockpos, Explosion explosion) {
		if (!world.isRemote) {
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) blockpos.getX() + 0.5F),
					(double) blockpos.getY(), (double) ((float) blockpos.getZ() + 0.5F),
					explosion.getExplosivePlacedBy());
			entitytntprimed.fuse = world.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
			world.spawnEntityInWorld(entitytntprimed);
		}
	}

	/**+
	 * Called when a player destroys this Block
	 */
	public void onBlockDestroyedByPlayer(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.explode(world, blockpos, iblockstate, (EntityLivingBase) null);
	}

	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote) {
			if (((Boolean) state.getValue(EXPLODE)).booleanValue()) {
				EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
						(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
				worldIn.spawnEntityInWorld(entitytntprimed);
				worldIn.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
			}
		}
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing enumfacing, float f, float f1, float f2) {
		if (entityplayer.getCurrentEquippedItem() != null) {
			Item item = entityplayer.getCurrentEquippedItem().getItem();
			if (item == Items.flint_and_steel || item == Items.fire_charge) {
				this.explode(world, blockpos, iblockstate.withProperty(EXPLODE, Boolean.valueOf(true)), entityplayer);
				world.setBlockToAir(blockpos);
				if (item == Items.flint_and_steel) {
					entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
				} else if (!entityplayer.capabilities.isCreativeMode) {
					--entityplayer.getCurrentEquippedItem().stackSize;
				}

				return true;
			}
		}

		return super.onBlockActivated(world, blockpos, iblockstate, entityplayer, enumfacing, f, f1, f2);
	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, IBlockState var3, Entity entity) {
		if (!world.isRemote && entity instanceof EntityArrow) {
			EntityArrow entityarrow = (EntityArrow) entity;
			if (entityarrow.isBurning()) {
				this.explode(world, blockpos,
						world.getBlockState(blockpos).withProperty(EXPLODE, Boolean.valueOf(true)),
						entityarrow.shootingEntity instanceof EntityLivingBase
								? (EntityLivingBase) entityarrow.shootingEntity
								: null);
				world.setBlockToAir(blockpos);
			}
		}

	}

	/**+
	 * Return whether this block can drop from an explosion.
	 */
	public boolean canDropFromExplosion(Explosion var1) {
		return false;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(EXPLODE, Boolean.valueOf((i & 1) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(EXPLODE)).booleanValue() ? 1 : 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { EXPLODE });
	}
}