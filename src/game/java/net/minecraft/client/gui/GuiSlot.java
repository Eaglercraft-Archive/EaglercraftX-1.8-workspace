package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;

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
public abstract class GuiSlot {

	private static final Logger excLogger = LogManager.getLogger("GuiSlotRenderer");

	protected final Minecraft mc;
	protected int width;
	protected int height;
	protected int top;
	protected int bottom;
	protected int right;
	protected int left;
	protected final int slotHeight;
	private int scrollUpButtonID;
	private int scrollDownButtonID;
	protected int mouseX;
	protected int mouseY;
	protected boolean field_148163_i = true;
	/**+
	 * Where the mouse was in the window when you first clicked to
	 * scroll
	 */
	protected int initialClickY = -2;
	protected float scrollMultiplier;
	protected float amountScrolled;
	/**+
	 * The element in the list that was selected
	 */
	protected int selectedElement = -1;
	protected long lastClicked;
	protected boolean field_178041_q = true;
	/**+
	 * Set to true if a selected element in this gui will show an
	 * outline box
	 */
	protected boolean showSelectionBox = true;
	protected boolean hasListHeader;
	protected int headerPadding;
	private boolean enabled = true;

	public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
		this.mc = mcIn;
		this.width = width;
		this.height = height;
		this.top = topIn;
		this.bottom = bottomIn;
		this.slotHeight = slotHeightIn;
		this.left = 0;
		this.right = width;
	}

	public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
		this.width = widthIn;
		this.height = heightIn;
		this.top = topIn;
		this.bottom = bottomIn;
		this.left = 0;
		this.right = widthIn;
	}

	public void setShowSelectionBox(boolean showSelectionBoxIn) {
		this.showSelectionBox = showSelectionBoxIn;
	}

	/**+
	 * Sets hasListHeader and headerHeight. Params: hasListHeader,
	 * headerHeight. If hasListHeader is false headerHeight is set
	 * to 0.
	 */
	protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
		this.hasListHeader = hasListHeaderIn;
		this.headerPadding = headerPaddingIn;
		if (!hasListHeaderIn) {
			this.headerPadding = 0;
		}

	}

	protected abstract int getSize();

	protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

	protected abstract boolean isSelected(int var1);

	/**+
	 * Return the height of the content being scrolled
	 */
	protected int getContentHeight() {
		return (this.getSize() + 1) * this.slotHeight + this.headerPadding;
	}

	protected abstract void drawBackground();

	protected void func_178040_a(int var1, int var2, int var3) {
	}

	protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

	/**+
	 * Handles drawing a list's header row.
	 */
	protected void drawListHeader(int parInt1, int parInt2, Tessellator parTessellator) {
	}

	protected void func_148132_a(int parInt1, int parInt2) {
	}

	protected void func_148142_b(int parInt1, int parInt2) {
	}

	public int getSlotIndexFromScreenCoords(int parInt1, int parInt2) {
		int i = this.left + this.width / 2 - this.getListWidth() / 2;
		int j = this.left + this.width / 2 + this.getListWidth() / 2;
		int k = parInt2 - this.top - this.headerPadding + (int) this.amountScrolled - 4;
		int l = k / this.slotHeight;
		return parInt1 < this.getScrollBarX() && parInt1 >= i && parInt1 <= j && l >= 0 && k >= 0 && l < this.getSize()
				? l
				: -1;
	}

	/**+
	 * Registers the IDs that can be used for the scrollbar's
	 * up/down buttons.
	 */
	public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
		this.scrollUpButtonID = scrollUpButtonIDIn;
		this.scrollDownButtonID = scrollDownButtonIDIn;
	}

	/**+
	 * Stop the thing from scrolling out of bounds
	 */
	protected void bindAmountScrolled() {
		this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, (float) this.func_148135_f());
	}

	public int func_148135_f() {
		return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
	}

	/**+
	 * Returns the amountScrolled field as an integer.
	 */
	public int getAmountScrolled() {
		return (int) this.amountScrolled;
	}

	public boolean isMouseYWithinSlotBounds(int parInt1) {
		return parInt1 >= this.top && parInt1 <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
	}

	/**+
	 * Scrolls the slot by the given amount. A positive value
	 * scrolls down, and a negative value scrolls up.
	 */
	public void scrollBy(int amount) {
		this.amountScrolled += (float) amount;
		this.bindAmountScrolled();
		this.initialClickY = -2;
	}

	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == this.scrollUpButtonID) {
				this.amountScrolled -= (float) (this.slotHeight * 2 / 3);
				this.initialClickY = -2;
				this.bindAmountScrolled();
			} else if (button.id == this.scrollDownButtonID) {
				this.amountScrolled += (float) (this.slotHeight * 2 / 3);
				this.initialClickY = -2;
				this.bindAmountScrolled();
			}

		}
	}

	public void drawScreen(int mouseXIn, int mouseYIn, float parFloat1) {
		if (this.field_178041_q) {
			this.mouseX = mouseXIn;
			this.mouseY = mouseYIn;
			this.drawBackground();
			int i = this.getScrollBarX();
			int j = i + 6;
			this.bindAmountScrolled();
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			float f = 32.0F;
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			worldrenderer.pos((double) this.left, (double) this.bottom, 0.0D)
					.tex((double) ((float) this.left / f),
							(double) ((float) (this.bottom + (int) this.amountScrolled) / f))
					.color(32, 32, 32, 255).endVertex();
			worldrenderer.pos((double) this.right, (double) this.bottom, 0.0D)
					.tex((double) ((float) this.right / f),
							(double) ((float) (this.bottom + (int) this.amountScrolled) / f))
					.color(32, 32, 32, 255).endVertex();
			worldrenderer.pos((double) this.right, (double) this.top, 0.0D)
					.tex((double) ((float) this.right / f),
							(double) ((float) (this.top + (int) this.amountScrolled) / f))
					.color(32, 32, 32, 255).endVertex();
			worldrenderer.pos((double) this.left, (double) this.top, 0.0D)
					.tex((double) ((float) this.left / f),
							(double) ((float) (this.top + (int) this.amountScrolled) / f))
					.color(32, 32, 32, 255).endVertex();
			tessellator.draw();
			int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
			int l = this.top + 4 - (int) this.amountScrolled;
			if (this.hasListHeader) {
				this.drawListHeader(k, l, tessellator);
			}

			this.drawSelectionBox(k, l, mouseXIn, mouseYIn, this.getSize());
			GlStateManager.disableDepth();
			byte b0 = 4;
			this.overlayBackground(0, this.top, 255, 255);
			this.overlayBackground(this.bottom, this.height, 255, 255);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 0, 1);
			GlStateManager.disableAlpha();
			GlStateManager.shadeModel(GL_SMOOTH);
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			worldrenderer.pos((double) this.left, (double) (this.top + b0), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0)
					.endVertex();
			worldrenderer.pos((double) this.right, (double) (this.top + b0), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0)
					.endVertex();
			worldrenderer.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255)
					.endVertex();
			worldrenderer.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255)
					.endVertex();
			tessellator.draw();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			worldrenderer.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
					.endVertex();
			worldrenderer.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
					.endVertex();
			worldrenderer.pos((double) this.right, (double) (this.bottom - b0), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0)
					.endVertex();
			worldrenderer.pos((double) this.left, (double) (this.bottom - b0), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0)
					.endVertex();
			tessellator.draw();
			int i1 = this.func_148135_f();
			if (i1 > 0) {
				int j1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
				j1 = MathHelper.clamp_int(j1, 32, this.bottom - this.top - 8);
				int k1 = (int) this.amountScrolled * (this.bottom - this.top - j1) / i1 + this.top;
				if (k1 < this.top) {
					k1 = this.top;
				}

				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				worldrenderer.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) i, (double) (k1 + j1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) j, (double) (k1 + j1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) j, (double) k1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				worldrenderer.pos((double) i, (double) k1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				tessellator.draw();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) i, (double) (k1 + j1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255)
						.endVertex();
				worldrenderer.pos((double) (j - 1), (double) (k1 + j1 - 1), 0.0D).tex(1.0D, 1.0D)
						.color(192, 192, 192, 255).endVertex();
				worldrenderer.pos((double) (j - 1), (double) k1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255)
						.endVertex();
				worldrenderer.pos((double) i, (double) k1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
				tessellator.draw();
			}

			this.func_148142_b(mouseXIn, mouseYIn);
			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(GL_FLAT);
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
		}
	}

	public void handleMouseInput() {
		handleInput(Mouse.getEventButton(), Mouse.getEventButtonState(), Mouse.getDWheel());
	}

	public void handleTouchInput() {
		mouseX = PointerInputAbstraction.getVCursorX() * width / mc.displayWidth;
		mouseY = height - PointerInputAbstraction.getVCursorY() * height / mc.displayHeight - 1;
		handleInput(0, Touch.getEventType() == EnumTouchEvent.TOUCHSTART, 0);
	}

	protected void handleInput(int eventButton, boolean eventState, int dWheel) {
		if (this.isMouseYWithinSlotBounds(this.mouseY)) {
			if (eventButton == 0 && eventState && this.mouseY >= this.top && this.mouseY <= this.bottom) {
				int i = (this.width - this.getListWidth()) / 2;
				int j = (this.width + this.getListWidth()) / 2;
				int k = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
				int l = k / this.slotHeight;
				if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
					this.elementClicked(l, false, this.mouseX, this.mouseY);
					this.selectedElement = l;
				} else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
					this.func_148132_a(this.mouseX - i, this.mouseY - this.top + (int) this.amountScrolled - 4);
				}
			}

			if (PointerInputAbstraction.getVCursorButtonDown(0) && this.getEnabled()) {
				if (this.initialClickY == -1) {
					boolean flag1 = true;
					if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
						int j2 = (this.width - this.getListWidth()) / 2;
						int k2 = (this.width + this.getListWidth()) / 2;
						int l2 = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
						int i1 = l2 / this.slotHeight;
						if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
							boolean flag = i1 == this.selectedElement
									&& Minecraft.getSystemTime() - this.lastClicked < 250L;
							this.elementClicked(i1, flag, this.mouseX, this.mouseY);
							this.selectedElement = i1;
							this.lastClicked = Minecraft.getSystemTime();
						} else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
							this.func_148132_a(this.mouseX - j2,
									this.mouseY - this.top + (int) this.amountScrolled - 4);
							flag1 = false;
						}

						int i3 = this.getScrollBarX();
						int j1 = i3 + 6;
						if (this.mouseX >= i3 && this.mouseX <= j1) {
							this.scrollMultiplier = -1.0F;
							int k1 = this.func_148135_f();
							if (k1 < 1) {
								k1 = 1;
							}

							int l1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top))
									/ (float) this.getContentHeight());
							l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
							this.scrollMultiplier /= (float) (this.bottom - this.top - l1) / (float) k1;
						} else {
							this.scrollMultiplier = 1.0F;
						}

						if (flag1) {
							this.initialClickY = this.mouseY;
						} else {
							this.initialClickY = -2;
						}
					} else {
						this.initialClickY = -2;
					}
				} else if (this.initialClickY >= 0) {
					this.amountScrolled -= (float) (this.mouseY - this.initialClickY) * this.scrollMultiplier;
					this.initialClickY = this.mouseY;
				}
			} else {
				this.initialClickY = -1;
			}

			if (dWheel != 0) {
				if (dWheel > 0) {
					dWheel = -1;
				} else if (dWheel < 0) {
					dWheel = 1;
				}

				this.amountScrolled += (float) (dWheel * this.slotHeight / 2);
			}

		}
	}

	public void setEnabled(boolean enabledIn) {
		this.enabled = enabledIn;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	/**+
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return 220;
	}

	/**+
	 * Draws the selection box around the selected slot element.
	 */
	protected void drawSelectionBox(int mouseXIn, int mouseYIn, int parInt3, int parInt4, int i) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		for (int j = 0; j < i; ++j) {
			int k = mouseYIn + j * this.slotHeight + this.headerPadding;
			int l = this.slotHeight - 4;
			if (k > this.bottom || k + l < this.top) {
				this.func_178040_a(j, mouseXIn, k);
			}

			if (this.showSelectionBox && this.isSelected(j)) {
				int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
				int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableTexture2D();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) i1, (double) (k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) j1, (double) (k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) j1, (double) (k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) i1, (double) (k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) (i1 + 1), (double) (k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) (j1 - 1), (double) (k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) (j1 - 1), (double) (k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) (i1 + 1), (double) (k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255)
						.endVertex();
				tessellator.draw();
				GlStateManager.enableTexture2D();
				if (parInt3 >= i1 && parInt3 <= j1 && parInt4 >= k - 2 && parInt4 <= k + l + 1) {
					Mouse.showCursor(EnumCursorType.HAND);
				}
			}

			try {
				this.drawSlot(j, mouseXIn, k, l, parInt3, parInt4);
			} catch (Throwable t) {
				excLogger.error(
						"Exception caught rendering a slot of a list on the screen! Game will continue running due to the suspicion that this could be an intentional crash attempt, and therefore it would be inconvenient if the user were to be locked out of this gui due to repeatedly triggering a full crash report");
				excLogger.error(t);
			}
		}

	}

	protected int getScrollBarX() {
		return this.width / 2 + 124;
	}

	/**+
	 * Overlays the background to hide scrolled items
	 */
	protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos((double) this.left, (double) endY, 0.0D).tex(0.0D, (double) ((float) endY / 32.0F))
				.color(64, 64, 64, endAlpha).endVertex();
		worldrenderer.pos((double) (this.left + this.width), (double) endY, 0.0D)
				.tex((double) ((float) this.width / 32.0F), (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha)
				.endVertex();
		worldrenderer.pos((double) (this.left + this.width), (double) startY, 0.0D)
				.tex((double) ((float) this.width / 32.0F), (double) ((float) startY / 32.0F))
				.color(64, 64, 64, startAlpha).endVertex();
		worldrenderer.pos((double) this.left, (double) startY, 0.0D).tex(0.0D, (double) ((float) startY / 32.0F))
				.color(64, 64, 64, startAlpha).endVertex();
		tessellator.draw();
	}

	/**+
	 * Sets the left and right bounds of the slot. Param is the left
	 * bound, right is calculated as left + width.
	 */
	public void setSlotXBoundsFromLeft(int leftIn) {
		this.left = leftIn;
		this.right = leftIn + this.width;
	}

	public int getSlotHeight() {
		return this.slotHeight;
	}
}