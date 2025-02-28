package Bot;/*telegram @tomihjhj_bot*/

import MobZ.Mob;
import java.util.Random;/*telegram @tomihjhj_bot*/
import java.util.ArrayList;/*telegram @tomihjhj_bot*/
import java.util.List;/*telegram @tomihjhj_bot*/
import services.PlayerService;
import services.SkillService;
import  utils.Util;/*telegram @tomihjhj_bot*/

public class Mobb {
     private Mob mAttack;/*telegram @tomihjhj_bot*/
     
     public long lastTimeChanM;/*telegram @tomihjhj_bot*/
     
     public Bot bot;/*telegram @tomihjhj_bot*/
     
     
     public Mobb(Bot b){
         this.bot = b;/*telegram @tomihjhj_bot*/
     }
     
     public void update(){
        this.Attack();/*telegram @tomihjhj_bot*/
        this.chanGeMap();/*telegram @tomihjhj_bot*/
     }
     
     public void GetMobAttack(){
         if(this.bot.zone.mobs.size() >= 1){
            if(this.mAttack == null || this.mAttack.isDie()){
            mAttack = this.bot.zone.mobs.get(new Random().nextInt(this.bot.zone.mobs.size()));/*telegram @tomihjhj_bot*/
         }
      }
    }
     
     
     public void Attack(){
         this.GetMobAttack();/*telegram @tomihjhj_bot*/
             if(Util.isTrue(50 , 100)){
                this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(0);/*telegram @tomihjhj_bot*/
             } else {
                this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(1);/*telegram @tomihjhj_bot*/
             }
       if(this.mAttack != null){
           if(this.bot.UseLastTimeSkill()){
               PlayerService.gI().playerMove(this.bot, this.mAttack.location.x, this.mAttack.location.y);/*telegram @tomihjhj_bot*/
               SkillService.gI().useSkill(this.bot, null, this.mAttack,-1, null);/*telegram @tomihjhj_bot*/
           }
      }
    }
    
        
   public void chanGeMap(){
       if(this.lastTimeChanM < ((System.currentTimeMillis() - 150000) -  new Random().nextInt(150000))){
           this.bot.joinMap();/*telegram @tomihjhj_bot*/
       }
   }
    
}
