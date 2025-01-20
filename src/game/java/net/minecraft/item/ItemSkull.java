package net.minecraft.item;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
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
public class ItemSkull extends Item {
	private static final String[] skullTypes = new String[] { "skeleton", "wither", "zombie", "char", "creeper" };

	public ItemSkull() {
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing == EnumFacing.DOWN) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(world, blockpos);
			if (!flag) {
				if (!world.getBlockState(blockpos).getBlock().getMaterial().isSolid()) {
					return false;
				}

				blockpos = blockpos.offset(enumfacing);
			}

			if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
				return false;
			} else if (!Blocks.skull.canPlaceBlockAt(world, blockpos)) {
				return false;
			} else {
				if (!world.isRemote) {
					world.setBlockState(blockpos,
							Blocks.skull.getDefaultState().withProperty(BlockSkull.FACING, enumfacing), 3);
					int i = 0;
					if (enumfacing == EnumFacing.UP) {
						i = MathHelper.floor_double((double) (entityplayer.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
					}

					TileEntity tileentity = world.getTileEntity(blockpos);
					if (tileentity instanceof TileEntitySkull) {
						TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
						if (itemstack.getMetadata() == 3) {
							GameProfile gameprofile = null;
							if (itemstack.hasTagCompound()) {
								NBTTagCompound nbttagcompound = itemstack.getTagCompound();
								if (nbttagcompound.hasKey("SkullOwner", 10)) {
									gameprofile = NBTUtil
											.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
								} else if (nbttagcompound.hasKey("SkullOwner", 8)
										&& nbttagcompound.getString("SkullOwner").length() > 0) {
									gameprofile = new GameProfile((EaglercraftUUID) null,
											nbttagcompound.getString("SkullOwner"));
								}
							}

							tileentityskull.setPlayerProfile(gameprofile);
						} else {
							tileentityskull.setType(itemstack.getMetadata());
						}

						tileentityskull.setSkullRotation(i);
						Blocks.skull.checkWitherSpawn(world, blockpos, tileentityskull);
					}

					--itemstack.stackSize;
				}

				return true;
			}
		}
	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs var2, List<ItemStack> list) {
		for (int i = 0; i < skullTypes.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}

	}

	/**+
	 * Converts the given ItemStack damage value into a metadata
	 * value to be placed in the world when this Item is placed as a
	 * Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int i) {
		return i;
	}

	/**+
	 * Returns the unlocalized name of this item. This version
	 * accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemstack) {
		int i = itemstack.getMetadata();
		if (i < 0 || i >= skullTypes.length) {
			i = 0;
		}

		return super.getUnlocalizedName() + "." + skullTypes[i];
	}

	public String getItemStackDisplayName(ItemStack itemstack) {
		if (itemstack.getMetadata() == 3 && itemstack.hasTagCompound()) {
			if (itemstack.getTagCompound().hasKey("SkullOwner", 8)) {
				return StatCollector.translateToLocalFormatted("item.skull.player.name",
						new Object[] { itemstack.getTagCompound().getString("SkullOwner") });
			}

			if (itemstack.getTagCompound().hasKey("SkullOwner", 10)) {
				NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("SkullOwner");
				if (nbttagcompound.hasKey("Name", 8)) {
					return StatCollector.translateToLocalFormatted("item.skull.player.name",
							new Object[] { nbttagcompound.getString("Name") });
				}
			}
		}

		return super.getItemStackDisplayName(itemstack);
	}

	/**+
	 * Called when an ItemStack with NBT data is read to potentially
	 * that ItemStack's NBT data
	 */
	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		super.updateItemStackNBT(nbt);
		if (nbt.hasKey("SkullOwner", 8) && nbt.getString("SkullOwner").length() > 0) {
			GameProfile gameprofile = new GameProfile((EaglercraftUUID) null, nbt.getString("SkullOwner"));
			gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
			nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
			return true;
		} else {
			return false;
		}
	}
}