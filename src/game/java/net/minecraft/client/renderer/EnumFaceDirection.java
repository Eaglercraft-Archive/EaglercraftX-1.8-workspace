package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

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
public enum EnumFaceDirection {
	DOWN(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX) }),
	UP(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX) }),
	NORTH(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX) }),
	SOUTH(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX) }),
	WEST(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.WEST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX) }),
	EAST(new EnumFaceDirection.VertexInformation[] {
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.SOUTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.DOWN_INDEX, EnumFaceDirection.Constants.NORTH_INDEX),
			new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.EAST_INDEX,
					EnumFaceDirection.Constants.UP_INDEX, EnumFaceDirection.Constants.NORTH_INDEX) });

	private static final EnumFaceDirection[] facings = new EnumFaceDirection[6];
	private final EnumFaceDirection.VertexInformation[] vertexInfos;

	public static EnumFaceDirection getFacing(EnumFacing facing) {
		return facings[facing.getIndex()];
	}

	private EnumFaceDirection(EnumFaceDirection.VertexInformation[] vertexInfosIn) {
		this.vertexInfos = vertexInfosIn;
	}

	public EnumFaceDirection.VertexInformation func_179025_a(int parInt1) {
		return this.vertexInfos[parInt1];
	}

	static {
		facings[EnumFaceDirection.Constants.DOWN_INDEX] = DOWN;
		facings[EnumFaceDirection.Constants.UP_INDEX] = UP;
		facings[EnumFaceDirection.Constants.NORTH_INDEX] = NORTH;
		facings[EnumFaceDirection.Constants.SOUTH_INDEX] = SOUTH;
		facings[EnumFaceDirection.Constants.WEST_INDEX] = WEST;
		facings[EnumFaceDirection.Constants.EAST_INDEX] = EAST;
	}

	public static final class Constants {
		public static final int SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
		public static final int UP_INDEX = EnumFacing.UP.getIndex();
		public static final int EAST_INDEX = EnumFacing.EAST.getIndex();
		public static final int NORTH_INDEX = EnumFacing.NORTH.getIndex();
		public static final int DOWN_INDEX = EnumFacing.DOWN.getIndex();
		public static final int WEST_INDEX = EnumFacing.WEST.getIndex();
	}

	public static class VertexInformation {
		public final int field_179184_a;
		public final int field_179182_b;
		public final int field_179183_c;

		private VertexInformation(int parInt1, int parInt2, int parInt3) {
			this.field_179184_a = parInt1;
			this.field_179182_b = parInt2;
			this.field_179183_c = parInt3;
		}
	}
}