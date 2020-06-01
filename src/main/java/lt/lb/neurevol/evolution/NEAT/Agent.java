package lt.lb.neurevol.evolution.NEAT;

import lt.lb.commons.interfaces.CloneSupport;

/**
 *
 * @author laim0nas100
 */
public class Agent implements CloneSupport<Agent>{
    public String id;

    public Agent(){
        
    }
    
    protected Agent(Agent agent){
        this.id = agent.id;
    }
    
    @Override
    public Agent clone(){
        return new Agent(this);
    }
}
