package com.doubleos.yd.command;

import com.doubleos.yd.packet.*;
import com.doubleos.yd.util.Variable;
import dev.toma.pubgmc.common.entity.EntityParachute;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityCommand extends CommandBase
{

    @Override
    public String getName()
    {
        return "mb";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "mb";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {

        Variable variable = Variable.Instance();

        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (args.length >= 1)
        {
            if (args[0].equals("킬로그"))
            {
                //mb 킬로그 살인자 피해자 아이템스택

                for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                {
                    Packet.networkWrapper.sendTo(new CPacketKillBroadcast(args[1], args[2], ((EntityPlayerMP) sender).getHeldItemMainhand()), players);
                }
            }
            else if (args[0].equalsIgnoreCase("페이드연출"))
            {

                if(!args[1].isEmpty() && !args[2].isEmpty() && !args[3].isEmpty() && !args[4].isEmpty() && !args[5].isEmpty())
                {
                    int aniTime = Integer.parseInt(args[1]);
                    int aniDelay = Integer.parseInt(args[2]);
                    float x = Float.parseFloat(args[3]);
                    float y = Float.parseFloat(args[4]);
                    float z = Float.parseFloat(args[5]);


                    Packet.networkWrapper.sendTo(new CPacketFadeEffectAdd(aniTime, aniDelay, x, y, z), (EntityPlayerMP) sender);
                }

            }
            else if (args[0].equalsIgnoreCase("지도"))
            {
                //animal 지도 0~4
                if (!args[1].isEmpty())
                {
                    int number = Integer.parseInt(args[1]);
                    for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                    {

                        Packet.networkWrapper.sendTo(new CPacketBeeHitActive(number), players);
                    }


                }
            }
            else if(args[0].equals("메인타이머"))
            {
                if (!args[1].isEmpty() && !args[2].isEmpty()) {
                    int max = Integer.parseInt(args[1]);
                    int sec = Integer.parseInt(args[2]);
                    //animal 메인타이머 분 초
                    for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                    {
                        Packet.networkWrapper.sendTo(new CPacketMainTimerSync(max, sec), (EntityPlayerMP) players);
                    }

                }

            }
            else if(args[0].equals("서브타이머"))
            {
                if (!args[1].isEmpty() && !args[2].isEmpty())
                {
                    int min = Integer.parseInt(args[1]);
                    int sec = Integer.parseInt(args[2]);
                    //animal 메인타이머 분 초
                    Packet.networkWrapper.sendTo(new CPacketSubTimerSync(min, sec), (EntityPlayerMP) sender);
                }
            }
            else if(args[0].equals("로딩타입"))
            {
                if (!args[1].isEmpty())
                {
                    //animal 로딩타입 red/blue
                    Packet.networkWrapper.sendTo(new CPacketLoadingType(args[1]), (EntityPlayerMP) sender);
                }
            }
            else if(args[0].equals("생존"))
            {
                //animal 돈 액수
                if (!args[1].isEmpty())
                {
                    int money = Integer.parseInt(args[1]);
                    for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                    {
                        Packet.networkWrapper.sendTo(new CPacketMoneySync(money), (EntityPlayerMP) players);
                    }

                }
            }
            else if (args[0].equalsIgnoreCase("룰지"))
            {
                Packet.networkWrapper.sendTo(new CPacketOpenBook(), (EntityPlayerMP) sender);
            }
            else if (args[0].equalsIgnoreCase("후기"))
            {
                Packet.networkWrapper.sendTo(new CPacketGameMenu(), (EntityPlayerMP) sender);
            }
            else if (args[0].equalsIgnoreCase("치킨1"))
            {
                //mb 치킨 스트링
                String joinString = String.join(" ", args);
                String replaceText = joinString.replaceAll("치킨1 ", "");
                Packet.networkWrapper.sendTo(new CPacketChicken(replaceText, 1), (EntityPlayerMP) sender);
            }
            else if (args[0].equalsIgnoreCase("치킨2"))
            {
                //mb 치킨 스트링
                String joinString = String.join(" ", args);
                String replaceText = joinString.replaceAll("치킨2 ", "");
                Packet.networkWrapper.sendTo(new CPacketChicken(replaceText, 2), (EntityPlayerMP) sender);
            }
            else if (args[0].equalsIgnoreCase("치킨4"))
            {
                //mb 치킨 스트링
                String joinString = String.join(" ", args);
                String replaceText = joinString.replaceAll("치킨4 ", "");
                Packet.networkWrapper.sendTo(new CPacketChicken(replaceText, 4), (EntityPlayerMP) sender);
            }
            else if(args[0].equals("팀초기화"))
            {
                //mb 돈 액수
                Packet.networkWrapper.sendTo(new CPacketTeamListClear(), (EntityPlayerMP) sender);

            }
            else if(args[0].equals("낙하산펼치기"))
            {
                if (!player.isRiding())
                {
                    if (!player.getEntityWorld().isRemote)
                    {
                        World worldIn = player.getEntityWorld();
                        EntityParachute chute = new EntityParachute(player.getEntityWorld(), player);
                        worldIn.spawnEntity((Entity) chute);
                        player.startRiding((Entity) chute);
                        Packet.networkWrapper.sendTo(new CPacketShopOpen(), player);
                    }

                }
            }
            else if(args[0].equals("팀등록"))
            {
                //mb 돈 액수
                Packet.networkWrapper.sendTo(new CPacketTeamListAdd(args[1]), (EntityPlayerMP) sender);

            }
            else if(args[0].equals("팀데스"))
            {
                //mb 돈 액수
                //mb 팀데스 이름 true false
                boolean alive = Boolean.parseBoolean(args[2]);
                Packet.networkWrapper.sendTo(new CPacketTeamDeath(args[1], alive), (EntityPlayerMP) sender);

            }
            else if(args[0].equals("자기장중심"))
            {
                //mb 자기장중심 x y
                int posX = Integer.parseInt(args[1]);
                int posZ = Integer.parseInt(args[2]);

                for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                {

                    Packet.networkWrapper.sendTo(new CPacketAreaMiddlePos(posX, posZ), (EntityPlayerMP) players);
                }

            }
            else if(args[0].equals("자기장크기"))
            {
                //mb 자기장크기 size
                float posX = Float.parseFloat(args[1]);

                for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                {

                    Packet.networkWrapper.sendTo(new CPacketAreaSize(posX), (EntityPlayerMP) players);
                }

            }
            else if(args[0].equals("자기장축소크기"))
            {
                //mb 자기장크기 size
                float posX = Float.parseFloat(args[1]);

                for (EntityPlayerMP players : server.getPlayerList().getPlayers())
                {

                    Packet.networkWrapper.sendTo(new CPacketAreaShrinkSize(posX), (EntityPlayerMP) players);
                }

            }
            else if(args[0].equals("킬상승"))
            {
                //mb 킬상승 size
                int posX = Integer.parseInt(args[1]);

                Packet.networkWrapper.sendTo(new CPacketKill(posX), (EntityPlayerMP) player);

            }


        }

    }


    public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            String[] commands = new String[]{"소환공", "removeall", "평가집", "리뷰책열기"};
            return getListOfStringsMatchingLastWord(args, commands);
        }
        else if(args.length >= 2)
        {
            String[] commands = new String[]{ "0", "45", "90", "135, 225", "270", "315", "360"};
            if(args[0].equals("소환공"))
            {
                return getListOfStringsMatchingLastWord(args, commands);
            }
        }
        if(args[0].equalsIgnoreCase("팀등록") && args.length > 1)
        {
            return  getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }

        return Collections.emptyList();
    }
}
