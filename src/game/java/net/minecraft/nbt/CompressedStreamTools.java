package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

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
public class CompressedStreamTools {
	/**+
	 * Load the gzipped compound from the inputstream.
	 */
	public static NBTTagCompound readCompressed(InputStream is) throws IOException {
		DataInputStream datainputstream = new DataInputStream(
				new BufferedInputStream(EaglerZLIB.newGZIPInputStream(is)));

		NBTTagCompound nbttagcompound;
		try {
			nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
		} finally {
			datainputstream.close();
		}

		return nbttagcompound;
	}

	/**+
	 * Write the compound, gzipped, to the outputstream.
	 */
	public static void writeCompressed(NBTTagCompound outputStream, OutputStream parOutputStream) throws IOException {
		DataOutputStream dataoutputstream = new DataOutputStream(
				new BufferedOutputStream(EaglerZLIB.newGZIPOutputStream(parOutputStream)));

		try {
			write(outputStream, (DataOutput) dataoutputstream);
		} finally {
			dataoutputstream.close();
		}

	}

	/**+
	 * Reads the given DataInput, constructs, and returns an
	 * NBTTagCompound with the data from the DataInput
	 */
	public static NBTTagCompound read(DataInputStream inputStream) throws IOException {
		/**+
		 * Reads the given DataInput, constructs, and returns an
		 * NBTTagCompound with the data from the DataInput
		 */
		return read(inputStream, NBTSizeTracker.INFINITE);
	}

	/**+
	 * Reads the given DataInput, constructs, and returns an
	 * NBTTagCompound with the data from the DataInput
	 */
	public static NBTTagCompound read(DataInput parDataInput, NBTSizeTracker parNBTSizeTracker) throws IOException {
		NBTBase nbtbase = func_152455_a(parDataInput, 0, parNBTSizeTracker);
		if (nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound) nbtbase;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void write(NBTTagCompound parNBTTagCompound, DataOutput parDataOutput) throws IOException {
		writeTag(parNBTTagCompound, parDataOutput);
	}

	private static void writeTag(NBTBase parNBTBase, DataOutput parDataOutput) throws IOException {
		parDataOutput.writeByte(parNBTBase.getId());
		if (parNBTBase.getId() != 0) {
			parDataOutput.writeUTF("");
			parNBTBase.write(parDataOutput);
		}
	}

	private static NBTBase func_152455_a(DataInput parDataInput, int parInt1, NBTSizeTracker parNBTSizeTracker)
			throws IOException {
		byte b0 = parDataInput.readByte();
		if (b0 == 0) {
			return new NBTTagEnd();
		} else {
			parDataInput.readUTF();
			NBTBase nbtbase = NBTBase.createNewByType(b0);

			try {
				nbtbase.read(parDataInput, parInt1, parNBTSizeTracker);
				return nbtbase;
			} catch (IOException ioexception) {
				CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
				crashreportcategory.addCrashSection("Tag name", "[UNNAMED TAG]");
				crashreportcategory.addCrashSection("Tag type", Byte.valueOf(b0));
				throw new ReportedException(crashreport);
			}
		}
	}
}