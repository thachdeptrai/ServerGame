// 
// Decompiled by Procyon v0.6.0
// 

package utils;

import java.io.IOException;

public class FileRunner
{
    public static void runBatchFile(final String batchFilePath) throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "cmd", "/c", "start", batchFilePath });
        processBuilder.start();
    }
}
