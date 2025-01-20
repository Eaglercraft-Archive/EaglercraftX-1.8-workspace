package net.minecraft.client.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

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
public class JsonException extends IOException {
	private final List<JsonException.Entry> field_151383_a = Lists.newArrayList();
	private final String field_151382_b;

	public JsonException(String parString1) {
		this.field_151383_a.add(new JsonException.Entry());
		this.field_151382_b = parString1;
	}

	public JsonException(String parString1, Throwable parThrowable) {
		super(parThrowable);
		this.field_151383_a.add(new JsonException.Entry());
		this.field_151382_b = parString1;
	}

	public void func_151380_a(String parString1) {
		((JsonException.Entry) this.field_151383_a.get(0)).func_151373_a(parString1);
	}

	public void func_151381_b(String parString1) {
		((JsonException.Entry) this.field_151383_a.get(0)).field_151376_a = parString1;
		this.field_151383_a.add(0, new JsonException.Entry());
	}

	public String getMessage() {
		return "Invalid " + ((JsonException.Entry) this.field_151383_a.get(this.field_151383_a.size() - 1)).toString()
				+ ": " + this.field_151382_b;
	}

	public static JsonException func_151379_a(Exception parException) {
		if (parException instanceof JsonException) {
			return (JsonException) parException;
		} else {
			String s = parException.getMessage();
			if (parException instanceof FileNotFoundException) {
				s = "File not found";
			}

			return new JsonException(s, parException);
		}
	}

	public static class Entry {
		private String field_151376_a;
		private final List<String> field_151375_b;

		private Entry() {
			this.field_151376_a = null;
			this.field_151375_b = Lists.newArrayList();
		}

		private void func_151373_a(String parString1) {
			this.field_151375_b.add(0, parString1);
		}

		public String func_151372_b() {
			return StringUtils.join(this.field_151375_b, "->");
		}

		public String toString() {
			return this.field_151376_a != null
					? (!this.field_151375_b.isEmpty() ? this.field_151376_a + " " + this.func_151372_b()
							: this.field_151376_a)
					: (!this.field_151375_b.isEmpty() ? "(Unknown file) " + this.func_151372_b() : "(Unknown file)");
		}
	}
}