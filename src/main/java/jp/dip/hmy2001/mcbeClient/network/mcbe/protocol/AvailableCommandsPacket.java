package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;
import jp.dip.hmy2001.mcbeClient.network.mcbe.data.CommandData;
import jp.dip.hmy2001.mcbeClient.network.mcbe.data.CommandOverloadData;
import jp.dip.hmy2001.mcbeClient.network.mcbe.data.CommandParameterData;

import java.util.ArrayList;
import java.util.HashMap;

public class AvailableCommandsPacket extends GamePacket {
    public ArrayList<String> enumValues = new ArrayList<>();
    public ArrayList<String> postFixes = new ArrayList<>();
    public HashMap<String, ArrayList<Integer>> enums = new HashMap<>();
    public ArrayList<CommandData> commands = new ArrayList<>();

    @Override
    public void decodeBody() {
        int enumValuesCount = readUnsignedVarInt();
        for (int i = 0; i < enumValuesCount; ++i) {
            enumValues.add(readString());
        }

        int postFixesCount = readUnsignedVarInt();
        for (int i = 0; i < postFixesCount; ++i) {
            postFixes.add(readString());
        }

        int enumsCount = readUnsignedVarInt();
        for (int i = 0; i < enumsCount; ++i) {
            String enumName = readString();
            int count = readUnsignedVarInt();
            ArrayList<Integer> indexes = new ArrayList();
            for (int j = 0; j < count; ++j) {
                if (enumValues.size() < 0xff) {
                    indexes.add((int) this.readByte());
                } else if (enumValues.size() < 0xffff) {
                    indexes.add((int) this.readShortLE());
                } else {
                    indexes.add(this.readIntLE());
                }
            }
            enums.put(enumName, indexes);
        }

        int commandsCount = readUnsignedVarInt();
        for (int i = 0; i < commandsCount; ++i) {
            CommandData data = new CommandData();
            data.name = readString();
            data.description = readString();
            data.flags = readByte();
            data.permissionLevel = readByte();
            data.enumIndex = readIntLE();

            int count = readUnsignedVarInt();
            for (int j = 0; j < count; ++j) {
                CommandOverloadData overload = new CommandOverloadData();
                int count2 = readUnsignedVarInt();
                for (int k = 0; k < count2; ++k) {
                    CommandParameterData parameter = new CommandParameterData();
                    parameter.name = readString();
                    parameter.type = readIntLE();
                    parameter.optional = readBoolean();

                    overload.parameters.add(parameter);
                }
                data.overload.add(overload);
            }
            commands.add(data);
        }
    }
}
