package net.minecraft.network;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.DecoderException;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.EncoderException;

import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBufInputStream;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBufOutputStream;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

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
public class PacketBuffer extends ByteBuf {
	private final ByteBuf buf;

	public PacketBuffer(ByteBuf wrapped) {
		this.buf = wrapped;
	}

	/**+
	 * Calculates the number of bytes required to fit the supplied
	 * int (0-5) if it were to be read/written using
	 * readVarIntFromBuffer or writeVarIntToBuffer
	 */
	public static int getVarIntSize(int input) {
		for (int i = 1; i < 5; ++i) {
			if ((input & -1 << i * 7) == 0) {
				return i;
			}
		}

		return 5;
	}

	public void writeByteArray(byte[] array) {
		this.writeVarIntToBuffer(array.length);
		this.writeBytes(array);
	}

	public byte[] readByteArray() {
		byte[] abyte = new byte[this.readVarIntFromBuffer()];
		this.readBytes(abyte);
		return abyte;
	}

	public BlockPos readBlockPos() {
		return BlockPos.fromLong(this.readLong());
	}

	public void writeBlockPos(BlockPos pos) {
		this.writeLong(pos.toLong());
	}

	public IChatComponent readChatComponent() throws IOException {
		return IChatComponent.Serializer.jsonToComponent(this.readStringFromBuffer(32767));
	}

	public void writeChatComponent(IChatComponent component) throws IOException {
		this.writeString(IChatComponent.Serializer.componentToJson(component));
	}

	public <T extends Enum<T>> T readEnumValue(Class<T> enumClass) {
		return (T) ((Enum[]) enumClass.getEnumConstants())[this.readVarIntFromBuffer()];
	}

	public void writeEnumValue(Enum<?> value) {
		this.writeVarIntToBuffer(value.ordinal());
	}

	/**+
	 * Reads a compressed int from the buffer. To do so it maximally
	 * reads 5 byte-sized chunks whose most significant bit dictates
	 * whether another byte should be read.
	 */
	public int readVarIntFromBuffer() {
		int i = 0;
		int j = 0;

		while (true) {
			byte b0 = this.readByte();
			i |= (b0 & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}

			if ((b0 & 128) != 128) {
				break;
			}
		}

		return i;
	}

	public long readVarLong() {
		long i = 0L;
		int j = 0;

		while (true) {
			byte b0 = this.readByte();
			i |= (long) (b0 & 127) << j++ * 7;
			if (j > 10) {
				throw new RuntimeException("VarLong too big");
			}

			if ((b0 & 128) != 128) {
				break;
			}
		}

		return i;
	}

	public void writeUuid(EaglercraftUUID uuid) {
		this.writeLong(uuid.getMostSignificantBits());
		this.writeLong(uuid.getLeastSignificantBits());
	}

	public EaglercraftUUID readUuid() {
		return new EaglercraftUUID(this.readLong(), this.readLong());
	}

	/**+
	 * Writes a compressed int to the buffer. The smallest number of
	 * bytes to fit the passed int will be written. Of each such
	 * byte only 7 bits will be used to describe the actual value
	 * since its most significant bit dictates whether the next byte
	 * is part of that same int. Micro-optimization for int values
	 * that are expected to have values below 128.
	 */
	public void writeVarIntToBuffer(int input) {
		while ((input & -128) != 0) {
			this.writeByte(input & 127 | 128);
			input >>>= 7;
		}

		this.writeByte(input);
	}

	public void writeVarLong(long value) {
		while ((value & -128L) != 0L) {
			this.writeByte((int) (value & 127L) | 128);
			value >>>= 7;
		}

		this.writeByte((int) value);
	}

	/**+
	 * Writes a compressed NBTTagCompound to this buffer
	 */
	public void writeNBTTagCompoundToBuffer(NBTTagCompound nbt) {
		if (nbt == null) {
			this.writeByte(0);
		} else {
			try {
				CompressedStreamTools.write(nbt, (DataOutput) (new ByteBufOutputStream(this)));
			} catch (IOException ioexception) {
				throw new EncoderException(ioexception);
			}
		}

	}

