package net.runelite.cache.definitions.loaders;

import java.util.ArrayList;
import java.util.List;
import net.runelite.cache.IndexType;
import net.runelite.cache.definitions.NpcDefinition;
import net.runelite.cache.io.InputStream;
import net.runelite.cache.utils.StringUtilities;

public class NpcLoader
{
	public static final IndexType INDEX_TYPE = IndexType.TWO;
	public static final int ARCHIVE_ID = 9;

	private final List<NpcDefinition> npcs = new ArrayList<>();

	public List<NpcDefinition> getNpcs()
	{
		return npcs;
	}

	public void load(int id, InputStream stream)
	{
		NpcDefinition def = new NpcDefinition(id);
		while (true)
		{
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
			{
				break;
			}

			this.decodeValues(opcode, def, stream);
		}
		npcs.add(def);
	}

	void decodeValues(int opcode, NpcDefinition def, InputStream stream)
	{
		int length;
		int index;
		if (1 == opcode)
		{
			length = stream.readUnsignedByte();
			def.models = new int[length];

			for (index = 0; index < length; ++index)
			{
				def.models[index] = stream.readUnsignedShort();
			}

		}
		else if (2 == opcode)
		{
			def.name = StringUtilities.readString_2(stream);
		}
		else if (12 == opcode)
		{
			def.tileSpacesOccupied = stream.readUnsignedShort();
		}
		else if (opcode == 13)
		{
			def.stanceAnimation = stream.readUnsignedShort();
		}
		else if (opcode == 14)
		{
			def.walkAnimation = stream.readUnsignedShort();
		}
		else if (15 == opcode)
		{
			def.anInt2165 = stream.readUnsignedShort();
		}
		else if (opcode == 16)
		{
			def.anInt2189 = stream.readUnsignedShort();
		}
		else if (17 == opcode)
		{
			def.walkAnimation = stream.readUnsignedShort();
			def.rotate180Animation = stream.readUnsignedShort();
			def.rotate90RightAnimation = stream.readUnsignedShort();
			def.rotate90LeftAnimation = stream.readUnsignedShort();
		}
		else if (opcode >= 30 && opcode < 35)
		{
			def.options[opcode - 30] = StringUtilities.readString_2(stream);
			if (def.options[opcode - 30].equalsIgnoreCase("Hidden"))
			{
				def.options[opcode - 30] = null;
			}
		}
		else if (opcode == 40)
		{
			length = stream.readUnsignedByte();
			def.recolorToFind = new short[length];
			def.recolorToReplace = new short[length];

			for (index = 0; index < length; ++index)
			{
				def.recolorToFind[index] = (short) stream.readUnsignedShort();
				def.recolorToReplace[index] = (short) stream.readUnsignedShort();
			}

		}
		else if (opcode == 41)
		{
			length = stream.readUnsignedByte();
			def.retextureToFind = new short[length];
			def.retextureToReplace = new short[length];

			for (index = 0; index < length; ++index)
			{
				def.retextureToFind[index] = (short) stream.readUnsignedShort();
				def.retextureToReplace[index] = (short) stream.readUnsignedShort();
			}

		}
		else if (60 != opcode)
		{
			if (opcode == 93)
			{
				def.renderOnMinimap = false;
			}
			else if (95 == opcode)
			{
				def.combatLevel = stream.readUnsignedShort();
			}
			else if (97 == opcode)
			{
				def.resizeX = stream.readUnsignedShort();
			}
			else if (98 == opcode)
			{
				def.resizeY = stream.readUnsignedShort();
			}
			else if (opcode == 99)
			{
				def.hasRenderPriority = true;
			}
			else if (100 == opcode)
			{
				def.ambient = stream.readByte();
			}
			else if (101 == opcode)
			{
				def.contrast = stream.readByte();
			}
			else if (opcode == 102)
			{
				def.headIcon = stream.readUnsignedShort();
			}
			else if (103 == opcode)
			{
				def.anInt2156 = stream.readUnsignedShort();
			}
			else if (opcode == 106)
			{
				def.anInt2174 = stream.readUnsignedShort();
				if ('\uffff' == def.anInt2174)
				{
					def.anInt2174 = -1;
				}

				def.anInt2187 = stream.readUnsignedShort();
				if ('\uffff' == def.anInt2187)
				{
					def.anInt2187 = -40212193;
				}

				length = stream.readUnsignedByte();
				def.anIntArray2185 = new int[length + 1];

				for (index = 0; index <= length; ++index)
				{
					def.anIntArray2185[index] = stream.readUnsignedShort();
					if (def.anIntArray2185[index] == '\uffff')
					{
						def.anIntArray2185[index] = -1;
					}
				}

			}
			else if (107 == opcode)
			{
				def.isClickable = false;
			}
			else if (opcode == 109)
			{
				def.aBool2170 = false;
			}
			else if (opcode == 111)
			{
				def.aBool2190 = true;
			}
			else if (opcode == 112)
			{
				def.anInt2184 = stream.readUnsignedByte();
			}
		}
		else
		{
			length = stream.readUnsignedByte();
			def.models_2 = new int[length];

			for (index = 0; index < length; ++index)
			{
				def.models_2[index] = stream.readUnsignedShort();
			}

		}
	}
}
