package net.minecraft.client.gui.achievement;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
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
public class GuiStats extends GuiScreen implements IProgressMeter {
	protected GuiScreen parentScreen;
	protected String screenTitle = "Select world";
	private GuiStats.StatsGeneral generalStats;
	private GuiStats.StatsItem itemStats;
	private GuiStats.StatsBlock blockStats;
	private GuiStats.StatsMobsList mobStats;
	private StatFileWriter field_146546_t;
	private GuiSlot displaySlot;
	/**+
	 * When true, the game will be paused when the gui is shown
	 */
	private boolean doesGuiPauseGame = true;

	public GuiStats(GuiScreen parGuiScreen, StatFileWriter parStatFileWriter) {
		this.parentScreen = parGuiScreen;
		this.field_146546_t = parStatFileWriter;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.screenTitle = I18n.format("gui.stats", new Object[0]);
		this.doesGuiPauseGame = true;
		this.mc.getNetHandler()
				.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if (this.displaySlot != null) {
			this.displaySlot.handleMouseInput();
		}

	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		if (this.displaySlot != null) {
			this.displaySlot.handleTouchInput();
		}
	}

	public void func_175366_f() {
		this.generalStats = new GuiStats.StatsGeneral(this.mc);
		this.generalStats.registerScrollButtons(1, 1);
		this.itemStats = new GuiStats.StatsItem(this.mc);
		this.itemStats.registerScrollButtons(1, 1);
		this.blockStats = new GuiStats.StatsBlock(this.mc);
		this.blockStats.registerScrollButtons(1, 1);
		this.mobStats = new GuiStats.StatsMobsList(this.mc);
		this.mobStats.registerScrollButtons(1, 1);
	}

	public void createButtons() {
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20,
				I18n.format("gui.done", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height - 52, 80, 20,
				I18n.format("stat.generalButton", new Object[0])));
		GuiButton guibutton;
		this.buttonList.add(guibutton = new GuiButton(2, this.width / 2 - 80, this.height - 52, 80, 20,
				I18n.format("stat.blocksButton", new Object[0])));
		GuiButton guibutton1;
		this.buttonList.add(guibutton1 = new GuiButton(3, this.width / 2, this.height - 52, 80, 20,
				I18n.format("stat.itemsButton", new Object[0])));
		GuiButton guibutton2;
		this.buttonList.add(guibutton2 = new GuiButton(4, this.width / 2 + 80, this.height - 52, 80, 20,
				I18n.format("stat.mobsButton", new Object[0])));
		if (this.blockStats.getSize() == 0) {
			guibutton.enabled = false;
		}

		if (this.itemStats.getSize() == 0) {
			guibutton1.enabled = false;
		}

