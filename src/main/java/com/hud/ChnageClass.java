package com.hud;

import net.minecraft.launchwrapper.IClassTransformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ChnageClass implements IClassTransformer {
    String[] chagneClass = new String[] { "GuiIngame", "GuiIngameForge", "Minecraft", "ScaledResolution", "EntityVehicle", "EntityParachute",
            "RenderPlayer", "GuiChat", "GhillieColorProcessor", "PlayerControllerMP"};

    String[] classes = new String[0];

    public byte[] transform(String arg0, String arg1, byte[] arg2)
    {
        if (check(arg1))
        {
            return patching(arg0, arg1, arg2, FakeModLoader.modFile);
        }

        return arg2;
    }

    private boolean check(String s)
    {
        for (String cl : this.chagneClass)
        {
            if (("net.minecraft.client.gui.inventory." + cl).equals(s))
                return true;
            else if(("net.minecraft.client.gui." + cl).equals(s))
            {
                System.out.println("GuiChat 변경 완료");
                return true;
            }
            else if(("net.minecraft.client." + cl).equals(s))
            {
                System.out.println("Minecraft 변경 완료");
                return true;
            }
            else if(("net.minecraft.client.renderer.entity." + cl).equals(s))
            {
                System.out.println("RenderPlayer 변경 완료" + cl);
                return true;
            }
            else if(("net.minecraft.world." + cl).equals(s))
            {
                System.out.println("World 변경 완료");
                return true;
            }
            else if(("net.minecraftforge.client." + cl).equals(s))
            {
                System.out.println("포지파일 변경 변경 완료");
                return true;
            }
            else if(("net.minecraft.entity." + cl).equals(s))
            {
                System.out.println("엔티티 포지파일 변경 변경 완료");
                return true;
            }
            else if(("dev.toma.pubgmc.common.entity.controllable." + cl).equals(s))
            {
                System.out.println("배그파일 변경 변경 완료");
                return true;
            }
            else if(("dev.toma.pubgmc.common.items.heal." + cl).equals(s))
            {
                System.out.println(s + "    배그파일 변경 변경 완료");
                return true;
            }
            else if(("dev.toma.pubgmc.common.entity." + cl).equals(s))
            {
                System.out.println(s + "    배그파일 변경 변경 완료");
                return true;
            }
            else if(("dev.toma.pubgmc.client." + cl).equals(s))
            {
                System.out.println(s + "    배그클라이언트 파일 완료");
                return true;
            }
            else if(("dev.toma.pubgmc.data.loot.processor." +cl).equals(s))
            {
                System.out.println("에어드랍 팅김 해결");
                return true;
            }
            else if(("net.minecraft.client.multiplayer." + cl).equals(s))
            {
                System.out.println("Contorl 파일 변경 변경 완료");
                return true;
            }
            else if(("net.minecraft.network." + cl).equals(s))
            {
                System.out.println("네트워크 파일 변경 변경 완료");
                return true;
            }
            else if(("net.minecraft.server.management." + cl).equals(s))
            {
                System.out.println("Intercation Manager 파일 변경 변경 완료");
                return true;
            }


        }
        for (String cl : this.classes) {
            if (cl.equals(s))
                return true;
        }
        return false;
    }

    private byte[] patching(String rebname, String name, byte[] data, File f) {
        try {
            ZipFile zip = new ZipFile(f);
            ZipEntry e = zip.getEntry(rebname.replace('.', '/') + ".class");
            if (e == null) {
                e = zip.getEntry(name.replace('.', '/') + ".class");

                if (e == null) {
                    zip.close();
                    return data;
                }
            }
            InputStream is = zip.getInputStream(e);
            data = rwAll(is);
            is.close();
            zip.close();
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private byte[] rwAll(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1)
            bos.write(buffer, 0, len);
        return bos.toByteArray();
    }
}
