package net.querz.mcaselector.version;

import net.querz.mcaselector.debug.Debug;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ColorMapping {

	// default tints from plains biome
	int DEFAULT_GRASS_TINT = 0x91bd59;
	int DEFAULT_FOLIAGE_TINT = 0x77ab2f;
	int DEFAULT_WATER_TINT = 0x3f76e4;

	// returns a color based on the block data given as the parameter
	int getRGB(Object o, int biome);

	boolean isFoliage(Object o);

	default int applyTint(int color, int tint) {
		int nr = (tint >> 16 & 0xFF) * (color >> 16 & 0xFF) >> 8;
		int ng = (tint >> 8 & 0xFF) * (color >> 8 & 0xFF) >> 8;
		int nb = (tint & 0xFF) * (color & 0xFF) >> 8;
		return color & 0xFF000000 | nr << 16 | ng << 8 | nb;
	}

	int[] biomeGrassTints = new int[256];
	int[] biomeFoliageTints = new int[256];
	int[] biomeWaterTints = new int[256];

	Map<String, Integer> biomeNameGrassTints = new HashMap<>();
	Map<String, Integer> biomeNameFoliageTints = new HashMap<>();
	Map<String, Integer> biomeNameWaterTints = new HashMap<>();

	TintInitializer tintInitializer = new TintInitializer();

	final class TintInitializer {

		private TintInitializer() {}

		static {
			Arrays.fill(biomeGrassTints, DEFAULT_GRASS_TINT);
			Arrays.fill(biomeFoliageTints, DEFAULT_FOLIAGE_TINT);
			Arrays.fill(biomeWaterTints, DEFAULT_WATER_TINT);

			try (BufferedReader bis = new BufferedReader(
				new InputStreamReader(Objects.requireNonNull(ColorMapping.class.getClassLoader().getResourceAsStream("mapping/all_biome_colors.txt"))))) {

				String line;
				while ((line = bis.readLine()) != null) {
					String[] elements = line.split(";");
					if (elements.length != 4) {
						Debug.dumpf("invalid line in biome color file: \"%s\"", line);
						continue;
					}

					int biomeID = Integer.parseInt(elements[0]);
					int grassColor = Integer.parseInt(elements[1], 16);
					int foliageColor = Integer.parseInt(elements[2], 16);
					int waterColor = Integer.parseInt(elements[3], 16);

					biomeGrassTints[biomeID] = grassColor;
					biomeFoliageTints[biomeID] = foliageColor;
					biomeWaterTints[biomeID] = waterColor;
				}

			} catch (IOException ex) {
				throw new RuntimeException("failed to read mapping/all_biome_colors.txt");
			}
		}
	}
}
