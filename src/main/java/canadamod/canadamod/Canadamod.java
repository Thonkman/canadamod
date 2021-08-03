package canadamod.canadamod;

import canadamod.canadamod.registry.Bushes;
import canadamod.canadamod.registry.Sounds;
import canadamod.canadamod.registry.items.CanadamodItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Canadamod implements ModInitializer {
    public static final String MOD_ID = "canadamod";

    public static Identifier getIdentifier(String path) {
        return new Identifier(MOD_ID + ":" + path);
    }

    @Override
    public void onInitialize() {
        CanadamodItems.registerItems();
        Bushes.registerBushes();
        Sounds.registerSounds();
    }
}