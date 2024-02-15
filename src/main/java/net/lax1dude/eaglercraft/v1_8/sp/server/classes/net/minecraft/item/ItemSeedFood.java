package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemFood;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumFacing;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class ItemSeedFood extends ItemFood {

	static {
		__checkIntegratedContextValid("net/minecraft/item/ItemSeedFood");
	}

	private Block crops;
	private Block soilId;

	public ItemSeedFood(int healAmount, float saturation, Block crops, Block soil) {
		super(healAmount, saturation, false);
		this.crops = crops;
		this.soilId = soil;
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing != EnumFacing.UP) {
			return false;
		} else if (!entityplayer.canPlayerEdit(blockpos.offset(enumfacing), enumfacing, itemstack)) {
			return false;
		} else if (world.getBlockState(blockpos).getBlock() == this.soilId && world.isAirBlock(blockpos.up())) {
			world.setBlockState(blockpos.up(), this.crops.getDefaultState());
			--itemstack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}