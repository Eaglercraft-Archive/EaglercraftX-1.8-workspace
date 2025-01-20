package net.minecraft.world.chunk.storage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.RandomAccessMemoryFile;

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
public class RegionFile {
	private static final byte[] emptySector = new byte[4096];
	private RandomAccessMemoryFile dataFile;
	private final int[] offsets = new int[1024];
	private final int[] chunkTimestamps = new int[1024];
	private List<Boolean> sectorFree;
	private int sizeDelta;

	public RegionFile(RandomAccessMemoryFile dataFile) {
		this.sizeDelta = 0;

		try {
			this.dataFile = dataFile;
			if (this.dataFile.getLength() < 4096) {
				for (int i = 0; i < 1024; ++i) {
					this.dataFile.writeInt(0);
				}

				for (int i1 = 0; i1 < 1024; ++i1) {
					this.dataFile.writeInt(0);
				}

				this.sizeDelta += 8192;
			}

			if ((this.dataFile.getLength() & 4095L) != 0L) {
				for (int j1 = 0; (long) j1 < (this.dataFile.getLength() & 4095L); ++j1) {
					this.dataFile.write(0);
				}
			}

			int k1 = (int) this.dataFile.getLength() / 4096;
			this.sectorFree = Lists.newArrayListWithCapacity(k1);

			for (int j = 0; j < k1; ++j) {
				this.sectorFree.add(Boolean.valueOf(true));
			}

			this.sectorFree.set(0, Boolean.valueOf(false));
			this.sectorFree.set(1, Boolean.valueOf(false));
			this.dataFile.seek(0);

			for (int l1 = 0; l1 < 1024; ++l1) {
				int k = this.dataFile.readInt();
				this.offsets[l1] = k;
				if (k != 0 && (k >> 8) + (k & 255) <= this.sectorFree.size()) {
					for (int l = 0; l < (k & 255); ++l) {
						this.sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
					}
				}
			}

			for (int i2 = 0; i2 < 1024; ++i2) {
				int j2 = this.dataFile.readInt();
				this.chunkTimestamps[i2] = j2;
			}
		} catch (IOException ioexception) {
			throw new RuntimeException("Could not initialize RegionFile!", ioexception);
		}

	}

	/**+
	 * Returns an uncompressed chunk stream from the region file.
	 */
	public synchronized DataInputStream getChunkDataInputStream(int x, int z) {
		if (this.outOfBounds(x, z)) {
			return null;
		} else {
			try {
				int i = this.getOffset(x, z);
				if (i == 0) {
					return null;
				} else {
					int j = i >> 8;
					int k = i & 255;
					if (j + k > this.sectorFree.size()) {
						return null;
					} else {
						this.dataFile.seek(j * 4096);
						int l = this.dataFile.readInt();
						if (l > 4096 * k) {
							return null;
						} else if (l <= 0) {
							return null;
						} else {
							byte b0 = this.dataFile.readByte();
							if (b0 == 1) {
								byte[] abyte1 = new byte[l - 1];
								this.dataFile.read(abyte1);
								return new DataInputStream(new BufferedInputStream(
										EaglerZLIB.newGZIPInputStream(new EaglerInputStream(abyte1))));
							} else if (b0 == 2) {
								byte[] abyte = new byte[l - 1];
								this.dataFile.read(abyte);
								return new DataInputStream(new BufferedInputStream(
										EaglerZLIB.newInflaterInputStream(new EaglerInputStream(abyte))));
							} else {
								return null;
							}
						}
					}
				}
			} catch (IOException var9) {
				return null;
			}
		}
	}

	/**+
	 * Returns an output stream used to write chunk data. Data is on
	 * disk when the returned stream is closed.
	 */
	public DataOutputStream getChunkDataOutputStream(int x, int z) throws IOException {
		return this.outOfBounds(x, z) ? null
				: new DataOutputStream(EaglerZLIB.newDeflaterOutputStream(new RegionFile.ChunkBuffer(x, z)));
	}

