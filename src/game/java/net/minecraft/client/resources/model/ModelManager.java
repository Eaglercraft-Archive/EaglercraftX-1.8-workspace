package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IRegistry;

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
public class ModelManager implements IResourceManagerReloadListener {
	private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
	private final TextureMap texMap;
	private final BlockModelShapes modelProvider;
	private IBakedModel defaultModel;

	public ModelBakery modelbakerytmp; // eagler hack

	public ModelManager(TextureMap textures) {
		this.texMap = textures;
		this.modelProvider = new BlockModelShapes(this);
	}

	public void onResourceManagerReload(IResourceManager iresourcemanager) {
		modelbakerytmp = new ModelBakery(iresourcemanager, this.texMap, this.modelProvider);
		try {
			this.modelRegistry = modelbakerytmp.setupModelRegistry();
			this.defaultModel = (IBakedModel) this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
			this.modelProvider.reloadModels();
		} finally {
			modelbakerytmp = null;
		}
	}

	public IBakedModel getModel(ModelResourceLocation modelLocation) {
		if (modelLocation == null) {
			return this.defaultModel;
		} else {
			IBakedModel ibakedmodel = (IBakedModel) this.modelRegistry.getObject(modelLocation);
			return ibakedmodel == null ? this.defaultModel : ibakedmodel;
		}
	}

	public IBakedModel getMissingModel() {
		return this.defaultModel;
	}

	public TextureMap getTextureMap() {
		return this.texMap;
	}

	public BlockModelShapes getBlockModelShapes() {
		return this.modelProvider;
	}
}