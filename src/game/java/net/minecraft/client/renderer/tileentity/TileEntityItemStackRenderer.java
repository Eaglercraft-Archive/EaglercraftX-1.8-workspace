package net.minecraft.client.renderer.tileentity;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

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
public class TileEntityItemStackRenderer {
	public static TileEntityItemStackRenderer instance = new TileEntityItemStackRenderer();
	private TileEntityChest field_147717_b = new TileEntityChest(0);
	private TileEntityChest field_147718_c = new TileEntityChest(1);
	private TileEntityEnderChest enderChest = new TileEntityEnderChest();
	private TileEntityBanner banner = new TileEntityBanner();
	private TileEntitySkull skull = new TileEntitySkull();

	public void renderByItem(ItemStack itemStackIn) {
		if (itemStackIn.getItem() == Items.banner) {
			this.banner.setItemValues(itemStackIn);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(this.banner, 0.0D, 0.0D, 0.0D, 0.0F);
		} else if (itemStackIn.getItem() == Items.skull) {
			GameProfile gameprofile = null;
			if (itemStackIn.hasTagCompound()) {
				NBTTagCompound nbttagcompound = itemStackIn.getTagCompound();
				if (nbttagcompound.hasKey("SkullOwner", 10)) {
					gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
				} else if (nbttagcompound.hasKey("SkullOwner", 8)
						&& nbttagcompound.getString("SkullOwner").length() > 0) {
					gameprofile = new GameProfile((EaglercraftUUID) null, nbttagcompound.getString("SkullOwner"));
					gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
					nbttagcompound.removeTag("SkullOwner");
					nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
				}
			}

			if (TileEntitySkullRenderer.instance != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.5F, 0.0F, -0.5F);
				GlStateManager.scale(2.0F, 2.0F, 2.0F);
				GlStateManager.disableCull();
				TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 0.0F,
						itemStackIn.getMetadata(), gameprofile, -1);
				GlStateManager.enableCull();
				GlStateManager.popMatrix();
			}
		} else {
			Block block = Block.getBlockFromItem(itemStackIn.getItem());
			if (block == Blocks.ender_chest) {
				TileEntityRendererDispatcher.instance.renderTileEntityAt(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F);
			} else if (block == Blocks.trapped_chest) {
				TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147718_c, 0.0D, 0.0D, 0.0D, 0.0F);
			} else {
				TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147717_b, 0.0D, 0.0D, 0.0D, 0.0F);
			}
		}

	}
}