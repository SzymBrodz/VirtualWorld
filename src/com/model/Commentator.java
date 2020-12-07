package com.model;

import com.common.Position;

public class Commentator {
    String comments = "";

    public void addFoxStatement(Organism fox, Position where){
        comments = comments + fox + " smelled there's someone stronger at " + where + "\n";
    }

    public void addTurtleDeflectStatement(Organism turtle, Organism attacker){
        comments = comments + turtle + " deflected the attack of " + attacker + "\n";
    }

    public void addTurtleNotMoveStatement(Organism turtle){
        comments = comments + turtle + " decided to be lazy this turn" + "\n";
    }

    public void addAntelopeStatement (Organism antelope, Organism attacker){
        comments = comments + antelope + " dodged the attack of " + attacker + "\n";
    }

    public void addCyberSheepStatement (Organism cybersheep, Position target){
        comments = comments + cybersheep + " acquired target at:" + target + "\n";
    }

    public void addFightStatement(Organism victim, Organism aggressor){
        if(victim.isAlive()){
            comments = comments + victim + " defended against " + aggressor + "\n";
        }
        else if (victim.isPlant()) comments = comments + aggressor + " ate " + victim + "\n";
        else comments = comments + aggressor + " killed " + victim + "\n";
    }

    public void addMoveStatement(Organism organism, Position where){
        comments = comments + organism + " moved to " + where + "\n";
    }

    public void addCopulationStatement(Organism mother, Organism father){
        comments = comments + father + " copulated with " + mother + "\n";
    }

    public void clearComments(){
        comments = "";
    }

    public String getComments(){
        return comments;
    }

}
