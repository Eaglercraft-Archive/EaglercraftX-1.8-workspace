package net.minecraft.client.renderer.block.model;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

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
public class ItemCameraTransforms {
	public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
	public static float field_181690_b = 0.0F;
	public static float field_181691_c = 0.0F;
	public static float field_181692_d = 0.0F;
	public static float field_181693_e = 0.0F;
	public static float field_181694_f = 0.0F;
	public static float field_181695_g = 0.0F;
	public static float field_181696_h = 0.0F;
	public static float field_181697_i = 0.0F;
	public static float field_181698_j = 0.0F;
	public final ItemTransformVec3f thirdPerson;
	public final ItemTransformVec3f firstPerson;
	public final ItemTransformVec3f head;
	public final ItemTransformVec3f gui;
	public final ItemTransformVec3f ground;
	public final ItemTransformVec3f fixed;

	private ItemCameraTransforms() {
		this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
				ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}

	public ItemCameraTransforms(ItemCameraTransforms parItemCameraTransforms) {
		this.thirdPerson = parItemCameraTransforms.thirdPerson;
		this.firstPerson = parItemCameraTransforms.firstPerson;
		this.head = parItemCameraTransforms.head;
		this.gui = parItemCameraTransforms.gui;
		this.ground = parItemCameraTransforms.ground;
		this.fixed = parItemCameraTransforms.fixed;
	}

	public ItemCameraTransforms(ItemTransformVec3f parItemTransformVec3f, ItemTransformVec3f parItemTransformVec3f_2,
			ItemTransformVec3f parItemTransformVec3f_3, ItemTransformVec3f parItemTransformVec3f_4,
			ItemTransformVec3f parItemTransformVec3f_5, ItemTransformVec3f parItemTransformVec3f_6) {
		this.thirdPerson = parItemTransformVec3f;
		this.firstPerson = parItemTransformVec3f_2;
		this.head = parItemTransformVec3f_3;
		this.gui = parItemTransformVec3f_4;
		this.ground = parItemTransformVec3f_5;
		this.fixed = parItemTransformVec3f_6;
	}

	public void applyTransform(ItemCameraTransforms.TransformType parTransformType) {
		ItemTransformVec3f itemtransformvec3f = this.getTransform(parTransformType);
		if (itemtransformvec3f != ItemTransformVec3f.DEFAULT) {
			GlStateManager.translate(itemtransformvec3f.translation.x + field_181690_b,
					itemtransformvec3f.translation.y + field_181691_c,
					itemtransformvec3f.translation.z + field_181692_d);
			GlStateManager.rotate(itemtransformvec3f.rotation.y + field_181694_f, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(itemtransformvec3f.rotation.x + field_181693_e, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(itemtransformvec3f.rotation.z + field_181695_g, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(itemtransformvec3f.scale.x + field_181696_h,
					itemtransformvec3f.scale.y + field_181697_i, itemtransformvec3f.scale.z + field_181698_j);
		}

	}

	public ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType parTransformType) {
		switch (parTransformType) {
		case THIRD_PERSON:
			return this.thirdPerson;
		case FIRST_PERSON:
			return this.firstPerson;
		case HEAD:
			return this.head;
		case GUI:
			return this.gui;
		case GROUND:
			return this.ground;
		case FIXED:
			return this.fixed;
		default:
			return ItemTransformVec3f.DEFAULT;
		}
	}

	public boolean func_181687_c(ItemCameraTransforms.TransformType parTransformType) {
		return !this.getTransform(parTransformType).equals(ItemTransformVec3f.DEFAULT);
	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemCameraTransforms> {
		public ItemCameraTransforms deserialize(JSONObject jsonobject) throws JSONException {
			ItemTransformVec3f itemtransformvec3f = this.func_181683_a(jsonobject, "thirdperson");
			ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(jsonobject, "firstperson");
			ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(jsonobject, "head");
			ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(jsonobject, "gui");
			ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(jsonobject, "ground");
			ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(jsonobject, "fixed");
			return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2,
					itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
		}

		private ItemTransformVec3f func_181683_a(JSONObject parJsonObject, String parString1) {
			return parJsonObject.has(parString1)
					? JSONTypeProvider.deserialize(parJsonObject.get(parString1), ItemTransformVec3f.class)
					: ItemTransformVec3f.DEFAULT;
		}
	}

	public static enum TransformType {
		NONE, THIRD_PERSON, FIRST_PERSON, HEAD, GUI, GROUND, FIXED;
	}
}