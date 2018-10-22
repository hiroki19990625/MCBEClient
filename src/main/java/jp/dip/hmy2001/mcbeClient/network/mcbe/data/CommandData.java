package jp.dip.hmy2001.mcbeClient.network.mcbe.data;

import java.util.ArrayList;

public class CommandData {
    public String name;
    public String description;

    public byte flags;
    public byte permissionLevel;

    public int enumIndex;

    public ArrayList<CommandOverloadData> overload = new ArrayList<>();
}
