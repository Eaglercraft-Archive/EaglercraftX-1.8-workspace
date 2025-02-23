package net.minecraft.client.settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;

import org.json.JSONArray;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVideoSettingsWarning;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredConfig;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
import net.lax1dude.eaglercraft.v1_8.recording.EnumScreenRecordingCodec;
import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.optifine.CustomSky;

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
public class GameSettings {
	private static final Logger logger = LogManager.getLogger();

	/**+
	 * GUI scale values
	 */
	private static final String[] GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small",
			"options.guiScale.normal", "options.guiScale.large" };
	private static final String[] PARTICLES = new String[] { "options.particles.all", "options.particles.decreased",
			"options.particles.minimal" };
	private static final String[] AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min",
			"options.ao.max" };
	private static final String[] STREAM_COMPRESSIONS = new String[] { "options.stream.compression.low",
			"options.stream.compression.medium", "options.stream.compression.high" };
	private static final String[] STREAM_CHAT_MODES = new String[] { "options.stream.chat.enabled.streaming",
			"options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
	private static final String[] STREAM_CHAT_FILTER_MODES = new String[] { "options.stream.chat.userFilter.all",
			"options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
	private static final String[] STREAM_MIC_MODES = new String[] { "options.stream.mic_toggle.mute",
			"options.stream.mic_toggle.talk" };
	private static final String[] field_181149_aW = new String[] { "options.off", "options.graphics.fast",
			"options.graphics.fancy" };
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse;
	public int renderDistanceChunks = -1;
	public boolean viewBobbing = true;
	public boolean anaglyph;
	public boolean fboEnable = true;
	public int limitFramerate = 260;
	/**+
	 * Clouds flag
	 */
	public int clouds = 1;
	public boolean fancyGraphics = false;
	/**+
	 * Smooth Lighting
	 */
	public int ambientOcclusion = 0;
	public List<String> resourcePacks = Lists.newArrayList();
	public List<String> field_183018_l = Lists.newArrayList();
	public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
	public boolean chatColours = true;
	public boolean chatLinks = true;
	public boolean chatLinksPrompt = true;
	public float chatOpacity = 1.0F;
	public boolean snooperEnabled = true;
	public boolean enableVsync = EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP;
	public boolean allowBlockAlternatives = true;
	public boolean reducedDebugInfo = false;
	public boolean hideServerAddress;
	public boolean advancedItemTooltips;
	/**+
	 * Whether to pause when the game loses focus, toggled by F3+P
	 */
	public boolean pauseOnLostFocus = true;
	private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet(EnumPlayerModelParts._VALUES);
	public boolean touchscreen;
	public int overrideWidth;
	public int overrideHeight;
	public boolean heldItemTooltips = true;
	public float chatScale = 1.0F;
	public float chatWidth = 1.0F;
	public float chatHeightUnfocused = 0.44366196F;
	public float chatHeightFocused = 1.0F;
	public boolean showInventoryAchievementHint = true;
	public int mipmapLevels = 4;
	private Map<SoundCategory, Float> mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
	public boolean field_181150_U = true;
	public boolean field_181151_V = true;
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
	public KeyBinding keyBindSprint = new KeyBinding("key.sprint", KeyboardConstants.KEY_R, "key.categories.movement");
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
	public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
	public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
	public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
	public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
	public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
	public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
	public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
	public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", KeyboardConstants.KEY_M,
			"key.categories.misc");
	public KeyBinding keyBindZoomCamera = new KeyBinding("key.zoomCamera", KeyboardConstants.KEY_C,
			"key.categories.misc");
	public KeyBinding keyBindFunction = new KeyBinding("key.function", KeyboardConstants.KEY_F, "key.categories.misc");
	public KeyBinding keyBindClose = new KeyBinding("key.close", KeyboardConstants.KEY_GRAVE, "key.categories.misc");
	public KeyBinding[] keyBindsHotbar = new KeyBinding[] {
			new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
			new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
			new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
			new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
			new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
			new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
			new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
			new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
			new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	public EnumDifficulty difficulty;
	public boolean hasCreatedDemoWorld;
	public int relayTimeout;
	public boolean hideJoinCode;
	public boolean hideGUI;
	public int thirdPersonView;
	public boolean showDebugInfo;
	public boolean showDebugProfilerChart;
	public boolean field_181657_aC;
	public String lastServer;
	public boolean smoothCamera;
	public boolean debugCamEnable;
	public float fovSetting;
	public float gammaSetting;
	public float saturation;
	/**+
	 * GUI scale
	 */
	public int guiScale = 3;
	public int particleSetting;
	public String language;
	public boolean forceUnicodeFont;
	public boolean hudFps = true;
	public boolean hudCoords = true;
	public boolean hudPlayer = false;
	public boolean hudWorld = false;
	public boolean hudStats = false;
	public boolean hud24h = false;
	public boolean chunkFix = true;
	public boolean fog = true;
	public int fxaa = 0;
	public boolean shaders = false;
	public boolean shadersAODisable = false;
	public EaglerDeferredConfig deferredShaderConf = new EaglerDeferredConfig();
	public boolean enableUpdateSvc = true;
	public boolean enableFNAWSkins = true;
	public boolean enableDynamicLights = false;
	public boolean hasHiddenPhishWarning = false;
	public boolean enableProfanityFilter = false;
	public boolean hasShownProfanityFilter = false;
	public float touchControlOpacity = 1.0f;
	public boolean hideDefaultUsernameWarning = false;
	public boolean hideVideoSettingsWarning = EagRuntime.getPlatformType() == EnumPlatformType.DESKTOP;

	public int voiceListenRadius = 16;
	public float voiceListenVolume = 0.5f;
	public float voiceSpeakVolume = 0.5f;
	public int voicePTTKey = 47; // V

	public EnumScreenRecordingCodec screenRecordCodec;
	public int screenRecordFPS = ScreenRecordingController.DEFAULT_FPS;
	public int screenRecordResolution = ScreenRecordingController.DEFAULT_RESOLUTION;
	public int screenRecordAudioBitrate = ScreenRecordingController.DEFAULT_AUDIO_BITRATE;
	public int screenRecordVideoBitrate = ScreenRecordingController.DEFAULT_VIDEO_BITRATE;
	public float screenRecordGameVolume = ScreenRecordingController.DEFAULT_GAME_VOLUME;
	public float screenRecordMicVolume = ScreenRecordingController.DEFAULT_MIC_VOLUME;

	public int betterGrassOF = 0;
	public int connectedTexturesOF = 1;
	public boolean customSkyOF = true;
	public boolean smartLeavesOF = false;
	public boolean customItemsOF = true;

	public GameSettings(Minecraft mcIn) {
		this.keyBindings = (KeyBinding[]) ArrayUtils.addAll(new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem,
				this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump,
				this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat,
				this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot,
				this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindZoomCamera, this.keyBindFunction,
				this.keyBindClose }, this.keyBindsHotbar);
		this.difficulty = EnumDifficulty.NORMAL;
		this.relayTimeout = 4;
		this.hideJoinCode = false;
		this.lastServer = "";
		this.fovSetting = 70.0F;
		this.gammaSetting = 1.0F;
		this.language = EagRuntime.getConfiguration().getDefaultLocale();
		this.forceUnicodeFont = false;
		this.mc = mcIn;
		this.renderDistanceChunks = 4;
		this.screenRecordCodec = ScreenRecordingController.getDefaultCodec();
		this.loadOptions();
	}

	/**+
	 * Represents a key or mouse button as a string. Args: key
	 */
	public static String getKeyDisplayString(int parInt1) {
		return parInt1 < 0 ? I18n.format("key.mouseButton", new Object[] { Integer.valueOf(parInt1 + 101) })
				: (parInt1 < 256 ? Keyboard.getKeyName(parInt1)
						: HString.format("%c", new Object[] { Character.valueOf((char) (parInt1 - 256)) })
								.toUpperCase());
	}

	/**+
	 * Returns whether the specified key binding is currently being
	 * pressed.
	 */
	public static boolean isKeyDown(KeyBinding parKeyBinding) {
		return parKeyBinding.getKeyCode() == 0 ? false
				: (parKeyBinding.getKeyCode() < 0
						? PointerInputAbstraction.getVCursorButtonDown(parKeyBinding.getKeyCode() + 100)
						: Keyboard.isKeyDown(parKeyBinding.getKeyCode()));
	}

	/**+
	 * Sets a key binding and then saves all settings.
	 */
	public void setOptionKeyBinding(KeyBinding parKeyBinding, int parInt1) {
		parKeyBinding.setKeyCode(parInt1);
		this.saveOptions();
	}

	/**+
	 * If the specified option is controlled by a slider (float
	 * value), this will set the float value.
	 */
	public void setOptionFloatValue(GameSettings.Options parOptions, float parFloat1) {
		if (parOptions == GameSettings.Options.SENSITIVITY) {
			this.mouseSensitivity = parFloat1;
		}

		if (parOptions == GameSettings.Options.FOV) {
			this.fovSetting = parFloat1;
		}

		if (parOptions == GameSettings.Options.GAMMA) {
			this.gammaSetting = parFloat1;
		}

		if (parOptions == GameSettings.Options.FRAMERATE_LIMIT) {
			this.limitFramerate = (int) parFloat1;
		}

		if (parOptions == GameSettings.Options.CHAT_OPACITY) {
			this.chatOpacity = parFloat1;
			this.mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (parOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
			this.chatHeightFocused = parFloat1;
			this.mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (parOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
			this.chatHeightUnfocused = parFloat1;
			this.mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (parOptions == GameSettings.Options.CHAT_WIDTH) {
			this.chatWidth = parFloat1;
			this.mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (parOptions == GameSettings.Options.CHAT_SCALE) {
			this.chatScale = parFloat1;
			this.mc.ingameGUI.getChatGUI().refreshChat();
		}

		if (parOptions == GameSettings.Options.MIPMAP_LEVELS) {
			int i = this.mipmapLevels;
			this.mipmapLevels = (int) parFloat1;
			if ((float) i != parFloat1) {
				this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
				this.mc.scheduleResourcesRefresh();
			}
		}

		if (parOptions == GameSettings.Options.BLOCK_ALTERNATIVES) {
			this.allowBlockAlternatives = !this.allowBlockAlternatives;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.RENDER_DISTANCE) {
			this.renderDistanceChunks = (int) parFloat1;
			this.mc.renderGlobal.setDisplayListEntitiesDirty();
		}

		if (parOptions == GameSettings.Options.EAGLER_TOUCH_CONTROL_OPACITY) {
			this.touchControlOpacity = parFloat1;
		}
	}

	/**+
	 * For non-float options. Toggles the option on/off, or cycles
	 * through the list i.e. render distances.
	 */
	public void setOptionValue(GameSettings.Options parOptions, int parInt1) {
		if (parOptions == GameSettings.Options.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if (parOptions == GameSettings.Options.GUI_SCALE) {
			this.guiScale = this.guiScale + parInt1 & 3;
		}

		if (parOptions == GameSettings.Options.PARTICLES) {
			this.particleSetting = (this.particleSetting + parInt1) % 3;
		}

		if (parOptions == GameSettings.Options.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if (parOptions == GameSettings.Options.RENDER_CLOUDS) {
			this.clouds = (this.clouds + parInt1) % 3;
		}

		if (parOptions == GameSettings.Options.FORCE_UNICODE_FONT) {
			this.forceUnicodeFont = !this.forceUnicodeFont;
			this.mc.fontRendererObj
					.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
		}

		if (parOptions == GameSettings.Options.FBO_ENABLE) {
			this.fboEnable = !this.fboEnable;
		}

		if (parOptions == GameSettings.Options.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
					I18n.format("resourcePack.load.pleaseWait"));
			this.mc.refreshResources();
		}

		if (parOptions == GameSettings.Options.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = (this.ambientOcclusion + parInt1) % 3;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.CHAT_VISIBILITY) {
			this.chatVisibility = EntityPlayer.EnumChatVisibility
					.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + parInt1) % 3);
		}

		if (parOptions == GameSettings.Options.CHAT_COLOR) {
			this.chatColours = !this.chatColours;
		}

		if (parOptions == GameSettings.Options.CHAT_LINKS) {
			this.chatLinks = !this.chatLinks;
		}

		if (parOptions == GameSettings.Options.CHAT_LINKS_PROMPT) {
			this.chatLinksPrompt = !this.chatLinksPrompt;
		}

		if (parOptions == GameSettings.Options.SNOOPER_ENABLED) {
			this.snooperEnabled = !this.snooperEnabled;
		}

		if (parOptions == GameSettings.Options.BLOCK_ALTERNATIVES) {
			this.allowBlockAlternatives = !this.allowBlockAlternatives;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.REDUCED_DEBUG_INFO) {
			this.reducedDebugInfo = !this.reducedDebugInfo;
		}

		if (parOptions == GameSettings.Options.ENTITY_SHADOWS) {
			this.field_181151_V = !this.field_181151_V;
		}

		if (parOptions == GameSettings.Options.HUD_FPS) {
			this.hudFps = !this.hudFps;
		}

		if (parOptions == GameSettings.Options.HUD_COORDS) {
			this.hudCoords = !this.hudCoords;
		}

		if (parOptions == GameSettings.Options.HUD_PLAYER) {
			this.hudPlayer = !this.hudPlayer;
		}

		if (parOptions == GameSettings.Options.HUD_STATS) {
			this.hudStats = !this.hudStats;
		}

		if (parOptions == GameSettings.Options.HUD_WORLD) {
			this.hudWorld = !this.hudWorld;
		}

		if (parOptions == GameSettings.Options.HUD_24H) {
			this.hud24h = !this.hud24h;
		}

		if (parOptions == GameSettings.Options.CHUNK_FIX) {
			this.chunkFix = !this.chunkFix;
		}

		if (parOptions == GameSettings.Options.FOG) {
			this.fog = !this.fog;
		}

		if (parOptions == GameSettings.Options.FXAA) {
			this.fxaa = (this.fxaa + parInt1) % 3;
		}

		if (parOptions == GameSettings.Options.FULLSCREEN) {
			this.mc.toggleFullscreen();
		}

		if (parOptions == GameSettings.Options.FNAW_SKINS) {
			this.enableFNAWSkins = !this.enableFNAWSkins;
			this.mc.getRenderManager().setEnableFNAWSkins(this.mc.getEnableFNAWSkins());
		}

		if (parOptions == GameSettings.Options.EAGLER_VSYNC) {
			this.enableVsync = !this.enableVsync;
		}

		if (parOptions == GameSettings.Options.EAGLER_DYNAMIC_LIGHTS) {
			this.enableDynamicLights = !this.enableDynamicLights;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.EAGLER_PROFANITY_FILTER) {
			this.enableProfanityFilter = !this.enableProfanityFilter;
		}

		if (parOptions == GameSettings.Options.OF_CONNECTED_TEXTURES) {
			this.connectedTexturesOF = (this.connectedTexturesOF + 1) % 3;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.OF_BETTER_GRASS) {
			this.betterGrassOF = (this.betterGrassOF + 1) % 3;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.OF_CUSTOM_SKIES) {
			this.customSkyOF = !this.customSkyOF;
			CustomSky.update();
		}

		if (parOptions == GameSettings.Options.OF_SMART_LEAVES) {
			this.smartLeavesOF = !this.smartLeavesOF;
			this.mc.renderGlobal.loadRenderers();
		}

		if (parOptions == GameSettings.Options.OF_CUSTOM_ITEMS) {
			this.customItemsOF = !this.customItemsOF;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(GameSettings.Options parOptions) {
		return parOptions == GameSettings.Options.FOV ? this.fovSetting
				: (parOptions == GameSettings.Options.GAMMA ? this.gammaSetting
						: (parOptions == GameSettings.Options.SATURATION ? this.saturation
								: (parOptions == GameSettings.Options.SENSITIVITY ? this.mouseSensitivity
										: (parOptions == GameSettings.Options.CHAT_OPACITY ? this.chatOpacity
												: (parOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED
														? this.chatHeightFocused
														: (parOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED
																? this.chatHeightUnfocused
																: (parOptions == GameSettings.Options.CHAT_SCALE
																		? this.chatScale
																		: (parOptions == GameSettings.Options.CHAT_WIDTH
																				? this.chatWidth
																				: (parOptions == GameSettings.Options.FRAMERATE_LIMIT
																						? (float) this.limitFramerate
																						: (parOptions == GameSettings.Options.MIPMAP_LEVELS
																								? (float) this.mipmapLevels
																								: (parOptions == GameSettings.Options.RENDER_DISTANCE
																										? (float) this.renderDistanceChunks
																										: (parOptions == GameSettings.Options.EAGLER_TOUCH_CONTROL_OPACITY
																												? this.touchControlOpacity
																												: 0.0F))))))))))));
	}

	public boolean getOptionOrdinalValue(GameSettings.Options parOptions) {
		switch (parOptions) {
		case INVERT_MOUSE:
			return this.invertMouse;
		case VIEW_BOBBING:
			return this.viewBobbing;
		case ANAGLYPH:
			return this.anaglyph;
		case FBO_ENABLE:
			return this.fboEnable;
		case CHAT_COLOR:
			return this.chatColours;
		case CHAT_LINKS:
			return this.chatLinks;
		case CHAT_LINKS_PROMPT:
			return this.chatLinksPrompt;
		case SNOOPER_ENABLED:
			return this.snooperEnabled;
		case FORCE_UNICODE_FONT:
			return this.forceUnicodeFont;
		case BLOCK_ALTERNATIVES:
			return this.allowBlockAlternatives;
		case REDUCED_DEBUG_INFO:
			return this.reducedDebugInfo;
		case ENTITY_SHADOWS:
			return this.field_181151_V;
		case HUD_COORDS:
			return this.hudCoords;
		case HUD_FPS:
			return this.hudFps;
		case HUD_PLAYER:
			return this.hudPlayer;
		case HUD_STATS:
			return this.hudStats;
		case HUD_WORLD:
			return this.hudWorld;
		case HUD_24H:
			return this.hud24h;
		case CHUNK_FIX:
			return this.chunkFix;
		case FOG:
			return this.fog;
		case FULLSCREEN:
			return this.mc.isFullScreen();
		case FNAW_SKINS:
			return this.enableFNAWSkins;
		case EAGLER_VSYNC:
			return this.enableVsync;
		case EAGLER_DYNAMIC_LIGHTS:
			return this.enableDynamicLights;
		case EAGLER_PROFANITY_FILTER:
			return this.enableProfanityFilter;
		case OF_CUSTOM_SKIES:
			return this.customSkyOF;
		case OF_SMART_LEAVES:
			return this.smartLeavesOF;
		case OF_CUSTOM_ITEMS:
			return this.customItemsOF;
		default:
			return false;
		}
	}

	/**+
	 * Returns the translation of the given index in the given
	 * String array. If the index is smaller than 0 or greater
	 * than/equal to the length of the String array, it is changed
	 * to 0.
	 */
	private static String getTranslation(String[] parArrayOfString, int parInt1) {
		if (parInt1 < 0 || parInt1 >= parArrayOfString.length) {
			parInt1 = 0;
		}

		return I18n.format(parArrayOfString[parInt1], new Object[0]);
	}

	/**+
	 * Gets a key binding.
	 */
	public String getKeyBinding(GameSettings.Options parOptions) {
		String s = I18n.format(parOptions.getEnumString(), new Object[0]) + ": ";
		if (parOptions.getEnumFloat()) {
			float f1 = this.getOptionFloatValue(parOptions);
			float f = parOptions.normalizeValue(f1);
			return parOptions == GameSettings.Options.SENSITIVITY
					? (f == 0.0F ? s + I18n.format("options.sensitivity.min", new Object[0])
							: (f == 1.0F ? s + I18n.format("options.sensitivity.max", new Object[0])
									: s + (int) (f * 200.0F) + "%"))
					: (parOptions == GameSettings.Options.FOV
							? (f1 == 70.0F ? s + I18n.format("options.fov.min", new Object[0])
									: (f1 == 110.0F ? s + I18n.format("options.fov.max", new Object[0]) : s + (int) f1))
							: (parOptions == GameSettings.Options.FRAMERATE_LIMIT
									? (f1 == parOptions.valueMax
											? s + I18n.format("options.framerateLimit.max", new Object[0])
											: s + (int) f1 + " fps")
									: (parOptions == GameSettings.Options.RENDER_CLOUDS
											? (f1 == parOptions.valueMin
													? s + I18n.format("options.cloudHeight.min", new Object[0])
													: s + ((int) f1 + 128))
											: (parOptions == GameSettings.Options.GAMMA
													? (f == 0.0F ? s + I18n.format("options.gamma.min", new Object[0])
															: (f == 1.0F
																	? s + I18n.format("options.gamma.max",
																			new Object[0])
																	: s + "+" + (int) (f * 100.0F) + "%"))
													: (parOptions == GameSettings.Options.SATURATION
															? s + (int) (f * 400.0F) + "%"
															: (parOptions == GameSettings.Options.CHAT_OPACITY
																	? s + (int) (f * 90.0F + 10.0F) + "%"
																	: (parOptions == GameSettings.Options.CHAT_SCALE
																			? s + (int) (f * 90.0F + 10.0F) + "%"
																			: (parOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED
																					? s + GuiNewChat
																							.calculateChatboxHeight(f)
																							+ "px"
																					: (parOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED
																							? s + GuiNewChat
																									.calculateChatboxHeight(
																											f)
																									+ "px"
																							: (parOptions == GameSettings.Options.CHAT_WIDTH
																									? s + GuiNewChat
																											.calculateChatboxWidth(
																													f)
																											+ "px"
																									: (parOptions == GameSettings.Options.RENDER_DISTANCE
																											? s + (int) f1
																													+ (f1 == 1.0F
																															? " chunk"
																															: " chunks")
																											: (parOptions == GameSettings.Options.MIPMAP_LEVELS
																													? (f == 0.0F
																															? s + I18n
																																	.format("options.off",
																																			new Object[0])
																															: s + (int) (f
																																	* 100.0F)
																																	+ "%")
																													: (parOptions == GameSettings.Options.EAGLER_TOUCH_CONTROL_OPACITY
																															? (s + (int) (f
																																	* 100.0F)
																																	+ "%")
																															: "yee")))))))))))));
		} else if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP
				&& parOptions == GameSettings.Options.EAGLER_VSYNC) {
			boolean flag = this.getOptionOrdinalValue(parOptions);
			return flag ? s + I18n.format("options.on", new Object[0])
					: EnumChatFormatting.RED + s + I18n.format("options.off", new Object[0]);
		} else if (parOptions.getEnumBoolean()) {
			boolean flag = this.getOptionOrdinalValue(parOptions);
			return flag ? s + I18n.format("options.on", new Object[0]) : s + I18n.format("options.off", new Object[0]);
		} else if (parOptions == GameSettings.Options.GUI_SCALE) {
			return s + getTranslation(GUISCALES, this.guiScale);
		} else if (parOptions == GameSettings.Options.CHAT_VISIBILITY) {
			return s + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
		} else if (parOptions == GameSettings.Options.PARTICLES) {
			return s + getTranslation(PARTICLES, this.particleSetting);
		} else if (parOptions == GameSettings.Options.AMBIENT_OCCLUSION) {
			return s + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
		} else if (parOptions == GameSettings.Options.RENDER_CLOUDS) {
			return s + getTranslation(field_181149_aW, this.clouds);
		} else if (parOptions == GameSettings.Options.GRAPHICS) {
			if (this.fancyGraphics) {
				return s + I18n.format("options.graphics.fancy", new Object[0]);
			} else {
				String s1 = "options.graphics.fast";
				return s + I18n.format("options.graphics.fast", new Object[0]);
			}
		} else if (parOptions == GameSettings.Options.FXAA) {
			if (this.fxaa == 0) {
				return s + I18n.format("options.fxaa.auto");
			} else if (this.fxaa == 1) {
				return s + I18n.format("options.on");
			} else {
				return s + I18n.format("options.off");
			}
		} else if (parOptions == GameSettings.Options.OF_CONNECTED_TEXTURES) {
			if (this.connectedTexturesOF == 0) {
				return s + I18n.format("options.off");
			} else if (this.connectedTexturesOF == 1) {
				return s + I18n.format("options.graphics.fast");
			} else {
				return s + I18n.format("options.graphics.fancy");
			}
		} else if (parOptions == GameSettings.Options.OF_BETTER_GRASS) {
			if (this.betterGrassOF == 0) {
				return s + I18n.format("options.off");
			} else if (this.betterGrassOF == 1) {
				return s + I18n.format("options.graphics.fast");
			} else {
				return s + I18n.format("options.graphics.fancy");
			}
		} else {
			return s;
		}
	}

	/**+
	 * Loads the options from the options file. It appears that this
	 * has replaced the previous 'loadOptions'
	 */
	public void loadOptions() {
		byte[] options = EagRuntime.getStorage("g");
		if (options == null) {
			return;
		}
		loadOptions(options);
	}

	/**+
	 * Loads the options from the options file. It appears that this
	 * has replaced the previous 'loadOptions'
	 */
	public void loadOptions(byte[] data) {
		try {
			BufferedReader bufferedreader = new BufferedReader(
					new InputStreamReader(EaglerZLIB.newGZIPInputStream(new EaglerInputStream(data))));
			String s = "";
			this.mapSoundLevels.clear();

			while ((s = bufferedreader.readLine()) != null) {
				try {
					String[] astring = s.split(":");
					if (astring[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("fov")) {
						this.fovSetting = this.parseFloat(astring[1]) * 40.0F + 70.0F;
					}

					if (astring[0].equals("gamma")) {
						this.gammaSetting = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("saturation")) {
						this.saturation = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("invertYMouse")) {
						this.invertMouse = astring[1].equals("true");
					}

					if (astring[0].equals("renderDistance")) {
						this.renderDistanceChunks = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("particles")) {
						this.particleSetting = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("bobView")) {
						this.viewBobbing = astring[1].equals("true");
					}

					if (astring[0].equals("anaglyph3d")) {
						this.anaglyph = astring[1].equals("true");
					}

					if (astring[0].equals("maxFps")) {
						this.limitFramerate = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("fboEnable")) {
						this.fboEnable = astring[1].equals("true");
					}

					if (astring[0].equals("difficulty")) {
						this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(astring[1]));
					}

					if (astring[0].equals("hasCreatedDemoWorld")) {
						this.hasCreatedDemoWorld = astring[1].equals("true");
					}

					if (astring[0].equals("relayTimeout")) {
						this.relayTimeout = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("hideJoinCode")) {
						this.hideJoinCode = astring[1].equals("true");
					}

					if (astring[0].equals("fancyGraphics")) {
						this.fancyGraphics = astring[1].equals("true");
					}

					if (astring[0].equals("ao")) {
						if (astring[1].equals("true")) {
							this.ambientOcclusion = 2;
						} else if (astring[1].equals("false")) {
							this.ambientOcclusion = 0;
						} else {
							this.ambientOcclusion = Integer.parseInt(astring[1]);
						}
					}

					if (astring[0].equals("renderClouds")) {
						if (astring[1].equals("true")) {
							this.clouds = 2;
						} else if (astring[1].equals("false")) {
							this.clouds = 0;
						} else if (astring[1].equals("fast")) {
							this.clouds = 1;
						}
					}

					if (astring[0].equals("resourcePacks")) {
						this.resourcePacks.clear();
						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
							if (o instanceof String) {
								this.resourcePacks.add((String) o);
							}
						}
						if (this.resourcePacks == null) {
							this.resourcePacks = Lists.newArrayList();
						}
					}

					if (astring[0].equals("incompatibleResourcePacks")) {
						this.field_183018_l.clear();
						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
							if (o instanceof String) {
								this.field_183018_l.add((String) o);
							}
						}
						if (this.field_183018_l == null) {
							this.field_183018_l = Lists.newArrayList();
						}
					}

					if (astring[0].equals("lastServer") && astring.length >= 2) {
						this.lastServer = s.substring(s.indexOf(58) + 1);
					}

					if (astring[0].equals("lang") && astring.length >= 2) {
						this.language = astring[1];
					}

					if (astring[0].equals("chatVisibility")) {
						this.chatVisibility = EntityPlayer.EnumChatVisibility
								.getEnumChatVisibility(Integer.parseInt(astring[1]));
					}

					if (astring[0].equals("chatColors")) {
						this.chatColours = astring[1].equals("true");
					}

					if (astring[0].equals("chatLinks")) {
						this.chatLinks = astring[1].equals("true");
					}

					if (astring[0].equals("chatLinksPrompt")) {
						this.chatLinksPrompt = astring[1].equals("true");
					}

					if (astring[0].equals("chatOpacity")) {
						this.chatOpacity = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("snooperEnabled")) {
						this.snooperEnabled = astring[1].equals("true");
					}

					if (astring[0].equals("enableVsyncEag")) {
						this.enableVsync = astring[1].equals("true");
					}

					if (astring[0].equals("hideServerAddress")) {
						this.hideServerAddress = astring[1].equals("true");
					}

					if (astring[0].equals("advancedItemTooltips")) {
						this.advancedItemTooltips = astring[1].equals("true");
					}

					if (astring[0].equals("pauseOnLostFocus")) {
						this.pauseOnLostFocus = astring[1].equals("true");
					}

					if (astring[0].equals("overrideHeight")) {
						this.overrideHeight = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("overrideWidth")) {
						this.overrideWidth = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("heldItemTooltips")) {
						this.heldItemTooltips = astring[1].equals("true");
					}

					if (astring[0].equals("chatHeightFocused")) {
						this.chatHeightFocused = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("chatHeightUnfocused")) {
						this.chatHeightUnfocused = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("chatScale")) {
						this.chatScale = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("chatWidth")) {
						this.chatWidth = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("showInventoryAchievementHint")) {
						this.showInventoryAchievementHint = astring[1].equals("true");
					}

					if (astring[0].equals("mipmapLevels")) {
						this.mipmapLevels = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("forceUnicodeFont")) {
						this.forceUnicodeFont = astring[1].equals("true");
					}

					if (astring[0].equals("allowBlockAlternatives")) {
						this.allowBlockAlternatives = astring[1].equals("true");
					}

					if (astring[0].equals("reducedDebugInfo")) {
						this.reducedDebugInfo = astring[1].equals("true");
					}

					if (astring[0].equals("useNativeTransport")) {
						this.field_181150_U = astring[1].equals("true");
					}

					if (astring[0].equals("entityShadows")) {
						this.field_181151_V = astring[1].equals("true");
					}

					if (astring[0].equals("hudFps")) {
						this.hudFps = astring[1].equals("true");
					}

					if (astring[0].equals("hudWorld")) {
						this.hudWorld = astring[1].equals("true");
					}

					if (astring[0].equals("hudCoords")) {
						this.hudCoords = astring[1].equals("true");
					}

					if (astring[0].equals("hudPlayer")) {
						this.hudPlayer = astring[1].equals("true");
					}

					if (astring[0].equals("hudStats")) {
						this.hudStats = astring[1].equals("true");
					}

					if (astring[0].equals("hud24h")) {
						this.hud24h = astring[1].equals("true");
					}

					if (astring[0].equals("chunkFix")) {
						this.chunkFix = astring[1].equals("true");
					}

					if (astring[0].equals("fog")) {
						this.fog = astring[1].equals("true");
					}

					if (astring[0].equals("fxaa")) {
						this.fxaa = (astring[1].equals("true") || astring[1].equals("false")) ? 0
								: Integer.parseInt(astring[1]);
					}

					for (KeyBinding keybinding : this.keyBindings) {
						if (astring[0].equals("key_" + keybinding.getKeyDescription())) {
							keybinding.setKeyCode(Integer.parseInt(astring[1]));
						}
					}

					if (astring[0].equals("shaders")) {
						this.shaders = astring[1].equals("true");
					}

					if (astring[0].equals("enableUpdateSvc")) {
						this.enableUpdateSvc = astring[1].equals("true");
					}

					if (astring[0].equals("voiceListenRadius")) {
						voiceListenRadius = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("voiceListenVolume")) {
						voiceListenVolume = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("voiceSpeakVolume")) {
						voiceSpeakVolume = this.parseFloat(astring[1]);
					}

					if (astring[0].equals("voicePTTKey")) {
						voicePTTKey = Integer.parseInt(astring[1]);
					}

					for (SoundCategory soundcategory : SoundCategory._VALUES) {
						if (astring[0].equals("soundCategory_" + soundcategory.getCategoryName())) {
							this.mapSoundLevels.put(soundcategory, Float.valueOf(this.parseFloat(astring[1])));
						}
					}

					for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts._VALUES) {
						if (astring[0].equals("modelPart_" + enumplayermodelparts.getPartName())) {
							this.setModelPartEnabled(enumplayermodelparts, astring[1].equals("true"));
						}
					}

					if (astring[0].equals("enableFNAWSkins")) {
						this.enableFNAWSkins = astring[1].equals("true");
					}

					if (astring[0].equals("enableDynamicLights")) {
						this.enableDynamicLights = astring[1].equals("true");
					}

					if (astring[0].equals("hasHiddenPhishWarning")) {
						this.hasHiddenPhishWarning = astring[1].equals("true");
					}

					if (astring[0].equals("enableProfanityFilter")) {
						this.enableProfanityFilter = astring[1].equals("true");
					}

					if (astring[0].equals("hasShownProfanityFilter")) {
						this.hasShownProfanityFilter = astring[1].equals("true");
					}

					if (astring[0].equals("screenRecordCodec")) {
						EnumScreenRecordingCodec codec = EnumScreenRecordingCodec.valueOf(astring[1]);
						if (!ScreenRecordingController.codecs.contains(codec)) {
							throw new IllegalStateException("Selected codec is not supported: " + codec.name);
						}
						screenRecordCodec = codec;
					}

					if (astring[0].equals("screenRecordFPS")) {
						screenRecordFPS = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("screenRecordFPS")) {
						screenRecordFPS = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("screenRecordResolution")) {
						screenRecordResolution = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("screenRecordAudioBitrate")) {
						screenRecordAudioBitrate = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("screenRecordVideoBitrate")) {
						screenRecordVideoBitrate = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("screenRecordGameVolume")) {
						screenRecordGameVolume = parseFloat(astring[1]);
					}

					if (astring[0].equals("screenRecordMicVolume")) {
						screenRecordMicVolume = parseFloat(astring[1]);
					}

					if (astring[0].equals("touchControlOpacity")) {
						touchControlOpacity = parseFloat(astring[1]);
					}

					if (astring[0].equals("hideDefaultUsernameWarning")) {
						this.hideDefaultUsernameWarning = astring[1].equals("true");
					}

					if (astring[0].equals("hideVideoSettingsWarning")) {
						hideVideoSettingsWarning = astring[1].equals("true");
					}

					if (astring[0].equals("betterGrassOF")) {
						betterGrassOF = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("connectedTexturesOF")) {
						connectedTexturesOF = Integer.parseInt(astring[1]);
					}

					if (astring[0].equals("customSkyOF")) {
						customSkyOF = astring[1].equals("true");
					}

					if (astring[0].equals("smartLeavesOF")) {
						smartLeavesOF = astring[1].equals("true");
					}

					if (astring[0].equals("customItemsOF")) {
						customItemsOF = astring[1].equals("true");
					}

					deferredShaderConf.readOption(astring[0], astring[1]);
				} catch (Exception var8) {
					logger.warn("Skipping bad option: " + s);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();

			Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());
			VoiceClientController.setVoiceListenVolume(voiceListenVolume);
			VoiceClientController.setVoiceSpeakVolume(voiceSpeakVolume);
			VoiceClientController.setVoiceProximity(voiceListenRadius);
			ScreenRecordingController.setGameVolume(screenRecordGameVolume);
			ScreenRecordingController.setMicrophoneVolume(screenRecordMicVolume);
			if (this.mc.getRenderManager() != null)
				this.mc.getRenderManager().setEnableFNAWSkins(this.enableFNAWSkins);
			if (this.shaders && !EaglerDeferredPipeline.isSupported()) {
				logger.error("Setting shaders to false because they are not supported");
				this.shaders = false;
			}
			if (this.enableDynamicLights && !DynamicLightsStateManager.isSupported()) {
				logger.error("Setting dynamic lights to false because they are not supported");
				this.enableDynamicLights = false;
			}
		} catch (Exception exception) {
			logger.error("Failed to load options");
			logger.error(exception);
		}

	}

	/**+
	 * Parses a string into a float.
	 */
	private float parseFloat(String parString1) {
		return parString1.equals("true") ? 1.0F : (parString1.equals("false") ? 0.0F : Float.parseFloat(parString1));
	}

	/**+
	 * Saves the options to the options file.
	 */
	public void saveOptions() {
		byte[] data = writeOptions();
		if (data != null) {
			EagRuntime.setStorage("g", data);
		}
		RelayManager.relayManager.save();
		this.sendSettingsToServer();
	}

	public byte[] writeOptions() {
		try {
			EaglerOutputStream bao = new EaglerOutputStream();
			PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(EaglerZLIB.newGZIPOutputStream(bao)));
			printwriter.println("invertYMouse:" + this.invertMouse);
			printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
			printwriter.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
			printwriter.println("gamma:" + this.gammaSetting);
			printwriter.println("saturation:" + this.saturation);
			printwriter.println("renderDistance:" + this.renderDistanceChunks);
			printwriter.println("guiScale:" + this.guiScale);
			printwriter.println("particles:" + this.particleSetting);
			printwriter.println("bobView:" + this.viewBobbing);
			printwriter.println("anaglyph3d:" + this.anaglyph);
			printwriter.println("maxFps:" + this.limitFramerate);
			printwriter.println("fboEnable:" + this.fboEnable);
			printwriter.println("difficulty:" + this.difficulty.getDifficultyId());
			printwriter.println("hasCreatedDemoWorld:" + this.hasCreatedDemoWorld);
			printwriter.println("relayTimeout:" + this.relayTimeout);
			printwriter.println("hideJoinCode:" + this.hideJoinCode);
			printwriter.println("fancyGraphics:" + this.fancyGraphics);
			printwriter.println("ao:" + this.ambientOcclusion);
			switch (this.clouds) {
			case 0:
				printwriter.println("renderClouds:false");
				break;
			case 1:
				printwriter.println("renderClouds:fast");
				break;
			case 2:
				printwriter.println("renderClouds:true");
			}

			printwriter.println("resourcePacks:" + toJSONArray(this.resourcePacks));
			printwriter.println("incompatibleResourcePacks:" + toJSONArray(this.field_183018_l));
			printwriter.println("lastServer:" + this.lastServer);
			printwriter.println("lang:" + this.language);
			printwriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
			printwriter.println("chatColors:" + this.chatColours);
			printwriter.println("chatLinks:" + this.chatLinks);
			printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
			printwriter.println("chatOpacity:" + this.chatOpacity);
			printwriter.println("snooperEnabled:" + this.snooperEnabled);
			printwriter.println("enableVsyncEag:" + this.enableVsync);
			printwriter.println("hideServerAddress:" + this.hideServerAddress);
			printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
			printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
			printwriter.println("overrideWidth:" + this.overrideWidth);
			printwriter.println("overrideHeight:" + this.overrideHeight);
			printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
			printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
			printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
			printwriter.println("chatScale:" + this.chatScale);
			printwriter.println("chatWidth:" + this.chatWidth);
			printwriter.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
			printwriter.println("mipmapLevels:" + this.mipmapLevels);
			printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
			printwriter.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
			printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
			printwriter.println("useNativeTransport:" + this.field_181150_U);
			printwriter.println("entityShadows:" + this.field_181151_V);
			printwriter.println("hudFps:" + this.hudFps);
			printwriter.println("hudWorld:" + this.hudWorld);
			printwriter.println("hudCoords:" + this.hudCoords);
			printwriter.println("hudPlayer:" + this.hudPlayer);
			printwriter.println("hudStats:" + this.hudStats);
			printwriter.println("hud24h:" + this.hud24h);
			printwriter.println("chunkFix:" + this.chunkFix);
			printwriter.println("fog:" + this.fog);
			printwriter.println("fxaa:" + this.fxaa);
			printwriter.println("shaders:" + this.shaders);
			printwriter.println("enableUpdateSvc:" + this.enableUpdateSvc);
			printwriter.println("voiceListenRadius:" + this.voiceListenRadius);
			printwriter.println("voiceListenVolume:" + this.voiceListenVolume);
			printwriter.println("voiceSpeakVolume:" + this.voiceSpeakVolume);
			printwriter.println("voicePTTKey:" + this.voicePTTKey);
			printwriter.println("enableFNAWSkins:" + this.enableFNAWSkins);
			printwriter.println("enableDynamicLights:" + this.enableDynamicLights);
			printwriter.println("hasHiddenPhishWarning:" + this.hasHiddenPhishWarning);
			printwriter.println("enableProfanityFilter:" + this.enableProfanityFilter);
			printwriter.println("hasShownProfanityFilter:" + this.hasShownProfanityFilter);
			if (screenRecordCodec != null) {
				printwriter.println("screenRecordCodec:" + this.screenRecordCodec);
			}
			printwriter.println("screenRecordFPS:" + this.screenRecordFPS);
			printwriter.println("screenRecordResolution:" + this.screenRecordResolution);
			printwriter.println("screenRecordAudioBitrate:" + this.screenRecordAudioBitrate);
			printwriter.println("screenRecordVideoBitrate:" + this.screenRecordVideoBitrate);
			printwriter.println("screenRecordGameVolume:" + this.screenRecordGameVolume);
			printwriter.println("screenRecordMicVolume:" + this.screenRecordMicVolume);
			printwriter.println("touchControlOpacity:" + this.touchControlOpacity);
			printwriter.println("hideDefaultUsernameWarning:" + this.hideDefaultUsernameWarning);
			printwriter.println("hideVideoSettingsWarning:" + this.hideVideoSettingsWarning);
			printwriter.println("betterGrassOF:" + this.betterGrassOF);
			printwriter.println("connectedTexturesOF:" + this.connectedTexturesOF);
			printwriter.println("customSkyOF:" + this.customSkyOF);
			printwriter.println("smartLeavesOF:" + this.smartLeavesOF);
			printwriter.println("customItemsOF:" + this.customItemsOF);

			for (KeyBinding keybinding : this.keyBindings) {
				printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
			}

			Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());

			for (SoundCategory soundcategory : SoundCategory._VALUES) {
				printwriter.println(
						"soundCategory_" + soundcategory.getCategoryName() + ":" + this.getSoundLevel(soundcategory));
			}

			for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts._VALUES) {
				printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":"
						+ this.setModelParts.contains(enumplayermodelparts));
			}

			deferredShaderConf.writeOptions(printwriter);

			printwriter.close();
			return bao.toByteArray();
		} catch (Exception exception) {
			logger.error("Failed to save options");
			logger.error(exception);
			return null;
		}

	}

	public float getSoundLevel(SoundCategory parSoundCategory) {
		return this.mapSoundLevels.containsKey(parSoundCategory)
				? ((Float) this.mapSoundLevels.get(parSoundCategory)).floatValue()
				: 1.0F;
	}

	public void setSoundLevel(SoundCategory parSoundCategory, float parFloat1) {
		this.mc.getSoundHandler().setSoundLevel(parSoundCategory, parFloat1);
		this.mapSoundLevels.put(parSoundCategory, Float.valueOf(parFloat1));
	}

	/**+
	 * Send a client info packet with settings information to the
	 * server
	 */
	public void sendSettingsToServer() {
		if (this.mc.thePlayer != null) {
			int i = 0;

			for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
				i |= enumplayermodelparts.getPartMask();
			}

			this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language,
					Math.max(this.renderDistanceChunks, 3), this.chatVisibility, this.chatColours, i));
		}

	}

	public Set<EnumPlayerModelParts> getModelParts() {
		return ImmutableSet.copyOf(this.setModelParts);
	}

	public void setModelPartEnabled(EnumPlayerModelParts parEnumPlayerModelParts, boolean parFlag) {
		if (parFlag) {
			this.setModelParts.add(parEnumPlayerModelParts);
		} else {
			this.setModelParts.remove(parEnumPlayerModelParts);
		}

		this.sendSettingsToServer();
	}

	public void switchModelPartEnabled(EnumPlayerModelParts parEnumPlayerModelParts) {
		if (!this.getModelParts().contains(parEnumPlayerModelParts)) {
			this.setModelParts.add(parEnumPlayerModelParts);
		} else {
			this.setModelParts.remove(parEnumPlayerModelParts);
		}

		this.sendSettingsToServer();
	}

	public int func_181147_e() {
		return this.renderDistanceChunks >= 4 ? this.clouds : 0;
	}

	public boolean func_181148_f() {
		return this.field_181150_U;
	}

	private String toJSONArray(List<String> e) {
		JSONArray arr = new JSONArray();
		for (String s : e) {
			arr.put(s);
		}
		return arr.toString();
	}

	public int checkBadVideoSettings() {
		return hideVideoSettingsWarning ? 0
				: ((renderDistanceChunks > 6 ? GuiScreenVideoSettingsWarning.WARNING_RENDER_DISTANCE : 0)
						| (!enableVsync ? GuiScreenVideoSettingsWarning.WARNING_VSYNC : 0)
						| (limitFramerate < 30 ? GuiScreenVideoSettingsWarning.WARNING_FRAME_LIMIT : 0));
	}

	public void fixBadVideoSettings() {
		if (renderDistanceChunks > 6)
			renderDistanceChunks = 4;
		if (!enableVsync)
			enableVsync = true;
		if (limitFramerate < 30)
			limitFramerate = 260;
	}

	public static enum Options {
		INVERT_MOUSE("options.invertMouse", false, true), SENSITIVITY("options.sensitivity", true, false),
		FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F), GAMMA("options.gamma", true, false),
		SATURATION("options.saturation", true, false),
		RENDER_DISTANCE("options.renderDistance", true, false, 1.0F, 18.0F, 1.0F),
		VIEW_BOBBING("options.viewBobbing", false, true), ANAGLYPH("options.anaglyph", false, true),
		FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0F, 260.0F, 10.0F),
		FBO_ENABLE("options.fboEnable", false, true), RENDER_CLOUDS("options.renderClouds", false, false),
		GRAPHICS("options.graphics", false, false), AMBIENT_OCCLUSION("options.ao", false, false),
		GUI_SCALE("options.guiScale", false, false), PARTICLES("options.particles", false, false),
		CHAT_VISIBILITY("options.chat.visibility", false, false), CHAT_COLOR("options.chat.color", false, true),
		CHAT_LINKS("options.chat.links", false, true), CHAT_OPACITY("options.chat.opacity", true, false),
		CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true), SNOOPER_ENABLED("options.snooper", false, true),
		CHAT_SCALE("options.chat.scale", true, false), CHAT_WIDTH("options.chat.width", true, false),
		CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
		CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
		MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
		FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
		STREAM_BYTES_PER_PIXEL("options.stream.bytesPerPixel", true, false),
		STREAM_VOLUME_MIC("options.stream.micVolumne", true, false),
		STREAM_VOLUME_SYSTEM("options.stream.systemVolume", true, false),
		STREAM_KBPS("options.stream.kbps", true, false), STREAM_FPS("options.stream.fps", true, false),
		STREAM_COMPRESSION("options.stream.compression", false, false),
		STREAM_SEND_METADATA("options.stream.sendMetadata", false, true),
		STREAM_CHAT_ENABLED("options.stream.chat.enabled", false, false),
		STREAM_CHAT_USER_FILTER("options.stream.chat.userFilter", false, false),
		STREAM_MIC_TOGGLE_BEHAVIOR("options.stream.micToggleBehavior", false, false),
		BLOCK_ALTERNATIVES("options.blockAlternatives", false, true),
		REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
		ENTITY_SHADOWS("options.entityShadows", false, true), HUD_FPS("options.hud.fps", false, true),
		HUD_COORDS("options.hud.coords", false, true), HUD_STATS("options.hud.stats", false, true),
		HUD_WORLD("options.hud.world", false, true), HUD_PLAYER("options.hud.player", false, true),
		HUD_24H("options.hud.24h", false, true), CHUNK_FIX("options.chunkFix", false, true),
		FOG("options.fog", false, true), FXAA("options.fxaa", false, false),
		FULLSCREEN("options.fullscreen", false, true),
		FNAW_SKINS("options.skinCustomisation.enableFNAWSkins", false, true),
		EAGLER_VSYNC("options.vsync", false, true), EAGLER_DYNAMIC_LIGHTS("options.dynamicLights", false, true),
		EAGLER_PROFANITY_FILTER("options.profanityFilterButton", false, true),
		EAGLER_TOUCH_CONTROL_OPACITY("options.touchControlOpacity", true, false),
		OF_CONNECTED_TEXTURES("options.connectedTexturesOF", false, false),
		OF_BETTER_GRASS("options.betterGrassOF", false, false), OF_CUSTOM_SKIES("options.customSkiesOF", false, true),
		OF_SMART_LEAVES("options.smartLeavesOF", false, true), OF_CUSTOM_ITEMS("options.customItemsOF", false, true);

		private final boolean enumFloat;
		private final boolean enumBoolean;
		private final String enumString;
		private final float valueStep;
		private float valueMin;
		private float valueMax;

		public static GameSettings.Options getEnumOptions(int parInt1) {
			for (GameSettings.Options gamesettings$options : values()) {
				if (gamesettings$options.returnEnumOrdinal() == parInt1) {
					return gamesettings$options;
				}
			}

			return null;
		}

		private Options(String parString2, boolean parFlag, boolean parFlag2) {
			this(parString2, parFlag, parFlag2, 0.0F, 1.0F, 0.0F);
		}

		private Options(String parString2, boolean parFlag, boolean parFlag2, float parFloat1, float parFloat2,
				float parFloat3) {
			this.enumString = parString2;
			this.enumFloat = parFlag;
			this.enumBoolean = parFlag2;
			this.valueMin = parFloat1;
			this.valueMax = parFloat2;
			this.valueStep = parFloat3;
		}

		public boolean getEnumFloat() {
			return this.enumFloat;
		}

		public boolean getEnumBoolean() {
			return this.enumBoolean;
		}

		public int returnEnumOrdinal() {
			return this.ordinal();
		}

		public String getEnumString() {
			return this.enumString;
		}

		public float getValueMax() {
			return this.valueMax;
		}

		public void setValueMax(float parFloat1) {
			this.valueMax = parFloat1;
		}

		public float normalizeValue(float parFloat1) {
			return MathHelper.clamp_float(
					(this.snapToStepClamp(parFloat1) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
		}

		public float denormalizeValue(float parFloat1) {
			return this.snapToStepClamp(
					this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(parFloat1, 0.0F, 1.0F));
		}

		public float snapToStepClamp(float parFloat1) {
			parFloat1 = this.snapToStep(parFloat1);
			return MathHelper.clamp_float(parFloat1, this.valueMin, this.valueMax);
		}

		protected float snapToStep(float parFloat1) {
			if (this.valueStep > 0.0F) {
				parFloat1 = this.valueStep * (float) Math.round(parFloat1 / this.valueStep);
			}

			return parFloat1;
		}
	}
}