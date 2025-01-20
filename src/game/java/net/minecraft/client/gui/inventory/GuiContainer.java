package net.minecraft.client.gui.inventory;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
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
public abstract class GuiContainer extends GuiScreen {
	/**+
	 * The location of the inventory background texture
	 */
	protected static final ResourceLocation inventoryBackground = new ResourceLocation(
			"textures/gui/container/inventory.png");
	/**+
	 * The X size of the inventory window in pixels.
	 */
	protected int xSize = 176;
	/**+
	 * The Y size of the inventory window in pixels.
	 */
	protected int ySize = 166;
	public Container inventorySlots;
	protected int guiLeft;
	protected int guiTop;
	private Slot theSlot;
	private Slot clickedSlot;
	private boolean isRightMouseClick;
	private ItemStack draggedStack;
	private int touchUpX;
	private int touchUpY;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	private ItemStack returningStack;
	private Slot currentDragTargetSlot;
	private long dragItemDropDelay;
	protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
	protected boolean dragSplitting;
	private int dragSplittingLimit;
	private int dragSplittingButton;
	private boolean ignoreMouseUp;
	private int dragSplittingRemnant;
	private long lastClickTime;
	private Slot lastClickSlot;
	private int lastClickButton;
	private boolean doubleClick;
	private ItemStack shiftClickedSlot;

