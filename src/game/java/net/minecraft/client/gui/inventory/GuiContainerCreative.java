package net.minecraft.client.gui.inventory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.carrotsearch.hppc.IntIntMap;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
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
public class GuiContainerCreative extends InventoryEffectRenderer {
	/**+
	 * The location of the creative inventory tabs texture
	 */
	private static final ResourceLocation creativeInventoryTabs = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");
	private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
	/**+
	 * Currently selected creative inventory tab index.
	 */
	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;
	private GuiTextField searchField;
	private List<Slot> field_147063_B;
	private Slot field_147064_C;
	private boolean field_147057_D;
	private CreativeCrafting field_147059_E;

	public GuiContainerCreative(EntityPlayer parEntityPlayer) {
		super(new GuiContainerCreative.ContainerCreative(parEntityPlayer));
		parEntityPlayer.openContainer = this.inventorySlots;
		this.allowUserInput = true;
		this.ySize = 136;
		this.xSize = 195;
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (!this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
		}

		this.updateActivePotionEffects();
	}

	/**+
	 * Called when the mouse is clicked over a slot or outside the
	 * gui.
	 */
	protected void handleMouseClick(Slot slot, int i, int j, int k) {
		this.field_147057_D = true;
		boolean flag = k == 1;
		k = i == -999 && k == 0 ? 4 : k;
		if (slot == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && k != 5) {
			InventoryPlayer inventoryplayer1 = this.mc.thePlayer.inventory;
			if (inventoryplayer1.getItemStack() != null) {
				if (j == 0) {
					this.mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer1.getItemStack(), true);
					this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
					inventoryplayer1.setItemStack((ItemStack) null);
				}

				if (j == 1) {
					ItemStack itemstack5 = inventoryplayer1.getItemStack().splitStack(1);
					this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
					this.mc.playerController.sendPacketDropItem(itemstack5);
					if (inventoryplayer1.getItemStack().stackSize == 0) {
						inventoryplayer1.setItemStack((ItemStack) null);
					}
				}
			}
		} else if (slot == this.field_147064_C && flag) {
			for (int i1 = 0; i1 < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++i1) {
				this.mc.playerController.sendSlotPacket((ItemStack) null, i1);
			}
		} else if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
			if (slot == this.field_147064_C) {
				this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
			} else if (k == 4 && slot != null && slot.getHasStack()) {
				ItemStack itemstack = slot.decrStackSize(j == 0 ? 1 : slot.getStack().getMaxStackSize());
				this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack, true);
				this.mc.playerController.sendPacketDropItem(itemstack);
			} else if (k == 4 && this.mc.thePlayer.inventory.getItemStack() != null) {
				this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.mc.thePlayer.inventory.getItemStack(), true);
				this.mc.playerController.sendPacketDropItem(this.mc.thePlayer.inventory.getItemStack());
				this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
			} else {
				this.mc.thePlayer.inventoryContainer.slotClick(
						slot == null ? i : ((GuiContainerCreative.CreativeSlot) slot).slot.slotNumber, j, k,
						this.mc.thePlayer);
				this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
			}
		} else if (k != 5 && slot.inventory == field_147060_v) {
			InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
			ItemStack itemstack1 = inventoryplayer.getItemStack();
			ItemStack itemstack2 = slot.getStack();
			if (k == 2) {
				if (itemstack2 != null && j >= 0 && j < 9) {
					ItemStack itemstack7 = itemstack2.copy();
					itemstack7.stackSize = itemstack7.getMaxStackSize();
					this.mc.thePlayer.inventory.setInventorySlotContents(j, itemstack7);
					this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
				}

				return;
			}

			if (k == 3) {
				if (inventoryplayer.getItemStack() == null && slot.getHasStack()) {
					ItemStack itemstack6 = slot.getStack().copy();
					itemstack6.stackSize = itemstack6.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack6);
				}

				return;
			}

			if (k == 4) {
				if (itemstack2 != null) {
					ItemStack itemstack3 = itemstack2.copy();
					itemstack3.stackSize = j == 0 ? 1 : itemstack3.getMaxStackSize();
					this.mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
					this.mc.playerController.sendPacketDropItem(itemstack3);
				}

				return;
			}

			if (itemstack1 != null && itemstack2 != null && itemstack1.isItemEqual(itemstack2)) {
				if (j == 0) {
					if (flag) {
						itemstack1.stackSize = itemstack1.getMaxStackSize();
					} else if (itemstack1.stackSize < itemstack1.getMaxStackSize()) {
						++itemstack1.stackSize;
					}
				} else if (itemstack1.stackSize <= 1) {
					inventoryplayer.setItemStack((ItemStack) null);
				} else {
					--itemstack1.stackSize;
				}
			} else if (itemstack2 != null && itemstack1 == null) {
				inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
				itemstack1 = inventoryplayer.getItemStack();
				if (flag) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}
			} else {
				inventoryplayer.setItemStack((ItemStack) null);
			}
		} else {
			this.inventorySlots.slotClick(slot == null ? i : slot.slotNumber, j, k, this.mc.thePlayer);
			if (Container.getDragEvent(j) == 2) {
				for (int l = 0; l < 9; ++l) {
					this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + l).getStack(), 36 + l);
				}
			} else if (slot != null) {
				ItemStack itemstack4 = this.inventorySlots.getSlot(slot.slotNumber).getStack();
				this.mc.playerController.sendSlotPacket(itemstack4,
						slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
			}
		}

	}

	protected void updateActivePotionEffects() {
		int i = this.guiLeft;
		super.updateActivePotionEffects();
		if (this.searchField != null && this.guiLeft != i) {
			this.searchField.xPosition = this.guiLeft + 82;
		}

	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		if (this.mc.playerController.isInCreativeMode()) {
			super.initGui();
			this.buttonList.clear();
			Keyboard.enableRepeatEvents(true);
			this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89,
					this.fontRendererObj.FONT_HEIGHT);
			this.searchField.setMaxStringLength(15);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setVisible(false);
			this.searchField.setTextColor(16777215);
			int i = selectedTabIndex;
			selectedTabIndex = -1;
			this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
			this.field_147059_E = new CreativeCrafting(this.mc);
			this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
		} else {
			this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
		}

	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		super.onGuiClosed();
		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
		}

		Keyboard.enableRepeatEvents(false);
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			} else {
				super.keyTyped(parChar1, parInt1);
			}

		} else {
			if (this.field_147057_D) {
				this.field_147057_D = false;
				this.searchField.setText("");
			}

			if (parInt1 == getCloseKey() || (parInt1 == 1 && Keyboard.areKeysLocked())) {
				mc.displayGuiScreen(null);
			} else if (!this.checkHotbarKeys(parInt1)) {
				if (this.searchField.textboxKeyTyped(parChar1, parInt1)) {
					this.updateCreativeSearch();
				} else {
					super.keyTyped(parChar1, parInt1);
				}

			}
		}
	}

	protected int getCloseKey() {
		return selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex() ? super.getCloseKey()
				: mc.gameSettings.keyBindClose.getKeyCode();
	}

	private void updateCreativeSearch() {
		GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative) this.inventorySlots;
		guicontainercreative$containercreative.itemList.clear();

		for (Item item : Item.itemRegistry) {
			if (item != null && item.getCreativeTab() != null) {
				item.getSubItems(item, (CreativeTabs) null, guicontainercreative$containercreative.itemList);
			}
		}

		for (int i = 0; i < Enchantment.enchantmentsBookList.length; ++i) {
			Enchantment enchantment = Enchantment.enchantmentsBookList[i];
			if (enchantment != null && enchantment.type != null) {
				Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
			}
		}

		Iterator iterator = guicontainercreative$containercreative.itemList.iterator();
		String s1 = this.searchField.getText().toLowerCase();

		while (iterator.hasNext()) {
			ItemStack itemstack = (ItemStack) iterator.next();
			boolean flag = false;

			List<String> lst = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
			for (int i = 0, l = lst.size(); i < l; ++i) {
				if (EnumChatFormatting.getTextWithoutFormattingCodes(lst.get(i)).toLowerCase().contains(s1)) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				iterator.remove();
			}
		}

		this.currentScroll = 0.0F;
		guicontainercreative$containercreative.scrollTo(0.0F);
	}

	/**+
	 * Draw the foreground layer for the GuiContainer (everything in
	 * front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		if (creativetabs.drawInForegroundOfTab()) {
			GlStateManager.disableBlend();
			this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6,
					4210752);
		}

	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		if (parInt3 == 0) {
			int i = parInt1 - this.guiLeft;
			int j = parInt2 - this.guiTop;

			for (int k = 0; k < CreativeTabs.creativeTabArray.length; ++k) {
				if (this.func_147049_a(CreativeTabs.creativeTabArray[k], i, j)) {
					return;
				}
			}
		}

		super.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		if (k == 0) {
			int l = i - this.guiLeft;
			int i1 = j - this.guiTop;

			for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
				CreativeTabs creativetabs = CreativeTabs.creativeTabArray[m];
				if (this.func_147049_a(creativetabs, l, i1)) {
					this.setCurrentCreativeTab(creativetabs);
					return;
				}
			}
		}

		super.mouseReleased(i, j, k);
	}

	@Override
	protected void touchTapped(int touchX, int touchY, int uid) {
		int l = touchX - this.guiLeft;
		int i1 = touchY - this.guiTop;

		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
			CreativeTabs creativetabs = CreativeTabs.creativeTabArray[m];
			if (this.func_147049_a(creativetabs, l, i1)) {
				this.setCurrentCreativeTab(creativetabs);
				break;
			}
		}

		super.touchTapped(touchX, touchY, uid);
	}

	/**+
	 * returns (if you are not on the inventoryTab) and (the flag
	 * isn't set) and (you have more than 1 page of items)
	 */
	private boolean needsScrollBars() {
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()
				&& CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
				&& ((GuiContainerCreative.ContainerCreative) this.inventorySlots).func_148328_e();
	}

	private void setCurrentCreativeTab(CreativeTabs parCreativeTabs) {
		int i = selectedTabIndex;
		selectedTabIndex = parCreativeTabs.getTabIndex();
		GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative) this.inventorySlots;
		this.dragSplittingSlots.clear();
		guicontainercreative$containercreative.itemList.clear();
		parCreativeTabs.displayAllReleventItems(guicontainercreative$containercreative.itemList);
		if (parCreativeTabs == CreativeTabs.tabInventory) {
			Container container = this.mc.thePlayer.inventoryContainer;
			if (this.field_147063_B == null) {
				this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
			}

			guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();

			for (int j = 0; j < container.inventorySlots.size(); ++j) {
				GuiContainerCreative.CreativeSlot guicontainercreative$creativeslot = new GuiContainerCreative.CreativeSlot(
						(Slot) container.inventorySlots.get(j), j);
				guicontainercreative$containercreative.inventorySlots.add(guicontainercreative$creativeslot);
				if (j >= 5 && j < 9) {
					int j1 = j - 5;
					int k1 = j1 / 2;
					int l1 = j1 % 2;
					guicontainercreative$creativeslot.xDisplayPosition = 9 + k1 * 54;
					guicontainercreative$creativeslot.yDisplayPosition = 6 + l1 * 27;
				} else if (j >= 0 && j < 5) {
					guicontainercreative$creativeslot.yDisplayPosition = -2000;
					guicontainercreative$creativeslot.xDisplayPosition = -2000;
				} else if (j < container.inventorySlots.size()) {
					int k = j - 9;
					int l = k % 9;
					int i1 = k / 9;
					guicontainercreative$creativeslot.xDisplayPosition = 9 + l * 18;
					if (j >= 36) {
						guicontainercreative$creativeslot.yDisplayPosition = 112;
					} else {
						guicontainercreative$creativeslot.yDisplayPosition = 54 + i1 * 18;
					}
				}
			}

			this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
			guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
		} else if (i == CreativeTabs.tabInventory.getTabIndex()) {
			guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
			this.field_147063_B = null;
		}

		if (this.searchField != null) {
			if (parCreativeTabs == CreativeTabs.tabAllSearch) {
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.updateCreativeSearch();
			} else {
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		guicontainercreative$containercreative.scrollTo(0.0F);
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0 && this.needsScrollBars()) {
			int j = ((GuiContainerCreative.ContainerCreative) this.inventorySlots).itemList.size() / 9 - 5;
			if (i > 0) {
				i = 1;
			}

			if (i < 0) {
				i = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
			this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
			((GuiContainerCreative.ContainerCreative) this.inventorySlots).scrollTo(this.currentScroll);
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		boolean flag = PointerInputAbstraction.getVCursorButtonDown(0);
		int k = this.guiLeft;
		int l = this.guiTop;
		int i1 = k + 175;
		int j1 = l + 18;
		int k1 = i1 + 14;
		int l1 = j1 + 112;
		if (!this.wasClicking && flag && i >= i1 && j >= j1 && i < k1 && j < l1) {
			this.isScrolling = this.needsScrollBars();
		}

		if (!flag) {
			this.isScrolling = false;
		}

		this.wasClicking = flag;
		if (this.isScrolling) {
			this.currentScroll = ((float) (j - j1) - 7.5F) / ((float) (l1 - j1) - 15.0F);
			this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
			((GuiContainerCreative.ContainerCreative) this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(i, j, f);

		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
			if (this.renderCreativeInventoryHoveringText(CreativeTabs.creativeTabArray[m], i, j)) {
				Mouse.showCursor(EnumCursorType.HAND);
				break;
			}
		}

		if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()
				&& this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16,
						16, i, j)) {
			this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), i, j);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
	}

	protected void renderToolTip(ItemStack itemstack, int i, int j) {
		if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
			List list = itemstack.getTooltipProfanityFilter(this.mc.thePlayer,
					this.mc.gameSettings.advancedItemTooltips);
			CreativeTabs creativetabs = itemstack.getItem().getCreativeTab();
			if (creativetabs == null && itemstack.getItem() == Items.enchanted_book) {
				IntIntMap map = EnchantmentHelper.getEnchantments(itemstack);
				if (map.size() == 1) {
					Enchantment enchantment = Enchantment.getEnchantmentById(map.keys().iterator().next().value);

					for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
						CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];
						if (creativetabs1.hasRelevantEnchantmentType(enchantment.type)) {
							creativetabs = creativetabs1;
							break;
						}
					}
				}
			}

			if (creativetabs != null) {
				list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE
						+ I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
			}

			for (int k = 0; k < list.size(); ++k) {
				if (k == 0) {
					list.set(k, itemstack.getRarity().rarityColor + (String) list.get(k));
				} else {
					list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
				}
			}

			this.drawHoveringText(list, i, j);
		} else {
			super.renderToolTip(itemstack, i, j);
		}

	}

	/**+
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float var1, int i, int j) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];

		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
			CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];
			this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
			if (creativetabs1.getTabIndex() != selectedTabIndex) {
				this.func_147051_a(creativetabs1);
			}
		}

		this.mc.getTextureManager().bindTexture(new ResourceLocation(
				"textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.searchField.drawTextBox();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int k = this.guiLeft + 175;
		int l = this.guiTop + 18;
		int i1 = l + 112;
		this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
		if (creativetabs.shouldHidePlayerInventory()) {
			this.drawTexturedModalRect(k, l + (int) ((float) (i1 - l - 17) * this.currentScroll),
					232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.func_147051_a(creativetabs);
		if (creativetabs == CreativeTabs.tabInventory) {
			GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, (float) (this.guiLeft + 43 - i),
					(float) (this.guiTop + 45 - 30 - j), this.mc.thePlayer);
		}

	}

	protected boolean func_147049_a(CreativeTabs parCreativeTabs, int parInt1, int parInt2) {
		int i = parCreativeTabs.getTabColumn();
		int j = 28 * i;
		int k = 0;
		if (i == 5) {
			j = this.xSize - 28 + 2;
		} else if (i > 0) {
			j += i;
		}

		if (parCreativeTabs.isTabInFirstRow()) {
			k = k - 32;
		} else {
			k = k + this.ySize;
		}

		return parInt1 >= j && parInt1 <= j + 28 && parInt2 >= k && parInt2 <= k + 32;
	}

	/**+
	 * Renders the creative inventory hovering text if mouse is over
	 * it. Returns true if did render or false otherwise. Params:
	 * current creative tab to be checked, current mouse x position,
	 * current mouse y position.
	 */
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs parCreativeTabs, int parInt1, int parInt2) {
		int i = parCreativeTabs.getTabColumn();
		int j = 28 * i;
		int k = 0;
		if (i == 5) {
			j = this.xSize - 28 + 2;
		} else if (i > 0) {
			j += i;
		}

		if (parCreativeTabs.isTabInFirstRow()) {
			k = k - 32;
		} else {
			k = k + this.ySize;
		}

		if (this.isPointInRegion(j + 3, k + 3, 23, 27, parInt1, parInt2)) {
			this.drawCreativeTabHoveringText(I18n.format(parCreativeTabs.getTranslatedTabLabel(), new Object[0]),
					parInt1, parInt2);
			return true;
		} else {
			return false;
		}
	}

	protected void func_147051_a(CreativeTabs parCreativeTabs) {
		boolean flag = parCreativeTabs.getTabIndex() == selectedTabIndex;
		boolean flag1 = parCreativeTabs.isTabInFirstRow();
		int i = parCreativeTabs.getTabColumn();
		int j = i * 28;
		int k = 0;
		int l = this.guiLeft + 28 * i;
		int i1 = this.guiTop;
		byte b0 = 32;
		if (flag) {
			k += 32;
		}

		if (i == 5) {
			l = this.guiLeft + this.xSize - 28;
		} else if (i > 0) {
			l += i;
		}

		if (flag1) {
			i1 = i1 - 28;
		} else {
			k += 64;
			i1 = i1 + (this.ySize - 4);
		}

		GlStateManager.disableLighting();
		this.drawTexturedModalRect(l, i1, j, k, 28, b0);
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		l = l + 6;
		i1 = i1 + 8 + (flag1 ? 1 : -1);
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		ItemStack itemstack = parCreativeTabs.getIconItemStack();
		this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
		this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
		GlStateManager.disableLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
		}

		if (parGuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}

	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	static class ContainerCreative extends Container {
		public List<ItemStack> itemList = Lists.newArrayList();

		public ContainerCreative(EntityPlayer parEntityPlayer) {
			InventoryPlayer inventoryplayer = parEntityPlayer.inventory;

			for (int i = 0; i < 5; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(
							new Slot(GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
				}
			}

			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
			}

			this.scrollTo(0.0F);
		}

		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}

		public void scrollTo(float parFloat1) {
			int i = (this.itemList.size() + 9 - 1) / 9 - 5;
			int j = (int) ((double) (parFloat1 * (float) i) + 0.5D);
			if (j < 0) {
				j = 0;
			}

			for (int k = 0; k < 5; ++k) {
				for (int l = 0; l < 9; ++l) {
					int i1 = l + (k + j) * 9;
					if (i1 >= 0 && i1 < this.itemList.size()) {
						GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9,
								(ItemStack) this.itemList.get(i1));
					} else {
						GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack) null);
					}
				}
			}

		}

		public boolean func_148328_e() {
			return this.itemList.size() > 45;
		}

		protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
		}

		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
			if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
				Slot slot = (Slot) this.inventorySlots.get(index);
				if (slot != null && slot.getHasStack()) {
					slot.putStack((ItemStack) null);
				}
			}

			return null;
		}

		public boolean canMergeSlot(ItemStack stack, Slot parSlot) {
			return parSlot.yDisplayPosition > 90;
		}

		public boolean canDragIntoSlot(Slot parSlot) {
			return parSlot.inventory instanceof InventoryPlayer
					|| parSlot.yDisplayPosition > 90 && parSlot.xDisplayPosition <= 162;
		}
	}

	class CreativeSlot extends Slot {
		private final Slot slot;

		public CreativeSlot(Slot parSlot, int parInt1) {
			super(parSlot.inventory, parInt1, 0, 0);
			this.slot = parSlot;
		}

		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
			this.slot.onPickupFromSlot(playerIn, stack);
		}

		public boolean isItemValid(ItemStack stack) {
			return this.slot.isItemValid(stack);
		}

		public ItemStack getStack() {
			return this.slot.getStack();
		}

		public boolean getHasStack() {
			return this.slot.getHasStack();
		}

		public void putStack(ItemStack stack) {
			this.slot.putStack(stack);
		}

		public void onSlotChanged() {
			this.slot.onSlotChanged();
		}

		public int getSlotStackLimit() {
			return this.slot.getSlotStackLimit();
		}

		public int getItemStackLimit(ItemStack stack) {
			return this.slot.getItemStackLimit(stack);
		}

		public String getSlotTexture() {
			return this.slot.getSlotTexture();
		}

		public ItemStack decrStackSize(int amount) {
			return this.slot.decrStackSize(amount);
		}

		public boolean isHere(IInventory inv, int slotIn) {
			return this.slot.isHere(inv, slotIn);
		}
	}

	public boolean blockPTTKey() {
		return searchField.isFocused();
	}

	@Override
	public boolean showCopyPasteButtons() {
		return searchField.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		searchField.fireInputEvent(event, param);
	}

}