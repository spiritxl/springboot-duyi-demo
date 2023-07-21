package com.duyi.controller;

import com.duyi.config.FoodConfig;
import com.duyi.config.VegetablesConfig;
import com.duyi.domain.Food;
import com.duyi.domain.Vegetables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class JsonController {

    @Autowired
    private VegetablesConfig vegetablesConfig;


    @RequestMapping("/vegetable")
    @ResponseBody
    public Vegetables vegetable(){
        Vegetables vegetables = new Vegetables();
        vegetables.setPotato(vegetablesConfig.getPotato());
        vegetables.setEggplant(vegetablesConfig.getEggplant());
        vegetables.setGreenpeper(vegetablesConfig.getGreenpeper());
        return vegetables;
    }

    @Autowired
    private FoodConfig foodConfig;

    @RequestMapping("/food")
    @ResponseBody
    public Food food(){
         Food food = new Food();
         food.setMeet(foodConfig.getMeet());
         food.setRice(foodConfig.getRice());
         return food;
    }
}