	public GuiContainer(Container inventorySlotsIn) {
		this.inventorySlots = inventorySlotsIn;
		this.ignoreMouseUp = true;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		super.initGui();
		if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
			primaryTouchPoint = -1;
			mouseReleased(lastTouchX, lastTouchY, 0);
		}
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		int k = this.guiLeft;
		int l = this.guiTop;
		this.drawGuiContainerBackgroundLayer(f, i, j);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		super.drawScreen(i, j, f);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) k, (float) l, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		this.theSlot = null;
		short short1 = 240;
		short short2 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F,
				(float) short2 / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);
			this.drawSlot(slot);
			if (!this.mc.gameSettings.touchscreen && slot.canBeHovered() && this.isMouseOverSlot(slot, i, j)) {
				this.theSlot = slot;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				int j1 = slot.xDisplayPosition;
				int k1 = slot.yDisplayPosition;
				GlStateManager.colorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
			GlStateManager.enableAlpha();
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(i, j);
		RenderHelper.enableGUIStandardItemLighting();
		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;
		if (itemstack != null) {
			byte b0 = 8;
			int j2 = this.draggedStack == null ? 8 : 16;
			String s = null;
			if (this.draggedStack != null && this.isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.stackSize = MathHelper.ceiling_float_int((float) itemstack.stackSize / 2.0F);
			} else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.stackSize = this.dragSplittingRemnant;
				if (itemstack.stackSize == 0) {
					s = "" + EnumChatFormatting.YELLOW + "0";
				}
			}
			this.drawItemStack(itemstack, i - k - b0, j - l - j2, s);
		}

		if (this.returningStack != null) {
			float f1 = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
			if (f1 >= 1.0F) {
				f1 = 1.0F;
				this.returningStack = null;
			}

			int k2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
			int l2 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) k2 * f1);
			int i2 = this.touchUpY + (int) ((float) l2 * f1);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		GlStateManager.popMatrix();
		if (!this.mc.gameSettings.touchscreen && inventoryplayer.getItemStack() == null && this.theSlot != null
				&& this.theSlot.getHasStack()) {
			ItemStack itemstack1 = this.theSlot.getStack();
			this.renderToolTip(itemstack1, i, j);
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
	}

	/**+
	 * Render an ItemStack. Args : stack, x, y, format
	 */
	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x,
				y - (this.draggedStack == null ? 0 : 8), altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}

	/**+
	 * Draw the foreground layer for the GuiContainer (everything in
	 * front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

	private void drawSlot(Slot slotIn) {
		int i = slotIn.xDisplayPosition;
		int j = slotIn.yDisplayPosition;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
		ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
		String s = null;
		if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		} else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
			if (this.dragSplittingSlots.size() == 1) {
				return;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
						slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);
				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}

				if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
					s = EnumChatFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
					itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
				}
			} else {
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		if (itemstack == null) {
			String s1 = slotIn.getSlotTexture();
			if (s1 != null) {
				EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	private void updateDragSplitting() {
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
		if (itemstack != null && this.dragSplitting) {
			this.dragSplittingRemnant = itemstack.stackSize;

			for (Slot slot : this.dragSplittingSlots) {
				ItemStack itemstack1 = itemstack.copy();
				int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1)) {
					itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
				}

				this.dragSplittingRemnant -= itemstack1.stackSize - i;
			}

		}
	}

	/**+
	 * Returns the slot at the given coordinates or null if there is
	 * none.
	 */
	private Slot getSlotAtPosition(int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
			if (this.isMouseOverSlot(slot, x, y)) {
				return slot;
			}
		}

		return null;
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		boolean flag = parInt3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
		Slot slot = this.getSlotAtPosition(parInt1, parInt2);
		long i = Minecraft.getSystemTime();
		this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L
				&& this.lastClickButton == parInt3;
		this.ignoreMouseUp = false;
		if (parInt3 == 0 || parInt3 == 1 || flag) {
			int j = this.guiLeft;
			int k = this.guiTop;
			boolean flag1 = parInt1 < j || parInt2 < k || parInt1 >= j + this.xSize || parInt2 >= k + this.ySize;
			int l = -1;
			if (slot != null) {
				l = slot.slotNumber;
			}

			if (flag1) {
				l = -999;
			}

//			if (this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null) {
//				this.mc.displayGuiScreen((GuiScreen) null);
//				return;
//			}

			if (l != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						this.clickedSlot = slot;
						this.draggedStack = null;
						this.isRightMouseClick = parInt3 == 1;
					} else {
						this.clickedSlot = null;
					}
				} else if (!this.dragSplitting) {
					if (this.mc.thePlayer.inventory.getItemStack() == null) {
						if (parInt3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
							this.handleMouseClick(slot, l, parInt3, 3);
						} else {
							boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							byte b0 = 0;
							if (flag2) {
								this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
								b0 = 1;
							} else if (l == -999) {
								b0 = 4;
							}

							this.handleMouseClick(slot, l, parInt3, b0);
						}

						this.ignoreMouseUp = true;
					} else {
						this.dragSplitting = true;
						this.dragSplittingButton = parInt3;
						this.dragSplittingSlots.clear();
						if (parInt3 == 0) {
							this.dragSplittingLimit = 0;
						} else if (parInt3 == 1) {
							this.dragSplittingLimit = 1;
						} else if (parInt3 == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
							this.dragSplittingLimit = 2;
						}
					}
				}
			}
		}

		this.lastClickSlot = slot;
		this.lastClickTime = i;
		this.lastClickButton = parInt3;
	}

	/**+
	 * Called when a mouse button is pressed and the mouse is moved
	 * around. Parameters are : mouseX, mouseY, lastButtonClicked &
	 * timeSinceMouseClick.
	 */
	protected void mouseClickMove(int i, int j, int k, long var4) {
		Slot slot = this.getSlotAtPosition(i, j);
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (k == 0 || k == 1) {
				if (this.draggedStack == null) {
					if (slot != this.clickedSlot && this.clickedSlot.getStack() != null) {
						this.draggedStack = this.clickedSlot.getStack().copy();
					}
				} else if (this.draggedStack.stackSize > 1 && slot != null
						&& Container.canAddItemToSlot(slot, this.draggedStack, false)) {
					long l = Minecraft.getSystemTime();
					if (this.currentDragTargetSlot == slot) {
						if (l - this.dragItemDropDelay > 500L) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.handleMouseClick(slot, slot.slotNumber, 1, 0);
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.dragItemDropDelay = l + 750L;
							--this.draggedStack.stackSize;
						}
					} else {
						this.currentDragTargetSlot = slot;
						this.dragItemDropDelay = l;
					}
				}
			}
		} else if (this.dragSplitting && slot != null && itemstack != null
				&& itemstack.stackSize > this.dragSplittingSlots.size()
				&& Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack)
				&& this.inventorySlots.canDragIntoSlot(slot)) {
			this.dragSplittingSlots.add(slot);
			this.updateDragSplitting();
		}

	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		Slot slot = this.getSlotAtPosition(i, j);
		int l = this.guiLeft;
		int i1 = this.guiTop;
		boolean flag = i < l || j < i1 || i >= l + this.xSize || j >= i1 + this.ySize;
		int j1 = -1;
		if (slot != null) {
			j1 = slot.slotNumber;
		}

		if (flag) {
			j1 = -999;
		}

		if (this.doubleClick && slot != null && k == 0 && this.inventorySlots.canMergeSlot((ItemStack) null, slot)) {
			if (isShiftKeyDown()) {
				if (slot != null && slot.inventory != null && this.shiftClickedSlot != null) {
					List<Slot> lst = this.inventorySlots.inventorySlots;
					for (int n = 0, m = lst.size(); n < m; ++n) {
						Slot slot2 = lst.get(n);
						if (slot2 != null && slot2.canTakeStack(this.mc.thePlayer) && slot2.getHasStack()
								&& slot2.inventory == slot.inventory
								&& Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
							this.handleMouseClick(slot2, slot2.slotNumber, k, 1);
						}
					}
				}
			} else {
				this.handleMouseClick(slot, j1, k, 6);
			}

			this.doubleClick = false;
			this.lastClickTime = 0L;
		} else {
			if (this.dragSplitting && this.dragSplittingButton != k) {
				this.dragSplitting = false;
				this.dragSplittingSlots.clear();
				this.ignoreMouseUp = true;
				return;
			}

			if (this.ignoreMouseUp) {
				this.ignoreMouseUp = false;
				return;
			}

			if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
				if (k == 0 || k == 1) {
					if (this.draggedStack == null && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack();
					}

					boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
					if (j1 != -1 && this.draggedStack != null && flag2) {
						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, k, 0);
						this.handleMouseClick(slot, j1, 0, 0);
						if (this.mc.thePlayer.inventory.getItemStack() != null) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, k, 0);
							this.touchUpX = i - l;
							this.touchUpY = j - i1;
							this.returningStackDestSlot = this.clickedSlot;
							this.returningStack = this.draggedStack;
							this.returningStackTime = Minecraft.getSystemTime();
						} else {
							this.returningStack = null;
						}
					} else if (this.draggedStack != null) {
						this.touchUpX = i - l;
						this.touchUpY = j - i1;
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					}

					this.draggedStack = null;
					this.clickedSlot = null;
				}
			} else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);

				for (Slot slot1 : this.dragSplittingSlots) {
					this.handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit),
							5);
				}

				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
			} else if (this.mc.thePlayer.inventory.getItemStack() != null) {
				if (k == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
					this.handleMouseClick(slot, j1, k, 3);
				} else {
					boolean flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
					if (flag1) {
						this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
					}

					this.handleMouseClick(slot, j1, k, flag1 ? 1 : 0);
				}
			}
		}

		if (this.mc.thePlayer.inventory.getItemStack() == null) {
			this.lastClickTime = 0L;
		}

		this.dragSplitting = false;
	}

	/**+
	 * Returns if the passed mouse position is over the specified
	 * slot. Args : slot, mouseX, mouseY
	 */
	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
		return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
	}

	/**+
	 * Test if the 2D point is in a rectangle (relative to the GUI).
	 * Args : rectX, rectY, rectWidth, rectHeight, pointX, pointY
	 */
	protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
		int i = this.guiLeft;
		int j = this.guiTop;
		pointX = pointX - i;
		pointY = pointY - j;
		return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1;
	}

	/**+
	 * Called when the mouse is clicked over a slot or outside the
	 * gui.
	 */
	protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
		if (slotIn != null) {
			slotId = slotIn.slotNumber;
		}

		this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType,
				this.mc.thePlayer);
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode()
				|| parInt1 == this.mc.gameSettings.keyBindInventory.getKeyCode() || (parInt1 == 1
						&& (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked()))) {
			this.mc.thePlayer.closeScreen();
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		} else if (parInt1 == 1) {
			showingCloseKey = EagRuntime.steadyTimeMillis();
		} else {
			this.checkHotbarKeys(parInt1);
			if (this.theSlot != null && this.theSlot.getHasStack()) {
				if (parInt1 == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
				} else if (parInt1 == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
				}
			}
		}
	}

	/**+
	 * This function is what controls the hotbar shortcut check when
	 * you press a number key when hovering a stack. Args : keyCode,
	 * Returns true if a Hotbar key is pressed, else false
	 */
	protected boolean checkHotbarKeys(int keyCode) {
		if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
			for (int i = 0; i < 9; ++i) {
				if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
					return true;
				}
			}
		}

		return false;
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		if (this.mc.thePlayer != null) {
			this.inventorySlots.onContainerClosed(this.mc.thePlayer);
		}
	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
			return;
		}
		if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
			primaryTouchPoint = -1;
			mouseReleased(lastTouchX, lastTouchY, 0);
		}
	}

	protected float getTouchModeScale() {
		return 1.25f;
	}

	private int primaryTouchPoint = -1;
	private int lastTouchX = -1;
	private int lastTouchY = -1;

	protected void touchStarted(int touchX, int touchY, int uid) {
		if (primaryTouchPoint == -1) {
			primaryTouchPoint = uid;
			lastTouchX = touchX;
			lastTouchY = touchY;
			mouseClicked(touchX, touchY, 0);
		}
	}

	protected void touchMoved(int touchX, int touchY, int uid) {
		if (primaryTouchPoint == uid) {
			lastTouchX = touchX;
			lastTouchY = touchY;
			mouseClickMove(touchX, touchY, 0, 0l);
		}
	}

	protected void touchEndMove(int touchX, int touchY, int uid) {
		if (primaryTouchPoint == uid) {
			primaryTouchPoint = -1;
			lastTouchX = touchX;
			lastTouchY = touchY;
			mouseReleased(touchX, touchY, 0);
		}
	}

	protected void touchTapped(int touchX, int touchY, int uid) {
		if (primaryTouchPoint == uid) {
			primaryTouchPoint = -1;
			lastTouchX = touchX;
			lastTouchY = touchY;
			mouseReleased(touchX, touchY, 0);
		}
	}

	protected boolean shouldTouchGenerateMouseEvents() {
		return false;
	}
}