package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class LoggingPrintStream extends PrintStream {
	private final String domain;
	private final Logger logger;
	private final boolean err;

	public LoggingPrintStream(String domainIn, boolean err, OutputStream outStream) {
		super(outStream);
		this.domain = domainIn;
		this.logger = LogManager.getLogger(domainIn);
		this.err = err;
	}

	public void println(String s) {
		this.logString(s);
	}

	public void println(Object parObject) {
		this.logString(String.valueOf(parObject));
	}

	private void logString(String string) {
		String callingClass = PlatformRuntime.getCallingClass(3);
		if (callingClass == null) {
			if (err) {
				logger.error(string);
			} else {
				logger.info(string);
			}
		} else {
			if (err) {
				logger.error("@({}): {}", new Object[] { callingClass, string });
			} else {
				logger.info("@({}): {}", new Object[] { callingClass, string });
			}
		}
	}
}