//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.toma.pubgmc.data.loot.processor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.pubgmc.common.items.equipment.ItemGhillie;
import dev.toma.pubgmc.data.loot.LootGenerationContext;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class GhillieColorProcessor implements LootProcessor {
    private final ColorProvider colorProvider;
    private final int[] colorList;

    public GhillieColorProcessor(ColorProvider colorProvider, int[] colorList) {
        this.colorProvider = colorProvider;
        this.colorList = colorList;
    }

    public void processItems(LootGenerationContext context, List<ItemStack> generated) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        int color;

        if (!generated.isEmpty()) {
            ItemStack first = (ItemStack)generated.get(0);
            ItemGhillie.setFoliageColor(first, 5429504);
        }
    }

    public LootProcessorType<?> getType() {
        return LootProcessors.GHILLIE_COLOR_PROCESSOR;
    }

    public static final class Serializer implements LootProcessorSerializer<GhillieColorProcessor> {
        public Serializer() {
        }

        public GhillieColorProcessor parse(JsonObject object) throws JsonParseException {
            String type = JsonUtils.getString(object, "colorProviderType").toUpperCase();

            ColorProvider colorProvider;
            try {
                colorProvider = GhillieColorProcessor.ColorProvider.valueOf(type);
            } catch (IllegalArgumentException var8) {
                throw new JsonSyntaxException("Unknown color provider type: '" + type + "'. Use one of: [" + (String)Arrays.stream(GhillieColorProcessor.ColorProvider.values()).map(Enum::name).collect(Collectors.joining(", ")) + "]");
            }

            int[] colorList = new int[0];
            if (colorProvider == GhillieColorProcessor.ColorProvider.COLOR_LIST) {
                JsonArray values = JsonUtils.getJsonArray(object, "colors");
                if (values.size() == 0) {
                    throw new JsonSyntaxException("At least one ghillie color must be defined");
                }

                colorList = new int[values.size()];

                for(int i = 0; i < values.size(); ++i) {
                    JsonElement element = values.get(i);
                    colorList[i] = element.getAsInt();
                }
            }

            return new GhillieColorProcessor(colorProvider, colorList);
        }

        public void serialize(JsonObject object, GhillieColorProcessor processor) {
            object.addProperty("colorProviderType", processor.colorProvider.name());
            if (processor.colorProvider == GhillieColorProcessor.ColorProvider.COLOR_LIST) {
                JsonArray array = new JsonArray();
                int[] var4 = processor.colorList;
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    int i = var4[var6];
                    array.add(i);
                }

                object.add("colors", array);
            }

        }
    }

    public static enum ColorProvider {
        BIOME,
        COLOR_LIST;

        private ColorProvider() {
        }
    }
}
