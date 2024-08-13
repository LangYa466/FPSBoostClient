package com.fpsboost.module;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.EventManager;
import com.fpsboost.events.misc.NameEvent;
import com.fpsboost.events.misc.TextEvent;
import com.fpsboost.events.update.UpdateEvent;
import com.fpsboost.util.IoUtil;
import com.fpsboost.util.RankUtil;
import com.fpsboost.util.WebUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:25
 */

public class RankManager implements Access.InstanceAccess {
    public static final String PRIMARY_COLOR = EnumChatFormatting.RED.toString();
    public static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();

    public static final List<String> adminList = new CopyOnWriteArrayList<>();

    public RankManager() {
        init();
    }

    private void init() {
        BufferedReader br = null;
        try {
            br = IoUtil.StringToBufferedReader(Objects.requireNonNull(WebUtils.get("https://fpsboost.langya.ink/rank.txt")));
            String line;
            for (line = br.readLine(); line != null; line = br.readLine()) {
                String[] tokens = line.split("-");
                String userName = tokens[0];
                String type = tokens[1];
                RankUtil.tokens.put(userName,type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EventManager.register(this);
    }

    @EventTarget
    public void onN(NameEvent e) {
        RankUtil.tokens.forEach((playerName, rank) -> set(e, playerName, rank));
    }

    private String getRank(String str, String color) {
        return SECONDARY_COLOR + "[" + color + EnumChatFormatting.BOLD + str + SECONDARY_COLOR + "] ";
    }

    private void set(NameEvent e, String playerName, String rank) {
        boolean set = false;
        String name = e.getName();
        if (name.contains(playerName) && !set) {
            if (rank.equals("Admin")) {
                adminList.add(playerName);
                e.setName(String.format("%s ", getRank(rank,PRIMARY_COLOR)) + e.getName());
            }  else {
                e.setName(String.format("%s ", getRank(rank,EnumChatFormatting.BLUE.toString())) + e.getName());
            }
            set = true;
        }
    }

}
/*
package com.fpsboost.module;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.EventManager;
import com.fpsboost.events.misc.TextEvent;
import com.fpsboost.util.IoUtil;
import com.fpsboost.util.RankUtil;
import com.fpsboost.util.WebUtils;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


public class RankManager {
    public static final String PRIMARY_COLOR = EnumChatFormatting.RED.toString();
    public static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();

    public static final List<String> adminList = new CopyOnWriteArrayList<>();

    public RankManager() {
        init();
    }

    private void init() {
        BufferedReader br = null;
        try {
            br = IoUtil.StringToBufferedReader(Objects.requireNonNull(WebUtils.get("https://fpsboost.langya.ink/rank.txt")));
            String line;
            for (line = br.readLine(); line != null; line = br.readLine()) {
                String[] tokens = line.split("-");
                String userName = tokens[0];
                String type = tokens[1];
                RankUtil.tokens.put(userName,type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EventManager.register(this);
    }

    @EventTarget
    public void onT(TextEvent e) {
        RankUtil.tokens.forEach((playerName, rank) -> set(e, playerName, rank));
    }

    private String getRank(String str, String color) {
        return SECONDARY_COLOR + "[" + color + EnumChatFormatting.BOLD + str + SECONDARY_COLOR + "] ";
    }

    private void set(TextEvent e,String playerName,String rank) {
        boolean set = false;
        if (e.text.contains(playerName) && !set) {
            if (rank.equals("Admin")) {
                adminList.add(playerName);
                e.text = String.format("%s ", getRank(rank,PRIMARY_COLOR)) + e.text;
            }  else {
                e.text = String.format("%s ", getRank(rank,EnumChatFormatting.BLUE.toString())) + e.text;
            }
            set = true;
        }
    }

}

 */
