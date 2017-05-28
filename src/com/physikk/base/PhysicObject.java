/*
 * Copyright (C) 2017 sleepersword
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.physikk.base;

import java.util.HashMap;

/**
 * The base class for all physical objects.
 * @author sleepersword
 */
public abstract class PhysicObject 
{
    protected Vector position;
    protected Vector velocity;
    protected double mass;
    protected HashMap<String, NamedVector> forces;
    
    private boolean active;
    private int ticks;
    
    protected PhysicObject(boolean isActive) {
        active = isActive;
        forces = new HashMap<>();
    }
    
    /// Full implemented
    
    /**
     * Calculates the total force on this object by superposition.
     * @return The total force.
     */
    public final NamedVector getTotalForce() {
        Vector res = Vector.NULLVECTOR();
        
        for(Vector f : forces.values()){
            res = res.sum(f);
        }
        
        return new NamedVector("total_force", res);
    }
    
    /**
     * Returns the position of this object.
     * @return The position.
     */
    public final Vector getPosition() {
        return position;
    }
    
    /**
     * Returns the velocity of this object.
     * @return The velocity.
     */
    public final Vector getVelocity() {
        return velocity;
    }
    
    /**
     * Returns true if this object is active.
     * @return Whether is active.
     */
    public final boolean isActive() {
        return active;
    }
       
    public final void activate() {
        active = true;
    }
    
    public final void deactivate() {
        active = false;
    }
    
    // Gets called internally by an PhysicManager
    public final void update() {
        // Don't update if inactive
        if(!active) return; 
        
        // Call specific method tick()
        this.tick();
        ticks++;
        if(ticks == Utils.TICKS_PER_SECOND)
        {
            this.tickSecond();
            ticks = 0;
        }
        
        // Calculate acceleration
        Vector acceleration = getTotalForce().scale(1.0 / mass);
        
        // Calculate the change of the velocity
        Vector delta_velo = acceleration.scale( Utils.TIME_PER_TICK );
        this.velocity = this.velocity.sum(delta_velo);
        
        // Calculate the change of the position
        Vector delta_pos = this.velocity.scale( Utils.TIME_PER_TICK );
        this.position = this.position.sum(delta_pos);
    }
        
    /// Abstract
    
    protected abstract void tick();
    protected abstract void tickSecond();
}
