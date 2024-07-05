package me.elephant1214.paperfixes.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@IFMLLoadingPlugin.Name("PaperFixes")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class PFLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
