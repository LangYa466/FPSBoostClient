import net.minecraft.client.main.Main;

import java.util.Arrays;

public class Start
{
    public static void main(String[] args)
    {
        Main.main(concat(new String[]{"--version", "mcp","--username","LangYa466","--uuid","99246980-df80-45e9-a80f-a5229d9ed160", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
