package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

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
public abstract class StateMapperBase implements IStateMapper {
	protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();

	public static String getPropertyString(Map<IProperty, Comparable> parMap) {
		StringBuilder stringbuilder = new StringBuilder();

		for (Entry entry : parMap.entrySet()) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}

			IProperty iproperty = (IProperty) entry.getKey();
			Comparable comparable = (Comparable) entry.getValue();
			stringbuilder.append(iproperty.getName());
			stringbuilder.append("=");
			stringbuilder.append(iproperty.getName(comparable));
		}

		if (stringbuilder.length() == 0) {
			stringbuilder.append("normal");
		}

		return stringbuilder.toString();
	}

	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
		for (IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
			this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
		}

		return this.mapStateModelLocations;
	}

	protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);
}