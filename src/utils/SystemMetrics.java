// 
// Decompiled by Procyon v0.6.0
// 

package utils;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class SystemMetrics
{
    private static String formatMemory(final double used, final double total) {
        return String.format("(%.1f/%.1f) GB", used, total);
    }
    
    private static String formatPercentage(final double percentage) {
        return String.format("%.0f%%", percentage);
    }
    
    private static double getUsedMemoryGB() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        final long totalMemory = osBean.getTotalMemorySize();
        final long freeMemory = osBean.getFreeMemorySize();
        final long usedMemory = totalMemory - freeMemory;
        return usedMemory / 1.073741824E9;
    }
    
    private static double getTotalMemoryGB() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        final long totalMemory = osBean.getTotalMemorySize();
        return totalMemory / 1.073741824E9;
    }
    
    private static double getRAMUsagePercentage() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        final long totalMemory = osBean.getTotalMemorySize();
        final long freeMemory = osBean.getFreeMemorySize();
        final long usedMemory = totalMemory - freeMemory;
        return usedMemory / (double)totalMemory * 100.0;
    }
    
    private static double getCPUUsagePercentage() {
        final OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        final double cpuUsage = osBean.getCpuLoad() * 100.0;
        return cpuUsage;
    }
    
    public static String ToString() {
        final double usedMemory = getUsedMemoryGB();
        final double totalMemory = getTotalMemoryGB();
        final double ramUsagePercentage = getRAMUsagePercentage();
        final double cpuUsagePercentage = getCPUUsagePercentage();
        final String string = "Used Memory: " + formatMemory(usedMemory, totalMemory) + "\nRAM Usage Percentage: " + formatPercentage(ramUsagePercentage) + "\nCPU Usage Percentage: " + formatPercentage(cpuUsagePercentage);
        return string;
    }
}
