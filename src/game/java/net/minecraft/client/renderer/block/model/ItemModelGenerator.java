package net.minecraft.client.renderer.block.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
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
public class ItemModelGenerator {
	public static final List<String> LAYERS = Lists
			.newArrayList(new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });

	public ModelBlock makeItemModel(TextureMap textureMapIn, ModelBlock blockModel) {
		HashMap hashmap = Maps.newHashMap();
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < LAYERS.size(); ++i) {
			String s = (String) LAYERS.get(i);
			if (!blockModel.isTexturePresent(s)) {
				break;
			}

			String s1 = blockModel.resolveTextureName(s);
			hashmap.put(s, s1);
			EaglerTextureAtlasSprite textureatlassprite = textureMapIn
					.getAtlasSprite((new ResourceLocation(s1)).toString());
			arraylist.addAll(this.func_178394_a(i, s, textureatlassprite));
		}

		if (arraylist.isEmpty()) {
			return null;
		} else {
			hashmap.put("particle", blockModel.isTexturePresent("particle") ? blockModel.resolveTextureName("particle")
					: (String) hashmap.get("layer0"));
			return new ModelBlock(arraylist, hashmap, false, false, blockModel.func_181682_g());
		}
	}

	private List<BlockPart> func_178394_a(int parInt1, String parString1,
			EaglerTextureAtlasSprite parTextureAtlasSprite) {
		HashMap hashmap = Maps.newHashMap();
		hashmap.put(EnumFacing.SOUTH, new BlockPartFace((EnumFacing) null, parInt1, parString1,
				new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0)));
		hashmap.put(EnumFacing.NORTH, new BlockPartFace((EnumFacing) null, parInt1, parString1,
				new BlockFaceUV(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0)));
		ArrayList arraylist = Lists.newArrayList();
		arraylist.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), hashmap,
				(BlockPartRotation) null, true));
		arraylist.addAll(this.func_178397_a(parTextureAtlasSprite, parString1, parInt1));
		return arraylist;
	}

	private List<BlockPart> func_178397_a(EaglerTextureAtlasSprite parTextureAtlasSprite, String parString1,
			int parInt1) {
		float f = (float) parTextureAtlasSprite.getIconWidth();
		float f1 = (float) parTextureAtlasSprite.getIconHeight();
		ArrayList arraylist = Lists.newArrayList();

		List<ItemModelGenerator.Span> lst = this.func_178393_a(parTextureAtlasSprite);
		for (int i = 0, l = lst.size(); i < l; ++i) {
			ItemModelGenerator.Span itemmodelgenerator$span = lst.get(i);
			float f2 = 0.0F;
			float f3 = 0.0F;
			float f4 = 0.0F;
			float f5 = 0.0F;
			float f6 = 0.0F;
			float f7 = 0.0F;
			float f8 = 0.0F;
			float f9 = 0.0F;
			float f10 = 0.0F;
			float f11 = 0.0F;
			float f12 = (float) itemmodelgenerator$span.func_178385_b();
			float f13 = (float) itemmodelgenerator$span.func_178384_c();
			float f14 = (float) itemmodelgenerator$span.func_178381_d();
			ItemModelGenerator.SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.func_178383_a();
			switch (itemmodelgenerator$spanfacing) {
			case UP:
				f6 = f12;
				f2 = f12;
				f4 = f7 = f13 + 1.0F;
				f8 = f14;
				f3 = f14;
				f9 = f14;
				f5 = f14;
				f10 = 16.0F / f;
				f11 = 16.0F / (f1 - 1.0F);
				break;
			case DOWN:
				f9 = f14;
				f8 = f14;
				f6 = f12;
				f2 = f12;
				f4 = f7 = f13 + 1.0F;
				f3 = f14 + 1.0F;
				f5 = f14 + 1.0F;
				f10 = 16.0F / f;
				f11 = 16.0F / (f1 - 1.0F);
				break;
			case LEFT:
				f6 = f14;
				f2 = f14;
				f7 = f14;
				f4 = f14;
				f9 = f12;
				f3 = f12;
				f5 = f8 = f13 + 1.0F;
				f10 = 16.0F / (f - 1.0F);
				f11 = 16.0F / f1;
				break;
			case RIGHT:
				f7 = f14;
				f6 = f14;
				f2 = f14 + 1.0F;
				f4 = f14 + 1.0F;
				f9 = f12;
				f3 = f12;
				f5 = f8 = f13 + 1.0F;
				f10 = 16.0F / (f - 1.0F);
				f11 = 16.0F / f1;
			}

			float f15 = 16.0F / f;
			float f16 = 16.0F / f1;
			f2 = f2 * f15;
			f4 = f4 * f15;
			f3 = f3 * f16;
			f5 = f5 * f16;
			f3 = 16.0F - f3;
			f5 = 16.0F - f5;
			f6 = f6 * f10;
			f7 = f7 * f10;
			f8 = f8 * f11;
			f9 = f9 * f11;
			HashMap hashmap = Maps.newHashMap();
			hashmap.put(itemmodelgenerator$spanfacing.getFacing(), new BlockPartFace((EnumFacing) null, parInt1,
					parString1, new BlockFaceUV(new float[] { f6, f8, f7, f9 }, 0)));
			switch (itemmodelgenerator$spanfacing) {
			case UP:
				arraylist.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f4, f3, 8.5F), hashmap,
						(BlockPartRotation) null, true));
				break;
			case DOWN:
				arraylist.add(new BlockPart(new Vector3f(f2, f5, 7.5F), new Vector3f(f4, f5, 8.5F), hashmap,
						(BlockPartRotation) null, true));
				break;
			case LEFT:
				arraylist.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f2, f5, 8.5F), hashmap,
						(BlockPartRotation) null, true));
				break;
			case RIGHT:
				arraylist.add(new BlockPart(new Vector3f(f4, f3, 7.5F), new Vector3f(f4, f5, 8.5F), hashmap,
						(BlockPartRotation) null, true));
			}
		}

		return arraylist;
	}

	private List<ItemModelGenerator.Span> func_178393_a(EaglerTextureAtlasSprite parTextureAtlasSprite) {
		int i = parTextureAtlasSprite.getIconWidth();
		int j = parTextureAtlasSprite.getIconHeight();
		ArrayList arraylist = Lists.newArrayList();

		for (int k = 0; k < parTextureAtlasSprite.getFrameCount(); ++k) {
			int[] aint = parTextureAtlasSprite.getFrameTextureData(k)[0];

			for (int l = 0; l < j; ++l) {
				for (int i1 = 0; i1 < i; ++i1) {
					boolean flag = !this.func_178391_a(aint, i1, l, i, j);
					this.func_178396_a(ItemModelGenerator.SpanFacing.UP, arraylist, aint, i1, l, i, j, flag);
					this.func_178396_a(ItemModelGenerator.SpanFacing.DOWN, arraylist, aint, i1, l, i, j, flag);
					this.func_178396_a(ItemModelGenerator.SpanFacing.LEFT, arraylist, aint, i1, l, i, j, flag);
					this.func_178396_a(ItemModelGenerator.SpanFacing.RIGHT, arraylist, aint, i1, l, i, j, flag);
				}
			}
		}

		return arraylist;
	}

	private void func_178396_a(ItemModelGenerator.SpanFacing parSpanFacing, List<ItemModelGenerator.Span> parList,
			int[] parArrayOfInt, int parInt1, int parInt2, int parInt3, int parInt4, boolean parFlag) {
		boolean flag = this.func_178391_a(parArrayOfInt, parInt1 + parSpanFacing.func_178372_b(),
				parInt2 + parSpanFacing.func_178371_c(), parInt3, parInt4) && parFlag;
		if (flag) {
			this.func_178395_a(parList, parSpanFacing, parInt1, parInt2);
		}

	}

	private void func_178395_a(List<ItemModelGenerator.Span> parList, ItemModelGenerator.SpanFacing parSpanFacing,
			int parInt1, int parInt2) {
		ItemModelGenerator.Span itemmodelgenerator$span = null;

		for (int j = 0, l = parList.size(); j < l; ++j) {
			ItemModelGenerator.Span itemmodelgenerator$span1 = parList.get(j);
			if (itemmodelgenerator$span1.func_178383_a() == parSpanFacing) {
				int i = parSpanFacing.func_178369_d() ? parInt2 : parInt1;
				if (itemmodelgenerator$span1.func_178381_d() == i) {
					itemmodelgenerator$span = itemmodelgenerator$span1;
					break;
				}
			}
		}

		int j = parSpanFacing.func_178369_d() ? parInt2 : parInt1;
		int k = parSpanFacing.func_178369_d() ? parInt1 : parInt2;
		if (itemmodelgenerator$span == null) {
			parList.add(new ItemModelGenerator.Span(parSpanFacing, k, j));
		} else {
			itemmodelgenerator$span.func_178382_a(k);
		}

	}

	private boolean func_178391_a(int[] parArrayOfInt, int parInt1, int parInt2, int parInt3, int parInt4) {
		return parInt1 >= 0 && parInt2 >= 0 && parInt1 < parInt3 && parInt2 < parInt4
				? (parArrayOfInt[parInt2 * parInt3 + parInt1] >> 24 & 255) == 0
				: true;
	}

	static class Span {
		private final ItemModelGenerator.SpanFacing spanFacing;
		private int field_178387_b;
		private int field_178388_c;
		private final int field_178386_d;

		public Span(ItemModelGenerator.SpanFacing spanFacingIn, int parInt1, int parInt2) {
			this.spanFacing = spanFacingIn;
			this.field_178387_b = parInt1;
			this.field_178388_c = parInt1;
			this.field_178386_d = parInt2;
		}

		public void func_178382_a(int parInt1) {
			if (parInt1 < this.field_178387_b) {
				this.field_178387_b = parInt1;
			} else if (parInt1 > this.field_178388_c) {
				this.field_178388_c = parInt1;
			}

		}

		public ItemModelGenerator.SpanFacing func_178383_a() {
			return this.spanFacing;
		}

		public int func_178385_b() {
			return this.field_178387_b;
		}

		public int func_178384_c() {
			return this.field_178388_c;
		}

		public int func_178381_d() {
			return this.field_178386_d;
		}
	}

	static enum SpanFacing {
		UP(EnumFacing.UP, 0, -1), DOWN(EnumFacing.DOWN, 0, 1), LEFT(EnumFacing.EAST, -1, 0),
		RIGHT(EnumFacing.WEST, 1, 0);

		private final EnumFacing facing;
		private final int field_178373_f;
		private final int field_178374_g;

		private SpanFacing(EnumFacing facing, int parInt2, int parInt3) {
			this.facing = facing;
			this.field_178373_f = parInt2;
			this.field_178374_g = parInt3;
		}

		public EnumFacing getFacing() {
			return this.facing;
		}

		public int func_178372_b() {
			return this.field_178373_f;
		}

		public int func_178371_c() {
			return this.field_178374_g;
		}

		private boolean func_178369_d() {
			return this == DOWN || this == UP;
		}
	}
}