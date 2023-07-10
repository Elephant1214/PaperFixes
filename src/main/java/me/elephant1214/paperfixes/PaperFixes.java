package me.elephant1214.paperfixes;

import io.papermc.paper.CacheKey;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = PaperFixes.MOD_ID, version = PaperFixes.VERSION, acceptableRemoteVersions = "*")
public class PaperFixes {
    public static final String MOD_ID = "paperfixes";
    public static final String VERSION = "0.1.0";
    public static final Logger LOGGER = LogManager.getLogger("PaperFixes");

    private static PaperFixes instance;
    public final HashMap<CacheKey, Float> explosionDensityCache = new HashMap<>();

    public PaperFixes() {
        instance = this;
    }

    public static PaperFixes getInstance() {
        return instance;
    }
}