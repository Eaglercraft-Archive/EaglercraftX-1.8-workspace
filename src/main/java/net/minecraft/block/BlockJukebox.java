package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class BlockJukebox extends BlockContainer {
	public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

	protected BlockJukebox() {
		super(Material.wood, MapColor.dirtColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer var4,
			EnumFacing var5, float var6, float var7, float var8) {
		if (((Boolean) iblockstate.getValue(HAS_RECORD)).booleanValue()) {
			iblockstate = iblockstate.withProperty(HAS_RECORD, Boolean.valueOf(false));
			world.setBlockState(blockpos, iblockstate, 2);
			return true;
		} else {
			return false;
		}
	}

	public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {

	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new BlockJukebox.TileEntityJukebox();
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
			ItemStack itemstack = ((BlockJukebox.TileEntityJukebox) tileentity).getRecord();
			if (itemstack != null) {
				return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
			}
		}

		return 0;
	}

	/**+
	 * The type of render function called. 3 for standard block
	 * models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	public int getRenderType() {
		return 3;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(HAS_RECORD, Boolean.valueOf(i > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(HAS_RECORD)).booleanValue() ? 1 : 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { HAS_RECORD });
	}

	public static class TileEntityJukebox extends TileEntity {
		private ItemStack record;

		public void readFromNBT(NBTTagCompound compound) {
			super.readFromNBT(compound);
			if (compound.hasKey("RecordItem", 10)) {
				this.setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
			} else if (compound.getInteger("Record") > 0) {
				this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
			}

		}

		public void writeToNBT(NBTTagCompound compound) {
			super.writeToNBT(compound);
			if (this.getRecord() != null) {
				compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
			}

		}

		public ItemStack getRecord() {
			return this.record;
		}

		public void setRecord(ItemStack recordStack) {
			this.record = recordStack;
			this.markDirty();
		}
	}
}