	/**+
	 * Reads a compressed NBTTagCompound from this buffer
	 */
	public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
		int i = this.readerIndex();
		byte b0 = this.readByte();
		if (b0 == 0) {
			return null;
		} else {
			this.readerIndex(i);
			return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
		}
	}

	/**+
	 * Writes the ItemStack's ID (short), then size (byte), then
	 * damage. (short)
	 */
	public void writeItemStackToBuffer(ItemStack stack) {
		if (stack == null) {
			this.writeShort(-1);
		} else {
			this.writeShort(Item.getIdFromItem(stack.getItem()));
			this.writeByte(stack.stackSize);
			this.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;
			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getTagCompound();
			}

			this.writeNBTTagCompoundToBuffer(nbttagcompound);
		}

	}

	/**+
	 * Reads an ItemStack from this buffer
	 */
	public ItemStack readItemStackFromBuffer() throws IOException {
		ItemStack itemstack = null;
		short short1 = this.readShort();
		if (short1 >= 0) {
			byte b0 = this.readByte();
			short short2 = this.readShort();
			itemstack = new ItemStack(Item.getItemById(short1), b0, short2);
			itemstack.setTagCompound(this.readNBTTagCompoundFromBuffer());
		}

		return itemstack;
	}

	/**+
	 * Reads a string from this buffer. Expected parameter is
	 * maximum allowed string length. Will throw IOException if
	 * string length exceeds this value!
	 */
	public String readStringFromBuffer(int maxLength) {
		int i = this.readVarIntFromBuffer();
		if (i > maxLength * 4) {
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i
					+ " > " + maxLength * 4 + ")");
		} else if (i < 0) {
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			String s = new String(this.readBytes(i).array(), StandardCharsets.UTF_8);
			if (s.length() > maxLength) {
				throw new DecoderException(
						"The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
			} else {
				return s;
			}
		}
	}

	public PacketBuffer writeString(String string) {
		byte[] abyte = string.getBytes(StandardCharsets.UTF_8);
		if (abyte.length > 32767) {
			throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
		} else {
			this.writeVarIntToBuffer(abyte.length);
			this.writeBytes(abyte);
			return this;
		}
	}

	public int capacity() {
		return this.buf.capacity();
	}

	public ByteBuf capacity(int i) {
		return this.buf.capacity(i);
	}

	public int maxCapacity() {
		return this.buf.maxCapacity();
	}

	public ByteOrder order() {
		return this.buf.order();
	}

	public ByteBuf order(ByteOrder byteorder) {
		return this.buf.order(byteorder);
	}

	public ByteBuf unwrap() {
		return this.buf.unwrap();
	}

	public boolean isDirect() {
		return this.buf.isDirect();
	}

	public int readerIndex() {
		return this.buf.readerIndex();
	}

	public ByteBuf readerIndex(int i) {
		return this.buf.readerIndex(i);
	}

	public int writerIndex() {
		return this.buf.writerIndex();
	}

	public ByteBuf writerIndex(int i) {
		return this.buf.writerIndex(i);
	}

	public ByteBuf setIndex(int parInt1, int parInt2) {
		return this.buf.setIndex(parInt1, parInt2);
	}

	public int readableBytes() {
		return this.buf.readableBytes();
	}

	public int writableBytes() {
		return this.buf.writableBytes();
	}

	public int maxWritableBytes() {
		return this.buf.maxWritableBytes();
	}

	public boolean isReadable() {
		return this.buf.isReadable();
	}

	public boolean isReadable(int i) {
		return this.buf.isReadable(i);
	}

	public boolean isWritable() {
		return this.buf.isWritable();
	}

	public boolean isWritable(int i) {
		return this.buf.isWritable(i);
	}

	public ByteBuf clear() {
		return this.buf.clear();
	}

	public ByteBuf markReaderIndex() {
		return this.buf.markReaderIndex();
	}

	public ByteBuf resetReaderIndex() {
		return this.buf.resetReaderIndex();
	}

	public ByteBuf markWriterIndex() {
		return this.buf.markWriterIndex();
	}

	public ByteBuf resetWriterIndex() {
		return this.buf.resetWriterIndex();
	}

	public ByteBuf discardReadBytes() {
		return this.buf.discardReadBytes();
	}

	public ByteBuf discardSomeReadBytes() {
		return this.buf.discardSomeReadBytes();
	}

	public ByteBuf ensureWritable(int parInt1) {
		return this.buf.ensureWritable(parInt1);
	}

	public int ensureWritable(int i, boolean flag) {
		return this.buf.ensureWritable(i, flag);
	}

	public boolean getBoolean(int parInt1) {
		return this.buf.getBoolean(parInt1);
	}

	public byte getByte(int parInt1) {
		return this.buf.getByte(parInt1);
	}

	public short getUnsignedByte(int parInt1) {
		return this.buf.getUnsignedByte(parInt1);
	}

	public short getShort(int parInt1) {
		return this.buf.getShort(parInt1);
	}

	public int getUnsignedShort(int parInt1) {
		return this.buf.getUnsignedShort(parInt1);
	}

	public int getMedium(int parInt1) {
		return this.buf.getMedium(parInt1);
	}

	public int getUnsignedMedium(int parInt1) {
		return this.buf.getUnsignedMedium(parInt1);
	}

	public int getInt(int parInt1) {
		return this.buf.getInt(parInt1);
	}

	public long getUnsignedInt(int parInt1) {
		return this.buf.getUnsignedInt(parInt1);
	}

	public long getLong(int parInt1) {
		return this.buf.getLong(parInt1);
	}

	public char getChar(int parInt1) {
		return this.buf.getChar(parInt1);
	}

	public float getFloat(int parInt1) {
		return this.buf.getFloat(parInt1);
	}

	public double getDouble(int parInt1) {
		return this.buf.getDouble(parInt1);
	}

	public ByteBuf getBytes(int parInt1, ByteBuf parByteBuf) {
		if (parByteBuf instanceof PacketBuffer) {
			return this.buf.getBytes(parInt1, ((PacketBuffer) parByteBuf).buf);
		} else {
			return this.buf.getBytes(parInt1, parByteBuf);
		}
	}

	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j);
		} else {
			return this.buf.getBytes(i, bytebuf, j);
		}
	}

	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
		} else {
			return this.buf.getBytes(i, bytebuf, j, k);
		}
	}

	public ByteBuf getBytes(int i, byte[] abyte) {
		return this.buf.getBytes(i, abyte);
	}

	public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
		return this.buf.getBytes(i, abyte, j, k);
	}

	public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
		return this.buf.getBytes(i, bytebuffer);
	}

	public ByteBuf getBytes(int parInt1, OutputStream parOutputStream, int parInt2) throws IOException {
		return this.buf.getBytes(parInt1, parOutputStream, parInt2);
	}

	public ByteBuf setBoolean(int parInt1, boolean parFlag) {
		return this.buf.setBoolean(parInt1, parFlag);
	}

	public ByteBuf setByte(int parInt1, int parInt2) {
		return this.buf.setByte(parInt1, parInt2);
	}

	public ByteBuf setShort(int parInt1, int parInt2) {
		return this.buf.setShort(parInt1, parInt2);
	}

	public ByteBuf setMedium(int parInt1, int parInt2) {
		return this.buf.setMedium(parInt1, parInt2);
	}

	public ByteBuf setInt(int parInt1, int parInt2) {
		return this.buf.setInt(parInt1, parInt2);
	}

	public ByteBuf setLong(int parInt1, long parLong1) {
		return this.buf.setLong(parInt1, parLong1);
	}

	public ByteBuf setChar(int parInt1, int parInt2) {
		return this.buf.setChar(parInt1, parInt2);
	}

	public ByteBuf setFloat(int parInt1, float parFloat1) {
		return this.buf.setFloat(parInt1, parFloat1);
	}

	public ByteBuf setDouble(int parInt1, double parDouble1) {
		return this.buf.setDouble(parInt1, parDouble1);
	}

	public ByteBuf setBytes(int parInt1, ByteBuf parByteBuf) {
		return this.buf.setBytes(parInt1, parByteBuf);
	}

	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j);
		} else {
			return this.buf.setBytes(i, bytebuf, j);
		}
	}

	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
		} else {
			return this.buf.setBytes(i, bytebuf, j, k);
		}
	}

	public ByteBuf setBytes(int i, byte[] abyte) {
		return this.buf.setBytes(i, abyte);
	}

	public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
		return this.buf.setBytes(i, abyte, j, k);
	}

	public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
		return this.buf.setBytes(i, bytebuffer);
	}

	public int setBytes(int parInt1, InputStream parInputStream, int parInt2) throws IOException {
		return this.buf.setBytes(parInt1, parInputStream, parInt2);
	}

	public ByteBuf setZero(int parInt1, int parInt2) {
		return this.buf.setZero(parInt1, parInt2);
	}

	public boolean readBoolean() {
		return this.buf.readBoolean();
	}

	public byte readByte() {
		return this.buf.readByte();
	}

	public short readUnsignedByte() {
		return this.buf.readUnsignedByte();
	}

	public short readShort() {
		return this.buf.readShort();
	}

	public int readUnsignedShort() {
		return this.buf.readUnsignedShort();
	}

	public int readMedium() {
		return this.buf.readMedium();
	}

	public int readUnsignedMedium() {
		return this.buf.readUnsignedMedium();
	}

	public int readInt() {
		return this.buf.readInt();
	}

	public long readUnsignedInt() {
		return this.buf.readUnsignedInt();
	}

	public long readLong() {
		return this.buf.readLong();
	}

	public char readChar() {
		return this.buf.readChar();
	}

	public float readFloat() {
		return this.buf.readFloat();
	}

	public double readDouble() {
		return this.buf.readDouble();
	}

	public ByteBuf readBytes(int parInt1) {
		return this.buf.readBytes(parInt1);
	}

	public ByteBuf readSlice(int parInt1) {
		return this.buf.readSlice(parInt1);
	}

	public ByteBuf readBytes(ByteBuf bytebuf) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.readBytes(((PacketBuffer) bytebuf).buf);
		} else {
			return this.buf.readBytes(bytebuf);
		}
	}

	public ByteBuf readBytes(ByteBuf bytebuf, int i) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i);
		} else {
			return this.buf.readBytes(bytebuf, i);
		}
	}

	public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i, j);
		} else {
			return this.buf.readBytes(bytebuf, i, j);
		}
	}

	public ByteBuf readBytes(byte[] abyte) {
		return this.buf.readBytes(abyte);
	}

	public ByteBuf readBytes(byte[] abyte, int i, int j) {
		return this.buf.readBytes(abyte, i, j);
	}

	public ByteBuf readBytes(ByteBuffer bytebuffer) {
		return this.buf.readBytes(bytebuffer);
	}

	public ByteBuf readBytes(OutputStream parOutputStream, int parInt1) throws IOException {
		return this.buf.readBytes(parOutputStream, parInt1);
	}

	public ByteBuf skipBytes(int parInt1) {
		return this.buf.skipBytes(parInt1);
	}

	public ByteBuf writeBoolean(boolean parFlag) {
		return this.buf.writeBoolean(parFlag);
	}

	public ByteBuf writeByte(int parInt1) {
		return this.buf.writeByte(parInt1);
	}

	public ByteBuf writeShort(int parInt1) {
		return this.buf.writeShort(parInt1);
	}

	public ByteBuf writeMedium(int parInt1) {
		return this.buf.writeMedium(parInt1);
	}

	public ByteBuf writeInt(int parInt1) {
		return this.buf.writeInt(parInt1);
	}

	public ByteBuf writeLong(long parLong1) {
		return this.buf.writeLong(parLong1);
	}

	public ByteBuf writeChar(int parInt1) {
		return this.buf.writeChar(parInt1);
	}

	public ByteBuf writeFloat(float parFloat1) {
		return this.buf.writeFloat(parFloat1);
	}

	public ByteBuf writeDouble(double parDouble1) {
		return this.buf.writeDouble(parDouble1);
	}

	public ByteBuf writeBytes(ByteBuf parByteBuf) {
		if (parByteBuf instanceof PacketBuffer) {
			return this.buf.writeBytes(((PacketBuffer) parByteBuf).buf);
		} else {
			return this.buf.writeBytes(parByteBuf);
		}
	}

	public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i);
		} else {
			return this.buf.writeBytes(bytebuf, i);
		}
	}

	public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
		if (bytebuf instanceof PacketBuffer) {
			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i, j);
		} else {
			return this.buf.writeBytes(bytebuf, i, j);
		}
	}

	public ByteBuf writeBytes(byte[] abyte) {
		return this.buf.writeBytes(abyte);
	}

	public ByteBuf writeBytes(byte[] abyte, int i, int j) {
		return this.buf.writeBytes(abyte, i, j);
	}

	public ByteBuf writeBytes(ByteBuffer bytebuffer) {
		return this.buf.writeBytes(bytebuffer);
	}

	public int writeBytes(InputStream parInputStream, int parInt1) throws IOException {
		return this.buf.writeBytes(parInputStream, parInt1);
	}

	public ByteBuf writeZero(int parInt1) {
		return this.buf.writeZero(parInt1);
	}

	public int indexOf(int parInt1, int parInt2, byte parByte1) {
		return this.buf.indexOf(parInt1, parInt2, parByte1);
	}

	public int bytesBefore(byte parByte1) {
		return this.buf.bytesBefore(parByte1);
	}

	public int bytesBefore(int i, byte b0) {
		return this.buf.bytesBefore(i, b0);
	}

	public int bytesBefore(int i, int j, byte b0) {
		return this.buf.bytesBefore(i, j, b0);
	}

	public ByteBuf copy() {
		return this.buf.copy();
	}

	public ByteBuf copy(int i, int j) {
		return this.buf.copy(i, j);
	}

	public ByteBuf slice() {
		return this.buf.slice();
	}

	public ByteBuf slice(int i, int j) {
		return this.buf.slice(i, j);
	}

	public ByteBuf duplicate() {
		return this.buf.duplicate();
	}

	public int nioBufferCount() {
		return this.buf.nioBufferCount();
	}

	public ByteBuffer nioBuffer() {
		return this.buf.nioBuffer();
	}

	public ByteBuffer nioBuffer(int i, int j) {
		return this.buf.nioBuffer(i, j);
	}

	public ByteBuffer internalNioBuffer(int parInt1, int parInt2) {
		return this.buf.internalNioBuffer(parInt1, parInt2);
	}

	public ByteBuffer[] nioBuffers() {
		return this.buf.nioBuffers();
	}

	public ByteBuffer[] nioBuffers(int i, int j) {
		return this.buf.nioBuffers(i, j);
	}

	public boolean hasArray() {
		return this.buf.hasArray();
	}

	public byte[] array() {
		return this.buf.array();
	}

	public int arrayOffset() {
		return this.buf.arrayOffset();
	}

	public boolean hasMemoryAddress() {
		return this.buf.hasMemoryAddress();
	}

	public long memoryAddress() {
		return this.buf.memoryAddress();
	}

	public String toString(Charset charset) {
		return this.buf.toString(charset);
	}

	public String toString(int i, int j, Charset charset) {
		return this.buf.toString(i, j, charset);
	}

	public int hashCode() {
		return this.buf.hashCode();
	}

	public boolean equals(Object object) {
		return this.buf.equals(object);
	}

	public int compareTo(ByteBuf bytebuf) {
		return this.buf.compareTo(bytebuf);
	}

	public String toString() {
		return this.buf.toString();
	}

	public byte[] toBytes() {
		int readerIndex = buf.readerIndex();
		int writerIndex = buf.writerIndex();
		byte[] bytes = new byte[writerIndex - readerIndex];
		buf.getBytes(readerIndex, bytes);
		return bytes;
	}

}