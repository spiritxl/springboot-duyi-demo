package com.test.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class People extends Thread{

    private String name;

    public void run(){
        getRedPacket();
    }

    private void getRedPacket() {
        while (true){
            VX instance = VX.getInstance();
            RedPackets packet = instance.getPacket();
            if(packet==null){
                System.out.println(name+"没抢到红包");
                break;
            }
            System.out.println(name+"抢到了"+String.format("%.2f", packet.getMoney()) + "元");
            break;
        }
    }
}
