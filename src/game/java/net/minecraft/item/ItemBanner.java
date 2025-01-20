package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
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
public class ItemBanner extends ItemBlock {
	public ItemBanner() {
		super(Blocks.standing_banner);
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing == EnumFacing.DOWN) {
			return false;
		} else if (!world.getBlockState(blockpos).getBlock().getMaterial().isSolid()) {
			return false;
		} else {
			blockpos = blockpos.offset(enumfacing);
			if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
				return false;
			} else if (!Blocks.standing_banner.canPlaceBlockAt(world, blockpos)) {
				return false;
			} else if (world.isRemote) {
				return true;
			} else {
				if (enumfacing == EnumFacing.UP) {
					int i = MathHelper
							.floor_double((double) ((entityplayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
					world.setBlockState(blockpos, Blocks.standing_banner.getDefaultState()
							.withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
				} else {
					world.setBlockState(blockpos,
							Blocks.wall_banner.getDefaultState().withProperty(BlockWallSign.FACING, enumfacing), 3);
				}

				--itemstack.stackSize;
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity instanceof TileEntityBanner) {
					((TileEntityBanner) tileentity).setItemValues(itemstack);
				}

				return true;
			}
		}
	}

	public String getItemStackDisplayName(ItemStack itemstack) {
		String s = "item.banner.";
		EnumDyeColor enumdyecolor = this.getBaseColor(itemstack);
		s = s + enumdyecolor.getUnlocalizedName() + ".name";
		return StatCollector.translateToLocal(s);
	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack itemstack, EntityPlayer var2, List<String> list, boolean var4) {
		NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag", false);
		if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

			for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i) {
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound1.getInteger("Color"));
				TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = TileEntityBanner.EnumBannerPattern
						.getPatternByID(nbttagcompound1.getString("Pattern"));
				if (tileentitybanner$enumbannerpattern != null) {
					list.add(StatCollector
							.translateToLocal("item.banner." + tileentitybanner$enumbannerpattern.getPatternName() + "."
									+ enumdyecolor.getUnlocalizedName()));
				}
			}

		}
	}

	public int getColorFromItemStack(ItemStack itemstack, int i) {
		if (i == 0) {
			return 16777215;
		} else {
			EnumDyeColor enumdyecolor = this.getBaseColor(itemstack);
			return enumdyecolor.getMapColor().colorValue;
		}
	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs var2, List<ItemStack> list) {
		EnumDyeColor[] colors = EnumDyeColor.META_LOOKUP;
		for (int i = 0; i < colors.length; ++i) {
			EnumDyeColor enumdyecolor = colors[i];
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			TileEntityBanner.func_181020_a(nbttagcompound, enumdyecolor.getDyeDamage(), (NBTTagList) null);
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setTag("BlockEntityTag", nbttagcompound);
			ItemStack itemstack = new ItemStack(item, 1, enumdyecolor.getDyeDamage());
			itemstack.setTagCompound(nbttagcompound1);
			list.add(itemstack);
		}

	}

	/**+
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab() {
		return CreativeTabs.tabDecorations;
	}

	private EnumDyeColor getBaseColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
		EnumDyeColor enumdyecolor = null;
		if (nbttagcompound != null && nbttagcompound.hasKey("Base")) {
			enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base"));
		} else {
			enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
		}

		return enumdyecolor;
	}
}