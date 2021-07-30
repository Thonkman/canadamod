package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sounds {
    public static final Identifier SASKATOON_PICK_1_ID = Canadamod.getIdentifier("saskatoon_pick_1");
    public static SoundEvent SASKATOON_PICK_1_EVENT = new SoundEvent(SASKATOON_PICK_1_ID);
    public static final Identifier SASKATOON_PICK_2_ID = Canadamod.getIdentifier("saskatoon_pick_2");
    public static SoundEvent SASKATOON_PICK_2_EVENT = new SoundEvent(SASKATOON_PICK_2_ID);
    public static final Identifier SASKATOON_PICK_3_ID = Canadamod.getIdentifier("saskatoon_pick_3");
    public static SoundEvent SASKATOON_PICK_3_EVENT = new SoundEvent(SASKATOON_PICK_3_ID);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_1_ID, SASKATOON_PICK_1_EVENT);
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_2_ID, SASKATOON_PICK_2_EVENT);
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_3_ID, SASKATOON_PICK_3_EVENT);
    }
}
