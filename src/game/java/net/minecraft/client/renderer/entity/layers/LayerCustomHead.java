package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;

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
public class LayerCustomHead implements LayerRenderer<EntityLivingBase> {
	private final ModelRenderer field_177209_a;

	public LayerCustomHead(ModelRenderer parModelRenderer) {
		this.field_177209_a = parModelRenderer;
	}

	public void doRenderLayer(EntityLivingBase entitylivingbase, float var2, float var3, float var4, float var5,
			float var6, float var7, float f) {
		ItemStack itemstack = entitylivingbase.getCurrentArmor(3);
		if (itemstack != null && itemstack.getItem() != null) {
			Item item = itemstack.getItem();
			Minecraft minecraft = Minecraft.getMinecraft();
			GlStateManager.pushMatrix();
			if (entitylivingbase.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			boolean flag = entitylivingbase instanceof EntityVillager
					|| entitylivingbase instanceof EntityZombie && ((EntityZombie) entitylivingbase).isVillager();
			if (!flag && entitylivingbase.isChild()) {
				float f1 = 2.0F;
				float f2 = 1.4F;
				GlStateManager.scale(f2 / f1, f2 / f1, f2 / f1);
				GlStateManager.translate(0.0F, 16.0F * f, 0.0F);
			}

			this.field_177209_a.postRender(0.0625F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			if (item instanceof ItemBlock) {
				float f3 = 0.625F;
				GlStateManager.translate(0.0F, -0.25F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(f3, -f3, -f3);
				if (flag) {
					GlStateManager.translate(0.0F, 0.1875F, 0.0F);
				}

				minecraft.getItemRenderer().renderItem(entitylivingbase, itemstack,
						ItemCameraTransforms.TransformType.HEAD);
			} else if (item == Items.skull) {
				float f4 = 1.1875F;
				GlStateManager.scale(f4, -f4, -f4);
				if (flag) {
					GlStateManager.translate(0.0F, 0.0625F, 0.0F);
				}

				GameProfile gameprofile = null;
				if (itemstack.hasTagCompound()) {
					NBTTagCompound nbttagcompound = itemstack.getTagCompound();
					if (nbttagcompound.hasKey("SkullOwner", 10)) {
						gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
					} else if (nbttagcompound.hasKey("SkullOwner", 8)) {
						String s = nbttagcompound.getString("SkullOwner");
						if (!StringUtils.isNullOrEmpty(s)) {
							gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((EaglercraftUUID) null, s));
							nbttagcompound.setTag("SkullOwner",
									NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
						}
					}
				}

				TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F,
						itemstack.getMetadata(), gameprofile, -1);
			}

			GlStateManager.popMatrix();
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}