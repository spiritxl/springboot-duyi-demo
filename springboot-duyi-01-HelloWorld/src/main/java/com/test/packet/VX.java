package com.test.packet;

import java.util.Collections;
import java.util.Vector;

public class VX {

    private VX(){};

    private static final VX instance = new VX();

    public static VX getInstance(){
        return instance;
    }

    private Vector<RedPackets> redPackets = new Vector<>();


    {
        Double totalMoney = 10.0;
        Integer totalPackets = 5;
        Double minMoney = 0.01;

        for (int i = 0; i < totalPackets; i++) {
            redPackets.add(new RedPackets(minMoney));
            totalMoney -= minMoney;
        }

        for (int i = 0; i < totalPackets; i++) {
            double remainingPackets = totalPackets - i - 1;
            double maxMoney = totalMoney - remainingPackets * minMoney;
            double money = Math.random() * (maxMoney - minMoney) + minMoney;
            redPackets.get(i).setMoney(redPackets.get(i).getMoney() + money);
            totalMoney -= money;
        }

        Collections.shuffle(redPackets);
    }

    public synchronized RedPackets getPacket() {
        if (redPackets.isEmpty()) {
            return null;
        }
        return redPackets.remove(0);
    }


}
