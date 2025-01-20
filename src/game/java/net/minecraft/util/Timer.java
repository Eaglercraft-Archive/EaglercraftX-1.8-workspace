package net.minecraft.util;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.minecraft.client.Minecraft;

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
public class Timer {
	float ticksPerSecond;
	private double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	/**+
	 * A multiplier to make the timer (and therefore the game) go
	 * faster or slower. 0.5 makes the game run at half-speed.
	 */
	public float timerSpeed = 1.0F;
	public float elapsedPartialTicks;
	private long lastSyncSysClock;
	private long lastSyncHRClock;
	private long field_74285_i;
	/**+
	 * A ratio used to sync the high-resolution clock to the system
	 * clock, updated once per second
	 */
	private double timeSyncAdjustment = 1.0D;

	public Timer(float parFloat1) {
		this.ticksPerSecond = parFloat1;
		this.lastSyncSysClock = Minecraft.getSystemTime();
		this.lastSyncHRClock = EagRuntime.nanoTime() / 1000000L;
	}

	/**+
	 * Updates all fields of the Timer using the current time
	 */
	public void updateTimer() {
		long i = Minecraft.getSystemTime();
		long j = i - this.lastSyncSysClock;
		long k = EagRuntime.nanoTime() / 1000000L;
		double d0 = (double) k / 1000.0D;
		if (j <= 1000L && j >= 0L) {
			this.field_74285_i += j;
			if (this.field_74285_i > 1000L) {
				long l = k - this.lastSyncHRClock;
				double d1 = (double) this.field_74285_i / (double) l;
				this.timeSyncAdjustment += (d1 - this.timeSyncAdjustment) * 0.20000000298023224D;
				this.lastSyncHRClock = k;
				this.field_74285_i = 0L;
			}

			if (this.field_74285_i < 0L) {
				this.lastSyncHRClock = k;
			}
		} else {
			this.lastHRTime = d0;
		}

		this.lastSyncSysClock = i;
		double d2 = (d0 - this.lastHRTime) * this.timeSyncAdjustment;
		this.lastHRTime = d0;
		d2 = MathHelper.clamp_double(d2, 0.0D, 1.0D);
		this.elapsedPartialTicks = (float) ((double) this.elapsedPartialTicks
				+ d2 * (double) this.timerSpeed * (double) this.ticksPerSecond);
		this.elapsedTicks = (int) this.elapsedPartialTicks;
		this.elapsedPartialTicks -= (float) this.elapsedTicks;
		if (this.elapsedTicks > 10) {
			this.elapsedTicks = 10;
		}
		this.renderPartialTicks = this.elapsedPartialTicks;
	}
}