package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.item;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockChest;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.item.EntityMinecart;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.item.EntityMinecartContainer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.InventoryPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.init.Blocks;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.inventory.Container;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.inventory.ContainerChest;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.DamageSource;
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
public class EntityMinecartChest extends EntityMinecartContainer {

	static {
		__checkIntegratedContextValid("net/minecraft/entity/item/EntityMinecartChest");
	}

	public EntityMinecartChest(World worldIn) {
		super(worldIn);
	}

	public EntityMinecartChest(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		super(worldIn, parDouble1, parDouble2, parDouble3);
	}

	public void killMinecart(DamageSource damagesource) {
		super.killMinecart(damagesource);
		if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
			this.dropItemWithOffset(Item.getItemFromBlock(Blocks.chest), 1, 0.0F);
		}

	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return 27;
	}

	public EntityMinecart.EnumMinecartType getMinecartType() {
		return EntityMinecart.EnumMinecartType.CHEST;
	}

	public IBlockState getDefaultDisplayTile() {
		return Blocks.chest.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);
	}

	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	public String getGuiID() {
		return "minecraft:chest";
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer entityplayer) {
		return new ContainerChest(inventoryplayer, this, entityplayer);
	}
}