	/**+
	 * args: x, z, data, length - write chunk data at (x, z) to disk
	 */
	protected synchronized void write(int x, int z, byte[] data, int length) {
		try {
			int i = this.getOffset(x, z);
			int j = i >> 8;
			int k = i & 255;
			int l = (length + 5) / 4096 + 1;
			if (l >= 256) {
				return;
			}

			if (j != 0 && k == l) {
				this.write(j, data, length);
			} else {
				for (int i1 = 0; i1 < k; ++i1) {
					this.sectorFree.set(j + i1, Boolean.valueOf(true));
				}

				int l1 = this.sectorFree.indexOf(Boolean.valueOf(true));
				int j1 = 0;
				if (l1 != -1) {
					for (int k1 = l1; k1 < this.sectorFree.size(); ++k1) {
						if (j1 != 0) {
							if (((Boolean) this.sectorFree.get(k1)).booleanValue()) {
								++j1;
							} else {
								j1 = 0;
							}
						} else if (((Boolean) this.sectorFree.get(k1)).booleanValue()) {
							l1 = k1;
							j1 = 1;
						}

						if (j1 >= l) {
							break;
						}
					}
				}

				if (j1 >= l) {
					j = l1;
					this.setOffset(x, z, l1 << 8 | l);

					for (int j2 = 0; j2 < l; ++j2) {
						this.sectorFree.set(j + j2, Boolean.valueOf(false));
					}

					this.write(j, data, length);
				} else {
					this.dataFile.seek(this.dataFile.getLength());
					j = this.sectorFree.size();

					for (int i2 = 0; i2 < l; ++i2) {
						this.dataFile.write(emptySector);
						this.sectorFree.add(Boolean.valueOf(false));
					}

					this.sizeDelta += 4096 * l;
					this.write(j, data, length);
					this.setOffset(x, z, j << 8 | l);
				}
			}

			this.setChunkTimestamp(x, z, (int) (System.currentTimeMillis() / 1000L));
		} catch (IOException ioexception) {
			throw new RuntimeException("Could not write chunk to RegionFile!", ioexception);
		}

	}

	/**+
	 * args: x, z, data, length - write chunk data at (x, z) to disk
	 */
	private void write(int sectorNumber, byte[] data, int length) throws IOException {
		this.dataFile.seek(sectorNumber * 4096);
		this.dataFile.writeInt(length + 1);
		this.dataFile.writeByte(2);
		this.dataFile.write(data, 0, length);
	}

	/**+
	 * args: x, z - check region bounds
	 */
	private boolean outOfBounds(int x, int z) {
		return x < 0 || x >= 32 || z < 0 || z >= 32;
	}

	/**+
	 * args: x, z - get chunk's offset in region file
	 */
	private int getOffset(int x, int z) {
		return this.offsets[x + z * 32];
	}

	/**+
	 * args: x, z, - true if chunk has been saved / converted
	 */
	public boolean isChunkSaved(int x, int z) {
		return this.getOffset(x, z) != 0;
	}

	/**+
	 * args: x, z, offset - sets the chunk's offset in the region
	 * file
	 */
	private void setOffset(int x, int z, int offset) throws IOException {
		this.offsets[x + z * 32] = offset;
		this.dataFile.seek((x + z * 32) * 4);
		this.dataFile.writeInt(offset);
	}

	/**+
	 * args: x, z, timestamp - sets the chunk's write timestamp
	 */
	private void setChunkTimestamp(int x, int z, int timestamp) throws IOException {
		this.chunkTimestamps[x + z * 32] = timestamp;
		this.dataFile.seek(4096 + (x + z * 32) * 4);
		this.dataFile.writeInt(timestamp);
	}

	public RandomAccessMemoryFile getFile() {
		return dataFile;
	}

	class ChunkBuffer extends EaglerOutputStream {
		private int chunkX;
		private int chunkZ;

		public ChunkBuffer(int x, int z) {
			super(8096);
			this.chunkX = x;
			this.chunkZ = z;
		}

		/**+
		 * close this RegionFile and prevent further writes
		 */
		public void close() throws IOException {
			RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
		}
	}
}