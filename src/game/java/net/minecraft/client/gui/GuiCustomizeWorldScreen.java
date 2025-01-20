package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;
import java.io.IOException;
import java.util.Random;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

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
public class GuiCustomizeWorldScreen extends GuiScreen
		implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder {
	private GuiCreateWorld field_175343_i;
	protected String field_175341_a = "Customize World Settings";
	protected String field_175333_f = "Page 1 of 3";
	protected String field_175335_g = "Basic Settings";
	protected String[] field_175342_h = new String[4];
	private GuiPageButtonList field_175349_r;
	private GuiButton field_175348_s;
	private GuiButton field_175347_t;
	private GuiButton field_175346_u;
	private GuiButton field_175345_v;
	private GuiButton field_175344_w;
	private GuiButton field_175352_x;
	private GuiButton field_175351_y;
	private GuiButton field_175350_z;
	private boolean field_175338_A = false;
	private int field_175339_B = 0;
	private boolean field_175340_C = false;
	private Predicate<String> field_175332_D = new Predicate<String>() {
		public boolean apply(String s) {
			Float f = Floats.tryParse(s);
			return s.length() == 0 || f != null && Floats.isFinite(f.floatValue()) && f.floatValue() >= 0.0F;
		}
	};
	private ChunkProviderSettings.Factory field_175334_E = new ChunkProviderSettings.Factory();
	private ChunkProviderSettings.Factory field_175336_F;
	/**+
	 * A Random instance for this world customization
	 */
	private Random random = new Random();

	public GuiCustomizeWorldScreen(GuiScreen parGuiScreen, String parString1) {
		this.field_175343_i = (GuiCreateWorld) parGuiScreen;
		this.func_175324_a(parString1);
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		int i = 0;
		int j = 0;
		if (this.field_175349_r != null) {
			i = this.field_175349_r.func_178059_e();
			j = this.field_175349_r.getAmountScrolled();
		}

		this.field_175341_a = I18n.format("options.customizeTitle", new Object[0]);
		this.buttonList.clear();
		this.buttonList.add(this.field_175345_v = new GuiButton(302, 20, 5, 80, 20,
				I18n.format("createWorld.customize.custom.prev", new Object[0])));
		this.buttonList.add(this.field_175344_w = new GuiButton(303, this.width - 100, 5, 80, 20,
				I18n.format("createWorld.customize.custom.next", new Object[0])));
		this.buttonList.add(this.field_175346_u = new GuiButton(304, this.width / 2 - 187, this.height - 27, 90, 20,
				I18n.format("createWorld.customize.custom.defaults", new Object[0])));
		this.buttonList.add(this.field_175347_t = new GuiButton(301, this.width / 2 - 92, this.height - 27, 90, 20,
				I18n.format("createWorld.customize.custom.randomize", new Object[0])));
		this.buttonList.add(this.field_175350_z = new GuiButton(305, this.width / 2 + 3, this.height - 27, 90, 20,
				I18n.format("createWorld.customize.custom.presets", new Object[0])));
		this.buttonList.add(this.field_175348_s = new GuiButton(300, this.width / 2 + 98, this.height - 27, 90, 20,
				I18n.format("gui.done", new Object[0])));
		this.field_175346_u.enabled = this.field_175338_A;
		this.field_175352_x = new GuiButton(306, this.width / 2 - 55, 160, 50, 20,
				I18n.format("gui.yes", new Object[0]));
		this.field_175352_x.visible = false;
		this.buttonList.add(this.field_175352_x);
		this.field_175351_y = new GuiButton(307, this.width / 2 + 5, 160, 50, 20, I18n.format("gui.no", new Object[0]));
		this.field_175351_y.visible = false;
		this.buttonList.add(this.field_175351_y);
		if (this.field_175339_B != 0) {
			this.field_175352_x.visible = true;
			this.field_175351_y.visible = true;
		}

		this.func_175325_f();
		if (i != 0) {
			this.field_175349_r.func_181156_c(i);
			this.field_175349_r.scrollBy(j);
			this.func_175328_i();
		}

	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.field_175349_r.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.field_175349_r.handleTouchInput();
	}

	private void func_175325_f() {
		GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry = new GuiPageButtonList.GuiListEntry[] {
				new GuiPageButtonList.GuiSlideEntry(160,
						I18n.format("createWorld.customize.custom.seaLevel", new Object[0]), true, this, 1.0F, 255.0F,
						(float) this.field_175336_F.seaLevel),
				new GuiPageButtonList.GuiButtonEntry(148,
						I18n.format("createWorld.customize.custom.useCaves", new Object[0]), true,
						this.field_175336_F.useCaves),
				new GuiPageButtonList.GuiButtonEntry(150,
						I18n.format("createWorld.customize.custom.useStrongholds", new Object[0]), true,
						this.field_175336_F.useStrongholds),
				new GuiPageButtonList.GuiButtonEntry(151,
						I18n.format("createWorld.customize.custom.useVillages", new Object[0]), true,
						this.field_175336_F.useVillages),
				new GuiPageButtonList.GuiButtonEntry(152,
						I18n.format("createWorld.customize.custom.useMineShafts", new Object[0]), true,
						this.field_175336_F.useMineShafts),
				new GuiPageButtonList.GuiButtonEntry(153,
						I18n.format("createWorld.customize.custom.useTemples", new Object[0]), true,
						this.field_175336_F.useTemples),
				new GuiPageButtonList.GuiButtonEntry(210,
						I18n.format("createWorld.customize.custom.useMonuments", new Object[0]), true,
						this.field_175336_F.useMonuments),
				new GuiPageButtonList.GuiButtonEntry(154,
						I18n.format("createWorld.customize.custom.useRavines", new Object[0]), true,
						this.field_175336_F.useRavines),
				new GuiPageButtonList.GuiButtonEntry(149,
						I18n.format("createWorld.customize.custom.useDungeons", new Object[0]), true,
						this.field_175336_F.useDungeons),
				new GuiPageButtonList.GuiSlideEntry(157,
						I18n.format("createWorld.customize.custom.dungeonChance", new Object[0]), true, this, 1.0F,
						100.0F, (float) this.field_175336_F.dungeonChance),
				new GuiPageButtonList.GuiButtonEntry(155,
						I18n.format("createWorld.customize.custom.useWaterLakes", new Object[0]), true,
						this.field_175336_F.useWaterLakes),
				new GuiPageButtonList.GuiSlideEntry(158,
						I18n.format("createWorld.customize.custom.waterLakeChance", new Object[0]), true, this, 1.0F,
						100.0F, (float) this.field_175336_F.waterLakeChance),
				new GuiPageButtonList.GuiButtonEntry(156,
						I18n.format("createWorld.customize.custom.useLavaLakes", new Object[0]), true,
						this.field_175336_F.useLavaLakes),
				new GuiPageButtonList.GuiSlideEntry(159,
						I18n.format("createWorld.customize.custom.lavaLakeChance", new Object[0]), true, this, 10.0F,
						100.0F, (float) this.field_175336_F.lavaLakeChance),
				new GuiPageButtonList.GuiButtonEntry(161,
						I18n.format("createWorld.customize.custom.useLavaOceans", new Object[0]), true,
						this.field_175336_F.useLavaOceans),
				new GuiPageButtonList.GuiSlideEntry(162,
						I18n.format("createWorld.customize.custom.fixedBiome", new Object[0]), true, this, -1.0F, 37.0F,
						(float) this.field_175336_F.fixedBiome),
				new GuiPageButtonList.GuiSlideEntry(163,
						I18n.format("createWorld.customize.custom.biomeSize", new Object[0]), true, this, 1.0F, 8.0F,
						(float) this.field_175336_F.biomeSize),
				new GuiPageButtonList.GuiSlideEntry(164,
						I18n.format("createWorld.customize.custom.riverSize", new Object[0]), true, this, 1.0F, 5.0F,
						(float) this.field_175336_F.riverSize) };
		GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry1 = new GuiPageButtonList.GuiListEntry[] {
				new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(165,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.dirtSize),
				new GuiPageButtonList.GuiSlideEntry(166,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.dirtCount),
				new GuiPageButtonList.GuiSlideEntry(167,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.dirtMinHeight),
				new GuiPageButtonList.GuiSlideEntry(168,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.dirtMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(169,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.gravelSize),
				new GuiPageButtonList.GuiSlideEntry(170,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.gravelCount),
				new GuiPageButtonList.GuiSlideEntry(171,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.gravelMinHeight),
				new GuiPageButtonList.GuiSlideEntry(172,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.gravelMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name", new Object[0]), false),
				null,
				new GuiPageButtonList.GuiSlideEntry(173,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.graniteSize),
				new GuiPageButtonList.GuiSlideEntry(174,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.graniteCount),
				new GuiPageButtonList.GuiSlideEntry(175,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.graniteMinHeight),
				new GuiPageButtonList.GuiSlideEntry(176,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.graniteMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name", new Object[0]), false),
				null,
				new GuiPageButtonList.GuiSlideEntry(177,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.dioriteSize),
				new GuiPageButtonList.GuiSlideEntry(178,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.dioriteCount),
				new GuiPageButtonList.GuiSlideEntry(179,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.dioriteMinHeight),
				new GuiPageButtonList.GuiSlideEntry(180,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.dioriteMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name", new Object[0]), false),
				null,
				new GuiPageButtonList.GuiSlideEntry(181,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.andesiteSize),
				new GuiPageButtonList.GuiSlideEntry(182,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.andesiteCount),
				new GuiPageButtonList.GuiSlideEntry(183,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.andesiteMinHeight),
				new GuiPageButtonList.GuiSlideEntry(184,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.andesiteMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(185,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.coalSize),
				new GuiPageButtonList.GuiSlideEntry(186,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.coalCount),
				new GuiPageButtonList.GuiSlideEntry(187,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.coalMinHeight),
				new GuiPageButtonList.GuiSlideEntry(189,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.coalMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(190,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.ironSize),
				new GuiPageButtonList.GuiSlideEntry(191,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.ironCount),
				new GuiPageButtonList.GuiSlideEntry(192,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.ironMinHeight),
				new GuiPageButtonList.GuiSlideEntry(193,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.ironMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(194,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.goldSize),
				new GuiPageButtonList.GuiSlideEntry(195,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.goldCount),
				new GuiPageButtonList.GuiSlideEntry(196,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.goldMinHeight),
				new GuiPageButtonList.GuiSlideEntry(197,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.goldMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name", new Object[0]), false),
				null,
				new GuiPageButtonList.GuiSlideEntry(198,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.redstoneSize),
				new GuiPageButtonList.GuiSlideEntry(199,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.redstoneCount),
				new GuiPageButtonList.GuiSlideEntry(200,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.redstoneMinHeight),
				new GuiPageButtonList.GuiSlideEntry(201,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.redstoneMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name", new Object[0]), false),
				null,
				new GuiPageButtonList.GuiSlideEntry(202,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.diamondSize),
				new GuiPageButtonList.GuiSlideEntry(203,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.diamondCount),
				new GuiPageButtonList.GuiSlideEntry(204,
						I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.diamondMinHeight),
				new GuiPageButtonList.GuiSlideEntry(205,
						I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.diamondMaxHeight),
				new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name", new Object[0]), false), null,
				new GuiPageButtonList.GuiSlideEntry(206,
						I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F,
						(float) this.field_175336_F.lapisSize),
				new GuiPageButtonList.GuiSlideEntry(207,
						I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F,
						(float) this.field_175336_F.lapisCount),
				new GuiPageButtonList.GuiSlideEntry(208,
						I18n.format("createWorld.customize.custom.center", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.lapisCenterHeight),
				new GuiPageButtonList.GuiSlideEntry(209,
						I18n.format("createWorld.customize.custom.spread", new Object[0]), false, this, 0.0F, 255.0F,
						(float) this.field_175336_F.lapisSpread) };
		GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry2 = new GuiPageButtonList.GuiListEntry[] {
				new GuiPageButtonList.GuiSlideEntry(100,
						I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]), false, this, 1.0F,
						5000.0F, this.field_175336_F.mainNoiseScaleX),
				new GuiPageButtonList.GuiSlideEntry(101,
						I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]), false, this, 1.0F,
						5000.0F, this.field_175336_F.mainNoiseScaleY),
				new GuiPageButtonList.GuiSlideEntry(102,
						I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]), false, this, 1.0F,
						5000.0F, this.field_175336_F.mainNoiseScaleZ),
				new GuiPageButtonList.GuiSlideEntry(103,
						I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]), false, this, 1.0F,
						2000.0F, this.field_175336_F.depthNoiseScaleX),
				new GuiPageButtonList.GuiSlideEntry(104,
						I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]), false, this, 1.0F,
						2000.0F, this.field_175336_F.depthNoiseScaleZ),
				new GuiPageButtonList.GuiSlideEntry(105,
						I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]), false, this,
						0.01F, 20.0F, this.field_175336_F.depthNoiseScaleExponent),
				new GuiPageButtonList.GuiSlideEntry(106,
						I18n.format("createWorld.customize.custom.baseSize", new Object[0]), false, this, 1.0F, 25.0F,
						this.field_175336_F.baseSize),
				new GuiPageButtonList.GuiSlideEntry(107,
						I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]), false, this, 1.0F,
						6000.0F, this.field_175336_F.coordinateScale),
				new GuiPageButtonList.GuiSlideEntry(108,
						I18n.format("createWorld.customize.custom.heightScale", new Object[0]), false, this, 1.0F,
						6000.0F, this.field_175336_F.heightScale),
				new GuiPageButtonList.GuiSlideEntry(109,
						I18n.format("createWorld.customize.custom.stretchY", new Object[0]), false, this, 0.01F, 50.0F,
						this.field_175336_F.stretchY),
				new GuiPageButtonList.GuiSlideEntry(110,
						I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]), false, this, 1.0F,
						5000.0F, this.field_175336_F.upperLimitScale),
				new GuiPageButtonList.GuiSlideEntry(111,
						I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]), false, this, 1.0F,
						5000.0F, this.field_175336_F.lowerLimitScale),
				new GuiPageButtonList.GuiSlideEntry(112,
						I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]), false, this, 1.0F,
						20.0F, this.field_175336_F.biomeDepthWeight),
				new GuiPageButtonList.GuiSlideEntry(113,
						I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]), false, this, 0.0F,
						20.0F, this.field_175336_F.biomeDepthOffset),
				new GuiPageButtonList.GuiSlideEntry(114,
						I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]), false, this, 1.0F,
						20.0F, this.field_175336_F.biomeScaleWeight),
				new GuiPageButtonList.GuiSlideEntry(115,
						I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]), false, this, 0.0F,
						20.0F, this.field_175336_F.biomeScaleOffset) };
		GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry3 = new GuiPageButtonList.GuiListEntry[] {
				new GuiPageButtonList.GuiLabelEntry(400,
						I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(132,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleX) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(401,
						I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(133,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleY) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(402,
						I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(134,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleZ) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(403,
						I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(135,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleX) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(404,
						I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(136,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleZ) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(405,
						I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]) + ":",
						false),
				new GuiPageButtonList.EditBoxEntry(137,
						HString.format("%2.3f",
								new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleExponent) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(406,
						I18n.format("createWorld.customize.custom.baseSize", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(138,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.baseSize) }), false,
						this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(407,
						I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(139,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.coordinateScale) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(408,
						I18n.format("createWorld.customize.custom.heightScale", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(140,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.heightScale) }), false,
						this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(409,
						I18n.format("createWorld.customize.custom.stretchY", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(141,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.stretchY) }), false,
						this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(410,
						I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(142,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.upperLimitScale) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(411,
						I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(143,
						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.lowerLimitScale) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(412,
						I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(144,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthWeight) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(413,
						I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(145,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthOffset) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(414,
						I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(146,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleWeight) }),
						false, this.field_175332_D),
				new GuiPageButtonList.GuiLabelEntry(415,
						I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]) + ":", false),
				new GuiPageButtonList.EditBoxEntry(147,
						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleOffset) }),
						false, this.field_175332_D) };
		this.field_175349_r = new GuiPageButtonList(this.mc, this.width, this.height, 32, this.height - 32, 25, this,
				new GuiPageButtonList.GuiListEntry[][] { aguipagebuttonlist$guilistentry,
						aguipagebuttonlist$guilistentry1, aguipagebuttonlist$guilistentry2,
						aguipagebuttonlist$guilistentry3 });

		for (int i = 0; i < 4; ++i) {
			this.field_175342_h[i] = I18n.format("createWorld.customize.custom.page" + i, new Object[0]);
		}

		this.func_175328_i();
	}

	public String func_175323_a() {
		return this.field_175336_F.toString().replace("\n", "");
	}

	public void func_175324_a(String parString1) {
		if (parString1 != null && parString1.length() != 0) {
			this.field_175336_F = ChunkProviderSettings.Factory.jsonToFactory(parString1);
		} else {
			this.field_175336_F = new ChunkProviderSettings.Factory();
		}

	}

	public void func_175319_a(int i, String s) {
		float f = 0.0F;

		try {
			f = Float.parseFloat(s);
		} catch (NumberFormatException var5) {
			;
		}

		float f1 = 0.0F;
		switch (i) {
		case 132:
			f1 = this.field_175336_F.mainNoiseScaleX = MathHelper.clamp_float(f, 1.0F, 5000.0F);
			break;
		case 133:
			f1 = this.field_175336_F.mainNoiseScaleY = MathHelper.clamp_float(f, 1.0F, 5000.0F);
			break;
		case 134:
			f1 = this.field_175336_F.mainNoiseScaleZ = MathHelper.clamp_float(f, 1.0F, 5000.0F);
			break;
		case 135:
			f1 = this.field_175336_F.depthNoiseScaleX = MathHelper.clamp_float(f, 1.0F, 2000.0F);
			break;
		case 136:
			f1 = this.field_175336_F.depthNoiseScaleZ = MathHelper.clamp_float(f, 1.0F, 2000.0F);
			break;
		case 137:
			f1 = this.field_175336_F.depthNoiseScaleExponent = MathHelper.clamp_float(f, 0.01F, 20.0F);
			break;
		case 138:
			f1 = this.field_175336_F.baseSize = MathHelper.clamp_float(f, 1.0F, 25.0F);
			break;
		case 139:
			f1 = this.field_175336_F.coordinateScale = MathHelper.clamp_float(f, 1.0F, 6000.0F);
			break;
		case 140:
			f1 = this.field_175336_F.heightScale = MathHelper.clamp_float(f, 1.0F, 6000.0F);
			break;
		case 141:
			f1 = this.field_175336_F.stretchY = MathHelper.clamp_float(f, 0.01F, 50.0F);
			break;
		case 142:
			f1 = this.field_175336_F.upperLimitScale = MathHelper.clamp_float(f, 1.0F, 5000.0F);
			break;
		case 143:
			f1 = this.field_175336_F.lowerLimitScale = MathHelper.clamp_float(f, 1.0F, 5000.0F);
			break;
		case 144:
			f1 = this.field_175336_F.biomeDepthWeight = MathHelper.clamp_float(f, 1.0F, 20.0F);
			break;
		case 145:
			f1 = this.field_175336_F.biomeDepthOffset = MathHelper.clamp_float(f, 0.0F, 20.0F);
			break;
		case 146:
			f1 = this.field_175336_F.biomeScaleWeight = MathHelper.clamp_float(f, 1.0F, 20.0F);
			break;
		case 147:
			f1 = this.field_175336_F.biomeScaleOffset = MathHelper.clamp_float(f, 0.0F, 20.0F);
		}

		if (f1 != f && f != 0.0F) {
			((GuiTextField) this.field_175349_r.func_178061_c(i)).setText(this.func_175330_b(i, f1));
		}

		((GuiSlider) this.field_175349_r.func_178061_c(i - 132 + 100)).func_175218_a(f1, false);
		if (!this.field_175336_F.equals(this.field_175334_E)) {
			this.func_181031_a(true);
		}

	}

	private void func_181031_a(boolean parFlag) {
		this.field_175338_A = parFlag;
		this.field_175346_u.enabled = parFlag;
	}

	public String getText(int i, String s, float f) {
		return s + ": " + this.func_175330_b(i, f);
	}

	private String func_175330_b(int parInt1, float parFloat1) {
		switch (parInt1) {
		case 100:
		case 101:
		case 102:
		case 103:
		case 104:
		case 107:
		case 108:
		case 110:
		case 111:
		case 132:
		case 133:
		case 134:
		case 135:
		case 136:
		case 139:
		case 140:
		case 142:
		case 143:
			return HString.format("%5.3f", new Object[] { Float.valueOf(parFloat1) });
		case 105:
		case 106:
		case 109:
		case 112:
		case 113:
		case 114:
		case 115:
		case 137:
		case 138:
		case 141:
		case 144:
		case 145:
		case 146:
		case 147:
			return HString.format("%2.3f", new Object[] { Float.valueOf(parFloat1) });
		case 116:
		case 117:
		case 118:
		case 119:
		case 120:
		case 121:
		case 122:
		case 123:
		case 124:
		case 125:
		case 126:
		case 127:
		case 128:
		case 129:
		case 130:
		case 131:
		case 148:
		case 149:
		case 150:
		case 151:
		case 152:
		case 153:
		case 154:
		case 155:
		case 156:
		case 157:
		case 158:
		case 159:
		case 160:
		case 161:
		default:
			return HString.format("%d", new Object[] { Integer.valueOf((int) parFloat1) });
		case 162:
			if (parFloat1 < 0.0F) {
				return I18n.format("gui.all", new Object[0]);
			} else if ((int) parFloat1 >= BiomeGenBase.hell.biomeID) {
				BiomeGenBase biomegenbase1 = BiomeGenBase.getBiomeGenArray()[(int) parFloat1 + 2];
				return biomegenbase1 != null ? biomegenbase1.biomeName : "?";
			} else {
				BiomeGenBase biomegenbase = BiomeGenBase.getBiomeGenArray()[(int) parFloat1];
				return biomegenbase != null ? biomegenbase.biomeName : "?";
			}
		}
	}

	public void func_175321_a(int i, boolean flag) {
		switch (i) {
		case 148:
			this.field_175336_F.useCaves = flag;
			break;
		case 149:
			this.field_175336_F.useDungeons = flag;
			break;
		case 150:
			this.field_175336_F.useStrongholds = flag;
			break;
		case 151:
			this.field_175336_F.useVillages = flag;
			break;
		case 152:
			this.field_175336_F.useMineShafts = flag;
			break;
		case 153:
			this.field_175336_F.useTemples = flag;
			break;
		case 154:
			this.field_175336_F.useRavines = flag;
			break;
		case 155:
			this.field_175336_F.useWaterLakes = flag;
			break;
		case 156:
			this.field_175336_F.useLavaLakes = flag;
			break;
		case 161:
			this.field_175336_F.useLavaOceans = flag;
			break;
		case 210:
			this.field_175336_F.useMonuments = flag;
		}

		if (!this.field_175336_F.equals(this.field_175334_E)) {
			this.func_181031_a(true);
		}

	}

	public void onTick(int i, float f) {
		switch (i) {
		case 100:
			this.field_175336_F.mainNoiseScaleX = f;
			break;
		case 101:
			this.field_175336_F.mainNoiseScaleY = f;
			break;
		case 102:
			this.field_175336_F.mainNoiseScaleZ = f;
			break;
		case 103:
			this.field_175336_F.depthNoiseScaleX = f;
			break;
		case 104:
			this.field_175336_F.depthNoiseScaleZ = f;
			break;
		case 105:
			this.field_175336_F.depthNoiseScaleExponent = f;
			break;
		case 106:
			this.field_175336_F.baseSize = f;
			break;
		case 107:
			this.field_175336_F.coordinateScale = f;
			break;
		case 108:
			this.field_175336_F.heightScale = f;
			break;
		case 109:
			this.field_175336_F.stretchY = f;
			break;
		case 110:
			this.field_175336_F.upperLimitScale = f;
			break;
		case 111:
			this.field_175336_F.lowerLimitScale = f;
			break;
		case 112:
			this.field_175336_F.biomeDepthWeight = f;
			break;
		case 113:
			this.field_175336_F.biomeDepthOffset = f;
			break;
		case 114:
			this.field_175336_F.biomeScaleWeight = f;
			break;
		case 115:
			this.field_175336_F.biomeScaleOffset = f;
		case 116:
		case 117:
		case 118:
		case 119:
		case 120:
		case 121:
		case 122:
		case 123:
		case 124:
		case 125:
		case 126:
		case 127:
		case 128:
		case 129:
		case 130:
		case 131:
		case 132:
		case 133:
		case 134:
		case 135:
		case 136:
		case 137:
		case 138:
		case 139:
		case 140:
		case 141:
		case 142:
		case 143:
		case 144:
		case 145:
		case 146:
		case 147:
		case 148:
		case 149:
		case 150:
		case 151:
		case 152:
		case 153:
		case 154:
		case 155:
		case 156:
		case 161:
		case 188:
		default:
			break;
		case 157:
			this.field_175336_F.dungeonChance = (int) f;
			break;
		case 158:
			this.field_175336_F.waterLakeChance = (int) f;
			break;
		case 159:
			this.field_175336_F.lavaLakeChance = (int) f;
			break;
		case 160:
			this.field_175336_F.seaLevel = (int) f;
			break;
		case 162:
			this.field_175336_F.fixedBiome = (int) f;
			break;
		case 163:
			this.field_175336_F.biomeSize = (int) f;
			break;
		case 164:
			this.field_175336_F.riverSize = (int) f;
			break;
		case 165:
			this.field_175336_F.dirtSize = (int) f;
			break;
		case 166:
			this.field_175336_F.dirtCount = (int) f;
			break;
		case 167:
			this.field_175336_F.dirtMinHeight = (int) f;
			break;
		case 168:
			this.field_175336_F.dirtMaxHeight = (int) f;
			break;
		case 169:
			this.field_175336_F.gravelSize = (int) f;
			break;
		case 170:
			this.field_175336_F.gravelCount = (int) f;
			break;
		case 171:
			this.field_175336_F.gravelMinHeight = (int) f;
			break;
		case 172:
			this.field_175336_F.gravelMaxHeight = (int) f;
			break;
		case 173:
			this.field_175336_F.graniteSize = (int) f;
			break;
		case 174:
			this.field_175336_F.graniteCount = (int) f;
			break;
		case 175:
			this.field_175336_F.graniteMinHeight = (int) f;
			break;
		case 176:
			this.field_175336_F.graniteMaxHeight = (int) f;
			break;
		case 177:
			this.field_175336_F.dioriteSize = (int) f;
			break;
		case 178:
			this.field_175336_F.dioriteCount = (int) f;
			break;
		case 179:
			this.field_175336_F.dioriteMinHeight = (int) f;
			break;
		case 180:
			this.field_175336_F.dioriteMaxHeight = (int) f;
			break;
		case 181:
			this.field_175336_F.andesiteSize = (int) f;
			break;
		case 182:
			this.field_175336_F.andesiteCount = (int) f;
			break;
		case 183:
			this.field_175336_F.andesiteMinHeight = (int) f;
			break;
		case 184:
			this.field_175336_F.andesiteMaxHeight = (int) f;
			break;
		case 185:
			this.field_175336_F.coalSize = (int) f;
			break;
		case 186:
			this.field_175336_F.coalCount = (int) f;
			break;
		case 187:
			this.field_175336_F.coalMinHeight = (int) f;
			break;
		case 189:
			this.field_175336_F.coalMaxHeight = (int) f;
			break;
		case 190:
			this.field_175336_F.ironSize = (int) f;
			break;
		case 191:
			this.field_175336_F.ironCount = (int) f;
			break;
		case 192:
			this.field_175336_F.ironMinHeight = (int) f;
			break;
		case 193:
			this.field_175336_F.ironMaxHeight = (int) f;
			break;
		case 194:
			this.field_175336_F.goldSize = (int) f;
			break;
		case 195:
			this.field_175336_F.goldCount = (int) f;
			break;
		case 196:
			this.field_175336_F.goldMinHeight = (int) f;
			break;
		case 197:
			this.field_175336_F.goldMaxHeight = (int) f;
			break;
		case 198:
			this.field_175336_F.redstoneSize = (int) f;
			break;
		case 199:
			this.field_175336_F.redstoneCount = (int) f;
			break;
		case 200:
			this.field_175336_F.redstoneMinHeight = (int) f;
			break;
		case 201:
			this.field_175336_F.redstoneMaxHeight = (int) f;
			break;
		case 202:
			this.field_175336_F.diamondSize = (int) f;
			break;
		case 203:
			this.field_175336_F.diamondCount = (int) f;
			break;
		case 204:
			this.field_175336_F.diamondMinHeight = (int) f;
			break;
		case 205:
			this.field_175336_F.diamondMaxHeight = (int) f;
			break;
		case 206:
			this.field_175336_F.lapisSize = (int) f;
			break;
		case 207:
			this.field_175336_F.lapisCount = (int) f;
			break;
		case 208:
			this.field_175336_F.lapisCenterHeight = (int) f;
			break;
		case 209:
			this.field_175336_F.lapisSpread = (int) f;
		}

		if (i >= 100 && i < 116) {
			Gui gui = this.field_175349_r.func_178061_c(i - 100 + 132);
			if (gui != null) {
				((GuiTextField) gui).setText(this.func_175330_b(i, f));
			}
		}

		if (!this.field_175336_F.equals(this.field_175334_E)) {
			this.func_181031_a(true);
		}

	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			switch (parGuiButton.id) {
			case 300:
				this.field_175343_i.chunkProviderSettingsJson = this.field_175336_F.toString();
				this.mc.displayGuiScreen(this.field_175343_i);
				break;
			case 301:
				for (int i = 0; i < this.field_175349_r.getSize(); ++i) {
					GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = this.field_175349_r.getListEntry(i);
					Gui gui = guipagebuttonlist$guientry.func_178022_a();
					if (gui instanceof GuiButton) {
						GuiButton guibutton = (GuiButton) gui;
						if (guibutton instanceof GuiSlider) {
							float f = ((GuiSlider) guibutton).func_175217_d() * (0.75F + this.random.nextFloat() * 0.5F)
									+ (this.random.nextFloat() * 0.1F - 0.05F);
							((GuiSlider) guibutton).func_175219_a(MathHelper.clamp_float(f, 0.0F, 1.0F));
						} else if (guibutton instanceof GuiListButton) {
							((GuiListButton) guibutton).func_175212_b(this.random.nextBoolean());
						}
					}

					Gui gui1 = guipagebuttonlist$guientry.func_178021_b();
					if (gui1 instanceof GuiButton) {
						GuiButton guibutton1 = (GuiButton) gui1;
						if (guibutton1 instanceof GuiSlider) {
							float f1 = ((GuiSlider) guibutton1).func_175217_d()
									* (0.75F + this.random.nextFloat() * 0.5F)
									+ (this.random.nextFloat() * 0.1F - 0.05F);
							((GuiSlider) guibutton1).func_175219_a(MathHelper.clamp_float(f1, 0.0F, 1.0F));
						} else if (guibutton1 instanceof GuiListButton) {
							((GuiListButton) guibutton1).func_175212_b(this.random.nextBoolean());
						}
					}
				}

				return;
			case 302:
				this.field_175349_r.func_178071_h();
				this.func_175328_i();
				break;
			case 303:
				this.field_175349_r.func_178064_i();
				this.func_175328_i();
				break;
			case 304:
				if (this.field_175338_A) {
					this.func_175322_b(304);
				}
				break;
			case 305:
				this.mc.displayGuiScreen(new GuiScreenCustomizePresets(this));
				break;
			case 306:
				this.func_175331_h();
				break;
			case 307:
				this.field_175339_B = 0;
				this.func_175331_h();
			}

		}
	}

	private void func_175326_g() {
		this.field_175336_F.func_177863_a();
		this.func_175325_f();
		this.func_181031_a(false);
	}

	private void func_175322_b(int parInt1) {
		this.field_175339_B = parInt1;
		this.func_175329_a(true);
	}

	private void func_175331_h() {
		switch (this.field_175339_B) {
		case 300:
			this.actionPerformed((GuiListButton) this.field_175349_r.func_178061_c(300));
			break;
		case 304:
			this.func_175326_g();
		}

		this.field_175339_B = 0;
		this.field_175340_C = true;
		this.func_175329_a(false);
	}

	private void func_175329_a(boolean parFlag) {
		this.field_175352_x.visible = parFlag;
		this.field_175351_y.visible = parFlag;
		this.field_175347_t.enabled = !parFlag;
		this.field_175348_s.enabled = !parFlag;
		this.field_175345_v.enabled = !parFlag;
		this.field_175344_w.enabled = !parFlag;
		this.field_175346_u.enabled = this.field_175338_A && !parFlag;
		this.field_175350_z.enabled = !parFlag;
		this.field_175349_r.func_181155_a(!parFlag);
	}

	private void func_175328_i() {
		this.field_175345_v.enabled = this.field_175349_r.func_178059_e() != 0;
		this.field_175344_w.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
		this.field_175333_f = I18n.format("book.pageIndicator",
				new Object[] { Integer.valueOf(this.field_175349_r.func_178059_e() + 1),
						Integer.valueOf(this.field_175349_r.func_178057_f()) });
		this.field_175335_g = this.field_175342_h[this.field_175349_r.func_178059_e()];
		this.field_175347_t.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		super.keyTyped(parChar1, parInt1);
		if (this.field_175339_B == 0) {
			switch (parInt1) {
			case 200:
				this.func_175327_a(1.0F);
				break;
			case 208:
				this.func_175327_a(-1.0F);
				break;
			default:
				this.field_175349_r.func_178062_a(parChar1, parInt1);
			}

		}
	}

	private void func_175327_a(float parFloat1) {
		Gui gui = this.field_175349_r.func_178056_g();
		if (gui instanceof GuiTextField) {
			float f = parFloat1;
			if (GuiScreen.isShiftKeyDown()) {
				f = parFloat1 * 0.1F;
				if (GuiScreen.isCtrlKeyDown()) {
					f *= 0.1F;
				}
			} else if (GuiScreen.isCtrlKeyDown()) {
				f = parFloat1 * 10.0F;
				if (GuiScreen.isAltKeyDown()) {
					f *= 10.0F;
				}
			}

			GuiTextField guitextfield = (GuiTextField) gui;
			Float f1 = Floats.tryParse(guitextfield.getText());
			if (f1 != null) {
				f1 = Float.valueOf(f1.floatValue() + f);
				int i = guitextfield.getId();
				String s = this.func_175330_b(guitextfield.getId(), f1.floatValue());
				guitextfield.setText(s);
				this.func_175319_a(i, s);
			}
		}
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		if (this.field_175339_B == 0 && !this.field_175340_C) {
			this.field_175349_r.mouseClicked(parInt1, parInt2, parInt3);
		}
	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		super.mouseReleased(i, j, k);
		if (this.field_175340_C) {
			this.field_175340_C = false;
		} else if (this.field_175339_B == 0) {
			this.field_175349_r.mouseReleased(i, j, k);
		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.field_175349_r.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, this.field_175341_a, this.width / 2, 2, 16777215);
		this.drawCenteredString(this.fontRendererObj, this.field_175333_f, this.width / 2, 12, 16777215);
		this.drawCenteredString(this.fontRendererObj, this.field_175335_g, this.width / 2, 22, 16777215);
		super.drawScreen(i, j, f);
		if (this.field_175339_B != 0) {
			drawRect(0, 0, this.width, this.height, Integer.MIN_VALUE);
			this.drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 99, -2039584);
			this.drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 185, -6250336);
			this.drawVerticalLine(this.width / 2 - 91, 99, 185, -2039584);
			this.drawVerticalLine(this.width / 2 + 90, 99, 185, -6250336);
			float f1 = 85.0F;
			float f2 = 180.0F;
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			this.mc.getTextureManager().bindTexture(optionsBackground);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			float f3 = 32.0F;
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			worldrenderer.pos((double) (this.width / 2 - 90), 185.0D, 0.0D).tex(0.0D, 2.65625D).color(64, 64, 64, 64)
					.endVertex();
			worldrenderer.pos((double) (this.width / 2 + 90), 185.0D, 0.0D).tex(5.625D, 2.65625D).color(64, 64, 64, 64)
					.endVertex();
			worldrenderer.pos((double) (this.width / 2 + 90), 100.0D, 0.0D).tex(5.625D, 0.0D).color(64, 64, 64, 64)
					.endVertex();
			worldrenderer.pos((double) (this.width / 2 - 90), 100.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 64)
					.endVertex();
			tessellator.draw();
			this.drawCenteredString(this.fontRendererObj,
					I18n.format("createWorld.customize.custom.confirmTitle", new Object[0]), this.width / 2, 105,
					16777215);
			this.drawCenteredString(this.fontRendererObj,
					I18n.format("createWorld.customize.custom.confirm1", new Object[0]), this.width / 2, 125, 16777215);
			this.drawCenteredString(this.fontRendererObj,
					I18n.format("createWorld.customize.custom.confirm2", new Object[0]), this.width / 2, 135, 16777215);
			this.field_175352_x.drawButton(this.mc, i, j);
			this.field_175351_y.drawButton(this.mc, i, j);
		}

	}

	@Override
	public boolean showCopyPasteButtons() {
		return field_175349_r.isTextFieldFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		field_175349_r.fireInputEvent(event, param);
	}

}