		if (this.mobStats.getSize() == 0) {
			guibutton2.enabled = false;
		}

	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (parGuiButton.id == 1) {
				this.displaySlot = this.generalStats;
			} else if (parGuiButton.id == 3) {
				this.displaySlot = this.itemStats;
			} else if (parGuiButton.id == 2) {
				this.displaySlot = this.blockStats;
			} else if (parGuiButton.id == 4) {
				this.displaySlot = this.mobStats;
			} else {
				this.displaySlot.actionPerformed(parGuiButton);
			}

		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		if (this.doesGuiPauseGame) {
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]),
					this.width / 2, this.height / 2, 16777215);
			this.drawCenteredString(this.fontRendererObj,
					lanSearchStates[(int) (Minecraft.getSystemTime() / 150L % (long) lanSearchStates.length)],
					this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
		} else {
			this.displaySlot.drawScreen(i, j, f);
			this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
			super.drawScreen(i, j, f);
		}

	}

	public void doneLoading() {
		if (this.doesGuiPauseGame) {
			this.func_175366_f();
			this.createButtons();
			this.displaySlot = this.generalStats;
			this.doesGuiPauseGame = false;
		}

	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return !this.doesGuiPauseGame;
	}

	private void drawStatsScreen(int parInt1, int parInt2, Item parItem) {
		this.drawButtonBackground(parInt1 + 1, parInt2 + 1);
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		this.itemRender.renderItemIntoGUI(new ItemStack(parItem, 1, 0), parInt1 + 2, parInt2 + 2);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
	}

	/**+
	 * Draws a gray box that serves as a button background.
	 */
	private void drawButtonBackground(int parInt1, int parInt2) {
		this.drawSprite(parInt1, parInt2, 0, 0);
	}

	/**+
	 * Draws a sprite from
	 * assets/textures/gui/container/stats_icons.png
	 */
	private void drawSprite(int parInt1, int parInt2, int parInt3, int parInt4) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(statIcons);
		float f = 0.0078125F;
		float f1 = 0.0078125F;
		boolean flag = true;
		boolean flag1 = true;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double) (parInt1 + 0), (double) (parInt2 + 18), (double) this.zLevel)
				.tex((double) ((float) (parInt3 + 0) * 0.0078125F), (double) ((float) (parInt4 + 18) * 0.0078125F))
				.endVertex();
		worldrenderer.pos((double) (parInt1 + 18), (double) (parInt2 + 18), (double) this.zLevel)
				.tex((double) ((float) (parInt3 + 18) * 0.0078125F), (double) ((float) (parInt4 + 18) * 0.0078125F))
				.endVertex();
		worldrenderer.pos((double) (parInt1 + 18), (double) (parInt2 + 0), (double) this.zLevel)
				.tex((double) ((float) (parInt3 + 18) * 0.0078125F), (double) ((float) (parInt4 + 0) * 0.0078125F))
				.endVertex();
		worldrenderer.pos((double) (parInt1 + 0), (double) (parInt2 + 0), (double) this.zLevel)
				.tex((double) ((float) (parInt3 + 0) * 0.0078125F), (double) ((float) (parInt4 + 0) * 0.0078125F))
				.endVertex();
		tessellator.draw();
	}

	abstract class Stats extends GuiSlot {
		protected int field_148218_l = -1;
		protected List<StatCrafting> statsHolder;
		protected Comparator<StatCrafting> statSorter;
		protected int field_148217_o = -1;
		protected int field_148215_p;

		protected Stats(Minecraft mcIn) {
			super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
			this.setShowSelectionBox(false);
			this.setHasListHeader(true, 20);
		}

		protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		}

		protected boolean isSelected(int var1) {
			return false;
		}

		protected void drawBackground() {
			GuiStats.this.drawDefaultBackground();
		}

		protected void drawListHeader(int i, int j, Tessellator var3) {
			if (!PointerInputAbstraction.getVCursorButtonDown(0)) {
				this.field_148218_l = -1;
			}

			if (this.field_148218_l == 0) {
				GuiStats.this.drawSprite(i + 115 - 18, j + 1, 0, 0);
			} else {
				GuiStats.this.drawSprite(i + 115 - 18, j + 1, 0, 18);
			}

			if (this.field_148218_l == 1) {
				GuiStats.this.drawSprite(i + 165 - 18, j + 1, 0, 0);
			} else {
				GuiStats.this.drawSprite(i + 165 - 18, j + 1, 0, 18);
			}

			if (this.field_148218_l == 2) {
				GuiStats.this.drawSprite(i + 215 - 18, j + 1, 0, 0);
			} else {
				GuiStats.this.drawSprite(i + 215 - 18, j + 1, 0, 18);
			}

			if (this.field_148217_o != -1) {
				short short1 = 79;
				byte b0 = 18;
				if (this.field_148217_o == 1) {
					short1 = 129;
				} else if (this.field_148217_o == 2) {
					short1 = 179;
				}

				if (this.field_148215_p == 1) {
					b0 = 36;
				}

				GuiStats.this.drawSprite(i + short1, j + 1, b0, 0);
			}

		}

		protected void func_148132_a(int i, int var2) {
			this.field_148218_l = -1;
			if (i >= 79 && i < 115) {
				this.field_148218_l = 0;
			} else if (i >= 129 && i < 165) {
				this.field_148218_l = 1;
			} else if (i >= 179 && i < 215) {
				this.field_148218_l = 2;
			}

			if (this.field_148218_l >= 0) {
				this.func_148212_h(this.field_148218_l);
				this.mc.getSoundHandler()
						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}

		}

		protected final int getSize() {
			return this.statsHolder.size();
		}

		protected final StatCrafting func_148211_c(int parInt1) {
			return (StatCrafting) this.statsHolder.get(parInt1);
		}

		protected abstract String func_148210_b(int var1);

		protected void func_148209_a(StatBase parStatBase, int parInt1, int parInt2, boolean parFlag) {
			if (parStatBase != null) {
				String s = parStatBase.format(GuiStats.this.field_146546_t.readStat(parStatBase));
				GuiStats.this.drawString(GuiStats.this.fontRendererObj, s,
						parInt1 - GuiStats.this.fontRendererObj.getStringWidth(s), parInt2 + 5,
						parFlag ? 16777215 : 9474192);
			} else {
				String s1 = "-";
				GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1,
						parInt1 - GuiStats.this.fontRendererObj.getStringWidth(s1), parInt2 + 5,
						parFlag ? 16777215 : 9474192);
			}

		}

		protected void func_148142_b(int i, int j) {
			if (j >= this.top && j <= this.bottom) {
				int k = this.getSlotIndexFromScreenCoords(i, j);
				int l = this.width / 2 - 92 - 16;
				if (k >= 0) {
					if (i < l + 40 || i > l + 40 + 20) {
						return;
					}

					StatCrafting statcrafting = this.func_148211_c(k);
					this.func_148213_a(statcrafting, i, j);
				} else {
					String s = "";
					if (i >= l + 115 - 18 && i <= l + 115) {
						s = this.func_148210_b(0);
					} else if (i >= l + 165 - 18 && i <= l + 165) {
						s = this.func_148210_b(1);
					} else {
						if (i < l + 215 - 18 || i > l + 215) {
							return;
						}

						s = this.func_148210_b(2);
					}

					s = ("" + I18n.format(s, new Object[0])).trim();
					if (s.length() > 0) {
						int i1 = i + 12;
						int j1 = j - 12;
						int k1 = GuiStats.this.fontRendererObj.getStringWidth(s);
						GuiStats.this.drawGradientRect(i1 - 3, j1 - 3, i1 + k1 + 3, j1 + 8 + 3, -1073741824,
								-1073741824);
						GuiStats.this.fontRendererObj.drawStringWithShadow(s, (float) i1, (float) j1, -1);
					}
				}

			}
		}

		protected void func_148213_a(StatCrafting parStatCrafting, int parInt1, int parInt2) {
			if (parStatCrafting != null) {
				Item item = parStatCrafting.func_150959_a();
				ItemStack itemstack = new ItemStack(item);
				String s = itemstack.getUnlocalizedName();
				String s1 = ("" + I18n.format(s + ".name", new Object[0])).trim();
				if (s1.length() > 0) {
					int i = parInt1 + 12;
					int j = parInt2 - 12;
					int k = GuiStats.this.fontRendererObj.getStringWidth(s1);
					GuiStats.this.drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
					GuiStats.this.fontRendererObj.drawStringWithShadow(s1, (float) i, (float) j, -1);
				}

			}
		}

		protected void func_148212_h(int parInt1) {
			if (parInt1 != this.field_148217_o) {
				this.field_148217_o = parInt1;
				this.field_148215_p = -1;
			} else if (this.field_148215_p == -1) {
				this.field_148215_p = 1;
			} else {
				this.field_148217_o = -1;
				this.field_148215_p = 0;
			}

			Collections.sort(this.statsHolder, this.statSorter);
		}
	}

	class StatsBlock extends GuiStats.Stats {
		public StatsBlock(Minecraft mcIn) {
			super(mcIn);
			this.statsHolder = Lists.newArrayList();

			for (int m = 0, l = StatList.objectMineStats.size(); m < l; ++m) {
				StatCrafting statcrafting = StatList.objectMineStats.get(m);
				boolean flag = false;
				int i = Item.getIdFromItem(statcrafting.func_150959_a());
				if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0) {
					flag = true;
				} else if (StatList.objectUseStats[i] != null
						&& GuiStats.this.field_146546_t.readStat(StatList.objectUseStats[i]) > 0) {
					flag = true;
				} else if (StatList.objectCraftStats[i] != null
						&& GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
					flag = true;
				}

				if (flag) {
					this.statsHolder.add(statcrafting);
				}
			}

			this.statSorter = new Comparator<StatCrafting>() {
				public int compare(StatCrafting statcrafting1, StatCrafting statcrafting2) {
					int j = Item.getIdFromItem(statcrafting1.func_150959_a());
					int k = Item.getIdFromItem(statcrafting2.func_150959_a());
					StatBase statbase = null;
					StatBase statbase1 = null;
					if (StatsBlock.this.field_148217_o == 2) {
						statbase = StatList.mineBlockStatArray[j];
						statbase1 = StatList.mineBlockStatArray[k];
					} else if (StatsBlock.this.field_148217_o == 0) {
						statbase = StatList.objectCraftStats[j];
						statbase1 = StatList.objectCraftStats[k];
					} else if (StatsBlock.this.field_148217_o == 1) {
						statbase = StatList.objectUseStats[j];
						statbase1 = StatList.objectUseStats[k];
					}

					if (statbase != null || statbase1 != null) {
						if (statbase == null) {
							return 1;
						}

						if (statbase1 == null) {
							return -1;
						}

						int l = GuiStats.this.field_146546_t.readStat(statbase);
						int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
						if (l != i1) {
							return (l - i1) * StatsBlock.this.field_148215_p;
						}
					}

					return j - k;
				}
			};
		}

		protected void drawListHeader(int i, int j, Tessellator tessellator) {
			super.drawListHeader(i, j, tessellator);
			if (this.field_148218_l == 0) {
				GuiStats.this.drawSprite(i + 115 - 18 + 1, j + 1 + 1, 18, 18);
			} else {
				GuiStats.this.drawSprite(i + 115 - 18, j + 1, 18, 18);
			}

			if (this.field_148218_l == 1) {
				GuiStats.this.drawSprite(i + 165 - 18 + 1, j + 1 + 1, 36, 18);
			} else {
				GuiStats.this.drawSprite(i + 165 - 18, j + 1, 36, 18);
			}

			if (this.field_148218_l == 2) {
				GuiStats.this.drawSprite(i + 215 - 18 + 1, j + 1 + 1, 54, 18);
			} else {
				GuiStats.this.drawSprite(i + 215 - 18, j + 1, 54, 18);
			}

		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			StatCrafting statcrafting = this.func_148211_c(i);
			Item item = statcrafting.func_150959_a();
			GuiStats.this.drawStatsScreen(j + 40, k, item);
			int l = Item.getIdFromItem(item);
			this.func_148209_a(StatList.objectCraftStats[l], j + 115, k, i % 2 == 0);
			this.func_148209_a(StatList.objectUseStats[l], j + 165, k, i % 2 == 0);
			this.func_148209_a(statcrafting, j + 215, k, i % 2 == 0);
		}

		protected String func_148210_b(int parInt1) {
			return parInt1 == 0 ? "stat.crafted" : (parInt1 == 1 ? "stat.used" : "stat.mined");
		}
	}

	class StatsGeneral extends GuiSlot {
		public StatsGeneral(Minecraft mcIn) {
			super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
			this.setShowSelectionBox(false);
		}

		protected int getSize() {
			return StatList.generalStats.size();
		}

		protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		}

		protected boolean isSelected(int var1) {
			return false;
		}

		protected int getContentHeight() {
			return this.getSize() * 10;
		}

		protected void drawBackground() {
			GuiStats.this.drawDefaultBackground();
		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			StatBase statbase = (StatBase) StatList.generalStats.get(i);
			GuiStats.this.drawString(GuiStats.this.fontRendererObj, statbase.getStatName().getUnformattedText(), j + 2,
					k + 1, i % 2 == 0 ? 16777215 : 9474192);
			String s = statbase.format(GuiStats.this.field_146546_t.readStat(statbase));
			GuiStats.this.drawString(GuiStats.this.fontRendererObj, s,
					j + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(s), k + 1,
					i % 2 == 0 ? 16777215 : 9474192);
		}
	}

	class StatsItem extends GuiStats.Stats {
		public StatsItem(Minecraft mcIn) {
			super(mcIn);
			this.statsHolder = Lists.newArrayList();

			for (int m = 0, l = StatList.itemStats.size(); m < l; ++m) {
				StatCrafting statcrafting = StatList.itemStats.get(m);
				boolean flag = false;
				int i = Item.getIdFromItem(statcrafting.func_150959_a());
				if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0) {
					flag = true;
				} else if (StatList.objectBreakStats[i] != null
						&& GuiStats.this.field_146546_t.readStat(StatList.objectBreakStats[i]) > 0) {
					flag = true;
				} else if (StatList.objectCraftStats[i] != null
						&& GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0) {
					flag = true;
				}

				if (flag) {
					this.statsHolder.add(statcrafting);
				}
			}

			this.statSorter = new Comparator<StatCrafting>() {
				public int compare(StatCrafting statcrafting1, StatCrafting statcrafting2) {
					int j = Item.getIdFromItem(statcrafting1.func_150959_a());
					int k = Item.getIdFromItem(statcrafting2.func_150959_a());
					StatBase statbase = null;
					StatBase statbase1 = null;
					if (StatsItem.this.field_148217_o == 0) {
						statbase = StatList.objectBreakStats[j];
						statbase1 = StatList.objectBreakStats[k];
					} else if (StatsItem.this.field_148217_o == 1) {
						statbase = StatList.objectCraftStats[j];
						statbase1 = StatList.objectCraftStats[k];
					} else if (StatsItem.this.field_148217_o == 2) {
						statbase = StatList.objectUseStats[j];
						statbase1 = StatList.objectUseStats[k];
					}

					if (statbase != null || statbase1 != null) {
						if (statbase == null) {
							return 1;
						}

						if (statbase1 == null) {
							return -1;
						}

						int l = GuiStats.this.field_146546_t.readStat(statbase);
						int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
						if (l != i1) {
							return (l - i1) * StatsItem.this.field_148215_p;
						}
					}

					return j - k;
				}
			};
		}

		protected void drawListHeader(int i, int j, Tessellator tessellator) {
			super.drawListHeader(i, j, tessellator);
			if (this.field_148218_l == 0) {
				GuiStats.this.drawSprite(i + 115 - 18 + 1, j + 1 + 1, 72, 18);
			} else {
				GuiStats.this.drawSprite(i + 115 - 18, j + 1, 72, 18);
			}

			if (this.field_148218_l == 1) {
				GuiStats.this.drawSprite(i + 165 - 18 + 1, j + 1 + 1, 18, 18);
			} else {
				GuiStats.this.drawSprite(i + 165 - 18, j + 1, 18, 18);
			}

			if (this.field_148218_l == 2) {
				GuiStats.this.drawSprite(i + 215 - 18 + 1, j + 1 + 1, 36, 18);
			} else {
				GuiStats.this.drawSprite(i + 215 - 18, j + 1, 36, 18);
			}

		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			StatCrafting statcrafting = this.func_148211_c(i);
			Item item = statcrafting.func_150959_a();
			GuiStats.this.drawStatsScreen(j + 40, k, item);
			int l = Item.getIdFromItem(item);
			this.func_148209_a(StatList.objectBreakStats[l], j + 115, k, i % 2 == 0);
			this.func_148209_a(StatList.objectCraftStats[l], j + 165, k, i % 2 == 0);
			this.func_148209_a(statcrafting, j + 215, k, i % 2 == 0);
		}

		protected String func_148210_b(int i) {
			return i == 1 ? "stat.crafted" : (i == 2 ? "stat.used" : "stat.depleted");
		}
	}

	class StatsMobsList extends GuiSlot {
		private final List<EntityList.EntityEggInfo> field_148222_l = Lists.newArrayList();

		public StatsMobsList(Minecraft mcIn) {
			super(mcIn, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64,
					GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
			this.setShowSelectionBox(false);

			for (ObjectCursor<EntityList.EntityEggInfo> entitylist$entityegginfo_ : EntityList.entityEggs.values()) {
				EntityList.EntityEggInfo entitylist$entityegginfo = entitylist$entityegginfo_.value;
				if (GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d) > 0
						|| GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e) > 0) {
					this.field_148222_l.add(entitylist$entityegginfo);
				}
			}

		}

		protected int getSize() {
			return this.field_148222_l.size();
		}

		protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		}

		protected boolean isSelected(int var1) {
			return false;
		}

		protected int getContentHeight() {
			return this.getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
		}

		protected void drawBackground() {
			GuiStats.this.drawDefaultBackground();
		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo) this.field_148222_l.get(i);
			String s = I18n.format("entity." + EntityList.getStringFromID(entitylist$entityegginfo.spawnedID) + ".name",
					new Object[0]);
			int l = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d);
			int i1 = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e);
			String s1 = I18n.format("stat.entityKills", new Object[] { Integer.valueOf(l), s });
			String s2 = I18n.format("stat.entityKilledBy", new Object[] { s, Integer.valueOf(i1) });
			if (l == 0) {
				s1 = I18n.format("stat.entityKills.none", new Object[] { s });
			}

			if (i1 == 0) {
				s2 = I18n.format("stat.entityKilledBy.none", new Object[] { s });
			}

			GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, j + 2 - 10, k + 1, 16777215);
			GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1, j + 2,
					k + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT, l == 0 ? 6316128 : 9474192);
			GuiStats.this.drawString(GuiStats.this.fontRendererObj, s2, j + 2,
					k + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT * 2, i1 == 0 ? 6316128 : 9474192);
		}
	}
}