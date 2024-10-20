package com.doubleos.yd.util;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SideOnly(Side.CLIENT)
public class ImageBuffer
{

    public static HashMap<String, BufferedImage> m_nameToBufferImage = new HashMap<>();


    public ImageBuffer()
    {

    }


    public static void init()
    {
        String[] member = new String[]{"d7297", "daju_", "Seoneng", "samsik23", "Huchu95", "Noonkkob", "Daju_", "RuTaeY", "KonG7"};

        System.out.println("한글테스트");
        for (int i = 0; i < member.length; i++)
        {
            int finalI = i;
            CompletableFuture<BufferedImage> future = CompletableFuture.supplyAsync(() ->
            {
                return dowloadImage(member[finalI]);
            });
            try {
                m_nameToBufferImage.put(member[finalI], future.get());
                System.out.println(member[finalI] + "다운로드 완료");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }


        }

    }

    public static BufferedImage dowloadImage(String playerName)
    {
        URL link = null;
        try {
            link = new URL("http://minotar.net/helm/"+playerName+"/128");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) link.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        try {
            BufferedImage image = ImageIO.read(connection.getInputStream());
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
