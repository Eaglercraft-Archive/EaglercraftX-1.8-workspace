package net.minecraft.client;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL._wglBindFramebuffer;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglerXBungeeVersion;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.futures.Executors;
import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
import net.lax1dude.eaglercraft.v1_8.futures.ListenableFutureTask;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFontRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglerMeshLoader;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BlockVertexIDs;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DebugFramebufferView;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShaderPackInfoReloadListener;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EmissiveItems;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.MetalsLUT;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.PBRTextureMapUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.TemperaturesLUT;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;
import net.lax1dude.eaglercraft.v1_8.profile.SkinPreviewRenderer;
import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;
import net.lax1dude.eaglercraft.v1_8.sp.IntegratedServerState;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.SkullCommand;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenDemoIntegratedServerStartup;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenSingleplayerConnecting;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.lax1dude.eaglercraft.v1_8.update.RelayUpdateChecker;
import net.lax1dude.eaglercraft.v1_8.voice.GuiVoiceOverlay;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.Profiler;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.StringTranslate;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class Minecraft implements IThreadListener {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
	public static final boolean isRunningOnMac = false;
	private ServerData currentServerData;
	private TextureManager renderEngine;
	private static Minecraft theMinecraft;
	public PlayerControllerMP playerController;
	private boolean fullscreen;
	private boolean enableGLErrorChecking = true;
	private boolean hasCrashed;
	private CrashReport crashReporter;
	public int displayWidth;
	public int displayHeight;
	private boolean field_181541_X = false;
	private Timer timer = new Timer(20.0F);
	public WorldClient theWorld;
	public RenderGlobal renderGlobal;
	private RenderManager renderManager;
	private RenderItem renderItem;
	private ItemRenderer itemRenderer;
	public EntityPlayerSP thePlayer;
	private Entity renderViewEntity;
	public Entity pointedEntity;
	public EffectRenderer effectRenderer;
	private final Session session;
	private boolean isGamePaused;
	private boolean wasPaused;
	public FontRenderer fontRendererObj;
	public FontRenderer standardGalacticFontRenderer;
	public GuiScreen currentScreen;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	private int leftClickCounter;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public GuiAchievement guiAchievement;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld;
	public MovingObjectPosition objectMouseOver;
	public GameSettings gameSettings;
	public MouseHelper mouseHelper;
	private final String launchedVersion;
	private static int debugFPS;
	private int rightClickDelayTimer;
	private String serverName;
	private int serverPort;
	public boolean inGameHasFocus;
	long systemTime = getSystemTime();
	private int joinPlayerCounter;
	public final FrameTimer field_181542_y = new FrameTimer();
	long field_181543_z = System.nanoTime();
	private final boolean jvm64bit;
	private EaglercraftNetworkManager myNetworkManager;
	private boolean integratedServerIsRunning;
	/**+
	 * The profiler instance
	 */
	public final Profiler mcProfiler = new Profiler();
	/**+
	 * Keeps track of how long the debug crash keycombo (F3+C) has
	 * been pressed for, in order to crash after 10 seconds.
	 */
	private long debugCrashKeyPressTime = -1L;
	private IReloadableResourceManager mcResourceManager;
	private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
	private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
	private final DefaultResourcePack mcDefaultResourcePack;
	private ResourcePackRepository mcResourcePackRepository;
	private LanguageManager mcLanguageManager;
	private TextureMap textureMapBlocks;
	private SoundHandler mcSoundHandler;
	private MusicTicker mcMusicTicker;
	private ResourceLocation mojangLogo;
	private final List<FutureTask<?>> scheduledTasks = new LinkedList();
	private long field_175615_aJ = 0L;
	private final Thread mcThread = Thread.currentThread();
	private ModelManager modelManager;
	private BlockRendererDispatcher blockRenderDispatcher;
	/**+
	 * Set to true to keep the game loop running. Set to false by
	 * shutdown() to allow the game loop to exit cleanly.
	 */
	volatile boolean running = true;
	/**+
	 * String that shows the debug information
	 */
	public String debug = "";
	public boolean field_175613_B = false;
	public boolean field_175614_C = false;
	public boolean field_175611_D = false;
	public boolean renderChunksMany = true;
	long debugUpdateTime = getSystemTime();
	int fpsCounter;
	long prevFrameTime = -1L;
	/**+
	 * Profiler currently displayed in the debug screen pie chart
	 */
	private String debugProfilerName = "root";
	public int joinWorldTickCounter = 0;
	private int dontPauseTimer = 0;
	public int bungeeOutdatedMsgTimer = 0;
	private boolean isLANOpen = false;

	public SkullCommand eagskullCommand;

	public GuiVoiceOverlay voiceOverlay;

	public float startZoomValue = 18.0f;
	public float adjustedZoomValue = 18.0f;
	public boolean isZoomKey = false;

	public Minecraft(GameConfiguration gameConfig) {
		theMinecraft = this;
		StringTranslate.initClient();
		this.launchedVersion = gameConfig.gameInfo.version;
		this.mcDefaultResourcePack = new DefaultResourcePack();
		this.session = gameConfig.userInfo.session;
		logger.info("Setting user: " + this.session.getProfile().getName());
		this.displayWidth = gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1;
		this.displayHeight = gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1;
		this.tempDisplayWidth = gameConfig.displayInfo.width;
		this.tempDisplayHeight = gameConfig.displayInfo.height;
		this.fullscreen = gameConfig.displayInfo.fullscreen;
		this.jvm64bit = isJvm64bit();
		String serverToJoin = EagRuntime.getConfiguration().getServerToJoin();
		if (serverToJoin != null) {
			ServerAddress addr = AddressResolver.resolveAddressFromURI(serverToJoin);
			this.serverName = addr.getIP();
			this.serverPort = addr.getPort();
		}

		Bootstrap.register();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
			crashreport.makeCategory("Initialization");
			this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
			return;
		}

		try {
			while (true) {
				if (!this.running) {
					break;
				}

				if (!this.hasCrashed || this.crashReporter == null) {
					this.runGameLoop();
					continue;
				}

				this.displayCrashReport(this.crashReporter);
			}
		} catch (MinecraftError var12) {
			// ??
		} catch (ReportedException reportedexception) {
			this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
			logger.fatal("Reported exception thrown!", reportedexception);
			this.displayCrashReport(reportedexception.getCrashReport());
		} catch (Throwable throwable1) {
			CrashReport crashreport1 = this
					.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
			logger.fatal("Unreported exception thrown!", throwable1);
			this.displayCrashReport(crashreport1);
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	/**+
	 * Starts the game: initializes the canvas, the title, the
	 * settings, etcetera.
	 */
	private void startGame() throws IOException {
		this.gameSettings = new GameSettings(this);
		this.defaultResourcePacks.add(this.mcDefaultResourcePack);
		if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
			this.displayWidth = this.gameSettings.overrideWidth;
			this.displayHeight = this.gameSettings.overrideHeight;
		}

		logger.info("EagRuntime Version: " + EagRuntime.getVersion());
		this.createDisplay();
		this.registerMetadataSerializers();
		EaglerFolderResourcePack.deleteOldResourcePacks(EaglerFolderResourcePack.SERVER_RESOURCE_PACKS, 604800000L);
		this.mcResourcePackRepository = new ResourcePackRepository(this.mcDefaultResourcePack, this.metadataSerializer_,
				this.gameSettings);
		this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
		this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
		this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
		this.refreshResources();
		this.renderEngine = new TextureManager(this.mcResourceManager);
		this.mcResourceManager.registerReloadListener(this.renderEngine);
		this.drawSplashScreen(this.renderEngine);
		this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
		this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
		this.mcMusicTicker = new MusicTicker(this);
		this.fontRendererObj = new EaglerFontRenderer(this.gameSettings,
				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
		if (this.gameSettings.language != null) {
			this.fontRendererObj.setUnicodeFlag(this.isUnicode());
			this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
		}

		this.standardGalacticFontRenderer = new EaglerFontRenderer(this.gameSettings,
				new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
		this.mcResourceManager.registerReloadListener(this.fontRendererObj);
		this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
		this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
		this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
		this.mcResourceManager.registerReloadListener(new ShaderPackInfoReloadListener());
		this.mcResourceManager.registerReloadListener(PBRTextureMapUtils.blockMaterialConstants);
		this.mcResourceManager.registerReloadListener(new TemperaturesLUT());
		this.mcResourceManager.registerReloadListener(new MetalsLUT());
		this.mcResourceManager.registerReloadListener(new EmissiveItems());
		this.mcResourceManager.registerReloadListener(new BlockVertexIDs());
		this.mcResourceManager.registerReloadListener(new EaglerMeshLoader());
		AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {
			public String formatString(String parString1) {
				try {
					return HString.format(parString1, new Object[] { GameSettings
							.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode()) });
				} catch (Exception exception) {
					return "Error: " + exception.getLocalizedMessage();
				}
			}
		});
		this.mouseHelper = new MouseHelper();
		this.checkGLError("Pre startup");
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL_SMOOTH);
		GlStateManager.clearDepth(1.0f);
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		GlStateManager.cullFace(GL_BACK);
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL_MODELVIEW);
		this.checkGLError("Startup");
		this.textureMapBlocks = new TextureMap("textures");
		this.textureMapBlocks.setEnablePBREagler(gameSettings.shaders);
		this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
		this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
		this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
		this.modelManager = new ModelManager(this.textureMapBlocks);
		this.mcResourceManager.registerReloadListener(this.modelManager);
		this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
		this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
		this.itemRenderer = new ItemRenderer(this);
		this.mcResourceManager.registerReloadListener(this.renderItem);
		this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
		this.mcResourceManager.registerReloadListener(this.entityRenderer);
		this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(),
				this.gameSettings);
		this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
		this.renderGlobal = new RenderGlobal(this);
		this.mcResourceManager.registerReloadListener(this.renderGlobal);
		this.guiAchievement = new GuiAchievement(this);
		GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
		SkinPreviewRenderer.initialize();
		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		this.eagskullCommand = new SkullCommand(this);
		this.voiceOverlay = new GuiVoiceOverlay(this);
		ScaledResolution voiceRes = new ScaledResolution(this);
		this.voiceOverlay.setResolution(voiceRes.getScaledWidth(), voiceRes.getScaledHeight());

		ServerList.initServerList(this);
		EaglerProfile.read();

		GuiScreen mainMenu = new GuiMainMenu();
		if (isDemo()) {
			mainMenu = new GuiScreenDemoIntegratedServerStartup(mainMenu);
		}
		if (this.serverName != null) {
			mainMenu = new GuiConnecting(mainMenu, this, this.serverName, this.serverPort);
		}

		this.displayGuiScreen(new GuiScreenEditProfile(mainMenu));

		this.renderEngine.deleteTexture(this.mojangLogo);
		this.mojangLogo = null;
		this.loadingScreen = new LoadingScreenRenderer(this);
	}

	private void registerMetadataSerializers() {
		this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(),
				TextureMetadataSection.class);
		this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(),
				FontMetadataSection.class);
		this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(),
				AnimationMetadataSection.class);
		this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(),
				PackMetadataSection.class);
		this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(),
				LanguageMetadataSection.class);
	}

	private void initStream() {
		throw new UnsupportedOperationException("wtf u trying to twitch stream in a browser game?");
	}

	private void createDisplay() {
		Display.create();
		Display.setTitle("Eaglercraft 1.8.8");
	}

	private static boolean isJvm64bit() {
		return true;
	}

	public String getVersion() {
		return this.launchedVersion;
	}

	public void crashed(CrashReport crash) {
		this.hasCrashed = true;
		this.crashReporter = crash;
	}

	/**+
	 * Wrapper around displayCrashReportInternal
	 */
	public void displayCrashReport(CrashReport crashReportIn) {
		String report = crashReportIn.getCompleteReport();
		Bootstrap.printToSYSOUT(report);
		PlatformRuntime.writeCrashReport(report);
		if (PlatformRuntime.getPlatformType() == EnumPlatformType.JAVASCRIPT) {
			System.err.println(
					"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.err.println("NATIVE BROWSER EXCEPTION:");
			if (!PlatformRuntime.printJSExceptionIfBrowser(crashReportIn.getCrashCause())) {
				System.err.println("<undefined>");
			}
			System.err.println(
					"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		}
	}

	public boolean isUnicode() {
		return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
	}

	public void refreshResources() {
		GlStateManager.recompileShaders();

		ArrayList arraylist = Lists.newArrayList(this.defaultResourcePacks);

		for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository
				.getRepositoryEntries()) {
			arraylist.add(resourcepackrepository$entry.getResourcePack());
		}

		if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
			arraylist.add(this.mcResourcePackRepository.getResourcePackInstance());
		}

		try {
			this.mcResourceManager.reloadResources(arraylist);
		} catch (RuntimeException runtimeexception) {
			logger.info("Caught error stitching, removing all assigned resourcepacks");
			logger.info(runtimeexception);
			arraylist.clear();
			arraylist.addAll(this.defaultResourcePacks);
			this.mcResourcePackRepository.setRepositories(Collections.emptyList());
			this.mcResourceManager.reloadResources(arraylist);
			this.gameSettings.resourcePacks.clear();
			this.gameSettings.field_183018_l.clear();
			this.gameSettings.saveOptions();
		}

		ShaderSource.clearCache();
		GuiMainMenu.doResourceReloadHack();

		this.mcLanguageManager.parseLanguageMetadata(arraylist);
		if (this.renderGlobal != null) {
			this.renderGlobal.loadRenderers();
		}

	}

	private void updateDisplayMode() {
		this.displayWidth = Display.getWidth();
		this.displayHeight = Display.getHeight();
	}

	private void drawSplashScreen(TextureManager textureManagerInstance) {
		Display.update();
		updateDisplayMode();
		GlStateManager.viewport(0, 0, displayWidth, displayHeight);
		ScaledResolution scaledresolution = new ScaledResolution(this);
		int i = scaledresolution.getScaleFactor();
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, (double) scaledresolution.getScaledWidth(),
				(double) scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		InputStream inputstream = null;

		try {
			inputstream = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
			this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo",
					new DynamicTexture(ImageData.loadImageFile(inputstream)));
			textureManagerInstance.bindTexture(this.mojangLogo);
		} catch (IOException ioexception) {
			logger.error("Unable to load logo: " + locationMojangPng, ioexception);
		} finally {
			IOUtils.closeQuietly(inputstream);
		}

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255)
				.endVertex();
		worldrenderer.pos((double) this.displayWidth, (double) this.displayHeight, 0.0D).tex(0.0D, 0.0D)
				.color(255, 255, 255, 255).endVertex();
		worldrenderer.pos((double) this.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		tessellator.draw();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		short short1 = 256;
		short short2 = 256;
		this.func_181536_a((scaledresolution.getScaledWidth() - short1) / 2,
				(scaledresolution.getScaledHeight() - short2) / 2, 0, 0, short1, short2, 255, 255, 255, 255);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		this.updateDisplay();
	}

	public void func_181536_a(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5, int parInt6, int parInt7,
			int parInt8, int parInt9, int parInt10) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos((double) parInt1, (double) (parInt2 + parInt6), 0.0D)
				.tex((double) ((float) parInt3 * f), (double) ((float) (parInt4 + parInt6) * f1))
				.color(parInt7, parInt8, parInt9, parInt10).endVertex();
		worldrenderer.pos((double) (parInt1 + parInt5), (double) (parInt2 + parInt6), 0.0D)
				.tex((double) ((float) (parInt3 + parInt5) * f), (double) ((float) (parInt4 + parInt6) * f1))
				.color(parInt7, parInt8, parInt9, parInt10).endVertex();
		worldrenderer.pos((double) (parInt1 + parInt5), (double) parInt2, 0.0D)
				.tex((double) ((float) (parInt3 + parInt5) * f), (double) ((float) parInt4 * f1))
				.color(parInt7, parInt8, parInt9, parInt10).endVertex();
		worldrenderer.pos((double) parInt1, (double) parInt2, 0.0D)
				.tex((double) ((float) parInt3 * f), (double) ((float) parInt4 * f1))
				.color(parInt7, parInt8, parInt9, parInt10).endVertex();
		Tessellator.getInstance().draw();
	}

	/**+
	 * Sets the argument GuiScreen as the main (topmost visible)
	 * screen.
	 */
	public void displayGuiScreen(GuiScreen guiScreenIn) {
		if (this.currentScreen != null) {
			this.currentScreen.onGuiClosed();
		}

		if (guiScreenIn == null && this.theWorld == null) {
			guiScreenIn = new GuiMainMenu();
		} else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F) {
			guiScreenIn = new GuiGameOver();
		}

		if (guiScreenIn instanceof GuiMainMenu) {
			this.gameSettings.showDebugInfo = false;
			this.ingameGUI.getChatGUI().clearChatMessages();
		}

		this.currentScreen = (GuiScreen) guiScreenIn;
		if (guiScreenIn != null) {
			this.setIngameNotInFocus();
			ScaledResolution scaledresolution = new ScaledResolution(this);
			int i = scaledresolution.getScaledWidth();
			int j = scaledresolution.getScaledHeight();
			((GuiScreen) guiScreenIn).setWorldAndResolution(this, i, j);
			this.skipRenderWorld = false;
		} else {
			this.mcSoundHandler.resumeSounds();
			this.setIngameFocus();
		}

	}

	public void shutdownIntegratedServer(GuiScreen cont) {
		if (SingleplayerServerController.shutdownEaglercraftServer()
				|| SingleplayerServerController.getStatusState() == IntegratedServerState.WORLD_UNLOADING) {
			displayGuiScreen(new GuiScreenIntegratedServerBusy(cont, "singleplayer.busy.stoppingIntegratedServer",
					"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
		} else {
			displayGuiScreen(cont);
		}
	}

	/**+
	 * Checks for an OpenGL error. If there is one, prints the error
	 * ID and error string.
	 */
	public void checkGLError(String message) {
		if (this.enableGLErrorChecking) {
			int i = EaglercraftGPU.glGetError();
			if (i != 0) {
				String s = EaglercraftGPU.gluErrorString(i);
				logger.error("########## GL ERROR ##########");
				logger.error("@ " + message);
				logger.error(i + ": " + s);
			}

		}
	}

	/**+
	 * Shuts down the minecraft applet by stopping the resource
	 * downloads, and clearing up GL stuff; called when the
	 * application (or web page) is exited.
	 */
	public void shutdownMinecraftApplet() {
		try {
			logger.info("Stopping!");

			try {
				this.loadWorld((WorldClient) null);
			} catch (Throwable var5) {
				;
			}

			this.mcSoundHandler.unloadSounds();
			if (SingleplayerServerController.isWorldRunning()) {
				SingleplayerServerController.shutdownEaglercraftServer();
				while (SingleplayerServerController.getStatusState() == IntegratedServerState.WORLD_UNLOADING) {
					EagUtils.sleep(50l);
					SingleplayerServerController.runTick();
				}
			}
			if (SingleplayerServerController.isIntegratedServerWorkerAlive()
					&& SingleplayerServerController.canKillWorker()) {
				SingleplayerServerController.killWorker();
				EagUtils.sleep(50l);
			}
		} finally {
			EagRuntime.destroy();
			if (!this.hasCrashed) {
				EagRuntime.exit();
			}

		}
	}

	/**+
	 * Called repeatedly from run()
	 */
	private void runGameLoop() throws IOException {
		long i = System.nanoTime();
		this.mcProfiler.startSection("root");
		if (Display.isCloseRequested()) {
			this.shutdown();
		}

		if (this.isGamePaused && this.theWorld != null) {
			float f = this.timer.renderPartialTicks;
			this.timer.updateTimer();
			this.timer.renderPartialTicks = f;
		} else {
			this.timer.updateTimer();
		}

		this.mcProfiler.startSection("scheduledExecutables");
		synchronized (this.scheduledTasks) {
			while (!this.scheduledTasks.isEmpty()) {
				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);
			}
		}

		this.mcProfiler.endSection();
		long l = System.nanoTime();
		this.mcProfiler.startSection("tick");

		if (this.timer.elapsedTicks > 1) {
			long watchdog = System.currentTimeMillis();
			for (int j = 0; j < this.timer.elapsedTicks; ++j) {
				this.runTick();
				long millis = System.currentTimeMillis();
				if (millis - watchdog > 50l) {
					watchdog = millis;
					EagUtils.sleep(0l);
				}
			}
		} else if (this.timer.elapsedTicks == 1) {
			this.runTick();
		}

		this.mcProfiler.endStartSection("preRenderErrors");
		long i1 = System.nanoTime() - l;
		this.checkGLError("Pre render");
		this.mcProfiler.endStartSection("sound");
		this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
		this.mcProfiler.endSection();
		this.mcProfiler.startSection("render");

		if (!Display.contextLost()) {
			this.mcProfiler.startSection("EaglercraftGPU_optimize");
			EaglercraftGPU.optimize();
			this.mcProfiler.endSection();
			_wglBindFramebuffer(0x8D40, null);
			GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
			GlStateManager.pushMatrix();
			GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			this.mcProfiler.startSection("display");
			GlStateManager.enableTexture2D();
			if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
				this.gameSettings.thirdPersonView = 0;
			}

			this.mcProfiler.endSection();
			if (!this.skipRenderWorld) {
				this.mcProfiler.endStartSection("gameRenderer");
				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
				this.mcProfiler.endSection();
			}

			this.mcProfiler.endSection();
			if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart
					&& !this.gameSettings.hideGUI) {
				if (!this.mcProfiler.profilingEnabled) {
					this.mcProfiler.clearProfiling();
				}

				this.mcProfiler.profilingEnabled = true;
				this.displayDebugInfo(i1);
			} else {
				this.mcProfiler.profilingEnabled = false;
				this.prevFrameTime = System.nanoTime();
			}

			this.guiAchievement.updateAchievementWindow();
			GlStateManager.popMatrix();
		}

		this.mcProfiler.startSection("root");
		this.updateDisplay();
		this.checkGLError("Post render");

		++this.fpsCounter;
		long k = System.nanoTime();
		this.field_181542_y.func_181747_a(k - this.field_181543_z);
		this.field_181543_z = k;

		while (getSystemTime() >= this.debugUpdateTime + 1000L) {
			debugFPS = this.fpsCounter;
			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",
					new Object[] { Integer.valueOf(debugFPS), Integer.valueOf(RenderChunk.renderChunksUpdated),
							RenderChunk.renderChunksUpdated != 1 ? "s" : "",
							(float) this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT
									.getValueMax() ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate),
							this.gameSettings.enableVsync ? " vsync" : "",
							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });
			RenderChunk.renderChunksUpdated = 0;
			this.debugUpdateTime += 1000L;
			this.fpsCounter = 0;
		}

		if (this.isFramerateLimitBelowMax()) {
			this.mcProfiler.startSection("fpslimit_wait");
			Display.sync(this.getLimitFramerate());
			this.mcProfiler.endSection();
		}

		Mouse.tickCursorShape();
		this.mcProfiler.endSection();
	}

	public void updateDisplay() {
		this.mcProfiler.startSection("display_update");
		if (Display.isVSyncSupported()) {
			Display.setVSync(this.gameSettings.enableVsync);
		} else {
			this.gameSettings.enableVsync = false;
		}
		Display.update();
		this.mcProfiler.endSection();
		this.checkWindowResize();
	}

	protected void checkWindowResize() {
		if (!this.fullscreen && Display.wasResized()) {
			int i = this.displayWidth;
			int j = this.displayHeight;
			this.displayWidth = Display.getWidth();
			this.displayHeight = Display.getHeight();
			if (this.displayWidth != i || this.displayHeight != j) {
				if (this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if (this.displayHeight <= 0) {
					this.displayHeight = 1;
				}

				this.resize(this.displayWidth, this.displayHeight);
			}
		}

	}

	public int getLimitFramerate() {
		return this.theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
	}

	public boolean isFramerateLimitBelowMax() {
		return (float) this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
	}

	/**+
	 * Update debugProfilerName in response to number keys in debug
	 * screen
	 */
	private void updateDebugProfilerName(int keyCount) {
		List list = this.mcProfiler.getProfilingData(this.debugProfilerName);
		if (list != null && !list.isEmpty()) {
			Profiler.Result profiler$result = (Profiler.Result) list.remove(0);
			if (keyCount == 0) {
				if (profiler$result.field_76331_c.length() > 0) {
					int i = this.debugProfilerName.lastIndexOf(".");
					if (i >= 0) {
						this.debugProfilerName = this.debugProfilerName.substring(0, i);
					}
				}
			} else {
				--keyCount;
				if (keyCount < list.size()
						&& !((Profiler.Result) list.get(keyCount)).field_76331_c.equals("unspecified")) {
					if (this.debugProfilerName.length() > 0) {
						this.debugProfilerName = this.debugProfilerName + ".";
					}

					this.debugProfilerName = this.debugProfilerName
							+ ((Profiler.Result) list.get(keyCount)).field_76331_c;
				}
			}

		}
	}

	/**+
	 * Parameter appears to be unused
	 */
	private void displayDebugInfo(long elapsedTicksTime) {
		if (this.mcProfiler.profilingEnabled) {
			List list = this.mcProfiler.getProfilingData(this.debugProfilerName);
			Profiler.Result profiler$result = (Profiler.Result) list.remove(0);
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.enableColorMaterial();
			GlStateManager.loadIdentity();
			GlStateManager.ortho(0.0D, (double) this.displayWidth, (double) this.displayHeight, 0.0D, 1000.0D, 3000.0D);
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, 0.0F, -2000.0F);
			EaglercraftGPU.glLineWidth(1.0F);
			GlStateManager.disableTexture2D();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			short short1 = 160;
			int i = this.displayWidth - short1 - 10;
			int j = this.displayHeight - short1 * 2;
			GlStateManager.enableBlend();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos((double) ((float) i - (float) short1 * 1.1F),
					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();
			worldrenderer.pos((double) ((float) i - (float) short1 * 1.1F), (double) (j + short1 * 2), 0.0D)
					.color(0, 0, 0, 100).endVertex();
			worldrenderer.pos((double) ((float) i + (float) short1 * 1.1F), (double) (j + short1 * 2), 0.0D)
					.color(0, 0, 0, 100).endVertex();
			worldrenderer.pos((double) ((float) i + (float) short1 * 1.1F),
					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();
			tessellator.draw();
			GlStateManager.disableBlend();
			double d0 = 0.0D;

			for (int k = 0; k < list.size(); ++k) {
				Profiler.Result profiler$result1 = (Profiler.Result) list.get(k);
				int l = MathHelper.floor_double(profiler$result1.field_76332_a / 4.0D) + 1;
				worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				int i1 = profiler$result1.func_76329_a();
				int j1 = i1 >> 16 & 255;
				int k1 = i1 >> 8 & 255;
				int l1 = i1 & 255;
				worldrenderer.pos((double) i, (double) j, 0.0D).color(j1, k1, l1, 255).endVertex();

				for (int i2 = l; i2 >= 0; --i2) {
					float f = (float) ((d0 + profiler$result1.field_76332_a * (double) i2 / (double) l)
							* 3.1415927410125732D * 2.0D / 100.0D);
					float f1 = MathHelper.sin(f) * (float) short1;
					float f2 = MathHelper.cos(f) * (float) short1 * 0.5F;
					worldrenderer.pos((double) ((float) i + f1), (double) ((float) j - f2), 0.0D).color(j1, k1, l1, 255)
							.endVertex();
				}

				tessellator.draw();
				worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);

				for (int l2 = l; l2 >= 0; --l2) {
					float f3 = (float) ((d0 + profiler$result1.field_76332_a * (double) l2 / (double) l)
							* 3.1415927410125732D * 2.0D / 100.0D);
					float f4 = MathHelper.sin(f3) * (float) short1;
					float f5 = MathHelper.cos(f3) * (float) short1 * 0.5F;
					worldrenderer.pos((double) ((float) i + f4), (double) ((float) j - f5), 0.0D)
							.color(j1 >> 1, k1 >> 1, l1 >> 1, 255).endVertex();
					worldrenderer.pos((double) ((float) i + f4), (double) ((float) j - f5 + 10.0F), 0.0D)
							.color(j1 >> 1, k1 >> 1, l1 >> 1, 255).endVertex();
				}

				tessellator.draw();
				d0 += profiler$result1.field_76332_a;
			}

			DecimalFormat decimalformat = new DecimalFormat("##0.00");
			GlStateManager.enableTexture2D();
			String s = "";
			if (!profiler$result.field_76331_c.equals("unspecified")) {
				s = s + "[0] ";
			}

			if (profiler$result.field_76331_c.length() == 0) {
				s = s + "ROOT ";
			} else {
				s = s + profiler$result.field_76331_c + " ";
			}

			int k2 = 16777215;
			this.fontRendererObj.drawStringWithShadow(s, (float) (i - short1), (float) (j - short1 / 2 - 16), k2);
			this.fontRendererObj.drawStringWithShadow(s = decimalformat.format(profiler$result.field_76330_b) + "%",
					(float) (i + short1 - this.fontRendererObj.getStringWidth(s)), (float) (j - short1 / 2 - 16), k2);

			for (int j2 = 0; j2 < list.size(); ++j2) {
				Profiler.Result profiler$result2 = (Profiler.Result) list.get(j2);
				String s1 = "";
				if (profiler$result2.field_76331_c.equals("unspecified")) {
					s1 = s1 + "[?] ";
				} else {
					s1 = s1 + "[" + (j2 + 1) + "] ";
				}

				s1 = s1 + profiler$result2.field_76331_c;
				this.fontRendererObj.drawStringWithShadow(s1, (float) (i - short1),
						(float) (j + short1 / 2 + j2 * 8 + 20), profiler$result2.func_76329_a());
				this.fontRendererObj.drawStringWithShadow(
						s1 = decimalformat.format(profiler$result2.field_76332_a) + "%",
						(float) (i + short1 - 50 - this.fontRendererObj.getStringWidth(s1)),
						(float) (j + short1 / 2 + j2 * 8 + 20), profiler$result2.func_76329_a());
				this.fontRendererObj.drawStringWithShadow(
						s1 = decimalformat.format(profiler$result2.field_76330_b) + "%",
						(float) (i + short1 - this.fontRendererObj.getStringWidth(s1)),
						(float) (j + short1 / 2 + j2 * 8 + 20), profiler$result2.func_76329_a());
			}

		}
	}

	/**+
	 * Called when the window is closing. Sets 'running' to false
	 * which allows the game loop to exit cleanly.
	 */
	public void shutdown() {
		this.running = false;
	}

	/**+
	 * Will set the focus to ingame if the Minecraft window is the
	 * active with focus. Also clears any GUI screen currently
	 * displayed
	 */
	public void setIngameFocus() {
		if (Display.isActive()) {
			if (!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen) null);
				this.leftClickCounter = 10000;
			}
		}
	}

	/**+
	 * Resets the player keystate, disables the ingame focus, and
	 * ungrabs the mouse cursor.
	 */
	public void setIngameNotInFocus() {
		if (this.inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	/**+
	 * Displays the ingame menu
	 */
	public void displayInGameMenu() {
		if (this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(boolean leftClick) {
		if (!leftClick) {
			this.leftClickCounter = 0;
		}

		if (this.leftClickCounter <= 0 && !this.thePlayer.isUsingItem()) {
			if (leftClick && this.objectMouseOver != null
					&& this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos blockpos = this.objectMouseOver.getBlockPos();
				if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air
						&& this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
					this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
					this.thePlayer.swingItem();
				}

			} else {
				this.playerController.resetBlockRemoving();
			}
		}
	}

	private void clickMouse() {
		if (this.leftClickCounter <= 0) {
			this.thePlayer.swingItem();
			if (this.objectMouseOver == null) {
				logger.error("Null returned as \'hitResult\', this shouldn\'t happen!");
				if (this.playerController.isNotCreative()) {
					this.leftClickCounter = 10;
				}

			} else {
				switch (this.objectMouseOver.typeOfHit) {
				case ENTITY:
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
					break;
				case BLOCK:
					BlockPos blockpos = this.objectMouseOver.getBlockPos();
					if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
						this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
						break;
					}
				case MISS:
				default:
					if (this.playerController.isNotCreative()) {
						this.leftClickCounter = 10;
					}
				}

			}
		}
	}

	/**+
	 * Called when user clicked he's mouse right button (place)
	 */
	private void rightClickMouse() {
		if (!this.playerController.func_181040_m()) {
			this.rightClickDelayTimer = 4;
			boolean flag = true;
			ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();
			if (this.objectMouseOver == null) {
				logger.warn("Null returned as \'hitResult\', this shouldn\'t happen!");
			} else {
				switch (this.objectMouseOver.typeOfHit) {
				case ENTITY:
					if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit,
							this.objectMouseOver)) {
						flag = false;
					} else if (this.playerController.interactWithEntitySendPacket(this.thePlayer,
							this.objectMouseOver.entityHit)) {
						flag = false;
					}
					break;
				case BLOCK:
					BlockPos blockpos = this.objectMouseOver.getBlockPos();
					if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
						int i = itemstack != null ? itemstack.stackSize : 0;
						if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos,
								this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
							flag = false;
							this.thePlayer.swingItem();
						}

						if (itemstack == null) {
							return;
						}

						if (itemstack.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
						} else if (itemstack.stackSize != i || this.playerController.isInCreativeMode()) {
							this.entityRenderer.itemRenderer.resetEquippedProgress();
						}
					}
				}
			}

			if (flag) {
				ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();
				if (itemstack1 != null
						&& this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1)) {
					this.entityRenderer.itemRenderer.resetEquippedProgress2();
				}
			}

		}
	}

	/**+
	 * Toggles fullscreen mode.
	 */
	public void toggleFullscreen() {
		Display.toggleFullscreen();
	}

	/**+
	 * Called to resize the current screen.
	 */
	private void resize(int width, int height) {
		this.displayWidth = Math.max(1, width);
		this.displayHeight = Math.max(1, height);
		ScaledResolution scaledresolution = new ScaledResolution(this);
		if (this.currentScreen != null) {
			this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
		}

		this.loadingScreen = new LoadingScreenRenderer(this);

		this.voiceOverlay.setResolution(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
	}

	public MusicTicker func_181535_r() {
		return this.mcMusicTicker;
	}

	/**+
	 * Runs the current tick.
	 */
	public void runTick() throws IOException {
		if (this.rightClickDelayTimer > 0) {
			--this.rightClickDelayTimer;
		}

		RateLimitTracker.tick();

		boolean isHostingLAN = LANServerController.isHostingLAN();
		this.isGamePaused = !isHostingLAN && this.isSingleplayer() && this.theWorld != null && this.thePlayer != null
				&& this.currentScreen != null && this.currentScreen.doesGuiPauseGame();

		if (isLANOpen && !isHostingLAN) {
			ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("lanServer.relayDisconnected"));
		}
		isLANOpen = isHostingLAN;

		if (wasPaused != isGamePaused) {
			SingleplayerServerController.setPaused(this.isGamePaused);
			wasPaused = isGamePaused;
		}

		SingleplayerServerController.runTick();
		RelayUpdateChecker.runTick();

		this.mcProfiler.startSection("gui");
		if (!this.isGamePaused) {
			this.ingameGUI.updateTick();
		}

		this.mcProfiler.endStartSection("eaglerVoice");
		VoiceClientController.tickVoiceClient(this);

		this.mcProfiler.endSection();
		this.entityRenderer.getMouseOver(1.0F);
		this.mcProfiler.startSection("gameMode");
		if (!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}

		this.mcProfiler.endStartSection("textures");
		if (!this.isGamePaused) {
			this.renderEngine.tick();
			GlStateManager.viewport(0, 0, displayWidth, displayHeight); // to be safe
		}

		if (this.currentScreen == null && this.thePlayer != null) {
			if (this.thePlayer.getHealth() <= 0.0F) {
				this.displayGuiScreen((GuiScreen) null);
			} else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
				this.displayGuiScreen(new GuiSleepMP());
			}
			if (this.currentScreen == null && this.dontPauseTimer <= 0) {
				if (!Mouse.isMouseGrabbed()) {
					this.setIngameNotInFocus();
					this.displayInGameMenu();
				}
			}
		} else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP
				&& !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen) null);
		}

		if (this.currentScreen != null) {
			this.leftClickCounter = 10000;
			this.dontPauseTimer = 6;
		} else {
			if (this.dontPauseTimer > 0) {
				--this.dontPauseTimer;
			}
		}

		if (this.currentScreen != null) {
			try {
				this.currentScreen.handleInput();
			} catch (Throwable throwable1) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
				crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>() {
					public String call() throws Exception {
						return Minecraft.this.currentScreen.getClass().getName();
					}
				});
				throw new ReportedException(crashreport);
			}

			if (this.currentScreen != null) {
				try {
					this.currentScreen.updateScreen();
				} catch (Throwable throwable) {
					CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
					CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
					crashreportcategory1.addCrashSectionCallable("Screen name", new Callable<String>() {
						public String call() throws Exception {
							return Minecraft.this.currentScreen.getClass().getName();
						}
					});
					throw new ReportedException(crashreport1);
				}
			}
		}

		if (this.currentScreen == null || this.currentScreen.allowUserInput) {
			this.mcProfiler.endStartSection("mouse");

			while (Mouse.next()) {
				int i = Mouse.getEventButton();
				KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
				if (Mouse.getEventButtonState()) {
					if (this.thePlayer.isSpectator() && i == 2) {
						this.ingameGUI.getSpectatorGui().func_175261_b();
					} else {
						KeyBinding.onTick(i - 100);
					}
				}

				long i1 = getSystemTime() - this.systemTime;
				if (i1 <= 200L) {
					int j = Mouse.getEventDWheel();
					if (j != 0) {
						if (this.isZoomKey) {
							this.adjustedZoomValue = MathHelper.clamp_float(adjustedZoomValue - j * 4.0f, 5.0f, 32.0f);
						} else if (this.thePlayer.isSpectator()) {
							j = j < 0 ? -1 : 1;
							if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
								this.ingameGUI.getSpectatorGui().func_175259_b(-j);
							} else {
								float f = MathHelper.clamp_float(
										this.thePlayer.capabilities.getFlySpeed() + (float) j * 0.005F, 0.0F, 0.2F);
								this.thePlayer.capabilities.setFlySpeed(f);
							}
						} else {
							this.thePlayer.inventory.changeCurrentItem(j);
						}
					}

					if (this.currentScreen == null) {
						if ((!this.inGameHasFocus || !Mouse.isActuallyGrabbed()) && Mouse.getEventButtonState()) {
							this.inGameHasFocus = false;
							this.setIngameFocus();
						}
					} else if (this.currentScreen != null) {
						this.currentScreen.handleMouseInput();
					}
				}
			}

			if (this.leftClickCounter > 0) {
				--this.leftClickCounter;
			}

			this.mcProfiler.endStartSection("keyboard");

			while (Keyboard.next()) {
				int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
				if (k == 0x1D && (areKeysLocked() || isFullScreen())) {
					KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), Keyboard.getEventKeyState());
				}
				KeyBinding.setKeyBindState(k, Keyboard.getEventKeyState());
				if (Keyboard.getEventKeyState()) {
					KeyBinding.onTick(k);
				}

				if (this.debugCrashKeyPressTime > 0L) {
					if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
						throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
					}

					if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
						this.debugCrashKeyPressTime = -1L;
					}
				} else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
					this.debugCrashKeyPressTime = getSystemTime();
				}

				this.dispatchKeypresses();
				if (Keyboard.getEventKeyState()) {
					if (EaglerDeferredPipeline.instance != null) {
						if (k == 62) {
							DebugFramebufferView.toggleDebugView();
						} else if (k == 0xCB || k == 0xC8) {
							DebugFramebufferView.switchView(-1);
						} else if (k == 0xCD || k == 0xD0) {
							DebugFramebufferView.switchView(1);
						}
					}

					if (this.currentScreen != null) {
						this.currentScreen.handleKeyboardInput();
					} else {
						if (k == 1 || (k > -1 && k == this.gameSettings.keyBindClose.getKeyCode())) {
							this.displayInGameMenu();
						}

						if (k == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null) {
							this.ingameGUI.getChatGUI().clearChatMessages();
						}

						if (k == 31 && Keyboard.isKeyDown(61)) {
							this.refreshResources();
						}

						if (k == 19 && Keyboard.isKeyDown(61)) { // F3+R
							if (gameSettings.shaders) {
								ShaderSource.clearCache();
								this.renderGlobal.loadRenderers();
							}
						}

						if (k == 17 && Keyboard.isKeyDown(61)) {
							;
						}

						if (k == 18 && Keyboard.isKeyDown(61)) {
							;
						}

						if (k == 47 && Keyboard.isKeyDown(61)) {
							;
						}

						if (k == 38 && Keyboard.isKeyDown(61)) {
							;
						}

						if (k == 22 && Keyboard.isKeyDown(61)) {
							;
						}

						if (k == 20 && Keyboard.isKeyDown(61)) {
							this.refreshResources();
						}

						if (k == 33 && Keyboard.isKeyDown(61)) {
							this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE,
									GuiScreen.isShiftKeyDown() ? -1 : 1);
						}

						if (k == 30 && Keyboard.isKeyDown(61)) {
							GlStateManager.recompileShaders();
							this.renderGlobal.loadRenderers();
						}

						if (k == 35 && Keyboard.isKeyDown(61)) {
							this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
							this.gameSettings.saveOptions();
						}

						if (k == 48 && Keyboard.isKeyDown(61)) {
							this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
						}

						if (k == 25 && Keyboard.isKeyDown(61)) {
							this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
							this.gameSettings.saveOptions();
						}

						if (k == 59) {
							this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
						}

						if (k == 61) {
							this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
							this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
							this.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
						}

						if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
							++this.gameSettings.thirdPersonView;
							if (this.gameSettings.thirdPersonView > 2) {
								this.gameSettings.thirdPersonView = 0;
							}

							if (this.gameSettings.thirdPersonView == 0) {
								this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
							} else if (this.gameSettings.thirdPersonView == 1) {
								this.entityRenderer.loadEntityShader((Entity) null);
							}

							this.renderGlobal.setDisplayListEntitiesDirty();
						}

						if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
							this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
						}
					}

					if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
						if (k == 11) {
							this.updateDebugProfilerName(0);
						}

						for (int j1 = 0; j1 < 9; ++j1) {
							if (k == 2 + j1) {
								this.updateDebugProfilerName(j1 + 1);
							}
						}
					}
				}
			}

			for (int l = 0; l < 9; ++l) {
				if (this.gameSettings.keyBindsHotbar[l].isPressed()) {
					if (this.thePlayer.isSpectator()) {
						this.ingameGUI.getSpectatorGui().func_175260_a(l);
					} else {
						this.thePlayer.inventory.currentItem = l;
					}
				}
			}

			boolean zoomKey = this.gameSettings.keyBindZoomCamera.isKeyDown();
			if (zoomKey != isZoomKey) {
				adjustedZoomValue = startZoomValue;
				isZoomKey = zoomKey;
			}

			boolean flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

			while (this.gameSettings.keyBindInventory.isPressed()) {
				if (this.playerController.isRidingHorse()) {
					this.thePlayer.sendHorseInventory();
				} else {
					this.getNetHandler().addToSendQueue(
							new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
					this.displayGuiScreen(new GuiInventory(this.thePlayer));
				}
			}

			while (this.gameSettings.keyBindDrop.isPressed()) {
				if (!this.thePlayer.isSpectator()) {
					this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
				}
			}

			while (this.gameSettings.keyBindChat.isPressed() && flag) {
				this.displayGuiScreen(new GuiChat());
			}

			if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag) {
				this.displayGuiScreen(new GuiChat("/"));
			}

			if (this.thePlayer.isUsingItem()) {
				if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
					this.playerController.onStoppedUsingItem(this.thePlayer);
				}

				while (this.gameSettings.keyBindAttack.isPressed()) {
					;
				}

				while (this.gameSettings.keyBindUseItem.isPressed()) {
					;
				}

				while (this.gameSettings.keyBindPickBlock.isPressed()) {
					;
				}
			} else {
				while (this.gameSettings.keyBindAttack.isPressed()) {
					this.clickMouse();
				}

				while (this.gameSettings.keyBindUseItem.isPressed()) {
					this.rightClickMouse();
				}

				while (this.gameSettings.keyBindPickBlock.isPressed()) {
					this.middleClickMouse();
				}
			}

			if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0
					&& !this.thePlayer.isUsingItem()) {
				this.rightClickMouse();
			}

			this.sendClickBlockToController(
					this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
		}

		if (this.theWorld != null) {
			if (this.thePlayer != null) {
				++this.joinPlayerCounter;
				if (this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.mcProfiler.endStartSection("gameRenderer");
			if (!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			this.mcProfiler.endStartSection("levelRenderer");
			if (!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			this.mcProfiler.endStartSection("level");
			if (!this.isGamePaused) {
				if (this.theWorld.getLastLightningBolt() > 0) {
					this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
				}

				this.theWorld.updateEntities();
			}
			this.eagskullCommand.tick();
		} else if (this.entityRenderer.isShaderActive()) {
			this.entityRenderer.func_181022_b();
		}

		if (!this.isGamePaused) {
			this.mcMusicTicker.update();
			this.mcSoundHandler.update();
		}

		if (this.theWorld != null) {
			if (!this.isGamePaused) {
				this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

				try {
					this.theWorld.tick();
				} catch (Throwable throwable2) {
					CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");
					if (this.theWorld == null) {
						CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
						crashreportcategory2.addCrashSection("Problem", "Level is null!");
					} else {
						this.theWorld.addWorldInfoToCrashReport(crashreport2);
					}

					throw new ReportedException(crashreport2);
				}
			}

			this.mcProfiler.endStartSection("animateTick");
			if (!this.isGamePaused && this.theWorld != null) {
				this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX),
						MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			this.mcProfiler.endStartSection("particles");
			if (!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		} else if (this.myNetworkManager != null) {
			this.mcProfiler.endStartSection("pendingConnection");
			this.myNetworkManager.processReceivedPackets();
		}

		if (this.theWorld != null) {
			++joinWorldTickCounter;
			if (bungeeOutdatedMsgTimer > 0) {
				if (--bungeeOutdatedMsgTimer == 0 && this.thePlayer.sendQueue != null) {
					String pluginBrand = this.thePlayer.sendQueue.getNetworkManager().getPluginBrand();
					String pluginVersion = this.thePlayer.sendQueue.getNetworkManager().getPluginVersion();
					if (pluginBrand != null && pluginVersion != null
							&& EaglerXBungeeVersion.isUpdateToPluginAvailable(pluginBrand, pluginVersion)) {
						String pfx = EnumChatFormatting.GOLD + "[EagX]" + EnumChatFormatting.AQUA;
						ingameGUI.getChatGUI().printChatMessage(
								new ChatComponentText(pfx + " ---------------------------------------"));
						ingameGUI.getChatGUI().printChatMessage(
								new ChatComponentText(pfx + " This server appears to be using version "
										+ EnumChatFormatting.YELLOW + pluginVersion));
						ingameGUI.getChatGUI().printChatMessage(
								new ChatComponentText(pfx + " of the EaglerXBungee plugin which is outdated"));
						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
						ingameGUI.getChatGUI()
								.printChatMessage(new ChatComponentText(pfx + " If you are the admin update to "
										+ EnumChatFormatting.YELLOW + EaglerXBungeeVersion.getPluginVersion()
										+ EnumChatFormatting.AQUA + " or newer"));
						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
						ingameGUI.getChatGUI().printChatMessage((new ChatComponentText(pfx + " Click: "))
								.appendSibling((new ChatComponentText("" + EnumChatFormatting.GREEN
										+ EnumChatFormatting.UNDERLINE + EaglerXBungeeVersion.getPluginButton()))
												.setChatStyle((new ChatStyle()).setChatClickEvent(
														new ClickEvent(ClickEvent.Action.EAGLER_PLUGIN_DOWNLOAD,
																"plugin_download.zip")))));
						ingameGUI.getChatGUI().printChatMessage(
								new ChatComponentText(pfx + " ---------------------------------------"));
					}
				}
			}
		} else {
			joinWorldTickCounter = 0;
			if (currentScreen != null && currentScreen.shouldHangupIntegratedServer()) {
				if (SingleplayerServerController.hangupEaglercraftServer()) {
					this.displayGuiScreen(new GuiScreenIntegratedServerBusy(currentScreen,
							"singleplayer.busy.stoppingIntegratedServer",
							"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
				}
			}
		}

		this.mcProfiler.endSection();
		this.systemTime = getSystemTime();
	}

	/**+
	 * Arguments: World foldername, World ingame name, WorldSettings
	 */
	public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
		this.loadWorld((WorldClient) null);
		Minecraft.getMinecraft().getRenderManager().setEnableFNAWSkins(this.gameSettings.enableFNAWSkins);
		session.reset();
		SingleplayerServerController.launchEaglercraftServer(folderName, gameSettings.difficulty.getDifficultyId(),
				Math.max(gameSettings.renderDistanceChunks, 2), worldSettingsIn);
		this.displayGuiScreen(new GuiScreenIntegratedServerBusy(
				new GuiScreenSingleplayerConnecting(new GuiMainMenu(), "Connecting to " + folderName),
				"singleplayer.busy.startingIntegratedServer", "singleplayer.failed.startingIntegratedServer",
				() -> SingleplayerServerController.isWorldReady(), (t, u) -> {
					Minecraft.this.displayGuiScreen(GuiScreenIntegratedServerBusy.createException(new GuiMainMenu(),
							((GuiScreenIntegratedServerBusy) t).failMessage, u));
				}));
	}

	/**+
	 * unloads the current world first
	 */
	public void loadWorld(WorldClient worldClientIn) {
		this.loadWorld(worldClientIn, "");
	}

	/**+
	 * unloads the current world first
	 */
	public void loadWorld(WorldClient worldClientIn, String loadingMessage) {
		if (worldClientIn == null) {
			NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();
			if (nethandlerplayclient != null) {
				nethandlerplayclient.cleanup();
			}
			session.reset();

			this.guiAchievement.clearAchievements();
			this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
		}

		this.renderViewEntity = null;
		this.myNetworkManager = null;
		if (this.loadingScreen != null) {
			this.loadingScreen.resetProgressAndMessage(loadingMessage);
			this.loadingScreen.displayLoadingString("");
		}

		if (worldClientIn == null && this.theWorld != null) {
			this.mcResourcePackRepository.func_148529_f();
			this.ingameGUI.func_181029_i();
			this.setServerData((ServerData) null);
			this.integratedServerIsRunning = false;
		}

		this.mcSoundHandler.stopSounds();
		this.theWorld = worldClientIn;
		if (worldClientIn != null) {
			if (this.renderGlobal != null) {
				this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
			}

			if (this.effectRenderer != null) {
				this.effectRenderer.clearEffects(worldClientIn);
			}

			if (this.thePlayer == null) {
				this.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.preparePlayerToSpawn();
			worldClientIn.spawnEntityInWorld(this.thePlayer);
			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			this.playerController.setPlayerCapabilities(this.thePlayer);
			this.renderViewEntity = this.thePlayer;
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	public void setDimensionAndSpawnPlayer(int dimension) {
		this.theWorld.setInitialSpawnLocation();
		this.theWorld.removeAllEntities();
		int i = 0;
		String s = null;
		if (this.thePlayer != null) {
			i = this.thePlayer.getEntityId();
			this.theWorld.removeEntity(this.thePlayer);
			s = this.thePlayer.getClientBrand();
		}

		this.renderViewEntity = null;
		EntityPlayerSP entityplayersp = this.thePlayer;
		this.thePlayer = this.playerController.func_178892_a(this.theWorld, new StatFileWriter());
		this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
		this.thePlayer.dimension = dimension;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		this.thePlayer.setClientBrand(s);
		this.theWorld.spawnEntityInWorld(this.thePlayer);
		this.playerController.flipPlayer(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.setEntityId(i);
		this.playerController.setPlayerCapabilities(this.thePlayer);
		this.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());
		if (this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen) null);
		}

	}

	/**+
	 * Gets whether this is a demo or not.
	 */
	public final boolean isDemo() {
		return EagRuntime.getConfiguration().isDemo();
	}

	public NetHandlerPlayClient getNetHandler() {
		return this.thePlayer != null ? this.thePlayer.sendQueue : null;
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	/**+
	 * Returns if ambient occlusion is enabled
	 */
	public static boolean isAmbientOcclusionEnabled() {
		if (theMinecraft == null)
			return false;
		GameSettings g = theMinecraft.gameSettings;
		return g.ambientOcclusion != 0 && !g.shadersAODisable;
	}

	/**+
	 * Called when user clicked he's mouse middle button (pick
	 * block)
	 */
	private void middleClickMouse() {
		if (this.objectMouseOver != null) {
			boolean flag = this.thePlayer.capabilities.isCreativeMode;
			int i = 0;
			boolean flag1 = false;
			TileEntity tileentity = null;
			Object object;
			if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos blockpos = this.objectMouseOver.getBlockPos();
				Block block = this.theWorld.getBlockState(blockpos).getBlock();
				if (block.getMaterial() == Material.air) {
					return;
				}

				object = block.getItem(this.theWorld, blockpos);
				if (object == null) {
					return;
				}

				if (flag && GuiScreen.isCtrlKeyDown()) {
					tileentity = this.theWorld.getTileEntity(blockpos);
				}

				Block block1 = object instanceof ItemBlock && !block.isFlowerPot()
						? Block.getBlockFromItem((Item) object)
						: block;
				i = block1.getDamageValue(this.theWorld, blockpos);
				flag1 = ((Item) object).getHasSubtypes();
			} else {
				if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY
						|| this.objectMouseOver.entityHit == null || !flag) {
					return;
				}

				if (this.objectMouseOver.entityHit instanceof EntityPainting) {
					object = Items.painting;
				} else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
					object = Items.lead;
				} else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
					EntityItemFrame entityitemframe = (EntityItemFrame) this.objectMouseOver.entityHit;
					ItemStack itemstack = entityitemframe.getDisplayedItem();
					if (itemstack == null) {
						object = Items.item_frame;
					} else {
						object = itemstack.getItem();
						i = itemstack.getMetadata();
						flag1 = true;
					}
				} else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
					EntityMinecart entityminecart = (EntityMinecart) this.objectMouseOver.entityHit;
					switch (entityminecart.getMinecartType()) {
					case FURNACE:
						object = Items.furnace_minecart;
						break;
					case CHEST:
						object = Items.chest_minecart;
						break;
					case TNT:
						object = Items.tnt_minecart;
						break;
					case HOPPER:
						object = Items.hopper_minecart;
						break;
					case COMMAND_BLOCK:
						object = Items.command_block_minecart;
						break;
					default:
						object = Items.minecart;
					}
				} else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
					object = Items.boat;
				} else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
					object = Items.armor_stand;
				} else {
					object = Items.spawn_egg;
					i = EntityList.getEntityID(this.objectMouseOver.entityHit);
					flag1 = true;
					if (!EntityList.entityEggs.containsKey(Integer.valueOf(i))) {
						return;
					}
				}
			}

			InventoryPlayer inventoryplayer = this.thePlayer.inventory;
			if (tileentity == null) {
				inventoryplayer.setCurrentItem((Item) object, i, flag1, flag);
			} else {
				ItemStack itemstack1 = this.func_181036_a((Item) object, i, tileentity);
				inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack1);
			}

			if (flag) {
				int j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
				this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
			}

		}
	}

	private ItemStack func_181036_a(Item parItem, int parInt1, TileEntity parTileEntity) {
		ItemStack itemstack = new ItemStack(parItem, 1, parInt1);
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		parTileEntity.writeToNBT(nbttagcompound);
		if (parItem == Items.skull && nbttagcompound.hasKey("Owner")) {
			NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
			NBTTagCompound nbttagcompound3 = new NBTTagCompound();
			nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
			itemstack.setTagCompound(nbttagcompound3);
			return itemstack;
		} else {
			itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			NBTTagList nbttaglist = new NBTTagList();
			nbttaglist.appendTag(new NBTTagString("(+NBT)"));
			nbttagcompound1.setTag("Lore", nbttaglist);
			itemstack.setTagInfo("display", nbttagcompound1);
			return itemstack;
		}
	}

	/**+
	 * adds core server Info (GL version , Texture pack, isModded,
	 * type), and the worldInfo to the crash report
	 */
	public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash) {
		theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>() {
			public String call() throws Exception {
				return Minecraft.this.launchedVersion;
			}
		});
		theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>() {
			public String call() {
				return EagRuntime.getVersion();
			}
		});
		theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>() {
			public String call() {
				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
						+ EaglercraftGPU.glGetString(7936);
			}
		});
		theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>() {
			public String call() throws Exception {
				return "Definitely Not; You're an eagler";
			}
		});
		theCrash.getCategory().addCrashSectionCallable("Type", new Callable<String>() {
			public String call() throws Exception {
				return "Client (map_client.txt)";
			}
		});
		theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>() {
			public String call() throws Exception {
				StringBuilder stringbuilder = new StringBuilder();

				for (String s : Minecraft.this.gameSettings.resourcePacks) {
					if (stringbuilder.length() > 0) {
						stringbuilder.append(", ");
					}

					stringbuilder.append(s);
					if (Minecraft.this.gameSettings.field_183018_l.contains(s)) {
						stringbuilder.append(" (incompatible)");
					}
				}

				return stringbuilder.toString();
			}
		});
		theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable<String>() {
			public String call() throws Exception {
				return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
			}
		});
		theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>() {
			public String call() throws Exception {
				return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection()
						: "N/A (disabled)";
			}
		});
		if (this.theWorld != null) {
			this.theWorld.addWorldInfoToCrashReport(theCrash);
		}

		return theCrash;
	}

	/**+
	 * Return the singleton Minecraft instance for the game
	 */
	public static Minecraft getMinecraft() {
		return theMinecraft;
	}

	public ListenableFuture<Object> scheduleResourcesRefresh() {
		return this.addScheduledTaskFuture(new Runnable() {
			public void run() {
				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
						I18n.format("resourcePack.load.pleaseWait"));
				Minecraft.this.refreshResources();
			}
		});
	}

	private String func_181538_aA() {
		return this.currentServerData != null ? "multiplayer" : "out_of_game";
	}

	/**+
	 * Returns whether snooping is enabled or not.
	 */
	public boolean isSnooperEnabled() {
		return this.gameSettings.snooperEnabled;
	}

	/**+
	 * Set the current ServerData instance.
	 */
	public void setServerData(ServerData serverDataIn) {
		this.currentServerData = serverDataIn;
	}

	public ServerData getCurrentServerData() {
		return this.currentServerData;
	}

	public boolean isIntegratedServerRunning() {
		return SingleplayerServerController.isWorldRunning();
	}

	/**+
	 * Returns true if there is only one player playing, and the
	 * current server is the integrated one.
	 */
	public boolean isSingleplayer() {
		return SingleplayerServerController.isWorldRunning();
	}

	public static void stopIntegratedServer() {

	}

	/**+
	 * Gets the system time in milliseconds.
	 */
	public static long getSystemTime() {
		return System.currentTimeMillis();
	}

	/**+
	 * Returns whether we're in full screen or not.
	 */
	public boolean isFullScreen() {
		return Display.isFullscreen();
	}

	public Session getSession() {
		return this.session;
	}

	public TextureManager getTextureManager() {
		return this.renderEngine;
	}

	public IResourceManager getResourceManager() {
		return this.mcResourceManager;
	}

	public ResourcePackRepository getResourcePackRepository() {
		return this.mcResourcePackRepository;
	}

	public LanguageManager getLanguageManager() {
		return this.mcLanguageManager;
	}

	public TextureMap getTextureMapBlocks() {
		return this.textureMapBlocks;
	}

	public boolean isJava64bit() {
		return this.jvm64bit;
	}

	public boolean isGamePaused() {
		return this.isGamePaused;
	}

	public SoundHandler getSoundHandler() {
		return this.mcSoundHandler;
	}

	public MusicTicker.MusicType getAmbientMusicType() {
		return this.thePlayer != null ? (this.thePlayer.worldObj.provider instanceof WorldProviderHell
				? MusicTicker.MusicType.NETHER
				: (this.thePlayer.worldObj.provider instanceof WorldProviderEnd
						? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS
								: MusicTicker.MusicType.END)
						: (this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying
								? MusicTicker.MusicType.CREATIVE
								: MusicTicker.MusicType.GAME)))
				: MusicTicker.MusicType.MENU;
	}

	public void dispatchKeypresses() {
		int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
		if (i != 0 && !Keyboard.isRepeatEvent()) {
			if (!(this.currentScreen instanceof GuiControls)
					|| ((GuiControls) this.currentScreen).time <= getSystemTime() - 20L) {
				if (Keyboard.getEventKeyState()) {
					if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
						this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot());
					}
				}
			}
		}
	}

	public Entity getRenderViewEntity() {
		return this.renderViewEntity;
	}

	public void setRenderViewEntity(Entity viewingEntity) {
		this.renderViewEntity = viewingEntity;
		this.entityRenderer.loadEntityShader(viewingEntity);
	}

	public <V> ListenableFuture<V> addScheduledTaskFuture(Callable<V> callableToSchedule) {
		Validate.notNull(callableToSchedule);
		ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
		synchronized (this.scheduledTasks) {
			this.scheduledTasks.add(listenablefuturetask);
			return listenablefuturetask;
		}
	}

	public ListenableFuture<Object> addScheduledTaskFuture(Runnable runnableToSchedule) {
		Validate.notNull(runnableToSchedule);
		return this.addScheduledTaskFuture(Executors.callable(runnableToSchedule));
	}

	public void addScheduledTask(Runnable runnableToSchedule) {
		this.addScheduledTaskFuture(Executors.callable(runnableToSchedule));
	}

	public BlockRendererDispatcher getBlockRendererDispatcher() {
		return this.blockRenderDispatcher;
	}

	public RenderManager getRenderManager() {
		return this.renderManager;
	}

	public RenderItem getRenderItem() {
		return this.renderItem;
	}

	public ItemRenderer getItemRenderer() {
		return this.itemRenderer;
	}

	public static int getDebugFPS() {
		return debugFPS;
	}

	public FrameTimer func_181539_aj() {
		return this.field_181542_y;
	}

	public boolean func_181540_al() {
		return this.field_181541_X;
	}

	public void func_181537_a(boolean parFlag) {
		this.field_181541_X = parFlag;
	}

	/**+
	 * Used in the usage snooper.
	 */
	public static int getGLMaximumTextureSize() {
		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
	}

	public boolean areKeysLocked() {
		return PlatformInput.lockKeys;
	}

	public ModelManager getModelManager() {
		return modelManager;
	}

	/**+
	 * Returns the save loader that is currently being used
	 */
	public ISaveFormat getSaveLoader() {
		return SingleplayerServerController.instance;
	}

	public void clearTitles() {
		ingameGUI.displayTitle(null, null, -1, -1, -1);
	}

	public boolean getEnableFNAWSkins() {
		boolean ret = this.gameSettings.enableFNAWSkins;
		if (this.thePlayer != null) {
			ret &= this.thePlayer.sendQueue.currentFNAWSkinAllowedState;
		}
		return ret;
	}
}