### Java 17 is recommended for compiling to TeaVM

### Java 8 or greater is required for the desktop runtime

**Most Java IDEs will allow you to import this repository as a gradle project for compiling it to JavaScript.**

Java must be added to your PATH!

**To compile the web client:**
1. Run `CompileEPK`
2. Run `CompileJS` (or the `generateJavaScript` gradle task in your IDE)
3. Check the "javascript" folder

**To compile an offline download:**
1. Run `CompileEPK`
2. Run `CompileJS` (or the `generateJavaScript` gradle task in your IDE)
3. Run `MakeOfflineDownload`
4. Check the "javascript" folder

**To compile the WASM GC client:**
Consult the [README](wasm_gc_teavm/README.md) in the wasm_gc_teavm folder

**To use the desktop runtime:**
1. Import the Eclipse project in "desktopRuntime/eclipseProject" into your IDE
2. Open one of the .java files from the source folders (workaround for a bug)
3. Run/Debug the client with the included "eaglercraftDebugRuntime" configuration

**Note:** If you are trying to use the desktop runtime on Linux, make sure you add the "desktopRuntime" folder to the `LD_LIBRARY_PATH` environment variable of the Java process. This should be done automatically by the Eclipse project's default run configuration, but it might not work properly on every system, or when the Eclipse project is imported into IntelliJ.

**See the main 1.8 repository's README for more info**

The source codes of EaglercraftXBungee and EaglercraftXVelocity are not included here.