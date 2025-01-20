package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

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
public class GuiFlatPresets extends GuiScreen {
	private static final List<GuiFlatPresets.LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
	private final GuiCreateFlatWorld parentScreen;
	private String presetsTitle;
	private String presetsShare;
	private String field_146436_r;
	private GuiFlatPresets.ListSlot field_146435_s;
	private GuiButton field_146434_t;
	private GuiTextField field_146433_u;

	public GuiFlatPresets(GuiCreateFlatWorld parGuiCreateFlatWorld) {
		this.parentScreen = parGuiCreateFlatWorld;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
		this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
		this.field_146436_r = I18n.format("createWorld.customize.presets.list", new Object[0]);
		this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, this.width - 100, 20);
		this.field_146435_s = new GuiFlatPresets.ListSlot();
		this.field_146433_u.setMaxStringLength(1230);
		this.field_146433_u.setText(this.parentScreen.func_146384_e());
		this.buttonList.add(this.field_146434_t = new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20,
				I18n.format("createWorld.customize.presets.select", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		this.func_146426_g();
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.field_146435_s.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.field_146435_s.handleTouchInput();
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		this.field_146433_u.mouseClicked(parInt1, parInt2, parInt3);
		super.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (!this.field_146433_u.textboxKeyTyped(parChar1, parInt1)) {
			super.keyTyped(parChar1, parInt1);
		}

	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.id == 0 && this.func_146430_p()) {
			this.parentScreen.func_146383_a(this.field_146433_u.getText());
			this.mc.displayGuiScreen(this.parentScreen);
		} else if (parGuiButton.id == 1) {
			this.mc.displayGuiScreen(this.parentScreen);
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.field_146435_s.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, this.presetsTitle, this.width / 2, 8, 16777215);
		this.drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
		this.drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
		this.field_146433_u.drawTextBox();
		super.drawScreen(i, j, f);
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.field_146433_u.updateCursorCounter();
		super.updateScreen();
	}

	public void func_146426_g() {
		boolean flag = this.func_146430_p();
		this.field_146434_t.enabled = flag;
	}

	private boolean func_146430_p() {
		return this.field_146435_s.field_148175_k > -1 && this.field_146435_s.field_148175_k < FLAT_WORLD_PRESETS.size()
				|| this.field_146433_u.getText().length() > 1;
	}

	private static void func_146425_a(String parString1, Item parItem, BiomeGenBase parBiomeGenBase,
			FlatLayerInfo... parArrayOfFlatLayerInfo) {
		func_175354_a(parString1, parItem, 0, parBiomeGenBase, (List<String>) null, parArrayOfFlatLayerInfo);
	}

	private static void func_146421_a(String parString1, Item parItem, BiomeGenBase parBiomeGenBase,
			List<String> parList, FlatLayerInfo... parArrayOfFlatLayerInfo) {
		func_175354_a(parString1, parItem, 0, parBiomeGenBase, parList, parArrayOfFlatLayerInfo);
	}

	private static void func_175354_a(String parString1, Item parItem, int parInt1, BiomeGenBase parBiomeGenBase,
			List<String> parList, FlatLayerInfo... parArrayOfFlatLayerInfo) {
		FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();

		for (int i = parArrayOfFlatLayerInfo.length - 1; i >= 0; --i) {
			flatgeneratorinfo.getFlatLayers().add(parArrayOfFlatLayerInfo[i]);
		}

		flatgeneratorinfo.setBiome(parBiomeGenBase.biomeID);
		flatgeneratorinfo.func_82645_d();
		if (parList != null) {
			for (int i = 0, l = parList.size(); i < l; ++i) {
				flatgeneratorinfo.getWorldFeatures().put(parList.get(i), Maps.newHashMap());
			}
		}

		FLAT_WORLD_PRESETS
				.add(new GuiFlatPresets.LayerItem(parItem, parInt1, parString1, flatgeneratorinfo.toString()));
	}

