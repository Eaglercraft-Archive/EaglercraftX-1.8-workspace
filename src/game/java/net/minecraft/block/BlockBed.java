package net.minecraft.block;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

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
public class BlockBed extends BlockDirectional {
	public static PropertyEnum<BlockBed.EnumPartType> PART;
	public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

	public BlockBed() {
		super(Material.cloth);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockBed.EnumPartType.FOOT)
				.withProperty(OCCUPIED, Boolean.valueOf(false)));
		this.setBedBounds();
	}

	public static void bootstrapStates() {
		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (world.isRemote) {
			return true;
		} else {
			if (iblockstate.getValue(PART) != BlockBed.EnumPartType.HEAD) {
				blockpos = blockpos.offset((EnumFacing) iblockstate.getValue(FACING));
				iblockstate = world.getBlockState(blockpos);
				if (iblockstate.getBlock() != this) {
					return true;
				}
			}
		}

		if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(blockpos) != BiomeGenBase.hell) {
			if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
					.getBoolean("bedSpawnPoint") && Math.abs(entityplayer.posX - (double) blockpos.getX()) <= 3.0D
					&& Math.abs(entityplayer.posY - (double) blockpos.getY()) <= 2.0D
					&& Math.abs(entityplayer.posZ - (double) blockpos.getZ()) <= 3.0D) {
				BlockPos blockpos1 = BlockBed.getSafeExitLocation(world, blockpos, 0);
				if (blockpos1 == null) {
					blockpos1 = blockpos.up();
				}
				entityplayer.setSpawnPoint(blockpos1.add(0.5F, 0.1F, 0.5F), false);
				entityplayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.setspawn"));
				if (entityplayer.isSneaking()) {
					return true;
				}
			}

			if (((Boolean) iblockstate.getValue(OCCUPIED)).booleanValue()) {
				EntityPlayer entityplayer1 = this.getPlayerInBed(world, blockpos);
				if (entityplayer1 != null) {
					entityplayer
							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
					return true;
				}

				iblockstate = iblockstate.withProperty(OCCUPIED, Boolean.valueOf(false));
				world.setBlockState(blockpos, iblockstate, 4);
			}

			EntityPlayer.EnumStatus entityplayer$enumstatus = entityplayer.trySleep(blockpos);
			if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
				iblockstate = iblockstate.withProperty(OCCUPIED, Boolean.valueOf(true));
				world.setBlockState(blockpos, iblockstate, 4);
				return true;
			} else {
				if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
					entityplayer
							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
				} else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
					entityplayer
							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
				}

				return true;
			}
		} else {
			world.setBlockToAir(blockpos);
			BlockPos blockpos1 = blockpos.offset(((EnumFacing) iblockstate.getValue(FACING)).getOpposite());
			if (world.getBlockState(blockpos1).getBlock() == this) {
				world.setBlockToAir(blockpos1);
			}

			world.newExplosion((Entity) null, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
					(double) blockpos.getZ() + 0.5D, 5.0F, true, true);
			return true;
		}
	}

	private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
		List<EntityPlayer> playerEntities = worldIn.playerEntities;
		for (int i = 0, l = playerEntities.size(); i < l; ++i) {
			EntityPlayer entityplayer = playerEntities.get(i);
			if (entityplayer.isPlayerSleeping() && entityplayer.playerLocation.equals(pos)) {
				return entityplayer;
			}
		}

		return null;
	}

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
		this.setBedBounds();
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			if (world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock() != this) {
				world.setBlockToAir(blockpos);
			}
		} else if (world.getBlockState(blockpos.offset(enumfacing)).getBlock() != this) {
			world.setBlockToAir(blockpos);
			if (!world.isRemote) {
				this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			}
		}

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {
		return iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : Items.bed;
	}

	private void setBedBounds() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
	}

	/**+
	 * Returns a safe BlockPos to disembark the bed
	 */
	public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
		EnumFacing enumfacing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for (int l = 0; l <= 1; ++l) {
			int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
			int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
			int k1 = i1 + 2;
			int l1 = j1 + 2;

			for (int i2 = i1; i2 <= k1; ++i2) {
				for (int j2 = j1; j2 <= l1; ++j2) {
					BlockPos blockpos = new BlockPos(i2, j, j2);
					if (hasRoomForPlayer(worldIn, blockpos)) {
						if (tries <= 0) {
							return blockpos;
						}

						--tries;
					}
				}
			}
		}

		return null;
	}

	protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
		return World.doesBlockHaveSolidTopSurface(worldIn, pos.down())
				&& !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()
				&& !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid();
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float f, int var5) {
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.FOOT) {
			super.dropBlockAsItemWithChance(world, blockpos, iblockstate, f, 0);
		}

	}

	public int getMobilityFlag() {
		return 1;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.bed;
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());
			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(i);
		return (i & 8) > 0
				? this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.HEAD).withProperty(FACING, enumfacing)
						.withProperty(OCCUPIED, Boolean.valueOf((i & 4) > 0))
				: this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.FOOT).withProperty(FACING,
						enumfacing);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == BlockBed.EnumPartType.FOOT) {
			IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing) state.getValue(FACING)));
			if (iblockstate.getBlock() == this) {
				state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
			}
		}

		return state;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			i |= 8;
			if (((Boolean) iblockstate.getValue(OCCUPIED)).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, PART, OCCUPIED });
	}

	public static enum EnumPartType implements IStringSerializable {
		HEAD("head"), FOOT("foot");

		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}