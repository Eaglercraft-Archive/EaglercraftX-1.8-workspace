package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class MapGenStructureIO {
	private static final Logger logger = LogManager.getLogger();
	private static Map<String, Class<? extends StructureStart>> startNameToClassMap = Maps.newHashMap();
	private static Map<Class<? extends StructureStart>, String> startClassToNameMap = Maps.newHashMap();
	private static Map<String, Class<? extends StructureComponent>> componentNameToClassMap = Maps.newHashMap();
	private static Map<Class<? extends StructureComponent>, String> componentClassToNameMap = Maps.newHashMap();

	private static void registerStructure(Class<? extends StructureStart> startClass, String structureName) {
		startNameToClassMap.put(structureName, startClass);
		startClassToNameMap.put(startClass, structureName);
	}

	static void registerStructureComponent(Class<? extends StructureComponent> componentClass, String componentName) {
		componentNameToClassMap.put(componentName, componentClass);
		componentClassToNameMap.put(componentClass, componentName);
	}

	public static String getStructureStartName(StructureStart start) {
		return (String) startClassToNameMap.get(start.getClass());
	}

	public static String getStructureComponentName(StructureComponent component) {
		return (String) componentClassToNameMap.get(component.getClass());
	}

	public static StructureStart getStructureStart(NBTTagCompound tagCompound, World worldIn) {
		StructureStart structurestart = null;

		try {
			Class oclass = (Class) startNameToClassMap.get(tagCompound.getString("id"));
			if (oclass != null) {
				structurestart = (StructureStart) oclass.newInstance();
			}
		} catch (Exception exception) {
			logger.warn("Failed Start with id " + tagCompound.getString("id"));
			logger.warn(exception);
		}

		if (structurestart != null) {
			structurestart.readStructureComponentsFromNBT(worldIn, tagCompound);
		} else {
			logger.warn("Skipping Structure with id " + tagCompound.getString("id"));
		}

		return structurestart;
	}

	public static StructureComponent getStructureComponent(NBTTagCompound tagCompound, World worldIn) {
		StructureComponent structurecomponent = null;

		try {
			Class oclass = (Class) componentNameToClassMap.get(tagCompound.getString("id"));
			if (oclass != null) {
				structurecomponent = (StructureComponent) oclass.newInstance();
			}
		} catch (Exception exception) {
			logger.warn("Failed Piece with id " + tagCompound.getString("id"));
			logger.warn(exception);
		}

		if (structurecomponent != null) {
			structurecomponent.readStructureBaseNBT(worldIn, tagCompound);
		} else {
			logger.warn("Skipping Piece with id " + tagCompound.getString("id"));
		}

		return structurecomponent;
	}

	static {
		registerStructure(StructureMineshaftStart.class, "Mineshaft");
		registerStructure(MapGenVillage.Start.class, "Village");
		registerStructure(MapGenNetherBridge.Start.class, "Fortress");
		registerStructure(MapGenStronghold.Start.class, "Stronghold");
		registerStructure(MapGenScatteredFeature.Start.class, "Temple");
		registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
		StructureMineshaftPieces.registerStructurePieces();
		StructureVillagePieces.registerVillagePieces();
		StructureNetherBridgePieces.registerNetherFortressPieces();
		StructureStrongholdPieces.registerStrongholdPieces();
		ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
		StructureOceanMonumentPieces.registerOceanMonumentPieces();
	}
}