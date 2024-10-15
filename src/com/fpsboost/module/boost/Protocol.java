package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * @author LangYa
 * @since 2024/10/15 21:27
 */
@Module(name = "HytProtocol",cnName = "花雨庭协议",description = "花雨庭绕绿和PacketFix",category = Category.Boost)
public class Protocol implements Access.InstanceAccess {

    private static byte[] encode(String data) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(data.getBytes());
            gzipOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sharkBee() {
        byte[] modList = "\u0002\u001c\tminecraft\u00061.12.2\tdepartmod\u00031.0\rscreenshotmod\u00031.0\u0003ess\u00051.0.2\u0007vexview\u00062.6.10\u0012basemodneteasecore\u00051.9.4\nsidebarmod\u00031.0\u000bskincoremod\u00061.12.2\u000ffullscreenpopup\f1.12.2.38000\bstoremod\u00031.0\u0003mcp\u00049.42\u0007skinmod\u00031.0\rplayermanager\u00031.0\rdepartcoremod\u00061.12.2\tmcbasemod\u00031.0\u0011mercurius_updater\u00031.0\u0003FML\t8.0.99.99\u000bneteasecore\u00061.12.2\u0007antimod\u00032.0\u000bfoamfixcore\u00057.7.4\nnetworkmod\u00061.11.2\u0007foamfix\t@VERSION@\u0005forge\f14.23.5.2768\rfriendplaymod\u00031.0\u0004libs\u00051.0.2\tfiltermod\u00031.0\u0007germmod\u00053.4.2\tpromotion\u000e1.0.0-SNAPSHOT\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000".getBytes();
        byte[] channels = "FML|HS\u0000FML\u0000FML|MP\u0000FML\u0000antimod\u0000ChatVexView\u0000Base64VexView\u0000HudBase64VexView\u0000FORGE\u0000germplugin-netease\u0000VexView\u0000hyt0\u0000armourers\u0000promotion".getBytes();
        byte[] vexView = encode("{\"packet_sub_type\":\"814:469\",\"packet_data\":\"2.6.10\",\"packet_type\":\"ver\"}");

        mc.getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().writeBytes(channels))));
        mc.getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload("FML|HS", new PacketBuffer(Unpooled.buffer().writeBytes(modList))));
        mc.getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload("VexView", new PacketBuffer(Unpooled.buffer().writeBytes(vexView))));
    }
}
