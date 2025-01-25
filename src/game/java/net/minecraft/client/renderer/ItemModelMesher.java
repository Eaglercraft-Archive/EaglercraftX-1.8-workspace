package net.minecraft.client.renderer;

import java.util.Map;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import net.optifine.CustomItems;

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
public class ItemModelMesher {
	private final IntObjectMap<ModelResourceLocation> simpleShapes = new IntObjectHashMap<>();
	private final IntObjectMap<IBakedModel> simpleShapesCache = new IntObjectHashMap<>();
	private final Map<Item, ItemMeshDefinition> shapers = Maps.newHashMap();
	private final ModelManager modelManager;

	public ItemModelMesher(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public EaglerTextureAtlasSprite getParticleIcon(Item item) {
		return this.getParticleIcon(item, 0);
	}

	public EaglerTextureAtlasSprite getParticleIcon(Item item, int meta) {
		return this.getItemModel(new ItemStack(item, 1, meta)).getParticleTexture();
	}

	public IBakedModel getItemModel(ItemStack stack) {
		Item item = stack.getItem();
		IBakedModel ibakedmodel = this.getItemModel(item, this.getMetadata(stack));
		if (ibakedmodel == null) {
			ItemMeshDefinition itemmeshdefinition = this.shapers.get(item);
			if (itemmeshdefinition != null) {
				ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
			}
		}

		if (ibakedmodel == null) {
			ibakedmodel = this.modelManager.getMissingModel();
		}

		if (Config.isCustomItems()) {
			ibakedmodel = CustomItems.getCustomItemModel(stack, ibakedmodel, (ResourceLocation) null, true);
		}

		return ibakedmodel;
	}

	protected int getMetadata(ItemStack stack) {
		return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
	}

	protected IBakedModel getItemModel(Item item, int meta) {
		return this.simpleShapesCache.get(this.getIndex(item, meta));
	}

	private int getIndex(Item item, int meta) {
		return Item.getIdFromItem(item) << 16 | meta;
	}

	public void register(Item item, int meta, ModelResourceLocation location) {
		this.simpleShapes.put(this.getIndex(item, meta), location);
		this.simpleShapesCache.put(this.getIndex(item, meta), this.modelManager.getModel(location));
	}

	public void register(Item item, ItemMeshDefinition definition) {
		this.shapers.put(item, definition);
	}

	public ModelManager getModelManager() {
		return this.modelManager;
	}

	public void rebuildCache() {
		this.simpleShapesCache.clear();

		for (IntObjectCursor<ModelResourceLocation> entry : this.simpleShapes) {
			this.simpleShapesCache.put(entry.key, this.modelManager.getModel(entry.value));
		}
	}
}