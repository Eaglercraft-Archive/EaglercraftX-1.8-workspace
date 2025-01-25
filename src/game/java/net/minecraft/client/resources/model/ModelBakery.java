package net.minecraft.client.resources.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BlockVertexIDs;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.VertexMarkerState;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;

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
public class ModelBakery {
	private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(new ResourceLocation[] {
			new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"),
			new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"),
			new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"),
			new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"),
			new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"),
			new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"),
			new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"),
			new ResourceLocation("items/empty_armor_slot_helmet"),
			new ResourceLocation("items/empty_armor_slot_chestplate"),
			new ResourceLocation("items/empty_armor_slot_leggings"),
			new ResourceLocation("items/empty_armor_slot_boots") });
	private static final Logger LOGGER = LogManager.getLogger();
	protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing",
			"missing");
	private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
	private static final Joiner JOINER = Joiner.on(" -> ");
	private final IResourceManager resourceManager;
	private final Map<ResourceLocation, EaglerTextureAtlasSprite> sprites = Maps.newHashMap();
	private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
	private final Map<ModelResourceLocation, ModelBlockDefinition.Variants> variants = Maps.newLinkedHashMap();
	private final TextureMap textureMap;
	private final BlockModelShapes blockModelShapes;
	private final FaceBakery faceBakery = new FaceBakery();
	private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
	private RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
	private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(
			"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
	private static final ModelBlock MODEL_COMPASS = ModelBlock.deserialize(
			"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
	private static final ModelBlock MODEL_CLOCK = ModelBlock.deserialize(
			"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
	private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize(
			"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
	private Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
	private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
	private Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();

	public ModelBakery(IResourceManager parIResourceManager, TextureMap parTextureMap,
			BlockModelShapes parBlockModelShapes) {
		this.resourceManager = parIResourceManager;
		this.textureMap = parTextureMap;
		this.blockModelShapes = parBlockModelShapes;
	}

	public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
		this.loadVariantItemModels();
		this.loadModelsCheck();
		this.loadSprites();
		this.bakeItemModels();
		this.bakeBlockModels();
		return this.bakedRegistry;
	}

	private void loadVariantItemModels() {
		this.loadVariants(this.blockModelShapes.getBlockStateMapper().putAllStateModelLocations().values());
		this.variants.put(MODEL_MISSING,
				new ModelBlockDefinition.Variants(MODEL_MISSING.getVariant(),
						Lists.newArrayList(new ModelBlockDefinition.Variant[] {
								new ModelBlockDefinition.Variant(new ResourceLocation(MODEL_MISSING.getResourcePath()),
										ModelRotation.X0_Y0, false, 1) })));
		ResourceLocation resourcelocation = new ResourceLocation("item_frame");
		ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
		this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
		this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
		this.loadVariantModels();
		this.loadItemModels();
	}

	private void loadVariants(Collection<ModelResourceLocation> parCollection) {
		for (ModelResourceLocation modelresourcelocation : parCollection) {
			try {
				ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(modelresourcelocation);

				try {
					this.registerVariant(modelblockdefinition, modelresourcelocation);
				} catch (Exception var6) {
					LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from "
							+ modelresourcelocation);
					LOGGER.warn(var6);
				}
			} catch (Exception exception) {
				LOGGER.warn("Unable to load definition " + modelresourcelocation);
				LOGGER.warn(exception);
			}
		}

	}

	private void registerVariant(ModelBlockDefinition parModelBlockDefinition,
			ModelResourceLocation parModelResourceLocation) {
		this.variants.put(parModelResourceLocation,
				parModelBlockDefinition.getVariants(parModelResourceLocation.getVariant()));
	}

	private ModelBlockDefinition getModelBlockDefinition(ResourceLocation parResourceLocation) {
		ResourceLocation resourcelocation = this.getBlockStateLocation(parResourceLocation);
		ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition) this.blockDefinitions.get(resourcelocation);
		if (modelblockdefinition == null) {
			ArrayList arraylist = Lists.newArrayList();

			try {
				for (IResource iresource : this.resourceManager.getAllResources(resourcelocation)) {
					InputStream inputstream = null;

					try {
						inputstream = iresource.getInputStream();
						ModelBlockDefinition modelblockdefinition1 = ModelBlockDefinition
								.parseFromReader(new InputStreamReader(inputstream, Charsets.UTF_8));
						arraylist.add(modelblockdefinition1);
					} catch (Exception exception) {
						throw new RuntimeException("Encountered an exception when loading model definition of \'"
								+ parResourceLocation + "\' from: \'" + iresource.getResourceLocation()
								+ "\' in resourcepack: \'" + iresource.getResourcePackName() + "\'", exception);
					} finally {
						IOUtils.closeQuietly(inputstream);
					}
				}
			} catch (IOException ioexception) {
				throw new RuntimeException("Encountered an exception when loading model definition of model "
						+ resourcelocation.toString(), ioexception);
			}

			modelblockdefinition = new ModelBlockDefinition((ArrayList<ModelBlockDefinition>) arraylist);
			this.blockDefinitions.put(resourcelocation, modelblockdefinition);
		}

		return modelblockdefinition;
	}

	private ResourceLocation getBlockStateLocation(ResourceLocation parResourceLocation) {
		return new ResourceLocation(parResourceLocation.getResourceDomain(),
				"blockstates/" + parResourceLocation.getResourcePath() + ".json");
	}

	private void loadVariantModels() {
		for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
			for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants) this.variants
					.get(modelresourcelocation)).getVariants()) {
				ResourceLocation resourcelocation = modelblockdefinition$variant.getModelLocation();
				if (this.models.get(resourcelocation) == null) {
					try {
						ModelBlock modelblock = this.loadModel(resourcelocation);
						this.models.put(resourcelocation, modelblock);
					} catch (Exception exception) {
						LOGGER.warn("Unable to load block model: \'" + resourcelocation + "\' for variant: \'"
								+ modelresourcelocation + "\'");
						LOGGER.warn(exception);
					}
				}
			}
		}

	}

	private ModelBlock loadModel(ResourceLocation parResourceLocation) throws IOException {
		String s = parResourceLocation.getResourcePath();
		if ("builtin/generated".equals(s)) {
			return MODEL_GENERATED;
		} else if ("builtin/compass".equals(s)) {
			return MODEL_COMPASS;
		} else if ("builtin/clock".equals(s)) {
			return MODEL_CLOCK;
		} else if ("builtin/entity".equals(s)) {
			return MODEL_ENTITY;
		} else {
			String str;
			if (s.startsWith("builtin/")) {
				String s1 = s.substring("builtin/".length());
				str = (String) BUILT_IN_MODELS.get(s1);
				if (str == null) {
					throw new FileNotFoundException(parResourceLocation.toString());
				}
			} else {
				IResource iresource = this.resourceManager.getResource(this.getModelLocation(parResourceLocation));
				try (InputStream is = iresource.getInputStream()) {
					str = IOUtils.inputStreamToString(is, StandardCharsets.UTF_8);
				}
			}

			ModelBlock modelblock = ModelBlock.deserialize(str);
			modelblock.name = parResourceLocation.toString();

			return modelblock;
		}
	}

	private ResourceLocation getModelLocation(ResourceLocation parResourceLocation) {
		String path = parResourceLocation.getResourcePath();
		return new ResourceLocation(parResourceLocation.getResourceDomain(),
				((path.startsWith("mcpatcher/") || path.startsWith("optifine/")) ? "" : "models/") + path + ".json");
	}

	private void loadItemModels() {
		this.registerVariantNames();

		for (Item item : Item.itemRegistry) {
			for (String s : this.getVariantNames(item)) {
				ResourceLocation resourcelocation = this.getItemLocation(s);
				this.itemLocations.put(s, resourcelocation);
				if (this.models.get(resourcelocation) == null) {
					try {
						ModelBlock modelblock = this.loadModel(resourcelocation);
						this.models.put(resourcelocation, modelblock);
					} catch (Exception exception) {
						LOGGER.warn("Unable to load item model: \'" + resourcelocation + "\' for item: \'"
								+ Item.itemRegistry.getNameForObject(item) + "\'");
						LOGGER.warn(exception);
					}
				}
			}
		}

	}

	public void loadItemModel(String p_loadItemModel_1_, ResourceLocation p_loadItemModel_2_,
			ResourceLocation p_loadItemModel_3_) {
		this.itemLocations.put(p_loadItemModel_1_, p_loadItemModel_2_);

		if (this.models.get(p_loadItemModel_2_) == null) {
			try {
				ModelBlock modelblock = this.loadModel(p_loadItemModel_2_);
				this.models.put(p_loadItemModel_2_, modelblock);
			} catch (Exception exception) {
				LOGGER.warn("Unable to load item model: \'{}\' for item: \'{}\'",
						new Object[] { p_loadItemModel_2_, p_loadItemModel_3_ });
				LOGGER.warn(exception.getClass().getName() + ": " + exception.getMessage());
			}
		}
	}

	private void registerVariantNames() {
		this.variantNames.put(Item.getItemFromBlock(Blocks.stone), Lists.newArrayList(new String[] { "stone", "granite",
				"granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.dirt),
				Lists.newArrayList(new String[] { "dirt", "coarse_dirt", "podzol" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.planks), Lists.newArrayList(new String[] { "oak_planks",
				"spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.sapling), Lists.newArrayList(new String[] { "oak_sapling",
				"spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.sand),
				Lists.newArrayList(new String[] { "sand", "red_sand" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.log),
				Lists.newArrayList(new String[] { "oak_log", "spruce_log", "birch_log", "jungle_log" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.leaves),
				Lists.newArrayList(new String[] { "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.sponge),
				Lists.newArrayList(new String[] { "sponge", "sponge_wet" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.sandstone),
				Lists.newArrayList(new String[] { "sandstone", "chiseled_sandstone", "smooth_sandstone" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.red_sandstone),
				Lists.newArrayList(new String[] { "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.tallgrass),
				Lists.newArrayList(new String[] { "dead_bush", "tall_grass", "fern" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.deadbush), Lists.newArrayList(new String[] { "dead_bush" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.wool),
				Lists.newArrayList(new String[] { "black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool",
						"purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool",
						"light_blue_wool", "magenta_wool", "orange_wool", "white_wool" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.yellow_flower),
				Lists.newArrayList(new String[] { "dandelion" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.red_flower),
				Lists.newArrayList(new String[] { "poppy", "blue_orchid", "allium", "houstonia", "red_tulip",
						"orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab),
				Lists.newArrayList(new String[] { "stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab",
						"stone_brick_slab", "nether_brick_slab", "quartz_slab" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab2),
				Lists.newArrayList(new String[] { "red_sandstone_slab" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass),
				Lists.newArrayList(new String[] { "black_stained_glass", "red_stained_glass", "green_stained_glass",
						"brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass",
						"silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass",
						"yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass",
						"orange_stained_glass", "white_stained_glass" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.monster_egg),
				Lists.newArrayList(new String[] { "stone_monster_egg", "cobblestone_monster_egg",
						"stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg",
						"chiseled_brick_monster_egg" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stonebrick), Lists.newArrayList(
				new String[] { "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.wooden_slab), Lists.newArrayList(new String[] { "oak_slab",
				"spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.cobblestone_wall),
				Lists.newArrayList(new String[] { "cobblestone_wall", "mossy_cobblestone_wall" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.anvil),
				Lists.newArrayList(new String[] { "anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.quartz_block),
				Lists.newArrayList(new String[] { "quartz_block", "chiseled_quartz_block", "quartz_column" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stained_hardened_clay),
				Lists.newArrayList(new String[] { "black_stained_hardened_clay", "red_stained_hardened_clay",
						"green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay",
						"purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay",
						"gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay",
						"yellow_stained_hardened_clay", "light_blue_stained_hardened_clay",
						"magenta_stained_hardened_clay", "orange_stained_hardened_clay",
						"white_stained_hardened_clay" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass_pane),
				Lists.newArrayList(new String[] { "black_stained_glass_pane", "red_stained_glass_pane",
						"green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane",
						"purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane",
						"gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane",
						"yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane",
						"orange_stained_glass_pane", "white_stained_glass_pane" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.leaves2),
				Lists.newArrayList(new String[] { "acacia_leaves", "dark_oak_leaves" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.log2),
				Lists.newArrayList(new String[] { "acacia_log", "dark_oak_log" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.prismarine),
				Lists.newArrayList(new String[] { "prismarine", "prismarine_bricks", "dark_prismarine" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.carpet),
				Lists.newArrayList(new String[] { "black_carpet", "red_carpet", "green_carpet", "brown_carpet",
						"blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet",
						"lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet",
						"white_carpet" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.double_plant), Lists.newArrayList(
				new String[] { "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia" }));
		this.variantNames.put(Items.bow,
				Lists.newArrayList(new String[] { "bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2" }));
		this.variantNames.put(Items.coal, Lists.newArrayList(new String[] { "coal", "charcoal" }));
		this.variantNames.put(Items.fishing_rod,
				Lists.newArrayList(new String[] { "fishing_rod", "fishing_rod_cast" }));
		this.variantNames.put(Items.fish,
				Lists.newArrayList(new String[] { "cod", "salmon", "clownfish", "pufferfish" }));
		this.variantNames.put(Items.cooked_fish, Lists.newArrayList(new String[] { "cooked_cod", "cooked_salmon" }));
		this.variantNames.put(Items.dye,
				Lists.newArrayList(new String[] { "dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue",
						"dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow",
						"dye_light_blue", "dye_magenta", "dye_orange", "dye_white" }));
		this.variantNames.put(Items.potionitem,
				Lists.newArrayList(new String[] { "bottle_drinkable", "bottle_splash" }));
		this.variantNames.put(Items.skull, Lists.newArrayList(
				new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence_gate),
				Lists.newArrayList(new String[] { "oak_fence_gate" }));
		this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence),
				Lists.newArrayList(new String[] { "oak_fence" }));
		this.variantNames.put(Items.oak_door, Lists.newArrayList(new String[] { "oak_door" }));
		CustomItems.update();
		CustomItems.loadModels(this);
	}

	private List<String> getVariantNames(Item parItem) {
		List list = (List) this.variantNames.get(parItem);
		if (list == null) {
			list = Collections
					.singletonList(((ResourceLocation) Item.itemRegistry.getNameForObject(parItem)).toString());
		}

		return list;
	}

	private ResourceLocation getItemLocation(String parString1) {
		ResourceLocation resourcelocation = new ResourceLocation(parString1);
		return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
	}

	private void bakeBlockModels() {
		boolean deferred = Minecraft.getMinecraft().gameSettings.shaders;
		for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
			WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
			int i = 0;

			for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants) this.variants
					.get(modelresourcelocation)).getVariants()) {
				ModelBlock modelblock = (ModelBlock) this.models.get(modelblockdefinition$variant.getModelLocation());
				if (modelblock != null && modelblock.isResolved()) {
					++i;
					if (deferred) {
						ModelBlock currentBlockModel = modelblock;
						ResourceLocation currentResourceLocation = modelblockdefinition$variant.getModelLocation();
						int blockId = -1;
						do {
							blockId = BlockVertexIDs.modelToID.getOrDefault(currentResourceLocation.toString(), -1);
							if (blockId != -1) {
								break;
							}
							currentResourceLocation = currentBlockModel.getParentLocation();
							currentBlockModel = models.get(currentResourceLocation);
						} while (currentBlockModel != null);
						if (blockId != -1) {
							VertexMarkerState.markId = blockId;
							try {
								weightedbakedmodel$builder.add(
										this.bakeModel(modelblock, modelblockdefinition$variant.getRotation(),
												modelblockdefinition$variant.isUvLocked()),
										modelblockdefinition$variant.getWeight());
							} finally {
								VertexMarkerState.markId = 0;
							}
							continue;
						}
					}
					weightedbakedmodel$builder.add(
							this.bakeModel(modelblock, modelblockdefinition$variant.getRotation(),
									modelblockdefinition$variant.isUvLocked()),
							modelblockdefinition$variant.getWeight());
				} else {
					LOGGER.warn("Missing model for: " + modelresourcelocation);
				}
			}

			if (i == 0) {
				LOGGER.warn("No weighted models for: " + modelresourcelocation);
			} else if (i == 1) {
				this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.first());
			} else {
				this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.build());
			}
		}

		for (Entry entry : this.itemLocations.entrySet()) {
			ResourceLocation resourcelocation = (ResourceLocation) entry.getValue();
			ModelResourceLocation modelresourcelocation1 = new ModelResourceLocation((String) entry.getKey(),
					"inventory");
			ModelBlock modelblock1 = (ModelBlock) this.models.get(resourcelocation);
			if (modelblock1 != null && modelblock1.isResolved()) {
				if (this.isCustomRenderer(modelblock1)) {
					this.bakedRegistry.putObject(modelresourcelocation1, new BuiltInModel(modelblock1.func_181682_g()));
				} else {
					this.bakedRegistry.putObject(modelresourcelocation1,
							this.bakeModel(modelblock1, ModelRotation.X0_Y0, false));
				}
			} else {
				LOGGER.warn("Missing model for: " + resourcelocation);
			}
		}

	}

	private Set<ResourceLocation> getVariantsTextureLocations() {
		HashSet hashset = Sets.newHashSet();
		ArrayList arraylist = Lists.newArrayList(this.variants.keySet());
		Collections.sort(arraylist, new Comparator<ModelResourceLocation>() {
			public int compare(ModelResourceLocation modelresourcelocation1,
					ModelResourceLocation modelresourcelocation2) {
				return modelresourcelocation1.toString().compareTo(modelresourcelocation2.toString());
			}
		});

		for (ModelResourceLocation modelresourcelocation : (List<ModelResourceLocation>) arraylist) {
			ModelBlockDefinition.Variants modelblockdefinition$variants = (ModelBlockDefinition.Variants) this.variants
					.get(modelresourcelocation);

			for (ModelBlockDefinition.Variant modelblockdefinition$variant : modelblockdefinition$variants
					.getVariants()) {
				ModelBlock modelblock = (ModelBlock) this.models.get(modelblockdefinition$variant.getModelLocation());
				if (modelblock == null) {
					LOGGER.warn("Missing model for: " + modelresourcelocation);
				} else {
					hashset.addAll(this.getTextureLocations(modelblock));
				}
			}
		}

		hashset.addAll(LOCATIONS_BUILTIN_TEXTURES);
		return hashset;
	}

	private IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked) {
		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.sprites
				.get(new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
		SimpleBakedModel.Builder simplebakedmodel$builder = (new SimpleBakedModel.Builder(modelBlockIn))
				.setTexture(textureatlassprite);

		for (BlockPart blockpart : modelBlockIn.getElements()) {
			for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
				BlockPartFace blockpartface = (BlockPartFace) blockpart.mapFaces.get(enumfacing);
				EaglerTextureAtlasSprite textureatlassprite1 = (EaglerTextureAtlasSprite) this.sprites
						.get(new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
				if (blockpartface.cullFace == null) {
					simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface,
							textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
				} else {
					simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotateFace(blockpartface.cullFace),
							this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing,
									modelRotationIn, uvLocked));
				}
			}
		}

		return simplebakedmodel$builder.makeBakedModel();
	}

	private BakedQuad makeBakedQuad(BlockPart parBlockPart, BlockPartFace parBlockPartFace,
			EaglerTextureAtlasSprite parTextureAtlasSprite, EnumFacing parEnumFacing, ModelRotation parModelRotation,
			boolean parFlag) {
		return this.faceBakery.makeBakedQuad(parBlockPart.positionFrom, parBlockPart.positionTo, parBlockPartFace,
				parTextureAtlasSprite, parEnumFacing, parModelRotation, parBlockPart.partRotation, parFlag,
				parBlockPart.shade);
	}

	private void loadModelsCheck() {
		this.loadModels();

		for (ModelBlock modelblock : this.models.values()) {
			modelblock.getParentFromMap(this.models);
		}

		ModelBlock.checkModelHierarchy(this.models);
	}

	private void loadModels() {
		List arraydeque = Lists.newLinkedList();
		HashSet hashset = Sets.newHashSet();

		for (ResourceLocation resourcelocation : this.models.keySet()) {
			hashset.add(resourcelocation);
			ResourceLocation resourcelocation1 = ((ModelBlock) this.models.get(resourcelocation)).getParentLocation();
			if (resourcelocation1 != null) {
				arraydeque.add(resourcelocation1);
			}
		}

		while (!arraydeque.isEmpty()) {
			ResourceLocation resourcelocation2 = (ResourceLocation) arraydeque.remove(0);

			try {
				if (this.models.get(resourcelocation2) != null) {
					continue;
				}

				ModelBlock modelblock = this.loadModel(resourcelocation2);
				this.models.put(resourcelocation2, modelblock);
				ResourceLocation resourcelocation3 = modelblock.getParentLocation();
				if (resourcelocation3 != null && !hashset.contains(resourcelocation3)) {
					arraydeque.add(resourcelocation3);
				}
			} catch (Exception exception) {
				LOGGER.warn("In parent chain: " + JOINER.join(this.getParentPath(resourcelocation2))
						+ "; unable to load model: \'" + resourcelocation2 + "\'");
				LOGGER.warn(exception);
			}

			hashset.add(resourcelocation2);
		}

	}

	private List<ResourceLocation> getParentPath(ResourceLocation parResourceLocation) {
		ArrayList arraylist = Lists.newArrayList(new ResourceLocation[] { parResourceLocation });
		ResourceLocation resourcelocation = parResourceLocation;

		while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
			arraylist.add(0, resourcelocation);
		}

		return arraylist;
	}

	private ResourceLocation getParentLocation(ResourceLocation parResourceLocation) {
		for (Entry entry : this.models.entrySet()) {
			ModelBlock modelblock = (ModelBlock) entry.getValue();
			if (modelblock != null && parResourceLocation.equals(modelblock.getParentLocation())) {
				return (ResourceLocation) entry.getKey();
			}
		}

		return null;
	}

	private Set<ResourceLocation> getTextureLocations(ModelBlock parModelBlock) {
		HashSet hashset = Sets.newHashSet();

		for (BlockPart blockpart : parModelBlock.getElements()) {
			for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
				ResourceLocation resourcelocation = new ResourceLocation(
						parModelBlock.resolveTextureName(blockpartface.texture));
				hashset.add(resourcelocation);
			}
		}

		hashset.add(new ResourceLocation(parModelBlock.resolveTextureName("particle")));
		return hashset;
	}

	private void loadSprites() {
		final Set set = this.getVariantsTextureLocations();
		set.addAll(this.getItemsTextureLocations());
		set.remove(TextureMap.LOCATION_MISSING_TEXTURE);
		IIconCreator iiconcreator = new IIconCreator() {
			public void registerSprites(TextureMap texturemap) {
				for (ResourceLocation resourcelocation : (Set<ResourceLocation>) set) {
					EaglerTextureAtlasSprite textureatlassprite = texturemap.registerSprite(resourcelocation);
					ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
				}

			}
		};
		this.textureMap.loadSprites(this.resourceManager, iiconcreator);
		this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
	}

	private Set<ResourceLocation> getItemsTextureLocations() {
		HashSet hashset = Sets.newHashSet();

		for (ResourceLocation resourcelocation : this.itemLocations.values()) {
			ModelBlock modelblock = (ModelBlock) this.models.get(resourcelocation);
			if (modelblock != null) {
				hashset.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
				if (this.hasItemModel(modelblock)) {
					for (String s : ItemModelGenerator.LAYERS) {
						ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(s));
						if (modelblock.getRootModel() == MODEL_COMPASS
								&& !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
							EaglerTextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());
						} else if (modelblock.getRootModel() == MODEL_CLOCK
								&& !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
							EaglerTextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());
						}

						hashset.add(resourcelocation2);
					}
				} else if (!this.isCustomRenderer(modelblock)) {
					for (BlockPart blockpart : modelblock.getElements()) {
						for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
							ResourceLocation resourcelocation1 = new ResourceLocation(
									modelblock.resolveTextureName(blockpartface.texture));
							hashset.add(resourcelocation1);
						}
					}
				}
			}
		}

		return hashset;
	}

	private boolean hasItemModel(ModelBlock parModelBlock) {
		if (parModelBlock == null) {
			return false;
		} else {
			ModelBlock modelblock = parModelBlock.getRootModel();
			return modelblock == MODEL_GENERATED || modelblock == MODEL_COMPASS || modelblock == MODEL_CLOCK;
		}
	}

	private boolean isCustomRenderer(ModelBlock parModelBlock) {
		if (parModelBlock == null) {
			return false;
		} else {
			ModelBlock modelblock = parModelBlock.getRootModel();
			return modelblock == MODEL_ENTITY;
		}
	}

	private void bakeItemModels() {
		for (ResourceLocation resourcelocation : this.itemLocations.values()) {
			ModelBlock modelblock = (ModelBlock) this.models.get(resourcelocation);
			if (this.hasItemModel(modelblock)) {
				ModelBlock modelblock1 = this.makeItemModel(modelblock);
				if (modelblock1 != null) {
					modelblock1.name = resourcelocation.toString();
				}

				this.models.put(resourcelocation, modelblock1);
			} else if (this.isCustomRenderer(modelblock)) {
				this.models.put(resourcelocation, modelblock);
			}
		}

		for (EaglerTextureAtlasSprite textureatlassprite : this.sprites.values()) {
			if (!textureatlassprite.hasAnimationMetadata()) {
				textureatlassprite.clearFramesTextureData();
			}
		}

	}

	private ModelBlock makeItemModel(ModelBlock parModelBlock) {
		return this.itemModelGenerator.makeItemModel(this.textureMap, parModelBlock);
	}

	// eagler hack
	public String getBaseTextureForBlockPre(int blockId, int metadata) {
		Block block = Block.blockRegistry.getObjectById(blockId);
		if (block != null) {
			IBlockState state = block.getStateFromMeta(metadata);
			if (state != null) {
				IStateMapper mapper = blockModelShapes.getBlockStateMapper().blockStateMap.get(block);
				ModelResourceLocation loc = null;
				if (mapper != null) {
					loc = mapper.putStateModelLocations(block).get(state);
				}
				if (loc == null) {
					loc = new ModelResourceLocation(Block.blockRegistry.getNameForObject(block),
							StateMapperBase.getPropertyString(state.getProperties()));
				}
				ModelBlockDefinition.Variants v = variants.get(loc);
				if (v != null && v.getVariants().size() > 0) {
					ModelBlockDefinition.Variant vv = v.getVariants().get(0);
					ModelBlock model = this.models.get(vv.getModelLocation());
					if (model != null) {
						String name = model.resolveTextureName("particle");
						if (name != null) {
							return name;
						}
					}
				}
			}
		}
		return null;
	}

	static {
		BUILT_IN_MODELS.put("missing",
				"{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
		MODEL_GENERATED.name = "generation marker";
		MODEL_COMPASS.name = "compass generation marker";
		MODEL_CLOCK.name = "class generation marker";
		MODEL_ENTITY.name = "block entity marker";
	}
}