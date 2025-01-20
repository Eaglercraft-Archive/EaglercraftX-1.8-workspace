package net.minecraft.item;

import net.minecraft.block.Block;

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
public class ItemColored extends ItemBlock {
	private final Block coloredBlock;
	private String[] subtypeNames;

	public ItemColored(Block block, boolean hasSubtypes) {
		super(block);
		this.coloredBlock = block;
		if (hasSubtypes) {
			this.setMaxDamage(0);
			this.setHasSubtypes(true);
		}

	}

	public int getColorFromItemStack(ItemStack itemstack, int var2) {
		return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(itemstack.getMetadata()));
	}

	/**+
	 * Converts the given ItemStack damage value into a metadata
	 * value to be placed in the world when this Item is placed as a
	 * Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int i) {
		return i;
	}

	public ItemColored setSubtypeNames(String[] names) {
		this.subtypeNames = names;
		return this;
	}

	/**+
	 * Returns the unlocalized name of this item. This version
	 * accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemstack) {
		if (this.subtypeNames == null) {
			return super.getUnlocalizedName(itemstack);
		} else {
			int i = itemstack.getMetadata();
			return i >= 0 && i < this.subtypeNames.length
					? super.getUnlocalizedName(itemstack) + "." + this.subtypeNames[i]
					: super.getUnlocalizedName(itemstack);
		}
	}
}