	static {
		func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains,
				Arrays.asList(new String[] { "village" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass),
						new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock) });
		func_146421_a("Tunnelers\' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills,
				Arrays.asList(new String[] { "biome_1", "dungeon", "decoration", "stronghold", "mineshaft" }),
				new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt),
						new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
		func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean,
				Arrays.asList(new String[] { "biome_1", "oceanmonument" }),
				new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand),
						new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone),
						new FlatLayerInfo(1, Blocks.bedrock) });
		func_175354_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(),
				BiomeGenBase.plains,
				Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon",
						"lake", "lava_lake" }),
				new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt),
						new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
		func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains,
				Arrays.asList(new String[] { "village", "biome_1" }),
				new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass),
						new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone),
						new FlatLayerInfo(1, Blocks.bedrock) });
		func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains,
				Arrays.asList(new String[] { "village", "biome_1" }),
				new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt),
						new FlatLayerInfo(2, Blocks.cobblestone) });
		func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert,
				Arrays.asList(
						new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon" }),
				new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone),
						new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
		func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert,
				new FlatLayerInfo[] { new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone),
						new FlatLayerInfo(1, Blocks.bedrock) });
	}

	static class LayerItem {
		public Item field_148234_a;
		public int field_179037_b;
		public String field_148232_b;
		public String field_148233_c;

		public LayerItem(Item parItem, int parInt1, String parString1, String parString2) {
			this.field_148234_a = parItem;
			this.field_179037_b = parInt1;
			this.field_148232_b = parString1;
			this.field_148233_c = parString2;
		}
	}

	class ListSlot extends GuiSlot {
		public int field_148175_k = -1;

		public ListSlot() {
			super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80,
					GuiFlatPresets.this.height - 37, 24);
		}

		private void func_178054_a(int parInt1, int parInt2, Item parItem, int parInt3) {
			this.func_148173_e(parInt1 + 1, parInt2 + 1);
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();
			GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(parItem, 1, parInt3), parInt1 + 2,
					parInt2 + 2);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
		}

		private void func_148173_e(int parInt1, int parInt2) {
			this.func_148171_c(parInt1, parInt2, 0, 0);
		}

		private void func_148171_c(int parInt1, int parInt2, int parInt3, int parInt4) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(Gui.statIcons);
			float f = 0.0078125F;
			float f1 = 0.0078125F;
			boolean flag = true;
			boolean flag1 = true;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos((double) (parInt1 + 0), (double) (parInt2 + 18), (double) GuiFlatPresets.this.zLevel)
					.tex((double) ((float) (parInt3 + 0) * 0.0078125F), (double) ((float) (parInt4 + 18) * 0.0078125F))
					.endVertex();
			worldrenderer.pos((double) (parInt1 + 18), (double) (parInt2 + 18), (double) GuiFlatPresets.this.zLevel)
					.tex((double) ((float) (parInt3 + 18) * 0.0078125F), (double) ((float) (parInt4 + 18) * 0.0078125F))
					.endVertex();
			worldrenderer.pos((double) (parInt1 + 18), (double) (parInt2 + 0), (double) GuiFlatPresets.this.zLevel)
					.tex((double) ((float) (parInt3 + 18) * 0.0078125F), (double) ((float) (parInt4 + 0) * 0.0078125F))
					.endVertex();
			worldrenderer.pos((double) (parInt1 + 0), (double) (parInt2 + 0), (double) GuiFlatPresets.this.zLevel)
					.tex((double) ((float) (parInt3 + 0) * 0.0078125F), (double) ((float) (parInt4 + 0) * 0.0078125F))
					.endVertex();
			tessellator.draw();
		}

		protected int getSize() {
			return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
		}

		protected void elementClicked(int i, boolean var2, int var3, int var4) {
			this.field_148175_k = i;
			GuiFlatPresets.this.func_146426_g();
			GuiFlatPresets.this.field_146433_u.setText(((GuiFlatPresets.LayerItem) GuiFlatPresets.FLAT_WORLD_PRESETS
					.get(GuiFlatPresets.this.field_146435_s.field_148175_k)).field_148233_c);
		}

		protected boolean isSelected(int i) {
			return i == this.field_148175_k;
		}

		protected void drawBackground() {
		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			GuiFlatPresets.LayerItem guiflatpresets$layeritem = (GuiFlatPresets.LayerItem) GuiFlatPresets.FLAT_WORLD_PRESETS
					.get(i);
			this.func_178054_a(j, k, guiflatpresets$layeritem.field_148234_a, guiflatpresets$layeritem.field_179037_b);
			GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.field_148232_b, j + 18 + 5, k + 6,
					16777215);
		}
	}

	@Override
	public boolean showCopyPasteButtons() {
		return field_146433_u.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		field_146433_u.fireInputEvent(event, param);
	}

}