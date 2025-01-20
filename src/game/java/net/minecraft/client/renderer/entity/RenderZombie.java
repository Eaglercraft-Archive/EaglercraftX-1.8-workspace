package net.minecraft.client.renderer.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

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
public class RenderZombie extends RenderBiped<EntityZombie> {
	private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation zombieVillagerTextures = new ResourceLocation(
			"textures/entity/zombie/zombie_villager.png");
	private final ModelBiped field_82434_o;
	private final ModelZombieVillager zombieVillagerModel;
	private final List<LayerRenderer<EntityZombie>> field_177121_n;
	private final List<LayerRenderer<EntityZombie>> field_177122_o;

	public RenderZombie(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
		LayerRenderer layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
		this.field_82434_o = this.modelBipedMain;
		this.zombieVillagerModel = new ModelZombieVillager();
		this.addLayer(new LayerHeldItem(this));
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.field_177189_c = new ModelZombie(0.5F, false);
				this.field_177186_d = new ModelZombie(1.0F, false);
			}
		};
		this.addLayer(layerbipedarmor);
		this.field_177122_o = Lists.newArrayList(this.layerRenderers);
		if (layerrenderer instanceof LayerCustomHead) {
			this.removeLayer(layerrenderer);
			this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
		}

		this.removeLayer(layerbipedarmor);
		this.addLayer(new LayerVillagerArmor(this));
		this.field_177121_n = Lists.newArrayList(this.layerRenderers);
	}

	/**+
	 * Actually renders the given argument. This is a synthetic
	 * bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual
	 * work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity>) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doe
	 */
	public void doRender(EntityZombie entityzombie, double d0, double d1, double d2, float f, float f1) {
		this.func_82427_a(entityzombie);
		super.doRender(entityzombie, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityZombie entityzombie) {
		return entityzombie.isVillager() ? zombieVillagerTextures : zombieTextures;
	}

	private void func_82427_a(EntityZombie zombie) {
		if (zombie.isVillager()) {
			this.mainModel = this.zombieVillagerModel;
			this.layerRenderers = this.field_177121_n;
		} else {
			this.mainModel = this.field_82434_o;
			this.layerRenderers = this.field_177122_o;
		}

		this.modelBipedMain = (ModelBiped) this.mainModel;
	}

	protected void rotateCorpse(EntityZombie entityzombie, float f, float f1, float f2) {
		if (entityzombie.isConverting()) {
			f1 += (float) (Math.cos((double) entityzombie.ticksExisted * 3.25D) * 3.141592653589793D * 0.25D);
		}

		super.rotateCorpse(entityzombie, f, f1, f2);
	}
}